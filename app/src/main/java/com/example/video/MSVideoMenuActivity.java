package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.msopengles.R;

/**
 * 视频菜单页面
 */
public class MSVideoMenuActivity extends AppCompatActivity {


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mContext=this;
    }

    /**
     * 硬件解码 视频播放
     * @param view
     */
    public void onCodecVideoPlay(View view) {
        startActivity(new Intent(mContext,VideoPlayByMediaCodecActivity.class));
    }


}