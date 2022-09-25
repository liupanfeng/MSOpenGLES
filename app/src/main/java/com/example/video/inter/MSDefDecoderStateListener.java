package com.example.video.inter;

import com.example.video.MSFrame;
import com.example.video.base.MSBaseDecoder;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:19
 * @Description:  默认实现的解码监听器
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface MSDefDecoderStateListener extends MSIDecoderStateListener {

    @Override
    default void decoderDestroy(MSBaseDecoder baseDecoder){}

    @Override
    default void decodeOneFrame(MSBaseDecoder baseDecoder, MSFrame frame){}

    @Override
    default void decoderFinish(MSBaseDecoder baseDecoder){}

    @Override
    default void decoderError(MSBaseDecoder baseDecoder, String message){}

    @Override
    default void decoderPause(MSBaseDecoder baseDecoder){}

    @Override
    default void decoderPrepare(MSBaseDecoder baseDecoder){}

    @Override
    default void decoderReady(MSBaseDecoder baseDecoder){}

    @Override
    default void decoderRunning(MSBaseDecoder baseDecoder){}


}
