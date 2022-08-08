#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include "android_log_util.h"
#include <android/native_window_jni.h>

#include <EGL/egl.h>
#include <GLES3/gl3.h>
#include "GLUtils.h"
#include "MSGLRenderContext.h"


/**
 * 定点着色器
 */
static const char*vertex_shader= GET_STR(
        /*输入的定点坐标 vec4向量*/
        attribute vec4 aPosition;
        /*输入的材质坐标*/
        attribute vec2 aTexCoord;
        /*输出的材质坐标*/
        varying vec2 vTexCoord;
        void main(){
            /*整体向下移动，让左边定点为0，0坐标  因为是中精度 必须是1.0不能是1*/
            vTexCoord=vec2(aTexCoord.x,1.0-aTexCoord.y);
            /*定点坐标不变向下传递 gl_Position 内置变量*/
            gl_Position=aPosition;
        });




/**
 * 软解码和部分x86硬解码 得到的都是yuv420p （也就是是i420）
 * 区别与NV21 以及NV12  硬件设备得到数据是NV21 根设备制造商有关系
 */
static const char *fragment_shader= GET_STR(
        /*使用中精度*/
        precision mediump float;
        /*从定点着色器传递过来的坐标点*/
        varying vec2 vTexCoord;
        /*输入的材质 uniform限定符号 */
        uniform sampler2D yTexture;
        uniform sampler2D uTexture;
        uniform sampler2D vTexture;

        void main(){
            vec3 yuv;
            vec3 rgb;
            /*
             * yTexture.r 取分量中的第一个值
             * texture2D 是一个内置函数
             * 第二个参数是传递过来的材质坐标
             * .r 不是获取红色分量 只是表示取一个分量的数据
             * */
            yuv.r=texture2D(yTexture,vTexCoord).r;
            /*-0.5：表示取四舍五入*/
            yuv.g=texture2D(uTexture,vTexCoord).r-0.5;
            yuv.b=texture2D(vTexture,vTexCoord).r-0.5;
            /*通过矩阵mat来进行转换 数值是固定的 网上的有很多资料*/
            rgb=mat3(1.0,     1.0,    1.0,
                     0.0,-0.39465,2.03211,
                     1.13983,-0.58060,0.0)*yuv;
            /*gl_FragColor 内置变量输出的像素颜色*/
            gl_FragColor=vec4(rgb,1.0);
        }
);


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_msopengles_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


/**
 * 初始化shader
 * @param shader
 * @param type
 * @return
 */
