package com.example.msopengles;

import static com.example.msopengles.utils.Constants.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.example.msopengles.activity.CommonActivity;
import com.example.msopengles.databinding.ActivityMainBinding;

/**
 * 菜单页面
 */
public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("msopengles");
    }

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        },100);

        initListener();
    }

    /**
     * 设置监听事件
     */
    private void initListener() {
        mBinding.btnPlayYuv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_TRIANGLE);
            }
        });

        mBinding.btnShowBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_SHOW_BITMAP);
            }
        });
    }

    public native String stringFromJNI();
}