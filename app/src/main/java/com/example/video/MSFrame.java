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

    public ByteBuffer buffer;

    public MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

    public final ByteBuffer getBuffer() {
        return this.buffer;
    }

    public final void setBuffer(ByteBuffer var1) {
        this.buffer = var1;
    }

    public final MediaCodec.BufferInfo getBufferInfo() {
        return this.bufferInfo;
    }

    public final void setBufferInfo(MediaCodec.BufferInfo info) {
        this.bufferInfo.set(info.offset, info.size, info.presentationTimeUs, info.flags);
    }

}
