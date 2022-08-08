//
// Created by ms on 2022/7/5.
//

#ifndef MSOPENGLES_MSGLRENDERCONTEXT_H
#define MSOPENGLES_MSGLRENDERCONTEXT_H

#include "TriangleSample.h"
#include "GLSampleBase.h"

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

    void SetParamsInt(int paramType, int value0, int value1);



private:
    static MSGLRenderContext *m_pContext;
    GLSampleBase *m_pCurSample;
    GLSampleBase *m_pBeforeSample;
    int m_ScreenW;
    int m_ScreenH;
};


#endif //MSOPENGLES_MSGLRENDERCONTEXT_H
