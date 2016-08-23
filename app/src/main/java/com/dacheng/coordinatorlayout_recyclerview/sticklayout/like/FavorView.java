/*
 * Copyright (C) 2015, 程序亦非猿
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dacheng.coordinatorlayout_recyclerview.sticklayout.like;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dacheng.coordinatorlayout_recyclerview.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * 直播点击的动画view
 */

public class FavorView extends RelativeLayout {
    private static Handler mTimer = new Handler(Looper.getMainLooper());
    private LikeRunnable mLikeRunnable = new LikeRunnable();
    private int mDelay = 500;
    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] interpolators;
    private int mHeight;// 整个layout 高度
    private int mWidth; // 整个layout 宽度
    private LayoutParams lp;// ImageView 的lp
    private Drawable[] drawables;//存储 drawable 的数组
    private static int DRAWABLE_NUM = 3;// drawable 个数
    private Random random = new Random();
    private int dHeight;//drawable 的高度
    private int dWidth;// drawable 宽高
    private Queue<Animator> mAnimatorQueue = new LinkedList<Animator>();//缓存ImageView的队列
    private Queue<ImageView> mImageViewQueue = new LinkedList<ImageView>();
    private boolean mStop = true;
    public FavorView(Context context) {
        super(context);
        init();
    }

    public FavorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        //初始化显示的图片
        drawables = new Drawable[DRAWABLE_NUM];
        Drawable red = getResources().getDrawable(R.mipmap.pl_red);
        Drawable yellow = getResources().getDrawable(R.mipmap.pl_yellow);
        Drawable blue = getResources().getDrawable(R.mipmap.pl_blue);

        drawables[0] = red;
        drawables[1] = yellow;
        drawables[2] = blue;
        //获取图的宽高 用于后面的计算
        //注意 我这里3张图片的大小都是一样的,所以我只取了一个
        dHeight = red.getIntrinsicHeight();
        dWidth = red.getIntrinsicWidth();

        //默认底部并且水平居中
        lp = new LayoutParams(dWidth, dHeight);
        lp.addRule(CENTER_HORIZONTAL, TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;

    }

    /**
     * 设置图片
     */
    public void setDrawables(Drawable[] drawables){
        if(drawables != null && drawables.length != 0){
            this.drawables = drawables;
            DRAWABLE_NUM = drawables.length;
            //这里默认取第一个drawable的宽高
            dHeight = drawables[0].getIntrinsicHeight();
            dWidth = drawables[0].getIntrinsicWidth();
        }

    }
    /**
     * 自动开始动画
     */
    public void startAnim(int num){
        if(!mStop){return;}
        if(num > 10000){
            mDelay = 100;
        }else if(num > 5000){
            mDelay = 200;
        }else if(num > 1000){
            mDelay = 300;
        }
        stopAnim();
        mStop = false;
        mTimer.postDelayed(mLikeRunnable, mDelay);
        
    }

    public void stopAnim() {
        mStop = true;
        mTimer.removeCallbacks(mLikeRunnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }


    public void addHeart() {

        View  view = new View(getContext());
        view.setBackground(drawables[random.nextInt(DRAWABLE_NUM)]);
        view.setLayoutParams(lp);
        addView(view);
        Animator set = getAnimator(view);
        set.addListener(new AnimEndListener(view));
        set.start();

    }

    private Animator getAnimator(View target) {
        //进入动画：进入时有透明度变化和缩放的过程
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);
        enter.setInterpolator(interpolators[random.nextInt(4)]);
        enter.playTogether(alpha, scaleX, scaleY,bezierValueAnimator);
        enter.setTarget(target);

        return enter;
    }


    /**
     * 贝塞尔动画
     * @param target
     * @return
     */
    private ValueAnimator getBezierValueAnimator(View target) {

        //初始化一个贝塞尔计算器- - 传入
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        //这里最好画个图 理解一下 传入了起点 和 终点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(1000);
        return animator;
    }

    /**
     * 获取（三阶贝塞尔）中间的两个点
     * @param scale
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View mImageView;
        public AnimEndListener(View imageView) {
            mImageView = imageView;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(mImageView);
        }


    }


    private class LikeRunnable implements Runnable {

        @Override
        public void run() {
            if (!mStop) {
                addHeart();
                mTimer.postDelayed(this, mDelay);
            }
        }
    }
}
