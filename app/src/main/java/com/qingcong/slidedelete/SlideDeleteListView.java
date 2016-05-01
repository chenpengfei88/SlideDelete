package com.qingcong.slidedelete;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by chenpengfei on 2016/4/30.
 */
public class SlideDeleteListView extends ListView implements View.OnTouchListener {

    private View currentSlideView;
    private View deleteView;
    private int slideViewWidth;
    private float mInitialMotionY;
    private float mInitalMotionX;
    // 检测到手机的最小滑动值
    private int mTouchSlop;
    private boolean isSlideDelete;
    private boolean isList;
    //拖动的总大小
    private boolean isScroll;
    private ValueAnimator valueAnimator;
    private boolean intercept;
    private int currentVisiblePosition;
    private int lastVisiblePosition;
    private View item;
    private onDeleteListener onDeleteListener;
    private int currentPositioin;

    public SlideDeleteListView(Context context) {
        super(context);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.setOnTouchListener(this);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSlideDelete = false;
                isList = false;
                isScroll = false;
                intercept = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(isList) {
            currentSlideView = null;
            if(deleteView != null)
                deleteView.setVisibility(View.INVISIBLE);
            return false;
        }
        if(isScroll) {
            return true;
        }
        ListView listView = (ListView) v;
        if(currentSlideView == null) {
            event.setAction(MotionEvent.ACTION_DOWN);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return getView(listView, event);
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                final float yDiff = y - mInitialMotionY;
                final float x = event.getX();
                final float xDiff = x - mInitalMotionX;
                //如果是X轴事件就不拦截
                if ((isSlideDelete || Math.abs(xDiff) > mTouchSlop && Math.abs(xDiff) * 0.5f > Math.abs(yDiff)) && currentSlideView!= null && !isScroll) {
                    //向左滑动
                    int left;
                    if(xDiff < 0) {
                         left = currentSlideView.getLeft() + (int)xDiff <= - slideViewWidth ? - slideViewWidth : currentSlideView.getLeft() + (int)xDiff;
                    } else {
                         left = currentSlideView.getLeft() + (int)xDiff >= 0 ? 0 : currentSlideView.getLeft() + (int)xDiff;
                    }
                    currentSlideView.layout(left, currentSlideView.getTop(), left + currentSlideView.getMeasuredWidth(), currentSlideView.getBottom());
                    mInitalMotionX = x;
                    isSlideDelete = true;
                    intercept = true;
                } else {
                    if(Math.abs(yDiff) > mTouchSlop) {
                        isList = true;
                        intercept = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(currentSlideView != null) {
                    intercept = true;
                    if(!isScroll) {
                        if(currentSlideView.getLeft() != 0) {
                            isScroll = true;
                            performAnimate(currentSlideView.getLeft(),  currentSlideView.getLeft() < -slideViewWidth / 2 ? - slideViewWidth : 0);
                        } else {
                            currentSlideView = null;
                            deleteView.setVisibility(View.INVISIBLE);
                        }
                        if(!isSlideDelete)
                            intercept = false;
                        else
                            intercept = true;
                    }
                }
        }
        return intercept;
    }

    public boolean getView(ListView listView, MotionEvent event) {
        if(currentSlideView != null) {
            if(!isScroll && currentSlideView.getLeft() != 0) {
                isScroll = true;
                performAnimate(currentSlideView.getLeft(), 0);
            } else {
                deleteView.setVisibility(View.VISIBLE);
            }
        } else {
            mInitialMotionY = event.getY();
            mInitalMotionX = event.getX();
            //根据xy得到当前view在listview中是第几个
             currentPositioin = listView.pointToPosition((int) mInitalMotionX, (int) mInitialMotionY);
            //当前view在可视范围内是第几个
            currentVisiblePosition = currentPositioin - listView.getFirstVisiblePosition();
            lastVisiblePosition = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
            //得到当前操作的view
            item = listView.getChildAt(currentVisiblePosition);
            if(item == null) return false;
            currentSlideView = item.findViewById(R.id.content_id);
            deleteView = item.findViewById(R.id.text_delete);
            deleteView.setVisibility(View.VISIBLE);
            deleteView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentSlideView.getLeft() == -slideViewWidth && !isScroll) {
                        currentSlideView.layout(0, currentSlideView.getTop(), currentSlideView.getMeasuredWidth(), currentSlideView.getBottom());
                        onDeleteListener.onDelete(currentPositioin);
                        deleteView.setVisibility(View.INVISIBLE);
                        currentSlideView = null;
                    }
                }
            });
            slideViewWidth = deleteView.getMeasuredWidth();
        }
        return false;
    }

    /**
     * 滑动动画
     */
    private void performAnimate(final int start, final int end) {
        valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator mEvaluator = new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if(currentSlideView == null) return;
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                float fraction = currentValue / 100f;
                int left = mEvaluator.evaluate(fraction, start, end);
                currentSlideView.layout(left, currentSlideView.getTop(), left + currentSlideView.getMeasuredWidth(), currentSlideView.getBottom());
                if(currentValue == 100) {
                    if(currentSlideView.getLeft() == 0) {
                        currentSlideView = null;
                        deleteView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        valueAnimator.setDuration(Math.abs(start - end) / mTouchSlop * 12).start();
    }

    public SlideDeleteListView.onDeleteListener getOnDeleteListener() {
        return onDeleteListener;
    }

    public void setOnDeleteListener(SlideDeleteListView.onDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface onDeleteListener{
        public void onDelete(int p);
    }

}
