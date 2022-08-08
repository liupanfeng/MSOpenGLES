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

public class MSGLSurfaceView extends GLSurfaceView {

    private static final String TAG = "MyGLSurfaceView";
    private MSGLRender mMSGLRender;

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
        /*GLSurfaceView设置render*/
        setRenderer(mMSGLRender);
        /*设置render模式为自动模式  耗性能  RENDERMODE_WHEN_DIRTY：通知刷新才会刷新 这里是demo 就使用自动模式了*/
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MSGLRender getNativeRender() {
        return mMSGLRender;
    }
}

