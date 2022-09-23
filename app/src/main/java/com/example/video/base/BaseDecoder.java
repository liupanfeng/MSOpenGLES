package com.example.video.base;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.example.video.DecodeState;
import com.example.video.Frame;
import com.example.video.inter.IDecoder;
import com.example.video.inter.IDecoderProgress;
import com.example.video.inter.IDecoderStateListener;
import com.example.video.inter.IExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:02
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public abstract class BaseDecoder implements IDecoder {

    private final String TAG = "BaseDecoder";

    private String mFilePath;

    /**
     * 解码器是否在运行
     */
    private boolean mIsRunning = true;

    /**
     * 线程等待锁
     */
    private Object mLock = new Object();

    /**
     * 是否可以进入解码
     */
    private boolean mReadyForDecode = false;


    /**
     * 音视频解码器
     */
    private MediaCodec mCodec = null;

    /**
     * 音视频数据读取器
     */
    private IExtractor mExtractor = null;

    /**
     * 解码输入缓存区
     */
    private ByteBuffer[] mInputBuffers = null;

    /**
     * 解码输出缓存区
     */
    private ByteBuffer[] mOutputBuffers = null;


    /**
     * 解码数据信息
     */
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private DecodeState mState = DecodeState.STOP;

    protected IDecoderStateListener mStateListener = null;

    /**
     * 流数据是否结束
     */
    private boolean mIsEOS = false;

    protected int mVideoWidth = 0;

    protected int mVideoHeight = 0;

    private long mDuration = 0;

    private long mStartPos = 0;

    private long mEndPos = 0;

    /**
     * 开始解码时间，用于音视频同步
     */
    private long mStartTimeForSync = -1L;


    // 是否需要音视频渲染同步
    private boolean mSyncRender = true;

    public BaseDecoder(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    @Override
    public void pause() {

    }

    @Override
    public void goOn() {

    }

    @Override
    public long seekTo(long position) {
        return 0;
    }

    @Override
    public long seekAndPlay(long position) {
        return 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isDecoding() {
        return false;
    }

    @Override
    public boolean isSeeking() {
        return false;
    }

    @Override
    public boolean isStop() {
        return false;
    }

    @Override
    public void setSizeListener(IDecoderProgress iDecoderProgress) {

    }

    @Override
    public void setStateListener() {

    }

    @Override
    final public void run() {
        if (mState == DecodeState.STOP) {
            mState = DecodeState.START;
        }
        mStateListener.decoderPrepare(this);

        //【解码步骤：1. 初始化，并启动解码器】
        if (!init()) {
            return;
        }

        Log.d(TAG, "开始解码");

        try {
            while (mIsRunning){
                if (mState != DecodeState.START &&
                        mState != DecodeState.DECODING &&
                        mState != DecodeState.SEEKING) {
                    Log.i(TAG, "进入等待：$mState");

                    waitDecode();

                    // ---------【同步时间矫正】-------------
                    //恢复同步的起始时间，即去除等待流失的时间
                    mStartTimeForSync = System.currentTimeMillis() - getCurTimeStamp();
                }

                if (!mIsRunning ||
                        mState == DecodeState.STOP) {
                    mIsRunning = false;
                    break;
                }

                if (mStartTimeForSync == -1L) {
                    mStartTimeForSync = System.currentTimeMillis();
                }

                //如果数据没有解码完毕，将数据推入解码器解码
                if (!mIsEOS) {
                    //【解码步骤：2. 见数据压入解码器输入缓冲】
                    mIsEOS = pushBufferToDecoder();
                }
                //【解码步骤：3. 将解码好的数据从缓冲区拉取出来】
                int index = pullBufferFromDecoder();
                if (index >= 0) {
                    // ---------【音视频同步】-------------
                    if (mSyncRender && mState == DecodeState.DECODING) {
                        sleepRender();
                    }
                    //【解码步骤：4. 渲染】
                    if (mSyncRender) {// 如果只是用于编码合成新视频，无需渲染
                        render(mOutputBuffers[index], mBufferInfo);
                    }

                    //将解码数据传递出去
                    Frame frame = new Frame();
                    frame.buffer = mOutputBuffers[index];
                    frame.setBufferInfo(mBufferInfo);
                    mStateListener.decodeOneFrame(this, frame);

                    //【解码步骤：5. 释放输出缓冲】
                    mCodec.releaseOutputBuffer(index, true);

                    if (mState == DecodeState.START) {
                        mState = DecodeState.PAUSE;
                    }
                }

                //【解码步骤：6. 判断解码是否完成】
                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    Log.i(TAG, "解码结束");
                    mState = DecodeState.FINISH;
                    mStateListener.decoderFinish(this);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }





    private void sleepRender() {
        long passTime = System.currentTimeMillis() - mStartTimeForSync;
        long curTime = getCurTimeStamp();
        if (curTime > passTime) {
            try {
                Thread.sleep(curTime - passTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int pullBufferFromDecoder() {
        // 查询是否有解码完成的数据，index >=0 时，表示数据有效，并且index为缓冲区索引
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        if (index==MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){

        }else if (index==MediaCodec.INFO_TRY_AGAIN_LATER){

        }else if ( MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED==index){
            mOutputBuffers = mCodec.getOutputBuffers();
        }
        return index;
    }

    protected  boolean pushBufferToDecoder(){
        int inputBufferIndex = mCodec.dequeueInputBuffer(1000);
        boolean isEndOfStream = false;
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);
            if (sampleSize < 0) {
                //如果数据已经取完，压入数据结束标志：MediaCodec.BUFFER_FLAG_END_OF_STREAM
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0,
                        0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                mCodec.queueInputBuffer(inputBufferIndex, 0,
                        sampleSize, mExtractor.getCurrentTimestamp(), 0);
            }
        }
        return isEndOfStream;
    }

    private boolean init() {
        if (mFilePath.isEmpty() || !new File(mFilePath).exists()) {
            Log.d(TAG, "文件路径为空");
            mStateListener.decoderError(this, "文件路径为空");
            return false;
        }

        if (!check()) {
            return false;
        }

        //初始化数据提取器
        mExtractor = initExtractor(mFilePath);

        if (mExtractor == null ||
                mExtractor.getFormat() == null) {
            Log.d(TAG, "无法解析文件");
            return false;
        }

        //初始化参数
        if (!initParams()) {
            return false;
        }

        //初始化渲染器
        if (!initRender()) {
            return false;
        }

        //初始化解码器
        if (!initCodec()) {
            return false;
        }
        return true;
    }

    /**
     * 检查子类参数
     */
    protected abstract boolean check();


    /**
     * 初始化数据提取器
     */
    public abstract IExtractor initExtractor(String path);


    private boolean initParams() {
        try {
            MediaFormat format = mExtractor.getFormat();
            mDuration = format.getLong(MediaFormat.KEY_DURATION) / 1000;
            if (mEndPos == 0L) {
                mEndPos = mDuration;
            }
            initSpecParams(mExtractor.getFormat());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean initCodec() {
        try {
            String type = mExtractor.getFormat().getString(MediaFormat.KEY_MIME);
            mCodec = MediaCodec.createDecoderByType(type);
            if (!configCodec(mCodec, mExtractor.getFormat())) {
                waitDecode();
            }
            mCodec.start();
            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 解码线程进入等待
     */
    private void waitDecode() {
        try {
            if (mState == DecodeState.PAUSE) {
                mStateListener.decoderPause(this);
            }
            synchronized (mLock) {
                mLock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化子类自己特有的参数
     */
    public abstract void initSpecParams(MediaFormat format);


    /**
     * 初始化渲染器
     */
    public abstract boolean initRender();


    /**
     * 配置解码器
     */
    public abstract boolean configCodec(MediaCodec codec, MediaFormat format);


    private long getCurTimeStamp() {
        return mBufferInfo.presentationTimeUs / 1000;
    }


    /**
     * 渲染
     */
   public abstract void  render(ByteBuffer outputBuffer ,
                        MediaCodec.BufferInfo bufferInfo);

    /**
     * 结束解码
     */
    public abstract void doneDecode();


    /**
     * 通知解码线程继续运行
     */
    protected void notifyDecode() {
        synchronized(mLock) {
            mLock.notifyAll();
        }
        if (mState == DecodeState.DECODING) {
            mStateListener.decoderRunning(this);
        }
    }
}
