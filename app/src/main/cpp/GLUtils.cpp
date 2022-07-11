#include "GLUtils.h"
#include <stdlib.h>
#include <cstring>
#include <GLES2/gl2ext.h>
#include "android_log_util.h"

GLuint GLUtils::LoadShader(GLenum shaderType, const char *pSource)
{
    GLuint shader = 0;
    shader = glCreateShader(shaderType);
    if (shader)
    {
        glShaderSource(shader, 1, &pSource, NULL);
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
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);

        /*链接完程序 删除 定点着色器和片段着色器*/
        glDetachShader(program, vertexShaderId);
        glDeleteShader(vertexShaderId);
        vertexShaderId = 0;
        glDetachShader(program, fragShaderId);
        glDeleteShader(fragShaderId);
        fragShaderId = 0;
        if (linkStatus != GL_TRUE)
        {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength)
            {
                char* buf = (char*) malloc((size_t)bufLength);
                if (buf)
                {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("GLUtils::CreateProgram Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    LOGD("GLUtils::CreateProgram program = %d", program);
    return program;
}

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

void GLUtils::CheckGLError(const char *pGLOperation)
{
    for (GLint error = glGetError(); error; error = glGetError())
    {
        LOGE("GLUtils::CheckGLError GL Operation %s() glError (0x%x)\n", pGLOperation, error);
    }

}


