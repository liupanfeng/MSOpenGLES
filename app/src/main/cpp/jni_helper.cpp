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
Java_com_example_msopengles_opengles_MSNativeRender_native_1set_1bitmap_1data(JNIEnv *env, jobject thiz,
                                                                              jint format, jint width,
                                                                              jint height,
                                                                              jbyteArray imageData) {
    int len = env->GetArrayLength (imageData);
    uint8_t* buf = new uint8_t[len];
    env->GetByteArrayRegion(imageData, 0, len, reinterpret_cast<jbyte*>(buf));
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