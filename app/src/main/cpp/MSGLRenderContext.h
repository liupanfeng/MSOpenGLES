//
// Created by ms on 2022/7/5.
//

#ifndef MSOPENGLES_MSGLRENDERCONTEXT_H
#define MSOPENGLES_MSGLRENDERCONTEXT_H

#include "TriangleSample.h"
#include "GLSampleBase.h"
#include "ImageDef.h"
#include "android_log_util.h"
#include "TriangleSample.h"
#include "TextureMapSample.h"
#include "NV21TextureMapSample.h"
#include "FBOSample.h"
#include "BasicLightingSample.h"


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

    void  SetParamsCharArray(int paramType,int value0 ,const char* value1);


    void UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY);

private:
    static MSGLRenderContext *m_pContext;
    /*给默认值是绘制三角形*/
    GLSampleBase *m_pCurSample = new TriangleSample();
    GLSampleBase *m_pBeforeSample;



    int m_ScreenW;
    int m_ScreenH;
};


#endif //MSOPENGLES_MSGLRENDERCONTEXT_H
