package com.gexin.artplatform.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * �Զ�����ʽ����
 * 
 * @author xiaoxin 2015-4-9
 */
public class FlowLayout extends ViewGroup {

	private static final String TAG = "FlowLayout";

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// mode EXACTLY
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content mode AT_MOST
		int height = 0;
		int width = 0;
		// ��¼ÿһ�еĿ�Ⱥ͸߶�
		int lineWidth = 0;
		int lineHeight = 0;
		// �õ��ڲ�Ԫ�صĸ���
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			// ������View�Ŀ�Ⱥ͸߶�
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// �õ�LayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// ��viewռ�ݵĿ��
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// ��viewռ�ݵĸ߶�
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			// ����
			if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
				// �Աȵõ����Ŀ��
				width = Math.max(width, lineWidth);
				// ����lineWidth
				lineWidth = childWidth;
				// ��¼�и�
				height += lineHeight;
				lineHeight = childHeight;
			} else { // δ����
				// �����п�
				lineWidth += childWidth;
				// ȡ������и�
				lineHeight = Math.max(lineHeight, childHeight);
			}

			// ���һ���ؼ�
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}

		Log.v(TAG, "sizeWidth:" + sizeWidth + ",width:" + width);
		Log.v(TAG, "sizeHeight:" + sizeHeight + ",height:" + height);
		setMeasuredDimension(
				//
				modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
				modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop()+ getPaddingBottom()//
		);

//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// �洢���е�View�����д洢
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@SuppressLint("DrawAllocation") @Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		mAllViews.clear();
		mLineHeight.clear();
		// ��ǰViewGroup�Ŀ��
		int width = getWidth();
		int lineWidth = 0;
		int lineHeight = 0;

		List<View> lineViews = new ArrayList<View>();
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// �����Ҫ����
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
					- getPaddingLeft() - getPaddingRight()) {
				// ��¼LineHeight
				mLineHeight.add(lineHeight);
				// ��¼��ǰ�е�Views
				mAllViews.add(lineViews);

				// �������ǵ��п���и�
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				// �������ǵ�View����
				lineViews = new ArrayList<View>();
			}
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
			
		}
		// �������һ��
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		// ������View��λ��

		int left = getPaddingLeft();
		int top = getPaddingTop();

		// ����
		int lineNum = mAllViews.size();

		for (int i = 0; i < lineNum; i++)
		{
			// ��ǰ�е����е�View
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);

			for (int j = 0; j < lineViews.size(); j++)
			{
				View child = lineViews.get(j);
				// �ж�child��״̬
				if (child.getVisibility() == View.GONE)
				{
					continue;
				}

				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();

				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				// Ϊ��View���в���
				child.layout(lc, tc, rc, bc);

				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
			}
			left = getPaddingLeft() ; 
			top += lineHeight ; 
		}
	}

	// �뵱ǰViewGroup��Ӧ��LayoutParams
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}
}
