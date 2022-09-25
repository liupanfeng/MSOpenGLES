package com.example.video.base;

import android.media.MediaCodec;
import android.util.Log;

import com.example.video.MSFrame;
import com.example.video.inter.MSIEncodeStateListener;
import com.example.video.muxer.MSMuxer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:31
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public abstract class MSBaseEncoder implements Runnable {

    private static final String TAG = "BaseEncoder";

    /**
     * 目标视频宽，只有视频编码的时候才有效
     */
    protected int mWidth;

    protected int mHeight;

    /**
     * MP4 视频合成器
     */
    private MSMuxer mMuxer;

    private boolean mRunning = true;

    /**
     * 帧序列
     */
    private List<MSFrame> mFrames;


    private MediaCodec mCodec;

    private MediaCodec.BufferInfo mBufferInfo;

    private ByteBuffer[] mOutputBuffers;


    private ByteBuffer[] mInputBuffers;


    private Object mLock = new Object();

    private boolean mIsEOS = false;

    private MSIEncodeStateListener mStateListener;


    public MSBaseEncoder(int mWidth, int mHeight, MSMuxer mMuxer) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mMuxer = mMuxer;

        initCodec();
    }

    /**
     * 初始化编码器
     */
    private void initCodec() {
        try {
            mCodec = MediaCodec.createEncoderByType(encodeType());
            configEncoder(mCodec);
            mCodec.start();

            mOutputBuffers = mCodec.getOutputBuffers();
            mInputBuffers = mCodec.getInputBuffers();
            Log.d(TAG, "编码器初始化完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        loopEncode();
        done();
    }

    /**
     * 循环编码
     */
    private synchronized void loopEncode() {
        Log.d(TAG, "开始编码");
        while (mRunning && !mIsEOS) {
            boolean empty = mFrames.isEmpty();
            if (empty) {
                try {
                    mLock.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!mFrames.isEmpty()) {
                MSFrame frame = mFrames.remove(0);
                if (encodeManually()) {
                    encode(frame);
                } else if (frame.buffer == null) {  // 如果是自动编码（比如视频），遇到结束帧的时候，直接结束掉
                    Log.e(TAG, "发送编码结束标志");
                    mCodec.signalEndOfInputStream();
                    mIsEOS = true;
                }

            }

            drain();
        }

    }

    private void encode(MSFrame frame) {
        
    }

    /**
     * 榨干编码输出数据
     */
    private void drain() {
        
    }


    private void justWait() {
        synchronized (mLock) {
            try {
                mLock.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void done() {

    }


    /**
     * 子类配置编码器
     */
    protected abstract void configEncoder(MediaCodec mCodec);


    protected abstract String encodeType();

    private boolean encodeManually() {
        return true;
    }
}
