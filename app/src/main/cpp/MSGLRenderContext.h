//
// Created by ms on 2022/7/5.
//

#ifndef MSOPENGLES_MSGLRENDERCONTEXT_H
#define MSOPENGLES_MSGLRENDERCONTEXT_H

#include "TriangleSample.h"

class MSGLRenderContext {
public:
    MSGLRenderContext();
    ~MSGLRenderContext();

public:
    void SetImageData(int format, int width, int height, uint8_t *pData);
    void OnSurfaceCreated();
    void OnSurfaceChanged(int width, int height);
    void OnDrawFrame();
    static MSGLRenderContext * GetInstance();
    static void DestroyInstance();

    static MSGLRenderContext *m_pContext;
    TriangleSample m_Sample;
};


#endif //MSOPENGLES_MSGLRENDERCONTEXT_H
