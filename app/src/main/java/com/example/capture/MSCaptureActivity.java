package com.example.capture;

import static com.example.capture.ByteFlowRender.IMAGE_FORMAT_I420;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.capture.camera.MSCamera2Wrapper;
import com.example.msopengles.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MSCaptureActivity extends BaseRenderActivity implements View.OnClickListener, MSCamera2FrameCallback {
    private static final String TAG = "MainActivity";
    private static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final String[] SAMPLE_TITLES = {
            "抖音传送带",
            "抖音蓝线挑战",
    };
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private RelativeLayout mSurfaceViewRoot;
    private MSCamera2Wrapper mCamera2Wrapper;
    private ImageButton mSwitchCamBtn, mSwitchRatioBtn, mSwitchFilterBtn;
    private int mSampleSelectedIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mscapture);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (mCamera2Wrapper != null) {
                    mCamera2Wrapper.capture();
                }
            }
        });

        initViews();


    }

    private void initViews() {
        mSwitchCamBtn = (ImageButton) findViewById(R.id.switch_camera_btn);
        mSwitchRatioBtn = (ImageButton) findViewById(R.id.switch_ratio_btn);
        mSwitchFilterBtn = (ImageButton) findViewById(R.id.switch_filter_btn);
        mSwitchCamBtn.bringToFront();
        mSwitchRatioBtn.bringToFront();
        mSwitchFilterBtn.bringToFront();
        mSwitchCamBtn.setOnClickListener(this);
        mSwitchRatioBtn.setOnClickListener(this);
        mSwitchFilterBtn.setOnClickListener(this);

        mSurfaceViewRoot = (RelativeLayout) findViewById(R.id.surface_root);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mSurfaceViewRoot.addView(mGLSurfaceView, p);
//        mByteFlowRender.init(mGLSurfaceView);
//        mByteFlowRender.loadShaderFromAssetsFile(mCurrentShaderIndex, getResources());

        mCamera2Wrapper = new MSCamera2Wrapper(this,this);
        //mCamera2Wrapper.setDefaultPreviewSize(getScreenSize());

        ViewTreeObserver treeObserver = mSurfaceViewRoot.getViewTreeObserver();
        treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean  onPreDraw() {
                mSurfaceViewRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                mRootViewSize = new Size(mSurfaceViewRoot.getMeasuredWidth(), mSurfaceViewRoot.getMeasuredHeight());
                updateGLSurfaceViewSize(mCamera2Wrapper.getPreviewSize());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            mCamera2Wrapper.startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, CAMERA_PERMISSION_REQUEST_CODE);
        }
        updateTransformMatrix(mCamera2Wrapper.getCameraId());
        if (mSurfaceViewRoot != null) {
            updateGLSurfaceViewSize(mCamera2Wrapper.getPreviewSize());
        }
    }


    @Override
    protected void onPause() {
        if (hasPermissionsGranted(REQUEST_PERMISSIONS)) {
            mCamera2Wrapper.stopCamera();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mByteFlowRender.unInit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_resolution) {
//            showChangeSizeDialog();
        } else if (id == R.id.action_switch_camera) {
//            String cameraId = mCamera2Wrapper.getCameraId();
//            String[] cameraIds = mCamera2Wrapper.getSupportCameraIds();
//            if (cameraIds != null) {
//                for (int i = 0; i < cameraIds.length; i++) {
//                    if (!cameraIds[i].equals(cameraId)) {
//                        mCamera2Wrapper.updateCameraId(cameraIds[i]);
//                        updateTransformMatrix(cameraIds[i]);
//                        updateGLSurfaceViewSize(mCamera2Wrapper.getPreviewSize());
//                        break;
//                    }
//                }
//            }
        }

        return true;
    }



    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPreviewFrame(byte[] data, int width, int height) {
        Log.d(TAG, "onPreviewFrame() called with: data = [" + data + "], width = [" + width + "], height = [" + height + "]");
//        mByteFlowRender.setRenderFrame(IMAGE_FORMAT_I420, data, width, height);
        mByteFlowRender.requestRender();
    }

    @Override
    public void onCaptureFrame(byte[] data, int width, int height) {
//        Log.d(TAG, "onCaptureFrame() called with: data = [" + data + "], width = [" + width + "], height = [" + height + "]");
//        ByteFlowFrame byteFlowFrame = new ByteFlowFrame(data, width, height);
//        String path = FrameUtil.encodeFrame(byteFlowFrame);
//        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, ImageActivity.class);
//        intent.putExtra("img_path", path);
//        intent.putExtra("img_ort", mCamera2Wrapper.getCameraId());
//        startActivity(intent);
    }
}