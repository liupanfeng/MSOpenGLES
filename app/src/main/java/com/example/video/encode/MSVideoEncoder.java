package com.example.video.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.example.video.base.MSBaseEncoder;
import com.example.video.muxer.MSMuxer;

import java.nio.ByteBuffer;

/**
 * @Author: lpf
 * @CreateDate: 2022/9/29 下午1:25
 * @Description: 视频编码器
 */
public class MSVideoEncoder extends MSBaseEncoder {

    private static final int DEFAULT_ENCODE_FRAME_RATE = 30;
    private String TAG = "lpf";
    private Surface mSurface = null;


    public MSVideoEncoder(int mWidth, int mHeight, MSMuxer mMuxer) {
        super(mWidth, mHeight, mMuxer);
    }

    @Override
    protected void release(MSMuxer muxer) {
        muxer.releaseVideoTrack();
    }

    @Override
    protected void writeData(MSMuxer mMuxer, ByteBuffer mOutputBuffer, MediaCodec.BufferInfo mBufferInfo) {
        mMuxer.writeVideoData(mOutputBuffer, mBufferInfo);
    }

    @Override
    protected void configEncoder(MediaCodec codec) {
        if (mWidth <= 0 || mHeight <= 0) {
            throw new IllegalArgumentException(
                    String.format("Encode width or height is invalid, width: %d, height: %d",
                            mWidth, mHeight));
        }

        int bitrate = 3 * mWidth * mHeight;
        MediaFormat outputFormat = MediaFormat.createVideoFormat(encodeType(), mWidth, mHeight);
        outputFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);   //设置比特率
        outputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, DEFAULT_ENCODE_FRAME_RATE);  //设置帧率
        outputFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);  //设置I帧间隔
        outputFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface); //设置颜色格式

        try {
            configEncoderWithCQ(codec, outputFormat);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，设置为系统默认配置 BITRATE_MODE_VBR
            try {
                configEncoderWithVBR(codec, outputFormat);
            } catch (Exception exception) {
                exception.printStackTrace();
                Log.e(TAG, "配置视频编码器失败");
            }
        }

        mSurface = codec.createInputSurface();
    }

    /**
     * BITRATE_MODE_VBR
     * 该值表示编码器会根据视频采集内容的复杂度
     * （实际上是帧间变化量、运动情况）动态调整输出码率，
     * 图像复杂则码率高，图像简单则码率低，码率会波动比较大
     */
    private void configEncoderWithVBR(MediaCodec codec, MediaFormat outputFormat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outputFormat.setInteger(MediaFormat.KEY_BITRATE_MODE,
                    MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
        }
        /**
         * 1.第一个参数格式
         * 2.surface 这个在解码的时候可以设置surface直接用于播放
         * 3.第三个是加密算法
         * 4.编码还是解码
         */
        codec.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    /**
     * 该值表示恒定质量码率，码率会非常大，编码的目标就是尽最大可能保证图像质量，而忽略码率大小
     *
     * @param codec
     * @param outputFormat
     */
    private void configEncoderWithCQ(MediaCodec codec, MediaFormat outputFormat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outputFormat.setInteger(MediaFormat.KEY_BITRATE_MODE,
                    MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ);
        }

        codec.configure(outputFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    @Override
    protected String encodeType() {
        return "video/avc";
    }

    @Override
    public void addTrack(MSMuxer muxer, MediaFormat mediaFormat) {
        muxer.addVideoTrack(mediaFormat);
    }

    public Surface getEncodeSurface() {
        return mSurface;
    }
}
