package com.example.video.inter;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午7:59
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface IDecoderProgress {

    /**
     * 视频宽高选装角度回调
     */
    void videoSizeChange(int width,int height,int rotation);


    /**
     * 视频播放进度回调
     */
    void videoProgressChange(long position);


}
