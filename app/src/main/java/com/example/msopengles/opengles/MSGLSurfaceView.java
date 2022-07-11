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
    private MSNativeRender mNativeRender;

    public MSGLSurfaceView(Context context) {
        this(context, null);
    }

    public MSGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(3);
//        setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        mNativeRender = new MSNativeRender();
        setRenderer(mNativeRender);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MSNativeRender getNativeRender() {
        return mNativeRender;
    }
}

