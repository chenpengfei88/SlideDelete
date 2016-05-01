package com.qingcong.slidedelete;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by chenpengfei on 2016/4/30.
 */
public class SlideDeleteItemView extends RelativeLayout {

    public SlideDeleteItemView(Context context) {
        super(context);
    }

    public SlideDeleteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDeleteItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View oneView = getChildAt(0);
        oneView.layout(oneView.getLeft(), 0, oneView.getRight(), oneView.getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View oneView = getChildAt(0);
        View twoView = getChildAt(1);
        oneView.measure(MeasureSpec.makeMeasureSpec(oneView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(twoView.getMeasuredHeight(), MeasureSpec.EXACTLY));
    }
}
