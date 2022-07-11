package com.example.msopengles.opengles;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/7/5 下午3:40
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */

public class MSNativeRender implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLSurfaceView";

    public native void native_OnInit();

    public native void native_OnUnInit();

    public native void native_SetImageData(int format, int width, int height, byte[] bytes);

    public native void native_OnSurfaceCreated();

    public native void native_OnSurfaceChanged(int width, int height);

    public native void native_OnDrawFrame();

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated() called with: gl = [" + gl10 + "], config = [" + eglConfig + "]");
        native_OnSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d(TAG, "onSurfaceChanged() called with: gl = [" + gl10 + "], width = [" + width + "], height = [" + height + "]");
        native_OnSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.d(TAG, "onDrawFrame() called with: gl = [" + gl10 + "]");
        native_OnDrawFrame();
    }
}
