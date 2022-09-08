package com.example.video.inter;

import com.example.video.Frame;
import com.example.video.base.BaseDecoder;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:19
 * @Description:  默认实现的解码监听器
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface DefDecoderStateListener extends IDecoderStateListener{

    @Override
    default void decoderDestroy(BaseDecoder baseDecoder){}

    @Override
    default void decodeOneFrame(BaseDecoder baseDecoder, Frame frame){}

    @Override
    default void decoderFinish(BaseDecoder baseDecoder){}

    @Override
    default void decoderError(BaseDecoder baseDecoder, String message){}

    @Override
    default void decoderPause(BaseDecoder baseDecoder){}

    @Override
    default void decoderPrepare(BaseDecoder baseDecoder){}

    @Override
    default void decoderReady(BaseDecoder baseDecoder){}

    @Override
    default void decoderRunning(BaseDecoder baseDecoder){}


}
