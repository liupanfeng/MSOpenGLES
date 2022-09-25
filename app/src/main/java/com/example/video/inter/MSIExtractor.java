package com.example.video.inter;

import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:27
 * @Description: 媒体提取器接口
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface MSIExtractor {

    /**
     * 获取媒体格式
     * @return
     */
    MediaFormat getFormat();

    /**
     * 读取音视频数据
     */
    int readBuffer(ByteBuffer byteBuffer);


    /**
     * 获取当前帧时间
     */
    long getCurrentTimestamp();

    int getSampleFlag();

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     */

    long seek(long position);

    void setStartPos(long position);


    /**
     * 停止读取数据
     */
    void stop();


}
