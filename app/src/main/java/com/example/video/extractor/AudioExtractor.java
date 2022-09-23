package com.example.video.extractor;

import android.media.MediaFormat;

import com.example.video.inter.IExtractor;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/20 下午4:54
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class AudioExtractor implements IExtractor {

    private String  filePath;

    public AudioExtractor(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public MediaFormat getFormat() {
        return null;
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return 0;
    }

    @Override
    public long getCurrentTimestamp() {
        return 0;
    }

    @Override
    public int getSampleFlag() {
        return 0;
    }

    @Override
    public long seek(long position) {
        return 0;
    }

    @Override
    public void setStartPos(long position) {

    }

    @Override
    public void stop() {

    }
}
