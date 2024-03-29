//
// Created by 56930 on 2022/8/14.
//

#ifndef MSOPENGLES_FBOSAMPLE_H
#define MSOPENGLES_FBOSAMPLE_H

#include "GLSampleBase.h"


class FBOSample : public GLSampleBase{
public:
    FBOSample();
    virtual ~FBOSample();
    virtual void LoadImage(NativeImage *pImage);
    virtual void Init();
    virtual void Draw(int screenW, int screenH);

    virtual void Destroy();

    bool CreateFrameBufferObj();

private:
    GLuint m_ImageTextureId;
    GLuint m_FboTextureId;
    GLuint m_FboId;

    GLuint m_VaoIds[2];
    GLuint m_VboIds[4];

    GLint m_SamplerLoc;
    NativeImage m_RenderImage;

    GLuint m_FboProgramObj;
    GLuint m_FboVertexShader;
    GLuint m_FboFragmentShader;
    GLint m_FboSamplerLoc;

};


#endif //MSOPENGLES_FBOSAMPLE_H
