package com.example.msopengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.example.msopengles.utils.PathUtils;

import java.io.File;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/7/5 下午12:07
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSPlay extends GLSurfaceView implements Runnable, SurfaceHolder.Callback {

    public MSPlay(Context context) {
        this(context,null);
    }

    public MSPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void run() {
        open(PathUtils.getYUVDir()+ File.separator +"out.yuv",getHolder().getSurface());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public native void open(String path,Object surface);
}
