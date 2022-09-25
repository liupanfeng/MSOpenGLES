package com.example.video.extractor;

import android.media.MediaFormat;

import com.example.video.inter.MSIExtractor;

import java.nio.ByteBuffer;

/**
 * @Author: lpf
 * @CreateDate: 2022/9/20 下午2:36
 * @Description: 视频媒体提取器
 */
public class MSVideoExtractor implements MSIExtractor {


    private MSExtractor mMediaExtractor = null;

    public MSVideoExtractor(String filePath) {
        mMediaExtractor = new MSExtractor(filePath);
    }

    @Override
    public MediaFormat getFormat() {
        return mMediaExtractor.getVideoFormat();
    }

    /**
     * 通过媒体提取器去读取数据
     *
     * @param byteBuffer
     * @return
     */
    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mMediaExtractor.readBuffer(byteBuffer);
    }

    @Override
    public long getCurrentTimestamp() {
        return mMediaExtractor.getCurSampleTime();
    }

    @Override
    public int getSampleFlag() {
        return mMediaExtractor.getCurFrameFlag();
    }

    @Override
    public long seek(long position) {
        return mMediaExtractor.seek(position);
    }

    @Override
    public void setStartPos(long position) {
        mMediaExtractor.setStartPos(position);
    }

    @Override
    public void stop() {
        mMediaExtractor.stop();
    }
}
