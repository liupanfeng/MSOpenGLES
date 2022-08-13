//
// Created by 56930 on 2022/8/13.
//

#ifndef MSOPENGLES_NV21TEXTUREMAPSAMPLE_H
#define MSOPENGLES_NV21TEXTUREMAPSAMPLE_H

#include "GLSampleBase.h"

class NV21TextureMapSample : public GLSampleBase{
private:
    /*两个纹理ID*/
    GLuint m_yTextureId;
    GLuint m_uvTextureId;

    /*两个采样器*/
    GLint m_ySamplerLoc;
    GLint m_uvSamplerLoc;

    NativeImage m_RenderImage;



public:

    /**
     * 初始化纹理ID 以及采样器
     */
    NV21TextureMapSample()
    {
        m_yTextureId = GL_NONE;
        m_uvTextureId = GL_NONE;

        m_ySamplerLoc = GL_NONE;
        m_uvSamplerLoc = GL_NONE;

    }

    virtual ~NV21TextureMapSample()
    {
        NativeImageUtil::FreeNativeImage(&m_RenderImage);
    }

    virtual void LoadImage(NativeImage *pImage);

    virtual void Init();

    virtual void Draw(int screenW, int screenH);

    virtual void Destroy();

};


#endif //MSOPENGLES_NV21TEXTUREMAPSAMPLE_H
