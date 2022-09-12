//
// Created by ms on 2022/7/5.
//

#include "TriangleSample.h"



TriangleSample::TriangleSample() {

}

TriangleSample::~TriangleSample() {
    if (m_ProgramObj) {
        glDeleteProgram(m_ProgramObj);
    }

}

/**
 * 使用顶点着色器和片段着色器 初始化并链接程序
 */
void TriangleSample::Init() {
    LOGE("vShaderStr======%s",vShaderStr);
    LOGE("fShaderStr======%s",fShaderStr);
    m_ProgramObj = GLUtils::CreateProgram(vShaderStr, fShaderStr, m_VertexShader, m_FragmentShader);

}

void TriangleSample::LoadImage(NativeImage *pImage) {

}

void TriangleSample::Draw(int screenW, int screenH) {
    LOGD("TriangleSample::Draw");
    //顶点
    GLfloat vVertices[] = {
            -0.5f, 0.5f, 0.0f,
            0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };

    if (m_ProgramObj == 0){
        return;
    }

    // Use the program object 使用程序对象
    glUseProgram(m_ProgramObj);

    /*
     * 加载定点数据
     *  第二个参数表示点的数量  将定点数据赋值给顶点着色器  0表示position  layout  vPosition 顶点数据就赋值给这个字段 了
     * */
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, vVertices);
    /*激活点坐标*/
    glEnableVertexAttribArray(0);  //参数0 根上边的第一个参数对应

    /*绘制三角形*/
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void TriangleSample::Destroy() {

}



