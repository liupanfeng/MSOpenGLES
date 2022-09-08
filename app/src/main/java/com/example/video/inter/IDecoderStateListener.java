package com.example.video.inter;

import com.example.video.Frame;
import com.example.video.base.BaseDecoder;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:01
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface IDecoderStateListener {
    /**
     * 准备解码
     * @param baseDecoder
     */
    void decoderPrepare(BaseDecoder baseDecoder);

    /**
     * 准备好解码
     * @param baseDecoder
     */
    void decoderReady(BaseDecoder baseDecoder);

    /**
     * 解码进行中
     * @param baseDecoder
     */
    void decoderRunning(BaseDecoder baseDecoder);

    /**
     * 解码暂停
     * @param baseDecoder
     */
    void decoderPause(BaseDecoder baseDecoder);

    /**
     * 解码一帧数据
     * @param baseDecoder
     * @param frame
     */
    void decodeOneFrame(BaseDecoder baseDecoder, Frame frame);

    /**
     * 解码完成
     * @param baseDecoder
     */
    void decoderFinish(BaseDecoder baseDecoder);

    /**
     * 解码完成
     * @param baseDecoder
     */
    void decoderDestroy(BaseDecoder baseDecoder);

    /**
     * 解码失败
     * @param baseDecoder
     * @param message
     */
    void decoderError(BaseDecoder baseDecoder,String message);

}
