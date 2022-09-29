package com.example.video.inter;

import com.example.video.base.MSBaseEncoder;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:31
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public interface MSIEncodeStateListener {
    void encodeStart(MSBaseEncoder encoder);
    void encodeProgress(MSBaseEncoder encoder);
    void encoderFinish(MSBaseEncoder encoder);

}
