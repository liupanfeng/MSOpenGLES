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

public class MSGLSurfaceView extends GLSurfaceView {

    private static final String TAG = "MyGLSurfaceView";
    private MSGLRender mMSGLRender;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;


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

