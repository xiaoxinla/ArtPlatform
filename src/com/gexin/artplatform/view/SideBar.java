package com.gexin.artplatform.view;

import com.gexin.artplatform.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * �Զ���������ͼ
 * 
 * @author xiaoxin
 * 
 */
public class SideBar extends View {

	//private static final String TAG = "SideBar";
	// ��ĸ��
	private static final String[] sAlphabet = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };
	// ѡ�е�λ��
	private int mChoosen = -1;
	// ��ͼ
	private Paint mPaint = new Paint();
	// ��ʾ��ĸTextView
	private TextView mLetterShow;

	private onTouchLetterChangeListener mTouchLetterChangeListener;

	/**
	 * ���������ĸ�Ľӿ�
	 * 
	 * @author xiaoxin
	 */
	public interface onTouchLetterChangeListener {
		public void onTouchLetterChange(String letter);
	}

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setLetterShow(TextView mLetterShow) {
		this.mLetterShow = mLetterShow;
	}

	public void setTouchLetterChangeListener(
			onTouchLetterChangeListener mTouchLetterChangeListener) {
		this.mTouchLetterChangeListener = mTouchLetterChangeListener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �����߶�
		int height = getHeight();
		// �������
		int width = getWidth();
		// ������ĸ�߶�
		int singleLetterHeight = height / sAlphabet.length;

		// ����ÿ����ĸ
		for (int i = 0; i < sAlphabet.length; i++) {
			// ���û�����ɫ
			mPaint.setColor(Color.rgb(33, 65, 98));
			// ��������
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			// ���ÿ����
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(20);

			// ��ĸ��ѡ�и���
			if (i == mChoosen) {
				mPaint.setColor(Color.parseColor("#3399ff"));
				// ���ô���
				mPaint.setFakeBoldText(true);
			}

			float xPos = width / 2 - mPaint.measureText(sAlphabet[i]) / 2;
			float yPos = singleLetterHeight * (i + 1);

			// �����ı�
			canvas.drawText(sAlphabet[i], xPos, yPos, mPaint);

			// ���û���
			mPaint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		final int action = event.getAction();
		// �����λ��
		final float yPos = event.getY();
		// �ϸ�ѡ�е�λ��
		final int lastChoosen = mChoosen;
		final onTouchLetterChangeListener listener = mTouchLetterChangeListener;
		// ��ε������ĸ����
		final int index = (int) (yPos / getHeight() * sAlphabet.length);

		switch (action) {
		case MotionEvent.ACTION_UP:
			init();
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_bg);
			if (lastChoosen != index) {
				if (index >= 0 && index < sAlphabet.length) {
					if (listener != null) {
						listener.onTouchLetterChange(sAlphabet[index]);
					}
					if (mLetterShow != null) {
						mLetterShow.setText(sAlphabet[index]);
						mLetterShow.setVisibility(View.VISIBLE);
					}

					mChoosen = index;
					invalidate();
				}
			}
			break;
		}
		return true;
	}

	/**
	 * ��ʼ��sidebar
	 */
	@SuppressWarnings("deprecation")
	public void init(){
		setBackgroundDrawable(new ColorDrawable(0x00000000));
		mChoosen = -1;
		invalidate();
		if (mLetterShow != null) {
			mLetterShow.setVisibility(View.INVISIBLE);
		}

	}
}
