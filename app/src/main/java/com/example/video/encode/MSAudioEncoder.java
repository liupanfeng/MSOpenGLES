package com.example.video.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import com.example.video.base.MSBaseEncoder;
import com.example.video.muxer.MSMuxer;

import java.nio.ByteBuffer;

/**
 *
 * @Author: lpf
 * @CreateDate: 2022/9/29 下午1:25
 * @Description: 音频编码器
 */
public class MSAudioEncoder extends MSBaseEncoder {


    private static final int DEST_SAMPLE_RATE = 44100;
    private static final int DEST_BIT_RATE = 128000;
    private String TAG = "lpf";


    public MSAudioEncoder(MSMuxer mMuxer) {
        super(mMuxer);
    }

    @Override
    protected void release(MSMuxer mMuxer) {
        mMuxer.releaseAudioTrack();
    }

    @Override
    protected void writeData(MSMuxer mMuxer, ByteBuffer mOutputBuffer, MediaCodec.BufferInfo mBufferInfo) {
        mMuxer.writeAudioData(mOutputBuffer,mBufferInfo);
    }

    @Override
    protected void configEncoder(MediaCodec codec) {
        /**
         * 传入采样率和通道
         */
        MediaFormat mediaFormat=MediaFormat.createAudioFormat(encodeType(),DEST_SAMPLE_RATE,2);
        /**
         * 设置音频编码码率
         */
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,DEST_BIT_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE,1024*10);

        try {
            configEncoderWithCQ(codec, mediaFormat);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，设置为系统默认配置 BITRATE_MODE_VBR
            try {
                configEncoderWithVBR(codec, mediaFormat);
            } catch (Exception exception) {
                exception.printStackTrace();
                Log.e(TAG, "配置视频编码器失败");
            }
        }

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
        return "audio/mp4a-latm";
    }

    @Override
    public void addTrack(MSMuxer muxer, MediaFormat mediaFormat) {
        muxer.addAudioTrack(mediaFormat);
    }


    public long frameWaitTimeMs() {
        return 5;
    }



}
