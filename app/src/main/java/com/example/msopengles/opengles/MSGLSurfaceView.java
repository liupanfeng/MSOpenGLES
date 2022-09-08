package com.example.msopengles.opengles;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/7/5 下午3:39
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import static com.example.msopengles.utils.Constants.*;

public class MSGLSurfaceView extends GLSurfaceView {

    private static final String TAG = "MyGLSurfaceView";
    private MSGLRender mMSGLRender;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;



    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    public static final int IMAGE_FORMAT_RGBA = 0x01;
    public static final int IMAGE_FORMAT_NV21 = 0x02;
    public static final int IMAGE_FORMAT_NV12 = 0x03;
    public static final int IMAGE_FORMAT_I420 = 0x04;
    public static final int IMAGE_FORMAT_YUYV = 0x05;
    public static final int IMAGE_FORMAT_GARY = 0x06;

    private float mPreviousY;
    private float mPreviousX;
    private int mXAngle;
    private int mYAngle;



    private ScaleGestureDetector mScaleGestureDetector;
    private float mPreScale = 1.0f;
    private float mCurScale = 1.0f;
    private long mLastMultiTouchTime;


    public MSGLSurfaceView(Context context) {
        this(context, null);
    }

    public MSGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*设置EGL 版本号*/
        this.setEGLContextClientVersion(3);
        /*设置EGL，给出条件 让系统选择一个合适的*/
        setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        /*初始化render*/
        mMSGLRender = new MSGLRender();
        Log.e("lpf","完成初始化mMSGLRender -----");
        /*GLSurfaceView设置render*/
        setRenderer(mMSGLRender);
        /*设置render模式为自动模式  耗性能  RENDERMODE_WHEN_DIRTY：通知刷新才会刷新 这里是demo 就使用自动模式了*/
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MSGLRender getNativeRender() {
        Log.e("lpf","获取 mMSGLRender ----");
        return mMSGLRender;
    }

    public void setAspectRatio(int width, int height) {
        Log.d(TAG, "setAspectRatio() called with: width = [" + width + "], height = [" + height + "]");
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }

        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }



    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getPointerCount() == 1) {
            consumeTouchEvent(e);
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - mLastMultiTouchTime > 200)
            {
                float y = e.getY();
                float x = e.getX();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float dy = y - mPreviousY;
                        float dx = x - mPreviousX;
                        mYAngle += dx * TOUCH_SCALE_FACTOR;
                        mXAngle += dy * TOUCH_SCALE_FACTOR;
                }
                mPreviousY = y;
                mPreviousX = x;

                switch (mMSGLRender.getSampleType()) {
                    case SAMPLE_TYPE_FBO_LEG:
                    case SAMPLE_TYPE_BASIC_LIGHTING:
                        mMSGLRender.updateTransformMatrix(mXAngle, mYAngle, mCurScale, mCurScale);
                        requestRender();
                        break;
                    default:
                        break;
                }
            }

        } else {
            mScaleGestureDetector.onTouchEvent(e);
        }

        return true;
    }



    public void consumeTouchEvent(MotionEvent e) {
        dealClickEvent(e);
        float touchX = -1, touchY = -1;
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                touchX = e.getX();
                touchY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchX = -1;
                touchY = -1;
                break;
            default:
                break;
        }

        //滑动、触摸
//        switch (mMSGLRender.getSampleType()) {
//            case SAMPLE_TYPE_KEY_SCRATCH_CARD:
//                mGLRender.setTouchLoc(touchX, touchY);
//                requestRender();
//                break;
//            default:
//                break;
//        }

        //点击
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //touchX = e.getX();
                //touchY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

    }


    public void dealClickEvent(MotionEvent e) {
        float touchX = -1, touchY = -1;
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = e.getX();
                touchY = e.getY();
            {
                //点击
//                switch (mGLRender.getSampleType()) {
//                    case SAMPLE_TYPE_KEY_SHOCK_WAVE:
//                        mGLRender.setTouchLoc(touchX, touchY);
//                        break;
//                    default:
//                        break;
//                }
            }
            break;
            default:
                break;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

}

