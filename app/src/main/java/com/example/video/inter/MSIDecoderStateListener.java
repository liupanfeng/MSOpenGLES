package com.example.video.inter;

import com.example.video.MSFrame;
import com.example.video.base.MSBaseDecoder;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:01
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface MSIDecoderStateListener {
    /**
     * 准备解码
     * @param baseDecoder
     */
    void decoderPrepare(MSBaseDecoder baseDecoder);

    /**
     * 准备好解码
     * @param baseDecoder
     */
    void decoderReady(MSBaseDecoder baseDecoder);

    /**
     * 解码进行中
     * @param baseDecoder
     */
    void decoderRunning(MSBaseDecoder baseDecoder);

    /**
     * 解码暂停
     * @param baseDecoder
     */
    void decoderPause(MSBaseDecoder baseDecoder);

    /**
     * 解码一帧数据
     * @param baseDecoder
     * @param frame
     */
    void decodeOneFrame(MSBaseDecoder baseDecoder, MSFrame frame);

    /**
     * 解码完成
     * @param baseDecoder
     */
    void decoderFinish(MSBaseDecoder baseDecoder);

    /**
     * 解码完成
     * @param baseDecoder
     */
    void decoderDestroy(MSBaseDecoder baseDecoder);

    /**
     * 解码失败
     * @param baseDecoder
     * @param message
     */
    void decoderError(MSBaseDecoder baseDecoder, String message);

}
