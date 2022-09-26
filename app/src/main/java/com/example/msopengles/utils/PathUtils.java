package com.example.msopengles.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @Author : lpf
 * @CreateDate : 2022/6/13 上午10:41
 * @Description : 路径工具类
 */
public class PathUtils {

    private static final String TAG = PathUtils.class.getName();

    private static String SDK_FILE_ROOT_DIRECTORY = "MSOpenGLES" + File.separator;

    private static String YUV_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "yuv";


    private static final String LOCAL_VIDEO_PATH = SDK_FILE_ROOT_DIRECTORY + "video";

    private static final String REPACK_VIDEO_PATH = SDK_FILE_ROOT_DIRECTORY + "repack_video";

    /**
     * 删除文件
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件和文件夹
     * @param filePath
     */
    public static void deleteFileAndDir(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }


    public static void deleteDirectoryFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            /*
             * 如要保留文件夹，只删除文件，请注释这行
             * To keep the folder and delete only the files, comment this line
             * */
            //file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }


    public static String getYUVDir() {
        return getFolderDirPath(YUV_DIRECTORY);
    }

    public static String getRootDir() {
        return getFolderDirPath(SDK_FILE_ROOT_DIRECTORY);
    }


    public static String getLocalVideoDir() {
        String dstDirPath = getFolderDirPath(LOCAL_VIDEO_PATH);
        if (dstDirPath == null) {
            return null;
        }
        return dstDirPath;
    }

    public static String getRepackVideoDir() {
        String dstDirPath = getFolderDirPath(REPACK_VIDEO_PATH);
        if (dstDirPath == null) {
            return null;
        }
        return dstDirPath;
    }

    public static String getFolderDirPath(String dstDirPathToCreate) {
        File dstFileDir = new File(Environment.getExternalStorageDirectory(), dstDirPathToCreate);
        if (AndroidOS.USE_SCOPED_STORAGE) {
            dstFileDir = new File(App.getContext().getExternalFilesDir(""), dstDirPathToCreate);
        }
        if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
            Log.e(TAG, "Failed to create file dir path--->" + dstDirPathToCreate);
            return null;
        }
        return dstFileDir.getAbsolutePath();
    }



    public static void copyAssetFile(Context context, String fileName, String className, String destFileDirPath) {
        /*
         * 模型文件不存在
         * The model file does not exist
         * */
        try {
            File folder = new File(destFileDirPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }
            String destFilePath=destFileDirPath+File.separator+fileName;
            File file=new File(destFilePath);
            if (file.exists()){
                return;
            }
            InputStream in = context.getAssets().open(className + File.separator + fileName);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
