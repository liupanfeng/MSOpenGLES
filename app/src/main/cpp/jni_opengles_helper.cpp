#include <jni.h>

#include <GLES2/gl2.h>
#include <GLES/gl.h>
#include "android_log_util.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniInitGL(JNIEnv *env, jobject thiz) {
    glClearColor(1.0, 0.0, 0.0, 1.0);  //将屏幕清理成红色
    glClearDepthf(1.0);
    glEnable(GL_DEPTH_TEST); //启动深度测试
    glDepthFunc(GL_LEQUAL);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniDrawGL(JNIEnv *env, jobject thiz) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //清理颜色缓冲区和深度缓存区
    glLoadIdentity();//重置
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniResizeGL(JNIEnv *env, jobject thiz, jint width,
                                                  jint height) {
    glViewport(0, 0, width, height);//重置视口的宽高
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrthof(-1, 1, -1, 1, 1.0, 1000.0);
}