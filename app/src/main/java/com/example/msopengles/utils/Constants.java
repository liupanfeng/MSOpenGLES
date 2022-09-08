package com.example.msopengles.utils;

/**
 * All rights Reserved, Designed By www.meishesdk.com
 *
 * @Author: lpf
 * @CreateDate: 2022/7/5 下午3:22
 * @Description:
 * @Copyright: www.meishesdk.com Inc. All rights reserved.
 */
public class Constants {
    public static final int IMAGE_FORMAT_RGBA = 0x01;
    public static final int IMAGE_FORMAT_NV21 = 0x02;
    public static final int IMAGE_FORMAT_NV12 = 0x03;
    public static final int IMAGE_FORMAT_I420 = 0x04;
    public static final int IMAGE_FORMAT_YUYV = 0x05;
    public static final int IMAGE_FORMAT_GARY = 0x06;



    public static final int SAMPLE_TYPE  =  200;
    /*绘制三角形*/
    public static final int SAMPLE_TYPE_TRIANGLE                = SAMPLE_TYPE;
    public static final int SAMPLE_TYPE_TEXTURE_MAP             = SAMPLE_TYPE + 1;
    public static final int SAMPLE_TYPE_YUV_TEXTURE_MAP         = SAMPLE_TYPE + 2;
    public static final int SAMPLE_TYPE_VAO                     = SAMPLE_TYPE + 3;
    public static final int SAMPLE_TYPE_FBO                     = SAMPLE_TYPE + 4;
    public static final int SAMPLE_TYPE_EGL                     = SAMPLE_TYPE + 5;
    public static final int SAMPLE_TYPE_FBO_LEG                 = SAMPLE_TYPE + 6;
    public static final int SAMPLE_TYPE_BASIC_LIGHTING          = SAMPLE_TYPE + 8;



    /*顶点着色器*/
    public static final int SAMPLE_TYPE_VERTEX_SHADER                 = 100;
    /*片段着色器*/
    public static final int SAMPLE_TYPE_FRAGMENT_SHADER               = 101;







}
