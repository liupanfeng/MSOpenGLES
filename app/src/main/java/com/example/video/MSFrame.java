package com.example.video;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:09
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSFrame {

    public ByteBuffer buffer = null;

    private MediaCodec.BufferInfo bufferInfo;

    public void setBufferInfo(MediaCodec.BufferInfo bufferInfo) {
        this.bufferInfo = bufferInfo;
    }


}
