//
// Created by ms on 2022/7/5.
//

#include "TriangleSample.h"

//static const char *vShaderStr = GET_STR(
//        layout(location = 0) in vec4 vPosition;
//        void main() {
//            gl_Position = vPosition;
//        });
//
//static const char *fShaderStr = GET_STR(
//        precision mediump float;
//        out vec4 fragColor;
//        void main() {
//            fragColor = vec4(1.0, 0.0, 0.0, 1.0);
//        }
//);

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
    char vShaderStr[] =
            "#version 300 es                          \n"
            "layout(location = 0) in vec4 vPosition;  \n"
            "void main()                              \n"
            "{                                        \n"
            "   gl_Position = vPosition;              \n"
            "}                                        \n";

    char fShaderStr[] =
            "#version 300 es                              \n"
            "precision mediump float;                     \n"
            "out vec4 fragColor;                          \n"
            "void main()                                  \n"
            "{                                            \n"
            "   fragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );  \n"
            "}                                            \n";

    m_ProgramObj = GLUtils::CreateProgram(vShaderStr, fShaderStr, m_VertexShader, m_FragmentShader);

}

void TriangleSample::LoadImage(NativeImage *pImage) {

}

void TriangleSample::Draw(int screenW, int screenH) {
    LOGD("TriangleSample::Draw");
    GLfloat vVertices[] = {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
    };

    if (m_ProgramObj == 0)
        return;

    // Use the program object
    glUseProgram(m_ProgramObj);

    /*
     * 加载定点数据
     *  第二个参数表示点的数量
     * */
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, vVertices);
    /*激活点坐标*/
    glEnableVertexAttribArray(0);

    /*绘制三角形*/
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

void TriangleSample::Destroy() {

}



