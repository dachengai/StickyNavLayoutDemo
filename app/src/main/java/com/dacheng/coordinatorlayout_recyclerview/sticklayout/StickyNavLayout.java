package com.dacheng.coordinatorlayout_recyclerview.sticklayout;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.dacheng.coordinatorlayout_recyclerview.R;

/**
 * StickyNavLayout 上移nav悬停效果
 * 三部分：header(头部) nav(导航) content(内容)
 */
public class StickyNavLayout extends LinearLayout implements NestedScrollingParent
{
    private static final String TAG = "StickyNavLayout";
    private View mHeader;//头部view
    private View mNav;//导航view
    private ViewGroup mContentView;//内容视图
    private int mHeaderViewHeight;//header 高度
    private OverScroller mScroller;


    /**
     *  child 开发始滑动时会回调这个方法，是否需要配合 child 一起进行处理滑动，
     *  如果需要配合，还会回调 onNestedScrollAccepted()
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target)
    {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        Log.e(TAG, "onNestedScroll");
    }

    /**
     * 每次child滑动前 都会回调到 Parent 的 onNestedPreScroll()，
     * Parent 可以在这个回调中“劫持”掉 Child 的滑动，也就是先于 Child 滑动
     * dy < 0 (向下滑动) dy > 0 (向上滑动)
     * @param target
     * @param dx
     * @param dy
     * @param consumed  The horizontal and vertical scroll distance consumed by this parent
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
        Log.e(TAG, "onNestedPreScroll"+"dy = "+dy);
        //dy > 0且滑动距离小于头部高度 stickynavlayout需要向上滑动来隐藏header
        boolean hiddenTop = dy > 0 && getScrollY() < mHeaderViewHeight;
        //dy < 0 且滑动距离大于0 且target在垂直方向是不可以滑动的  这时stickynavlayout需要向下滑动来显示header
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop)
        {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed)
    {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY)
    {
        Log.e(TAG, "onNestedPreFling");
        if (getScrollY() >= mHeaderViewHeight) return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes()
    {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }



    public StickyNavLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }

    /**
     * 结束填充view时，获取三部分对应的view
     */
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mHeader = findViewById(R.id.id_stickynavlayout_header);
        mNav = findViewById(R.id.id_stickynavlayout_nav);
        View view = findViewById(R.id.id_stickynavlayout_content);
        if (!(view instanceof ViewGroup))
        {
            throw new RuntimeException(
                    "id_stickynavlayout_content should be ViewGroup's children and implement NestedScrollingChild!");
        }
        mContentView = (ViewPager) view;
    }

    /**
     * 测量stickynavlayout高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mContentView.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mHeader.getMeasuredHeight() + mNav.getMeasuredHeight() + mContentView.getMeasuredHeight());

    }

    /**
     * 当view大小有变动时，记录头部高度
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderViewHeight = mHeader.getMeasuredHeight();
    }


    public void fling(int velocityY)
    {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mHeaderViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y)
    {
        if (y < 0)
        {
            y = 0;
        }
        if (y > mHeaderViewHeight)
        {
            y = mHeaderViewHeight;
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
    }

    /**
     * 计算滑动，实现弹性滑动
     */
    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }


}
