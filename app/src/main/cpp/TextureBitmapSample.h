//
// Created by ms on 2022/7/11.
//

#ifndef MSOPENGLES_TEXTUREBITMAPSAMPLE_H
#define MSOPENGLES_TEXTUREBITMAPSAMPLE_H

#include "BaseSample.h"

class TextureBitmapSample :BaseSample {
public:
    TextureBitmapSample();
    ~TextureBitmapSample();

public:

    void loadImage(NativeImage *pImage);

    virtual void init();

    virtual void draw(int screenW, int screenH);

    virtual void destroy();

public:

    GLuint m_textureId;
    GLint m_SamplerLoc;
    NativeImage m_RenderImage;


};


#endif //MSOPENGLES_TEXTUREBITMAPSAMPLE_H