GLint initShader(const char*shader_code,GLint type){
    GLint shader=glCreateShader(type);
    if (shader==0){
        LOGE("egl create %d shader error",type);
        return 0;
    }

    glShaderSource(
            shader,
            1,          //创建shader的数量
            &shader_code,     //shader的源码
            0           //传0 表示获取shader_code的长度
            );

    glCompileShader(shader);

    GLint state;
    glGetShaderiv(shader,GL_COMPILE_STATUS,&state);
    if (state==0){
        LOGE("egl compile %d shader error",type);
        glDeleteShader(shader);
        return 0;
    }
    LOGD("egl compile %d shader success",type);
    return shader;

}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_msopengles_MSPlay_open(JNIEnv *env, jobject thiz, jstring path, jobject surface) {

    const char *_path = env->GetStringUTFChars(path, JNI_OK);
    LOGD("path is %s", _path);

    FILE *fp = fopen(_path, "rb");
    if (!fp) {
        LOGE("open path error");
        return;
    }

    /*得到window*/
    ANativeWindow *aNativeWindow = ANativeWindow_fromSurface(env, surface);

    /*----------------------------EGL--------------------------*/
    EGLDisplay eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY) {
        LOGE("egl get display error");
        return;
    }
    /*不传版本号，使用系统默认的*/
    if (EGL_TRUE != eglInitialize(eglDisplay, 0, 0)) {
        LOGE("egl eglInitialize error");
        return;
    }

    /*输出参数*/
    EGLConfig config;
    EGLint config_num;
    /*输入参数，别忘记结束符号EGL_NONE*/
    GLint configSpec[] = {
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_SURFACE_TYPE, EGL_WINDOW_BIT, EGL_NONE
    };
    /*选择配置信息  让系统给出最优的配置*/
    if (EGL_TRUE != eglChooseConfig(eglDisplay, configSpec, &config, 1, &config_num)) {
        LOGE("egl eglChooseConfig error");
        return;
    }
    /*通过拿到的ANativeWindow elgDisplay config 信息 创建EGLSurface*/
    EGLSurface eglSurface = eglCreateWindowSurface(eglDisplay, config, aNativeWindow, 0);
    if (eglSurface == EGL_NO_SURFACE) {
        LOGE("egl create window error");
        return;
    }

    /*创建关联的EGLContext*/
    const EGLint ctxAttr[] = {
            EGL_CONTEXT_CLIENT_VERSION,
            2,
            EGL_NONE
    };

    /*EGL_NO_CONTEXT 不使用共享context*/
    EGLContext eglContext = eglCreateContext(eglDisplay, config, EGL_NO_CONTEXT, ctxAttr);
    if (eglContext == EGL_NO_CONTEXT) {
        LOGE("egl create context error");
        return;
    }
    /*
     * 第二个参数：用来绘制的
     * 第三个参数：用来读的
     * 这两个可以一样，3D的时候可以使用不同的
     * */
    if (EGL_TRUE != eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
        LOGE("egl eglMakeCurrent  error");
        return;
    }

    LOGD("egl init success");


    /*----------------------------Load Shader --------------------------*/

    GLint v_shader = initShader(vertex_shader, GL_VERTEX_SHADER);
    GLint f_shader = initShader(fragment_shader, GL_FRAGMENT_SHADER);


    /*----------------------------Create Program--------------------------*/
    GLint program = glCreateProgram();
    if (program == 0) {
        LOGE("glCreateProgram failed!");
        return;
    }
    /*将着色器代码 加入程序中*/
    glAttachShader(program, v_shader);
    glAttachShader(program, f_shader);

    glLinkProgram(program);

    GLint link_state;
    glGetProgramiv(program, GL_LINK_STATUS, &link_state);
    if (link_state != GL_TRUE) {
        LOGE("glLinkProgram failed!");
        return;
    }
    glUseProgram(program);

    LOGD("glLinkProgram success!");


    /*--------------------------获取数据-----------------------------------*/
    //加入三维顶点数据 两个三角形组成正方形
    static float vers[] = {
            1.0f, -1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
    };
    GLuint apos = (GLuint) glGetAttribLocation(program, "aPosition");
    glEnableVertexAttribArray(apos);
    //传递顶点
    glVertexAttribPointer(apos, 3, GL_FLOAT, GL_FALSE, 12, vers);

    //加入材质坐标数据
    static float txts[] = {
            1.0f, 0.0f, //右下
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0, 1.0
    };
    GLuint atex = (GLuint) glGetAttribLocation(program, "aTexCoord");
    glEnableVertexAttribArray(atex);
    glVertexAttribPointer(atex, 2, GL_FLOAT, GL_FALSE, 8, txts);


    int width = 424;
    int height = 240;

    //材质纹理初始化
    //设置纹理层
    glUniform1i(glGetUniformLocation(program, "yTexture"), 0); //对于纹理第1层
    glUniform1i(glGetUniformLocation(program, "uTexture"), 1); //对于纹理第2层
    glUniform1i(glGetUniformLocation(program, "vTexture"), 2); //对于纹理第3层

    //创建opengl纹理
    GLuint texts[3] = {0};
    //创建三个纹理
    glGenTextures(3, texts);

    //设置纹理属性
    glBindTexture(GL_TEXTURE_2D, texts[0]);
    //缩小的过滤器
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    //设置纹理的格式和大小
    glTexImage2D(GL_TEXTURE_2D,
                 0,           //细节基本 0默认
                 GL_LUMINANCE,//gpu内部格式 亮度，灰度图
                 width, height, //拉升到全屏
                 0,             //边框
                 GL_LUMINANCE,//数据的像素格式 亮度，灰度图 要与上面一致
                 GL_UNSIGNED_BYTE, //像素的数据类型
                 NULL                    //纹理的数据
    );

    //设置纹理属性
    glBindTexture(GL_TEXTURE_2D, texts[1]);
    //缩小的过滤器
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    //设置纹理的格式和大小
    glTexImage2D(GL_TEXTURE_2D,
                 0,           //细节基本 0默认
                 GL_LUMINANCE,//gpu内部格式 亮度，灰度图
                 width / 2, height / 2, //拉升到全屏
                 0,             //边框
                 GL_LUMINANCE,//数据的像素格式 亮度，灰度图 要与上面一致
                 GL_UNSIGNED_BYTE, //像素的数据类型
                 NULL                    //纹理的数据
    );

    //设置纹理属性
    glBindTexture(GL_TEXTURE_2D, texts[2]);
    //缩小的过滤器
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    //设置纹理的格式和大小
    glTexImage2D(GL_TEXTURE_2D,
                 0,           //细节基本 0默认
                 GL_LUMINANCE,//gpu内部格式 亮度，灰度图
                 width / 2, height / 2, //拉升到全屏
                 0,             //边框
                 GL_LUMINANCE,//数据的像素格式 亮度，灰度图 要与上面一致
                 GL_UNSIGNED_BYTE, //像素的数据类型
                 NULL                    //纹理的数据
    );





    //////////////////////////////////////////////////////
    ////纹理的修改和显示
    unsigned char *buf[3] = {0};
    buf[0] = new unsigned char[width * height];
    buf[1] = new unsigned char[width * height / 4];
    buf[2] = new unsigned char[width * height / 4];


    for (int i = 0; i < 1000000; i++) {
        memset(buf[0],i,width*height);
         memset(buf[1],i,width*height/4);
        memset(buf[2],i,width*height/4);

        //420p   yyyyyyyy uu vv
//        if (feof(fp) == 0) {
//            //yyyyyyyy
//            fread(buf[0], 1, width * height, fp);
//            fread(buf[1], 1, width * height / 4, fp);
//            fread(buf[2], 1, width * height / 4, fp);
//        }





        //激活第1层纹理,绑定到创建的opengl纹理
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texts[0]);
        //替换纹理内容
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_LUMINANCE, GL_UNSIGNED_BYTE,
                        buf[0]);



        //激活第2层纹理,绑定到创建的opengl纹理
        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D, texts[1]);
        //替换纹理内容
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width / 2, height / 2, GL_LUMINANCE,
                        GL_UNSIGNED_BYTE, buf[1]);


        //激活第2层纹理,绑定到创建的opengl纹理
        glActiveTexture(GL_TEXTURE0 + 2);
        glBindTexture(GL_TEXTURE_2D, texts[2]);
        //替换纹理内容
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width / 2, height / 2, GL_LUMINANCE,
                        GL_UNSIGNED_BYTE, buf[2]);

        //三维绘制
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        //窗口显示
        eglSwapBuffers(eglDisplay, eglSurface);


    }

    env->ReleaseStringUTFChars(path, _path);
}



