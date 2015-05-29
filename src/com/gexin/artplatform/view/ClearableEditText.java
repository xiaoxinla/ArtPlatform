package com.gexin.artplatform.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.gexin.artplatform.R;

public class ClearableEditText extends EditText implements
		OnFocusChangeListener, TextWatcher {

	// ɾ����ť
	private Drawable mClearDrawable;

	public ClearableEditText(Context context) {
		this(context, null);
	}

	public ClearableEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.clearbtn);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	/**
	 * ��Ϊ���ǲ���ֱ�Ӹ�EditText���õ���¼������������ü�ס���ǰ��µ�λ����ģ�����¼� �����ǰ��µ�λ�� �� EditText�Ŀ�� -
	 * ͼ�굽�ؼ��ұߵļ�� - ͼ��Ŀ�� �� EditText�Ŀ�� - ͼ�굽�ؼ��ұߵļ��֮�����Ǿ�������ͼ�꣬��ֱ����û�п���
	 */
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - mClearDrawable
							.getIntrinsicWidth())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if(hasFocus()){
			if(getText().length()>0){
				setClearIconVisible(true);
			}else {
				setClearIconVisible(false);
			}
		}
	}
	
	private void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], 
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

}
