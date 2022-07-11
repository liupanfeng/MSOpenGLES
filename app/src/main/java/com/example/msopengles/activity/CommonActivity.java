package com.example.msopengles.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.msopengles.databinding.ActivityCommonBinding;
import com.example.msopengles.opengles.MSGLSurfaceView;

public class CommonActivity extends AppCompatActivity {
    private ActivityCommonBinding mBinding;
    private MSGLSurfaceView mMSGLSurfaceView;
    private int mType;

    public static void actionCommonActivity(Context context, int type) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCommonBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mMSGLSurfaceView = mBinding.msGlSurfaceView;
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra("type", 0);
        }

    }


}