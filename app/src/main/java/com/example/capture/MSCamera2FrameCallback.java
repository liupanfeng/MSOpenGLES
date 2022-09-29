package com.example.capture;

/**
 *
 * @Author: lpf
 * @CreateDate: 2022/9/29 下午4:52
 * @Description:
 */
public interface MSCamera2FrameCallback {

    void onPreviewFrame(byte[] data, int width, int height);
    void onCaptureFrame(byte[] data, int width, int height);

}
