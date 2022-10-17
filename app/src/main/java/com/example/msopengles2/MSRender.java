package com.example.msopengles2;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/10/17 下午5:44
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSRender implements GLSurfaceView.Renderer {

    static {
        System.loadLibrary("msopengles");
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        jniInitGL();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        jniResizeGL(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        jniDrawGL();
    }

    private native void jniInitGL();

    private native void jniDrawGL();

    private native void jniResizeGL(int width,int height);


}
