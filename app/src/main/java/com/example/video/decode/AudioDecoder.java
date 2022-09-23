package com.example.video.decode;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.example.video.base.BaseDecoder;
import com.example.video.extractor.AudioExtractor;
import com.example.video.inter.IExtractor;

import java.nio.ByteBuffer;

/**
 *
 * @Author: lpf
 * @CreateDate: 2022/9/20 下午4:51
 * @Description: 音频解码器
 */
public class AudioDecoder extends BaseDecoder {

    /**采样率*/
    private int mSampleRate = -1;

    /**声音通道数量*/
    private int mChannels = 1;

    /**PCM采样位数*/
    private int mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;

    /**音频播放器*/
    private AudioTrack mAudioTrack = null;

    /**音频数据缓存*/
    private ByteBuffer mAudioOutTempBuf = null;



    public AudioDecoder(String mFilePath) {
        super(mFilePath);
    }

    @Override
    protected boolean check() {
        return true;
    }

    @Override
    public IExtractor initExtractor(String path) {
        return new AudioExtractor(path);
    }

    @Override
    public void initSpecParams(MediaFormat format) {
        try {
            mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

           if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                mPCMEncodeBit= format.getInteger(MediaFormat.KEY_PCM_ENCODING);
            } else {
                //如果没有这个参数，默认为16位采样
                mPCMEncodeBit=AudioFormat.ENCODING_PCM_16BIT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initRender() {
        return false;
    }

    @Override
    public boolean configCodec(MediaCodec codec, MediaFormat format) {
        codec.configure(format,null,null,0);
        return true;
    }

    @Override
    public void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {
        int channel=0;
        if (mChannels == 1) {
            //单声道
            channel= AudioFormat.CHANNEL_OUT_MONO;
        } else {
            //双声道
            channel = AudioFormat.CHANNEL_OUT_STEREO;
        }

        //获取最小缓冲区
        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPCMEncodeBit);
        mAudioOutTempBuf = ByteBuffer.allocate(minBufferSize/2);


    }

    @Override
    public void doneDecode() {

    }
}
