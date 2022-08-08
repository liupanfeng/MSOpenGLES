//
// Created by ms on 2022/7/5.
//
#include "ImageDef.h"
#include "TriangleSample.h"
#include "MSGLRenderContext.h"
#include "android_log_util.h"

MSGLRenderContext* MSGLRenderContext::m_pContext = nullptr;



MSGLRenderContext::MSGLRenderContext()
{

}

MSGLRenderContext::~MSGLRenderContext()
{

}

void MSGLRenderContext::SetImageData(int format, int width, int height, uint8_t *pData)
{
    LOGD("MSGLRenderContext::SetImageData format=%d, width=%d, height=%d, pData=%p", format, width, height, pData);
    NativeImage nativeImage;
    nativeImage.format = format;
    nativeImage.width = width;
    nativeImage.height = height;
    nativeImage.ppPlane[0] = pData;

    switch (format)
    {
        case IMAGE_FORMAT_NV12:
        case IMAGE_FORMAT_NV21:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            break;
        case IMAGE_FORMAT_I420:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            nativeImage.ppPlane[2] = nativeImage.ppPlane[1] + width * height / 4;
            break;
        default:
            break;
    }

//    m_TextureMapSample->LoadImage(&nativeImage);

}
/**
 *  初始化画布 调用init
 */
void MSGLRenderContext::OnSurfaceCreated()
{
    LOGD("MSGLRenderContext::OnSurfaceCreated");
    glClearColor(0.0f,0.0f,1.0f, 1.0f);

}
/**
 * 连接状态发生变化之后 更改视口
 * @param width
 * @param height
 */
void MSGLRenderContext::OnSurfaceChanged(int width, int height)
{
    LOGD("MSGLRenderContext::OnSurfaceChanged [w, h] = [%d, %d]", width, height);
    /*宽高 变化了更新视口*/
    glViewport(0, 0, width, height);
    m_ScreenW = width;
    m_ScreenH = height;



}

void MSGLRenderContext::OnDrawFrame()
{
    LOGD("MSGLRenderContext::OnDrawFrame");
    /*清理深度缓冲和颜色缓冲*/
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    if (m_pBeforeSample)
    {
        m_pBeforeSample->Destroy();
        delete m_pBeforeSample;
        m_pBeforeSample = nullptr;
    }


    if (m_pCurSample)
    {
        m_pCurSample->Init();
        m_pCurSample->Draw(m_ScreenW, m_ScreenH);
    }
}

MSGLRenderContext *MSGLRenderContext::GetInstance()
{
    LOGD("MSGLRenderContext::GetInstance");
    if (m_pContext == nullptr)
    {
        m_pContext = new MSGLRenderContext();
    }
    return m_pContext;
}

void MSGLRenderContext::DestroyInstance()
{
    LOGD("MSGLRenderContext::DestroyInstance");
    if (m_pContext)
    {
        delete m_pContext;
        m_pContext = nullptr;
    }

}

/**
 * 通过参数控制加载那个子类  c++多态
 * @param paramType
 * @param value0
 * @param value1
 */
void MSGLRenderContext::SetParamsInt(int paramType, int value0, int value1) {
    LOGD("MSGLRenderContext::SetParamsInt paramType = %d, value0 = %d, value1 = %d", paramType, value0, value1);
    if (paramType == SAMPLE_TYPE){
        m_pBeforeSample = m_pCurSample;
        LOGD("MSGLRenderContext::SetParamsInt 0 m_pBeforeSample = %p", m_pBeforeSample);
        switch (value0) {
            case SAMPLE_TYPE_KEY_TRIANGLE:
                m_pCurSample = new TriangleSample();
                break;
            case SAMPLE_TYPE_KEY_TEXTURE_MAP:

                break;

        }
    }

}

