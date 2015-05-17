package com.gexin.artplatform.imagecache.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class ZHttpClient {
    final String TAG = "HttpClient";

    String mUrl;
    List<NameValuePair> mParams;

    public ZHttpClient(String url) {
        Log.i(TAG, "url = " + url);
        mUrl = url;
        mParams = new ArrayList<NameValuePair>();
    }

    public void addParam(String key, String value) {
        mParams.add(new BasicNameValuePair(key, value));
    }

    public void addParam(String key, int value) {
        mParams.add(new BasicNameValuePair(key, String.valueOf(value)));
    }

    public void addParam(String key, long value) {
        mParams.add(new BasicNameValuePair(key, String.valueOf(value)));
    }

    public void clearParams() {
        mParams.clear();
    }

    int id = -1;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public interface DataReciever {
        public void onSizeRecieved(long size);

        public void onDataRecieved(DefaultHttpClient client, byte[] data, int count) throws Exception;
    }

    public interface InputStreamReciever {
        public void onSizeRecieved(long size);

        public void handleStream(DefaultHttpClient client, InputStream is) throws Exception;
    }

    class MDataReciever implements DataReciever {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        @Override
        public void onSizeRecieved(long size) {

        }

        @Override
        public void onDataRecieved(DefaultHttpClient client, byte[] data, int count) {
            bos.write(data, 0, count);
        }
    }

    class MFileReceiver implements DataReciever {
        FileOutputStream fos;

        public MFileReceiver(String path) throws Exception {
            fos = new FileOutputStream(path);
        }

        @Override
        public void onSizeRecieved(long size) {

        }

        @Override
        public void onDataRecieved(DefaultHttpClient client, byte[] data, int count) throws Exception {
            fos.write(data, 0, count);
            fos.flush();
        }

        public void finish() throws Exception {
            fos.close();
        }
    }

    public byte[] postToByteArray() {
        MDataReciever r = new MDataReciever();
        try {
            HttpPost request = new HttpPost(mUrl);
            request.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
            request.setHeader("Accept-Encoding", "gzip");
            if (!execute(request, r, null)) {
                return null;
            }
            byte[] ret = r.bos.toByteArray();
            // Log.hex(TAG, ret, 0, ret.length);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String post() {
        byte[] ret = postToByteArray();
        if (ret != null) {
            String result = new String(ret, 0, ret.length);
            Log.w(TAG, result);
            return result;
        } else {
            return null;
        }
    }

    public byte[] getToByteArray() {
        MDataReciever r = new MDataReciever();
        try {
            HttpGet request = new HttpGet(mUrl);
            request.setHeader("Accept-Encoding", "gzip");
            if (!execute(request, r, null)) {
                return null;
            }
            byte[] ret = r.bos.toByteArray();
            // Log.hex(TAG, ret, 0, ret.length);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get() {
        byte[] ret = getToByteArray();
        if (ret != null) {
            String result = new String(ret, 0, ret.length);
            Log.w(TAG, result);
            return result;
        } else {
            return null;
        }
    }

    public boolean download(String path) {
        try {
            MFileReceiver f = new MFileReceiver(path);
            HttpGet request = new HttpGet(mUrl);
            request.setHeader("Accept-Encoding", "gzip");
            boolean ret = execute(request, f, null);
            f.finish();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean execute(HttpUriRequest request, DataReciever data_reciever, InputStreamReciever ins_reciever)
            throws Exception {
        for (NameValuePair p : mParams) {
            Log.w(TAG, p.getName() + "=" + p.getValue());
        }

        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            Log.w(TAG, "response code invalid: " + response.getStatusLine().getStatusCode());
            return false;
        }

        HttpEntity entity = response.getEntity();
        long content_length = entity.getContentLength();
        Log.w(TAG, "content_length: " + content_length);

        if (data_reciever != null) {
            data_reciever.onSizeRecieved(content_length);
        }
        if (ins_reciever != null) {
            ins_reciever.onSizeRecieved(content_length);
        }

        Header contentEncoding = entity.getContentEncoding();
        InputStream is = entity.getContent();
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            is = new GZIPInputStream(is);
            Log.w(TAG, "using GZIP");
        }

        if (data_reciever != null) {
            long pos = 0;
            byte[] buffer = new byte[16384];
            while (true) {
                int count = is.read(buffer);
                if (count <= 0) {
                    Log.w(TAG, "read: " + count);
                    break;
                }

                Log.i(TAG, "read: " + count);
                data_reciever.onDataRecieved(client, buffer, count);
                pos += count;

                if (mListener != null) {
                    mListener.onProgress(id, pos, content_length);
                }
            }
        } else if (ins_reciever != null) {
            ins_reciever.handleStream(client, is);
        } else {
            client.getConnectionManager().shutdown();
        }

        is.close();
        return true;
    }

    public interface ProgressListener {
        public void onProgress(int id, long progress, long total);
    }

    ProgressListener mListener = null;

    public void setProgressListener(ProgressListener listener) {
        mListener = listener;
    }
}
