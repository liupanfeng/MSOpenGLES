package com.example.video.inter;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午7:56
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface IDecoder extends Runnable{

    void pause();

    void goOn();

   long  seekTo(long position);

   long seekAndPlay(long position);

   void stop();

   boolean isDecoding();

   boolean isSeeking();

   boolean isStop();

    /**
     * 设置尺寸监听器
     */
   void setSizeListener(IDecoderProgress iDecoderProgress);


   void setStateListener();
}
