//
// Created by 56930 on 2022/8/7.
//

#ifndef MSOPENGLES_GLSAMPLEBASE_H
#define MSOPENGLES_GLSAMPLEBASE_H

#include <GLES3/gl3.h>
#include "ImageDef.h"
#include "stdint.h"

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
    virtual void Destroy();

protected:
    GLuint m_VertexShader;
    GLuint m_FragmentShader;
    GLuint m_ProgramObj;
    int m_SurfaceWidth;
    int m_SurfaceHeight;
};


#endif //MSOPENGLES_GLSAMPLEBASE_H
