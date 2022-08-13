//
// Created by ms on 2022/7/5.
//

#ifndef MSOPENGLES_TRIANGLESAMPLE_H
#define MSOPENGLES_TRIANGLESAMPLE_H


#include "GLSampleBase.h"


class TriangleSample  : public GLSampleBase{


public:
    TriangleSample();
    virtual ~TriangleSample();

    virtual void LoadImage(NativeImage *pImage);
    virtual void Init();
    virtual void Draw(int screenW, int screenH);
    virtual void Destroy();

};


#endif //MSOPENGLES_TRIANGLESAMPLE_H
