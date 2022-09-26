package com.example.video.muxer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import com.example.msopengles.utils.PathUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/9/8 下午8:37
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class MSMuxer {

    private static final String TAG = "lpf";

    private String mFilePath;
    /**
     * 媒体混合器
     */
    private MediaMuxer mMediaMuxer;

    private int mVideoTrackIndex = -1;

    private int mAudioTrackIndex = -1;

    private boolean mIsAddAudioTrack = false;

    private boolean mIsAddVideoTrack = false;

    private boolean mIsAddAudioEnd = false;

    private boolean mIsAddVideoEnd = false;

    private boolean mIsStart = false;

    private IMuxerStateListener mIMuxerStateListener;

    public void setIMuxerStateListener(IMuxerStateListener iMuxerStateListener) {
        this.mIMuxerStateListener = iMuxerStateListener;
    }

    public MSMuxer() throws IOException {
        String fileName = "ms_repack_video.mp4";
        String fileDirPath = PathUtils.getRepackVideoDir();
        mFilePath = fileDirPath + File.separator + fileName;
        Log.d(TAG, "合成视频的路径是："+mFilePath);

        /**
         * 指定输入文件的名称以及路径
         * 指定输出文件的格式
         */
        mMediaMuxer = new MediaMuxer(mFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }


    public void addVideoTrack(MediaFormat mediaFormat) {
        if (mIsAddVideoTrack) {
            return;
        }

        if (mMediaMuxer != null) {
            mVideoTrackIndex = mMediaMuxer.addTrack(mediaFormat);
            Log.d(TAG, "添加视频轨道");
            mIsAddVideoTrack = true;
            startMuxer();
        }

    }


    public void addAudioTrack(MediaFormat mediaFormat) {
        if (mIsAddAudioTrack) {
            return;
        }

        if (mMediaMuxer != null) {
            mAudioTrackIndex = mMediaMuxer.addTrack(mediaFormat);
            Log.d(TAG, "添加音频轨道");
            mIsAddAudioTrack = true;
            startMuxer();
        }
    }

    public void setNoAudio() {
        if (mIsAddAudioTrack) {
            return;
        }
        mIsAddAudioTrack = true;
        mIsAddAudioEnd = true;
        startMuxer();
    }

    public void setNoVideo() {
        if (mIsAddVideoTrack) {
            return;
        }
        mIsAddVideoTrack = true;
        mIsAddVideoEnd = true;
        startMuxer();
    }

    public void writeVideoData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mIsStart && mMediaMuxer != null) {
            mMediaMuxer.writeSampleData(mVideoTrackIndex, byteBuffer, bufferInfo);
        }
    }

    public void writeAudioData(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mIsStart && mMediaMuxer != null) {
            mMediaMuxer.writeSampleData(mAudioTrackIndex, byteBuffer, bufferInfo);
        }
    }

    public void releaseVideoTrack() {
        mIsAddVideoEnd = true;
        release();
    }

    public void releaseAudioTrack() {
        mIsAddAudioEnd = true;
        release();
    }


    private void release() {
        if (mIsAddVideoEnd && mIsAddAudioEnd) {
            mIsAddVideoTrack = false;
            mIsAddAudioTrack = false;
            try {
                mMediaMuxer.stop();
                mMediaMuxer.release();
                mMediaMuxer = null;
                Log.d(TAG, "退出封装器");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mIMuxerStateListener != null) {
                    mIMuxerStateListener.onMuxerFinish();
                }
            }
        }
    }

    private void startMuxer() {
        if (mIsAddVideoTrack && mIsAddAudioTrack) {
            mMediaMuxer.start();
            mIsStart = true;
            if (mIMuxerStateListener != null) {
                mIMuxerStateListener.onMuxerStart();
            }
            Log.d(TAG, "启动封装器");
        }
    }

    interface IMuxerStateListener {
        void onMuxerStart();

        void onMuxerFinish();
    }
}
