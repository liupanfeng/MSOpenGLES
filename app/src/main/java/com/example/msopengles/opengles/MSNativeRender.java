package com.example.msopengles.opengles;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 * @Author: lpf
 * @CreateDate: 2022/7/5 下午3:40
 * @Description:
 */

public class MSNativeRender {

    private static final String TAG = "MyGLSurfaceView";

    public native void native_OnInit();

    public native void native_OnUnInit();

    /*将bitmap数据 通过纹理 映射到surfaceView上*/
    public native void native_set_bitmap_data(int format, int width, int height, byte[] bytes);

    public native void native_OnSurfaceCreated();

    public native void native_OnSurfaceChanged(int width, int height);

    public native void native_OnDrawFrame();


    public native void native_SetParamsInt(int paramType, int value0, int value1);

    public native void native_SetParamsString(int paramType, int value0, String value1);


    public native void native_UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY);
}
