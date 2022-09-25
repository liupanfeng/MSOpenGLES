package com.example.video.extractor;

import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音视频 分离器
 *
 * 1.根据mime 类型定位到音频轨道和视频轨道
 * 2.seek 到指定的位置
 * 3.读取样本数据
 */
public class MSExtractor {

    private String mFilePath;
    /**
     * 媒体提取器
     */
    private MediaExtractor mMediaExtractor;

    /**
     * 音频轨道
     */
    private int mAudioTrack = -1;

    /**
     * 视频轨道
     */
    private int mVideoTrack = -1;

    /**
     * 时间戳
     */
    private long mCurSampleTime;

    /**
     * 当前帧的标记
     */
    private int mCurFrameFlag;

    /**
     * 开始解码位置
     */
    private long mStartPos;


    public MSExtractor(String filePath) {
        this.mFilePath = filePath;
        mMediaExtractor = new MediaExtractor();
        try {
            mMediaExtractor.setDataSource(mFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频轨道
     *
     * @return
     */
    public MediaFormat getVideoFormat() {
        int trackCount = mMediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mMediaExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                mVideoTrack = i;
                return trackFormat;
            }
        }
        return null;
    }


    public MediaFormat getAudioFormat() {
        int trackCount = mMediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mMediaExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                mAudioTrack = i;
                return trackFormat;
            }
        }
        return null;
    }

    /**
     * 读取视频数据
     *
     * @param byteBuffer
     * @return
     */
    public int readBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        selectSourceTrack();
        int sampleData = mMediaExtractor.readSampleData(byteBuffer, 0);
        if (sampleData < 0) {
            return -1;
        }
        mCurSampleTime = mMediaExtractor.getSampleTime();   //获取当前的时间戳
        mCurFrameFlag = mMediaExtractor.getSampleFlags();   //获取当前样本的flag  这个flag是啥？

        mMediaExtractor.advance();  //进入下一帧数据

        return sampleData;
    }

    /**
     * 选中资源轨道
     */
    private void selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mMediaExtractor.selectTrack(mVideoTrack);
        } else if (mAudioTrack >= 0) {
            mMediaExtractor.selectTrack(mAudioTrack);
        }
    }

    public long seek(long position){
        /**
         * 在指定的时间或者之前找到i帧
         */
        mMediaExtractor.seekTo(position,MediaExtractor.SEEK_TO_PREVIOUS_SYNC);

        return mMediaExtractor.getSampleTime();
    }


    public void stop(){
        mMediaExtractor.release();
        mMediaExtractor=null;
    }


    public int getVideoTrack(){
        return mVideoTrack;
    }


    public int getAudioTrack(){
        return mAudioTrack;
    }

    public long getCurSampleTime() {
        return mCurSampleTime;
    }

    public int getCurFrameFlag() {
        return mCurFrameFlag;
    }


    public void setStartPos(long mStartPos) {
        this.mStartPos = mStartPos;
    }
}
