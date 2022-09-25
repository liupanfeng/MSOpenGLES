package com.example.video.extractor;

import android.media.MediaFormat;

import com.example.video.inter.MSIExtractor;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/20 下午4:54
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSAudioExtractor implements MSIExtractor {


    private MSExtractor mMsExtractor;

    public MSAudioExtractor(String filePath) {
        mMsExtractor = new MSExtractor(filePath);
    }

    @Override
    public MediaFormat getFormat() {
        return mMsExtractor.getAudioFormat();
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mMsExtractor.readBuffer(byteBuffer);
    }

    @Override
    public long getCurrentTimestamp() {
        return mMsExtractor.getCurSampleTime();
    }

    @Override
    public int getSampleFlag() {
        return mMsExtractor.getCurFrameFlag();
    }

    @Override
    public long seek(long position) {
        return mMsExtractor.seek(position);
    }

    @Override
    public void setStartPos(long position) {
        mMsExtractor.setStartPos(position);
    }

    @Override
    public void stop() {
        mMsExtractor.stop();
    }
}
