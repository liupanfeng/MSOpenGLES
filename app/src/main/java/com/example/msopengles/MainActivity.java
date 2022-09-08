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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_TEXTURE_MAP);
            }
        });

        mBinding.btnLoadNv21.setOnClickListener(this);
        mBinding.btnLoadFbo.setOnClickListener(this);
        mBinding.btnLoadBasicLight.setOnClickListener(this);
    }

    public native String stringFromJNI();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_load_nv21:
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_YUV_TEXTURE_MAP);
                break;
            case R.id.btn_load_fbo:
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_FBO);
                break;
            case R.id.btn_load_basic_light:
                CommonActivity.actionCommonActivity(MainActivity.this,SAMPLE_TYPE_BASIC_LIGHTING);
                break;
            default:
                break;
        }

    }
}