//
// Created by ms on 2022/7/5.
//

#include <jni.h>
#include <string>
#include "MSGLRenderContext.h"


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1OnInit(JNIEnv *env, jobject thiz) {
    MSGLRenderContext::GetInstance();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1OnUnInit(JNIEnv *env, jobject thiz) {
    MSGLRenderContext::DestroyInstance();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1set_1bitmap_1data(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jint format,
                                                                              jint width,
                                                                              jint height,
                                                                              jbyteArray imageData) {
    /*获取bitmap数据的长度*/
    int len = env->GetArrayLength(imageData);
    /*将数据拷贝到buffer里边 */
    uint8_t *buf = new uint8_t[len];
    env->GetByteArrayRegion(imageData, 0, len, reinterpret_cast<jbyte *>(buf));

    MSGLRenderContext::GetInstance()->SetImageData(format, width, height, buf);
    delete[] buf;
    env->DeleteLocalRef(imageData);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1OnSurfaceCreated(JNIEnv *env,
                                                                             jobject thiz) {
    MSGLRenderContext::GetInstance()->OnSurfaceCreated();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1OnSurfaceChanged(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jint width,
                                                                             jint height) {
    MSGLRenderContext::GetInstance()->OnSurfaceChanged(width, height);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1OnDrawFrame(JNIEnv *env, jobject thiz) {
    MSGLRenderContext::GetInstance()->OnDrawFrame();
}

/**
 * 设置Int参数
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1SetParamsInt(JNIEnv *env, jobject thiz,
                                                                         jint param_type,
                                                                         jint value0, jint value1) {

    MSGLRenderContext::GetInstance()->SetParamsInt(param_type, value0, value1);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1SetParamsString(JNIEnv *env,
                                                                            jobject thiz,
                                                                            jint param_type,
                                                                            jint value0,
                                                                            jstring value1) {
    /*jni 转换 char * */
    const char* content=env->GetStringUTFChars(value1,JNI_OK);
    MSGLRenderContext::GetInstance()->SetParamsCharArray(param_type,value0,content);
    /*释放指针*/
    env->ReleaseStringUTFChars(value1,content);

}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_opengles_MSNativeRender_native_1UpdateTransformMatrix(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jfloat rotate_x,
                                                                                  jfloat rotate_y,
                                                                                  jfloat scale_x,
                                                                                  jfloat scale_y) {

    MSGLRenderContext::GetInstance()->UpdateTransformMatrix(rotate_x,rotate_y,scale_x,scale_y);
}

