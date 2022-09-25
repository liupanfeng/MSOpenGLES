package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.msopengles.R;
import com.example.msopengles.utils.PathUtils;
import com.example.video.decode.MSAudioDecoder;
import com.example.video.decode.MSVideoDecoder;
import com.example.video.inter.MSIDecoderProgress;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoPlayByMediaCodecActivity extends AppCompatActivity {

    private static final long TIME_BASE = 1000000;

    private String mVideoPath = PathUtils.getLocalVideoDir()+File.separator+"demo.mp4";

    private MSVideoDecoder mMsVideoDecoder;
    private MSAudioDecoder mMSAudioDecoder;

    private SeekBar mSeekBar;
    private TextView totalDuration;
    private TextView currentPlaytime;
    private ImageView btn_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_by_media_codec);

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        mSeekBar = findViewById(R.id.seekBar);
        currentPlaytime = findViewById(R.id.currentPlaytime);
        totalDuration = findViewById(R.id.totalDuration);
        btn_play = findViewById(R.id.btn_play);


        ExecutorService executorService = Executors.newFixedThreadPool(3);

        mMsVideoDecoder = new MSVideoDecoder(mVideoPath, surfaceView, null);




        executorService.execute(mMsVideoDecoder);

        mMSAudioDecoder = new MSAudioDecoder(mVideoPath);
        executorService.execute(mMSAudioDecoder);

        mMsVideoDecoder.startPlay();
        mMSAudioDecoder.startPlay();
        btn_play.setBackgroundResource(R.mipmap.icon_edit_pause);


        initListener();

    }

    private void initListener() {
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean stop = mMsVideoDecoder.isStop();
                if (stop){
                    mMsVideoDecoder.startPlay();
                    mMSAudioDecoder.startPlay();
                    btn_play.setBackgroundResource(R.mipmap.icon_edit_pause);
                }else{
                    mMsVideoDecoder.pause();
                    mMSAudioDecoder.pause();
                    btn_play.setBackgroundResource(R.mipmap.icon_edit_play);
                }
            }
        });


        mMsVideoDecoder.setSizeListener(new MSIDecoderProgress() {
            @Override
            public void videoSizeChange(int width, int height, int rotation) {

            }

            @Override
            public void videoProgressChange(long position) {
                Log.d("lpf", "position=" + position);
                updateProgress(position);
            }

            @Override
            public void videoDuration(long duration) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setMax((int) (duration / TIME_BASE));
                        totalDuration.setText(formatTimeStrWithUs(duration));
                    }
                });
            }
        });
    }

    private long prePosition;

    private void updateProgress(long position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (position >= prePosition) {
                    prePosition = position;
                    mSeekBar.setProgress((int) (position / TIME_BASE));
                    currentPlaytime.setText(formatTimeStrWithUs(position));
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMsVideoDecoder.stop();
        mMSAudioDecoder.stop();
    }



    private String formatTimeStrWithUs(long us) {
        int second = (int) (us / 1000000.0);
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        return hh > 0 ? String.format("%02d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);
    }

}