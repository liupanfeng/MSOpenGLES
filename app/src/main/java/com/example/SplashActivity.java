package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.msopengles.OpenGLESMenuActivity;
import com.example.msopengles.R;
import com.example.video.MSVideoMenuActivity;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.btn_opengl_es).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, OpenGLESMenuActivity.class));
            }
        });


        findViewById(R.id.btn_video_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MSVideoMenuActivity.class));
            }
        });


//        Object obj = new Object();
//
//        ReferenceQueue referenceQueue = new ReferenceQueue();
//
//        WeakReference weakReference = new WeakReference(obj, referenceQueue);  //被回收之后会进入引用队列
//
//        System.out.println("weakReference=" + weakReference);
//
//        Runtime.getRuntime().gc();   //让可以回收的对象回收
//
//        obj=null;
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Reference findRef=null;
//
//        do {
//            findRef=referenceQueue.poll();
//            System.out.println("findRef="+findRef+" 是否等于weakReference="+(findRef==weakReference));
//        }while (findRef!=null);



    }
}