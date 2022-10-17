#include <jni.h>

#include <GLES2/gl2.h>
#include <GLES/gl.h>
#include "android_log_util.h"
#include "MSGLPrimitivesDef.h"

void drawTriangle();
void drawTriangleStrip();

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniInitGL(JNIEnv *env, jobject thiz) {
    glClearColor(0.0, 0.0, 0.0, 1.0);  //将屏幕清理成红色
    glClearDepthf(1.0);
    glEnable(GL_DEPTH_TEST); //启动深度测试
    glDepthFunc(GL_LEQUAL);  //指定深度缓冲比较值
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniDrawGL(JNIEnv *env, jobject thiz) {
    drawTriangleStrip();









}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles2_MSRender_jniResizeGL(JNIEnv *env, jobject thiz, jint width,
                                                  jint height) {
    //GL_PROJECTION是对投影矩阵操作，GL_MODELVIEW是对模型视景矩阵操作，GL_TEXTURE是对纹理矩阵进行随后的操作
    glViewport(0, 0, width, height);//重置视口的宽高
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrthof(-1, 1, -1, 1, 0.1, 1000.0);

    glMatrixMode(GL_MODELVIEW);  //GL_MODELVIEW是对模型视景矩阵操作
    glLoadIdentity();
}

/**
 * 绘制彩色矩形
 */
void drawTriangleStrip(){
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //清理颜色缓冲区和深度缓存区
    glLoadIdentity();//重置
    //固定编程管线 绘制矩形
    MSFloat7 vertexTriangle[]={
            {- 0.5,0.1,-0.1,1.0,0.0,0.0,1.0},
            {-0.5,0.9,-0.1,0.0,1.0,0.0,1.0},
            {0.5,0.1,-0.1,0.0,0.0,1.0,1.0},
            {0.5,0.9,-0.1,1.0,0.0,1.0,1.0},
    };

    glEnableClientState(GL_VERTEX_ARRAY);  //启用顶点数组
    glEnableClientState(GL_COLOR_ARRAY);  //启用颜色数组

    glVertexPointer(3,GL_FLOAT,sizeof (MSFloat7),vertexTriangle);
    glColorPointer(4,GL_FLOAT,sizeof (MSFloat7),&vertexTriangle[0].r);

    glDrawArrays(GL_TRIANGLE_STRIP,0,4);

    glDisableClientState(GL_VERTEX_ARRAY);
    glDisableClientState(GL_COLOR_ARRAY);
}

/**
 * 绘制彩色三角形
 */
void drawTriangle(){

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //清理颜色缓冲区和深度缓存区
    glLoadIdentity();//重置

    //固定编程管线 绘制三角形///////////////////////////////
    MSFloat7 vertexTriangle2[] ={
            {- 0.5,-0.1,-0.1,1.0,0.0,0.0,1.0},
            {-0.5,-0.9,-0.1,0.0,1.0,0.0,1.0},
            {0.5,-0.1,-0.1,0.0,0.0,1.0,1.0},

    };

    glEnableClientState(GL_VERTEX_ARRAY);  //启用顶点数组
    glEnableClientState(GL_COLOR_ARRAY);  //启用颜色数组

    glVertexPointer(3,GL_FLOAT,sizeof (MSFloat7),vertexTriangle2);
    glColorPointer(4,GL_FLOAT,sizeof (MSFloat7),&vertexTriangle2[0].r);

    glDrawArrays(GL_TRIANGLES,0,3);

    glDisableClientState(GL_VERTEX_ARRAY);
    glDisableClientState(GL_COLOR_ARRAY);
}