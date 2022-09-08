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
import com.example.msopengles.opengles.MSGLRender;
import com.example.msopengles.opengles.MSGLSurfaceView;
import com.example.msopengles.utils.TextResourceReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CommonActivity extends AppCompatActivity {
    private ActivityCommonBinding mBinding;
    private MSGLSurfaceView mMSGLSurfaceView;
    private int mType;
    private MSGLRender mMSGLRender;

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
        mMSGLRender = mMSGLSurfaceView.getNativeRender();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra("type", 0);
        }

        mMSGLRender.setParamsInt(SAMPLE_TYPE, mType, 0);


        switch (mType){
            case SAMPLE_TYPE_TRIANGLE:
                setVAndFShader(R.raw.vertex_triangle, R.raw.fragment_triangle);
                break;
            case SAMPLE_TYPE_TEXTURE_MAP:
                setVAndFShader(R.raw.vertext_texture_map, R.raw.fragment_texture_map);
                loadRGBABitmap(R.mipmap.test);
                break;
            case SAMPLE_TYPE_YUV_TEXTURE_MAP:
                setVAndFShader(R.raw.vertex_nv21_texture_map, R.raw.fragment_nv21_texture_map);
                loadNV21Image();
                break;
            case SAMPLE_TYPE_FBO:
                Bitmap bitmap = loadRGBAImage(R.drawable.lye);
                mMSGLSurfaceView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMSGLSurfaceView.setAspectRatio(bitmap.getWidth(), bitmap.getHeight());
                    }
                },3000);

                break;
            case SAMPLE_TYPE_BASIC_LIGHTING:
                loadRGBAImage(R.mipmap.girl);
                break;

            default:
                break;
        }

    }

    private void setVAndFShader(int vShader,int fShader) {
        String vertexShader;
        String fragmentShader;
        vertexShader = TextResourceReader.readTextFileFromResource(this, vShader);
        fragmentShader = TextResourceReader.readTextFileFromResource(this,fShader);
        mMSGLRender.setParamsString(SAMPLE_TYPE, SAMPLE_TYPE_VERTEX_SHADER, vertexShader);
        mMSGLRender.setParamsString(SAMPLE_TYPE, SAMPLE_TYPE_FRAGMENT_SHADER, fragmentShader);
    }

    private void loadNV21Image() {
        InputStream inputStream=null;
        try {
           inputStream= getAssets().open("YUV_Image_840x1074.NV21");

        } catch (IOException e) {
            e.printStackTrace();
        }


        int lenght = 0;
        try {
            lenght = inputStream.available();
            byte[] buffer = new byte[lenght];
            inputStream.read(buffer);
            mMSGLRender.setImageData(IMAGE_FORMAT_NV21, 840, 1074, buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try
            {
                inputStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void loadRGBABitmap(int resId) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),resId);
        if (bitmap!=null){
            int byteCount = bitmap.getByteCount();
            ByteBuffer allocate = ByteBuffer.allocate(byteCount);
            bitmap.copyPixelsToBuffer(allocate);
            byte[] array = allocate.array();
            mMSGLRender.setBitmapData(IMAGE_FORMAT_RGBA, bitmap.getWidth(), bitmap.getHeight(), array);
        }

    }


    private Bitmap loadRGBAImage(int resId) {
        InputStream is = this.getResources().openRawResource(resId);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                int bytes = bitmap.getByteCount();
                ByteBuffer buf = ByteBuffer.allocate(bytes);
                bitmap.copyPixelsToBuffer(buf);
                byte[] byteArray = buf.array();
                mMSGLRender.setImageData(IMAGE_FORMAT_RGBA, bitmap.getWidth(), bitmap.getHeight(), byteArray);
            }
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMSGLRender.unInit();
    }
}