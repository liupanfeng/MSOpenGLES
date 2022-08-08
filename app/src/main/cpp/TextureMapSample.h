//
// Created by 56930 on 2022/8/7.
//

#ifndef MSOPENGLES_TEXTUREMAPSAMPLE_H
#define MSOPENGLES_TEXTUREMAPSAMPLE_H

#include "ImageDef.h"
#include <GLES3/gl3.h>
#include "GLUtils.h"
#include "GLSampleBase.h"

/**
 * 纹理映射
 */
class TextureMapSample : public GLSampleBase {
public:
    TextureMapSample();
    ~TextureMapSample();

    void LoadImage(NativeImage *pImage);

    virtual void Init();

    virtual void Draw(int screenW, int screenH);

    virtual void Destroy();

private:

    GLint m_SamplerLoc;
    NativeImage m_RenderImage;
    GLuint m_TextureId;
};


#endif //MSOPENGLES_TEXTUREMAPSAMPLE_H
