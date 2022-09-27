package com.example.video.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import com.example.msopengles.utils.AndroidOS;
import com.example.video.extractor.MSAudioExtractor;
import com.example.video.extractor.MSVideoExtractor;

import java.io.IOException;
import java.io.ObjectInput;
import java.nio.ByteBuffer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: lpf
 * @CreateDate: 2022/9/26 下午1:48
 * @Description: 视频合成类
 */
public class MSMP4Repack {

    private static final String TAG = "MSMP4Repack";

    private String mFilePath;
    private MSAudioExtractor mMSAudioExtractor;
    private MSVideoExtractor mMSVideoExtractor;
    private MSMuxer mMSMuxer;

    public MSMP4Repack(String filePath) throws IOException {
        this.mFilePath = filePath;
        mMSAudioExtractor = new MSAudioExtractor(mFilePath);
        mMSVideoExtractor = new MSVideoExtractor(mFilePath);
        mMSMuxer = new MSMuxer();
    }


    public void startRepack() {
        MediaFormat msVideoExtractorFormat = mMSVideoExtractor.getFormat();
        MediaFormat msAudioExtractorFormat = mMSAudioExtractor.getFormat();
        if (msVideoExtractorFormat != null) {
            mMSMuxer.addVideoTrack(msVideoExtractorFormat);
        } else {
            mMSMuxer.setNoVideo();
        }
        if (msAudioExtractorFormat != null) {
            mMSMuxer.addAudioTrack(msAudioExtractorFormat);
        } else {
            mMSMuxer.setNoAudio();
        }

        Observable.just(1).map(new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) throws Exception {
                Log.d("lpf", "apply threadName=" + Thread.currentThread().getName());
                ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                if (msAudioExtractorFormat != null) {
                    int size = mMSAudioExtractor.readBuffer(buffer);
                    while (size > 0) {
                        bufferInfo.set(0, size, mMSAudioExtractor.getCurrentTimestamp(), mMSAudioExtractor.getSampleFlag());
                        mMSMuxer.writeAudioData(buffer, bufferInfo);
                        size = mMSAudioExtractor.readBuffer(buffer);
                    }
                }

                if (msVideoExtractorFormat != null) {
                    int size = mMSVideoExtractor.readBuffer(buffer);
                    while (size > 0) {
                        bufferInfo.set(0, size, mMSVideoExtractor.getCurrentTimestamp(), mMSVideoExtractor.getSampleFlag());
                        mMSMuxer.writeVideoData(buffer, bufferInfo);
                        size = mMSVideoExtractor.readBuffer(buffer);
                    }
                }
                mMSAudioExtractor.stop();
                mMSVideoExtractor.stop();

                mMSMuxer.releaseVideoTrack();
                mMSMuxer.releaseAudioTrack();

                return 1;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.d("lpf", "accept threadName=" + Thread.currentThread().getName() + "  视频合成成功");
            }
        }).subscribe();

    }


}
