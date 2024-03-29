
#ifndef MSOPENGLES_BASICLIGHTINGSAMPLE_H
#define MSOPENGLES_BASICLIGHTINGSAMPLE_H

#include "GLSampleBase.h"
#include <detail/type_mat.hpp>
#include <detail/type_mat4x4.hpp>


/**
 * 基础光照   环境光  散射光  镜面光
 */
class BasicLightingSample : public GLSampleBase
{
public:
    BasicLightingSample();

    virtual ~BasicLightingSample();

    virtual void LoadImage(NativeImage *pImage);

    virtual void Init();
    virtual void Draw(int screenW, int screenH);

    virtual void Destroy();

    virtual void UpdateTransformMatrix(float rotateX, float rotateY, float scaleX, float scaleY);

    void UpdateMVPMatrix(glm::mat4 &mvpMatrix, int angleX, int angleY, float ratio);

private:
    GLuint m_TextureId;
    GLint m_SamplerLoc;
    GLint m_MVPMatLoc;
    GLint m_ModelMatrixLoc;
    GLint m_LightPosLoc;
    GLint m_LightColorLoc;
    GLint m_ViewPosLoc;

    GLuint m_VaoId;
    GLuint m_VboIds[1];
    GLuint m_TfoId;
    GLuint m_TfoBufId;
    NativeImage m_RenderImage;
    glm::mat4 m_MVPMatrix;
    glm::mat4 m_ModelMatrix;

    int m_AngleX;
    int m_AngleY;

    float m_ScaleX;
    float m_ScaleY;
};



#endif //MSOPENGLES_BASICLIGHTINGSAMPLE_H
