//
// Created by 56930 on 2022/8/7.
//

#ifndef MSOPENGLES_GLSAMPLEBASE_H
#define MSOPENGLES_GLSAMPLEBASE_H

#include <GLES3/gl3.h>
#include "ImageDef.h"
#include "stdint.h"

#define VERTEX_SHADER_TYPE                             100
#define FRAGMENT_SHADER_TYPE                           101


#define SAMPLE_TYPE                             200
#define SAMPLE_TYPE_KEY_TRIANGLE                SAMPLE_TYPE + 0
#define SAMPLE_TYPE_KEY_TEXTURE_MAP             SAMPLE_TYPE + 1
#define SAMPLE_TYPE_KEY_YUV_TEXTURE_MAP         SAMPLE_TYPE + 2
#define SAMPLE_TYPE_KEY_VAO                     SAMPLE_TYPE + 3
#define SAMPLE_TYPE_KEY_FBO                     SAMPLE_TYPE + 4
#define SAMPLE_TYPE_KEY_FBO_LEG_LENGTHEN        SAMPLE_TYPE + 6

class GLSampleBase {

public:
    GLSampleBase(){
        m_ProgramObj = 0;
        m_VertexShader = 0;
        m_FragmentShader = 0;
        m_SurfaceWidth = 0;
        m_SurfaceHeight = 0;
    }
    ~GLSampleBase(){}

    virtual void LoadImage(NativeImage *pImage){};
    /**
     * 纯虚函数
     */
    virtual void Init() = 0;
    /**
     * 纯虚函数
     * @param screenW
     * @param screenH
     */
    virtual void Draw(int screenW, int screenH) = 0;
    /*如果只有头文件  必须写{}*/
    virtual void Destroy(){}

     void setVertexShader(const char* shader){
//        this->vShaderStr=shader;  不能使用浅浅拷贝 方便释放可能会造成悬空指针
        this->vShaderStr=new char [strlen(shader)+1];
        strcpy(this->vShaderStr,shader);
    }

     void setFragmentShader(const char* shader){
//        this->fShaderStr=shader;
        this->fShaderStr=new char[strlen(shader)+1];
         strcpy(this->fShaderStr,shader);
    }

protected:
    GLuint m_VertexShader;
    GLuint m_FragmentShader;
    char* vShaderStr=0;
    char* fShaderStr=0;
    GLuint m_ProgramObj;
    int m_SurfaceWidth;
    int m_SurfaceHeight;
};


#endif //MSOPENGLES_GLSAMPLEBASE_H
