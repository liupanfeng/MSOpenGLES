package com.example.msopengles.opengles;

import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.msopengles.utils.Constants;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MSGLRender implements GLSurfaceView.Renderer {

    private static final String TAG = "MSGLRender";
    private MSNativeRender mMSNativeRender;
    private int mSampleType;

    public MSGLRender() {
        mMSNativeRender=new MSNativeRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated() called with: gl = [" + gl10 + "], config = [" + eglConfig + "]");
        mMSNativeRender.native_OnSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d(TAG, "onSurfaceChanged() called with: gl = [" + gl10 + "], width = [" + width + "], height = [" + height + "]");
        mMSNativeRender.native_OnSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.d(TAG, "onDrawFrame() called with: gl = [" + gl10 + "]");
        mMSNativeRender.native_OnDrawFrame();
    }



    /**
     * 设置bitmap数据
     * @param imageFormatRgba
     * @param width
     * @param height
     * @param array
     */
    public void setBitmapData(int imageFormatRgba, int width, int height, byte[] array) {
        mMSNativeRender.native_set_bitmap_data(imageFormatRgba,width,height,array);
    }

    public void init() {
        mMSNativeRender.native_OnInit();
    }

    public void unInit() {
        mMSNativeRender.native_OnUnInit();
    }

    public void setParamsInt(int paramType, int value0, int value1) {
        if (paramType == Constants.SAMPLE_TYPE) {
            mSampleType = value0;
        }
        mMSNativeRender.native_SetParamsInt(paramType, value0, value1);
    }




}
