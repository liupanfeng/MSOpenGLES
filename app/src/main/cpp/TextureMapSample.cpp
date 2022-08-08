//
// Created by 56930 on 2022/8/7.
//

#include "TextureMapSample.h"

TextureMapSample::TextureMapSample() {
    m_TextureId = 0;
}

TextureMapSample::~TextureMapSample() {

}

void TextureMapSample::LoadImage(NativeImage *pImage) {
    LOGD("TextureMapSample::LoadImage pImage = %p", pImage->ppPlane[0]);
    if (pImage)
    {
        m_RenderImage.width = pImage->width;
        m_RenderImage.height = pImage->height;
        m_RenderImage.format = pImage->format;
        /*将数据从pImage 拷贝到m_RenderImage*/
        NativeImageUtil::CopyNativeImage(pImage, &m_RenderImage);
    }
}

void TextureMapSample::Init() {
    /*create RGBA texture*/
    glGenTextures(1, &m_TextureId);
    glBindTexture(GL_TEXTURE_2D, m_TextureId);
    /*参数边缘检测  截取拉伸和边缘拉伸*/
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    /*边缘线性检测*/
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    glBindTexture(GL_TEXTURE_2D, GL_NONE);


    /*输出shader 日志  shader 写错了，很容易出问题 */
    LOGE("vShaderStr======%s",vShaderStr);
    LOGE("fShaderStr======%s",fShaderStr);

    m_ProgramObj = GLUtils::CreateProgram(vShaderStr, fShaderStr, m_VertexShader, m_FragmentShader);
    if (m_ProgramObj)
    {
        m_SamplerLoc = glGetUniformLocation(m_ProgramObj, "s_TextureMap");
    }
    else
    {
        LOGE("TextureMapSample::Init create program fail");
    }

}

void TextureMapSample::Draw(int screenW, int screenH) {
    LOGD("TextureMapSample::Draw()");
    if(m_ProgramObj == GL_NONE || m_TextureId == GL_NONE) {
        return;
    }
    glClear(GL_STENCIL_BUFFER_BIT | GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(1.0, 1.0, 1.0, 1.0);
//    GLfloat verticesCoords[] = {
//            -1.0f,  0.5f, 0.0f,  // Position 0
//            -1.0f, -0.5f, 0.0f,  // Position 1
//            1.0f, -0.5f, 0.0f,   // Position 2
//            1.0f,  0.5f, 0.0f,   // Position 3
//    };


    /*控制的是渲染区域*/
    GLfloat verticesCoords[] = {
            -1.0f,  1.0f, 0.0f,  // Position 0
            -1.0f, -1.0f, 0.0f,  // Position 1
            1.0f, -1.0f, 0.0f,   // Position 2
            1.0f,  1.0f, 0.0f,   // Position 3
    };

    /*纹理坐标系*/
    GLfloat textureCoords[] = {
            0.0f,  0.0f,        // TexCoord 0
            0.0f,  1.0f,        // TexCoord 1
            1.0f,  1.0f,        // TexCoord 2
            1.0f,  0.0f         // TexCoord 3
    };


    GLushort indices[] = { 0, 1, 2, 0, 2, 3 };

    /*upload RGBA image data*/
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_TextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, m_RenderImage.width, m_RenderImage.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, m_RenderImage.ppPlane[0]);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);

    // Use the program object
    glUseProgram (m_ProgramObj);

    // Load the vertex position
    glVertexAttribPointer (0, 3, GL_FLOAT,
                           GL_FALSE, 3 * sizeof (GLfloat), verticesCoords);
    // Load the texture coordinate
    glVertexAttribPointer (1, 2, GL_FLOAT,
                           GL_FALSE, 2 * sizeof (GLfloat), textureCoords);

    glEnableVertexAttribArray (0);
    glEnableVertexAttribArray (1);

    // Bind the RGBA map
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_TextureId);

    // Set the RGBA map sampler to texture unit to 0
    glUniform1i(m_SamplerLoc, 0);

    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, indices);

}

void TextureMapSample::Destroy() {

}
