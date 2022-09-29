package com.example.video.base;

import android.media.MediaCodec;
import android.media.MediaFormat;
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
    protected int mWidth = -1;

    protected int mHeight = -1;

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
    /**
     * 当前编码帧信息
     */
    private MediaCodec.BufferInfo mBufferInfo;

    /**
     * 编码输出缓冲区
     */
    private ByteBuffer[] mOutputBuffers;

    /**
     * 编码输入缓冲区
     */
    private ByteBuffer[] mInputBuffers;


    private Object mLock = new Object();

    /**
     * 是否编码结束
     */
    private boolean mIsEOS = false;
    /**
     * 编码状态监听器
     */
    private MSIEncodeStateListener mStateListener;


    public MSBaseEncoder( MSMuxer mMuxer) {
        this.mMuxer = mMuxer;
        initCodec();
    }

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
                if (encodeManually()) {//手动编码
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
        int index = mCodec.dequeueInputBuffer(-1);  //-1 表示一直等待
        /*向编码器输入数据*/
        if (index>0){
            ByteBuffer inputBuffer = mInputBuffers[index];
            inputBuffer.clear();
            if (frame.buffer!=null){
                inputBuffer.put(frame.buffer);
            }
        }

        if (frame.buffer == null || frame.bufferInfo.size <= 0) { // 小于等于0时，为结束符标记
            mCodec.queueInputBuffer(index,0,0,frame.bufferInfo.presentationTimeUs,MediaCodec.BUFFER_FLAG_END_OF_STREAM);
        }else {
            frame.buffer.flip();
            frame.buffer.mark();
            mCodec.queueInputBuffer(index,0,frame.bufferInfo.size,frame.bufferInfo.presentationTimeUs,0);
        }

        frame.buffer.clear();

    }

    /**
     * 编码输出数据 全部处理完
     */
    private void drain() {
        while (!mIsEOS){
            int index=mCodec.dequeueOutputBuffer(mBufferInfo,1000);
            if (index== MediaCodec.INFO_TRY_AGAIN_LATER){

            } else if (index ==  MediaCodec.INFO_OUTPUT_FORMAT_CHANGED ){
                addTrack(mMuxer, mCodec.getOutputFormat());
            } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED){
                mOutputBuffers = mCodec.getOutputBuffers();
            }else {
                if (mBufferInfo.flags==MediaCodec.BUFFER_FLAG_END_OF_STREAM){
                    mIsEOS=true;
                    mBufferInfo.set(0,0,0,mBufferInfo.flags);
                    Log.d(TAG, "编码结束");
                }

                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                    // SPS or PPS, which should be passed by MediaFormat.
                    mCodec.releaseOutputBuffer(index, false);
                    continue;
                }

                if (!mIsEOS) {
                    writeData(mMuxer, mOutputBuffers[index], mBufferInfo);
                }

                mCodec.releaseOutputBuffer(index, false);
            }

        }
    }


    /**
     * 编码结束，是否资源
     */
    private void done() {
        try {
            Log.i(TAG, "release");
            release(mMuxer);
            mCodec.stop();
            mCodec.release();
            mRunning = false;
            mStateListener.encoderFinish(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void release(MSMuxer mMuxer);


    /**
     * 往mp4写入音视频数据
     */
    protected abstract void writeData(MSMuxer mMuxer, ByteBuffer mOutputBuffer, MediaCodec.BufferInfo mBufferInfo);


    private void justWait() {
        synchronized (mLock) {
            try {
                mLock.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通知继续编码
     */
    private void notifyGo() {
        try {
            synchronized(mLock) {
                mLock.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 子类配置编码器
     */
    protected abstract void configEncoder(MediaCodec mCodec);


    protected abstract String encodeType();

    /**
     * 视频编码通过Surface，MediaCodec自动完成编码；音频数据需要用户自己压入编码缓冲区，完成编码
     * @return  现在默认使用手动编码
     */
    private boolean encodeManually() {
        return true;
    }


    /**
     * 将一帧数据压入队列，等待编码
     */
    void encodeOneFrame(MSFrame frame) throws InterruptedException {
        synchronized(mFrames) {
            mFrames.add(frame);
        }
        notifyGo();
        // 延时一点时间，避免掉帧
        Thread.sleep(frameWaitTimeMs());
    }

    /**
     * 通知结束编码
     */
    void endOfStream() {
        synchronized(mFrames) {
            MSFrame frame = new MSFrame();
            frame.buffer = null;
            mFrames.add(frame);
            notifyGo();
        }
    }

    /**
     * 设置状态监听器
     */
    void setStateListener(MSIEncodeStateListener l ) {
        this.mStateListener = l;
    }

    /**
     * 每一帧排队等待时间
     */
    long frameWaitTimeMs(){
        return 20L;
    }

    /**
     * 配置mp4音视频轨道
     */
    public abstract void addTrack(MSMuxer muxer, MediaFormat mediaFormat);


}
