package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

import com.example.msopengles.R;

import java.io.File;

public class VideoPlayByMediaCodecActivity extends AppCompatActivity {

    private String mVideoPath= Environment.getExternalStorageDirectory().
            getAbsolutePath()+ File.separator+"test.mp4";

    private VideoDe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_by_media_codec);
    }
}