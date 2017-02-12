package com.cast.zys.cartainlayout;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 创建自定义的购物车动画实例 在点击按钮的时候产生抛物线效果
 * <p>
 * author by song-2017/2/11.21:40
 */

public class CartAnimLayout extends FrameLayout {
    public CartAnimLayout(Context context) {
        this(context, null);
    }

    public CartAnimLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CartAnimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //坐标
    private PointF mLocation = new PointF();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取当前控件在界面的屏幕坐标
        int[] layoutLoc = new int[2];
        getLocationInWindow(layoutLoc);
        mLocation.set(layoutLoc[0], layoutLoc[1]);
    }

    /**
     * 定义方法 开启动画
     *
     * @param startView    //开始控件
     * @param endView      //结束控件
     * @param layoutIdMove
     */
    public void startCartAnim(View startView, View endView, int layoutIdMove) {
        //获取开始的位置 放到int集合中
        int[] startLoc = new int[2];
        startView.getLocationInWindow(startLoc);
        PointF startF = new PointF(startLoc[0], startLoc[1]);

        //获取结束位置 放到int集合中
        int[] endLoc = new int[2];
        endView.getLocationInWindow(endLoc);
        final PointF endF = new PointF(endLoc[0], endLoc[1]);

        //将要添加的布局打气进当前布局
        final View moveView = LayoutInflater.from(getContext()).inflate(layoutIdMove, this, false);

        //开始动画
        //创建属性动画集合
        AnimatorSet set = new AnimatorSet();
        //创建缩放的动画
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(moveView, "scaleX", 1.0f, 0.3f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(moveView, "scaleY", 1.0f, 0.3f);
        //创建抛物线的动画
        ValueAnimator pathAnim = ObjectAnimator.ofObject(beisaier, startF, endF);
        //设置抛物线动画的监听
        pathAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取动画中的值
                PointF newPointF = (PointF) valueAnimator.getAnimatedValue();
                //将要执行动画的控件的坐标不断修改
                moveView.setX(newPointF.x);
                moveView.setY(newPointF.y);
            }
        });

        //设置动画集合一块加载
        set.playTogether(scaleXAnim, scaleYAnim, pathAnim);
        //设置动画的监听
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始的时候加载显示控件
                CartAnimLayout.this.addView(moveView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束的时候删除隐藏控件
                CartAnimLayout.this.removeView(moveView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        set.addListener(listener);
        //设置动画的时长
        set.setDuration(3000);
        //开启动画
        set.start();
    }

    //创建路径计算器
    private TypeEvaluator<PointF> beisaier = new TypeEvaluator<PointF>() {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //线性计算
            //贝塞尔曲线
            PointF newF = new PointF((startValue.x + endValue.x) / 2, 0);
            return BezierCurve.bezier(fraction, startValue, newF, endValue);
        }
    };

}
