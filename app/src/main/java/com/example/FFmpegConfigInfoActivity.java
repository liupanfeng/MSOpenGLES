package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.msopengles.R;

public class FFmpegConfigInfoActivity extends AppCompatActivity {

    static {
        System.loadLibrary("msopengles");
    }

    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_config_info);
        mTvContent = findViewById(R.id.tv_content);
//        mTvContent.setText(getFFmpegConfigInfo());
    }

    public native String getFFmpegConfigInfo();
}