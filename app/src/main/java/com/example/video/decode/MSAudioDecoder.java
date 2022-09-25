package com.example.video.decode;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;

import com.example.video.base.MSBaseDecoder;
import com.example.video.extractor.MSAudioExtractor;
import com.example.video.inter.MSIExtractor;

import java.nio.ByteBuffer;

/**
 * @Author: lpf
 * @CreateDate: 2022/9/20 下午4:51
 * @Description: 音频解码器
 */
public class MSAudioDecoder extends MSBaseDecoder {

    /**
     * 采样率
     */
    private int mSampleRate = -1;

    /**
     * 声音通道数量
     */
    private int mChannels = 1;

    /**
     * PCM采样位数
     */
    private int mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * 音频播放器
     */
    private AudioTrack mAudioTrack = null;

    /**
     * 音频数据缓存
     */
    private short[] mAudioOutTempBuf = null;


    public MSAudioDecoder(String mFilePath) {
        super(mFilePath);
    }

    @Override
    protected boolean check() {
        return true;
    }

    @Override
    public MSIExtractor initExtractor(String path) {
        return new MSAudioExtractor(path);
    }

    @Override
    public void initSpecParams(MediaFormat format) {
        try {
            mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            if (format.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
                mPCMEncodeBit = format.getInteger(MediaFormat.KEY_PCM_ENCODING);
            } else {
                //如果没有这个参数，默认为16位采样
                mPCMEncodeBit = AudioFormat.ENCODING_PCM_16BIT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean initRender() {
        int channel = 0;
        if (mChannels == 1) {
            //单声道
            channel = AudioFormat.CHANNEL_OUT_MONO;
        } else {
            //双声道
            channel = AudioFormat.CHANNEL_OUT_STEREO;
        }

        //获取最小缓冲区
        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPCMEncodeBit);
        mAudioOutTempBuf = new short[minBufferSize / 2];  //分配minBufferSize/2 这么大的内存

        mAudioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,   //播放类型 music
                mSampleRate,        //采样率
                channel,            //通道
                mPCMEncodeBit,       //采样位数
                minBufferSize,         //缓冲区大小
                AudioTrack.MODE_STREAM     //播放模式：数据流动态写入  MODE_STATIC：这个是一次性写入
        );

        mAudioTrack.play();
        return true;
    }

    @Override
    public boolean configCodec(MediaCodec codec, MediaFormat format) {
        codec.configure(format, null, null, 0);
        return true;
    }

    @Override
    public void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mAudioOutTempBuf.length<bufferInfo.size/2){
            mAudioOutTempBuf=new short[bufferInfo.size/2];
        }
        outputBuffer.position(0);
        outputBuffer.asShortBuffer().get(mAudioOutTempBuf,0,bufferInfo.size/2);

        mAudioTrack.write(mAudioOutTempBuf,0,bufferInfo.size/2);
    }



    @Override
    public void doneDecode() {
        mAudioTrack.stop();
        mAudioTrack.release();
    }
}
