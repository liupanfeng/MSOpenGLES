package com.example.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;

import com.example.msopengles.R;
import com.example.msopengles.utils.PathUtils;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

        Observable.just(1).map(new Function<Integer, Object>() {
            @Override
            public Object apply(Integer integer) throws Exception {
                copyAssetFilterToSdCard();
                return 1;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        }).subscribe();

    }

    /**
     * 硬件解码 视频播放
     * @param view
     */
    public void onCodecVideoPlay(View view) {
        startActivity(new Intent(mContext,VideoPlayByMediaCodecActivity.class));
    }



    private void copyAssetFilterToSdCard() {
        AssetManager assets = getAssets();
        String localFilterDir = PathUtils.getLocalVideoDir();
        try {
            String[] videos = assets.list("video");
            for (String fileName : videos) {
                PathUtils.copyAssetFile(mContext, fileName,
                        "video", localFilterDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}