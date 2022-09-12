#include "GLUtils.h"
#include <stdlib.h>
#include <cstring>
#include <GLES2/gl2ext.h>
#include "android_log_util.h"

/**
 *  加载着色器
 * @param shaderType
 * @param pSource
 * @return
 */
GLuint GLUtils::LoadShader(GLenum shaderType, const char *pSource)
{
    GLuint shader = 0;
    /*创建shader*/
    shader = glCreateShader(shaderType);
    if (shader)
    {
        glShaderSource(shader, 1, &pSource, NULL);
        /*编译shader*/
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled)
        {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen)
            {
                char* buf = (char*) malloc((size_t)infoLen);
                if (buf)
                {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGD("GLUtils::LoadShader Could not compile shader %d:\n%s\n", shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}
/**
 * 编译连接 shader
 * @param pVertexShaderSource   顶点着色器源码
 * @param pFragShaderSource   片段着色器源码
 * @param vertexShaderId
 * @param fragShaderId
 * @return 返回program
 */
GLuint GLUtils::CreateProgram(const char *pVertexShaderSource, const char *pFragShaderSource, GLuint &vertexShaderId, GLuint &fragShaderId)
{
    GLuint program = 0;
    /*加载定点着色器*/
    vertexShaderId = LoadShader(GL_VERTEX_SHADER, pVertexShaderSource);
    if (!vertexShaderId) return program;
    /*加载片段着色器*/
    fragShaderId = LoadShader(GL_FRAGMENT_SHADER, pFragShaderSource);
    if (!fragShaderId) return program;

    /*创建程序*/
    program = glCreateProgram();
    if (program)
    {
        /*将定点 交给程序*/
        glAttachShader(program, vertexShaderId);
        CheckGLError("glAttachShader");
        /*将片段数据 交给程序*/
        glAttachShader(program, fragShaderId);
        CheckGLError("glAttachShader");

        /*链接程序*/
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        /*获取链接状态*/
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);

        /*链接完程序 删除 定点着色器和片段着色器*/
        glDetachShader(program, vertexShaderId);
        glDeleteShader(vertexShaderId);
        vertexShaderId = 0;

        /*先跟陈旭断开链接*/
        glDetachShader(program, fragShaderId);
        /*删除片元着色器*/
        glDeleteShader(fragShaderId);
        fragShaderId = 0;

        /*判断链接状态*/
        if (linkStatus != GL_TRUE)
        {
            GLint bufLength = 0;
            /*链接失败了 获取log日志*/
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength)
            {
                /*申请指针大小*/
                char* buf = (char*) malloc((size_t)bufLength);
                if (buf)
                {
                    /*获取日志*/
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("GLUtils::CreateProgram Could not link program:\n%s\n", buf);
                    /*释放申请的内存空间*/
                    free(buf);
                }
            }
            /*删除程序*/
            glDeleteProgram(program);
            program = 0;
        }
    }
    LOGD("GLUtils::CreateProgram program = %d", program);
    return program;
}

/**
 * 删除程序
 * @param program
 */
void GLUtils::DeleteProgram(GLuint &program)
{
    LOGD("GLUtils::DeleteProgram");
    if (program)
    {
        glUseProgram(0);
        glDeleteProgram(program);
        program = 0;
    }
}

/**
 * 检测错误
 * @param pGLOperation
 */
void GLUtils::CheckGLError(const char *pGLOperation)
{
    for (GLint error = glGetError(); error; error = glGetError())
    {
        LOGE("GLUtils::CheckGLError GL Operation %s() glError (0x%x)\n", pGLOperation, error);
    }

}


