package com.example.video.decode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.video.DecodeState;
import com.example.video.base.BaseDecoder;
import com.example.video.extractor.VideoExtractor;
import com.example.video.inter.IExtractor;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/13 下午5:57
 * @Description:  视频解码器 主要承担视频解码的操作
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSVideoDecoder extends BaseDecoder {

    private static final String TAG="MSVideoDecoder";

    private SurfaceView mSurfaceView;
    private Surface mSurface;

    /**
     * 通过构造方法将视频路径和Surface传进来
     * @param filePath
     * @param surfaceView
     * @param surface
     */
    public MSVideoDecoder(String filePath,SurfaceView surfaceView,Surface surface) {
        super(filePath);
        this.mSurfaceView=surfaceView;
        this.mSurface=surface;
    }

    @Override
    protected boolean check() {
        if (mSurfaceView == null && mSurface == null) {
            Log.e(TAG, "SurfaceView和Surface都为空");
            mStateListener.decoderError(this, "SurfaceView和Surface都为空");
            return false;
        }
        return true;
    }

    @Override
    public IExtractor initExtractor(String path) {
        return new VideoExtractor(path);  //视频解码 让视频提取器去
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
     * @param codec
     * @param format
     * @return
     */
    @Override
    public boolean configCodec(MediaCodec codec, MediaFormat format) {
        if (mSurface!=null){
            codec.configure(format,mSurface,null,0);
            notifyDecode();
        }else if (mSurfaceView.getHolder().getSurface()!=null){
            mSurface = mSurfaceView.getHolder().getSurface();
            configCodec(codec, format);
        }else {
           mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
               @Override
               public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                   mSurface = surfaceHolder.getSurface();
                   configCodec(codec, format);
               }

               @Override
               public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

               }

               @Override
               public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

               }
           });
            return false;
        }

        return true;
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
