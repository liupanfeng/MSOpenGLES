package com.example.msopengles.activity;

import static com.example.msopengles.utils.Constants.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.msopengles.R;
import com.example.msopengles.databinding.ActivityCommonBinding;
import com.example.msopengles.opengles.MSGLSurfaceView;
import com.example.msopengles.opengles.MSNativeRender;

import java.nio.ByteBuffer;

public class CommonActivity extends AppCompatActivity {
    private ActivityCommonBinding mBinding;
    private MSGLSurfaceView mMSGLSurfaceView;
    private int mType;
    private MSNativeRender mNativeRender;

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
         mNativeRender = mMSGLSurfaceView.getNativeRender();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra("type", 0);
        }

        switch (mType){
            case SAMPLE_TYPE_SHOW_BITMAP:
                loadRGBABitmap(R.mipmap.test);
                break;
        }

    }

    private void loadRGBABitmap(int resId) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),resId);
        if (bitmap!=null){
            int byteCount = bitmap.getByteCount();
            ByteBuffer allocate = ByteBuffer.allocate(byteCount);
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            mNativeRender.setBitmapData(IMAGE_FORMAT_RGBA, bitmap.getWidth(), bitmap.getHeight(), array);
        }

    }


}