package com.example.video.decode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.msopengles.utils.AndroidOS;
import com.example.video.base.MSBaseDecoder;
import com.example.video.extractor.MSVideoExtractor;
import com.example.video.inter.MSIExtractor;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/13 下午5:57
 * @Description: 视频解码器 主要承担视频解码的操作
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSVideoDecoder extends MSBaseDecoder {

    private static final String TAG = "MSVideoDecoder";

    private SurfaceView mSurfaceView;
    private Surface mSurface;

    /**
     * 通过构造方法将视频路径和Surface传进来
     *
     * @param filePath
     * @param surfaceView
     * @param surface
     */
    public MSVideoDecoder(String filePath, SurfaceView surfaceView, Surface surface) {
        super(filePath);
        this.mSurfaceView = surfaceView;
        this.mSurface = surface;
    }


    @Override
    protected boolean check() {
        if (mSurfaceView == null && mSurface == null) {
            Log.e(TAG, "SurfaceView和Surface都为空");
            if (mStateListener != null) {
                mStateListener.decoderError(this, "SurfaceView和Surface都为空");
            }
            return false;
        }
        return true;
    }

    @Override
    public MSIExtractor initExtractor(String path) {
        return new MSVideoExtractor(path);  //视频解码 让视频提取器去
    }

    @Override
    public void initSpecParams(MediaFormat format) {

    }

    @Override
    public boolean initRender() {
        return true;
    }

    /**
     * 配置编码器
     *
     * @param codec
     * @param format
     * @return
     */
    @Override
    public boolean configCodec(MediaCodec codec, MediaFormat format) {
        if (AndroidOS.VERSION_HIGH_OR_EQUAL_Q){
            Log.d("BaseDecoder","VERSION_HIGH_OR_EQUAL_Q----");
            mSurface = mSurfaceView.getHolder().getSurface();
            codec.configure(format, mSurface, null, 0);
            notifyDecode();
            return true;
        }else{
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
                @Override
                public void surfaceRedrawNeeded(@NonNull SurfaceHolder holder) {

                }

                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    Log.d("BaseDecoder","surfaceCreated----");
                    mSurface = surfaceHolder.getSurface();
                    //配置最好是放在这里  否则会报 The surface has been released  surface还没有初始化成功
                    codec.configure(format, mSurface, null, 0);
                    notifyDecode();
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    Log.d("BaseDecoder","surfaceChanged----");
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    Log.d("BaseDecoder","surfaceDestroyed----");
                }
            });
            return false;
        }

    }

    //进行绘制操作
    @Override
    public void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {

    }

    /**
     * 结束编码
     */
    @Override
    public void doneDecode() {

    }


}
