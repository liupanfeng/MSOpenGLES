package com.example.video.muxer;

import android.media.MediaFormat;

import com.example.video.extractor.MSAudioExtractor;
import com.example.video.extractor.MSVideoExtractor;

import java.io.IOException;

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

    public MSMP4Repack(String mFilePath) throws IOException {
        this.mFilePath = mFilePath;
        mMSAudioExtractor = new MSAudioExtractor(mFilePath);
        mMSVideoExtractor = new MSVideoExtractor(mFilePath);
        mMSMuxer=new MSMuxer();
    }


    public void startRepack(){
        MediaFormat msVideoExtractorFormat = mMSVideoExtractor.getFormat();
        MediaFormat msAudioExtractorFormat = mMSAudioExtractor.getFormat();
        if (msVideoExtractorFormat!=null){
            mMSMuxer.addVideoTrack(msVideoExtractorFormat);
        }else{
            mMSMuxer.setNoVideo();
        }
        if (msAudioExtractorFormat!=null){
            mMSMuxer.addAudioTrack(msAudioExtractorFormat);
        }else {
            mMSMuxer.setNoAudio();
        }


    }


}
