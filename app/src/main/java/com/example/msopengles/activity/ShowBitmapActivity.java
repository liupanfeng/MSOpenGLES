package com.example.msopengles.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.msopengles.R;

/**
 * 使用GLSurfaceView 加载Bitmap
 * 用到了纹理映射相关的内容
 */
public class ShowBitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bitmap);
    }
}