package com.gexin.artplatform.db;

import java.util.List;

import com.gexin.artplatform.bean.ThreadInfo;

/**
 * ���ݷ��ʽӿ�
 * @author xiaoxin
 *
 */
public interface ThreadDAO {

	/**
	 * �����߳���Ϣ
	 * @param threadInfo
	 */
	public void insertThread(ThreadInfo threadInfo);
	/**
	 * ɾ���߳�
	 * @param url
	 * @param thread_id
	 */
	public void deleteThread(String url,int thread_id);
	/**
	 * �����߳����ؽ���
	 * @param url
	 * @param thread_id
	 * @param finished
	 */
	public void updateThread(String url,int thread_id,int finished);
	/**
	 * ��ѯ�ļ����߳���Ϣ
	 * @param url
	 * @return
	 */
	public List<ThreadInfo> getThreads(String url);
	/**
	 * �߳���Ϣ�Ƿ����
	 * @param url
	 * @param thread_id
	 * @return
	 */
	public boolean isExists(String url,int thread_id);
	
}
