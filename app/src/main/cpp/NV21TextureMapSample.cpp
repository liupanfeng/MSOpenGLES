//
// Created by 56930 on 2022/8/13.
//

#include "NV21TextureMapSample.h"

void NV21TextureMapSample::LoadImage(NativeImage *pImage) {
    LOGD("NV21TextureMapSample::LoadImage pImage = %p", pImage->ppPlane[0]);
    if (pImage){
        /*拷贝数据  将传递进来的数据  进行拷贝 拷贝给m_RenderImage*/
        m_RenderImage.width = pImage->width;
        m_RenderImage.height = pImage->height;
        m_RenderImage.format = pImage->format;
        /*
         * 第一个参数是拷贝来源
         * 第二个参数是拷贝给谁
         * */
        NativeImageUtil::CopyNativeImage(pImage,&m_RenderImage);
    }
}

void NV21TextureMapSample::Init() {

    /*通过shader 创建程序 给两个shader id赋值*/
    m_ProgramObj= GLUtils::CreateProgram(vShaderStr, fShaderStr, m_VertexShader, m_FragmentShader);

    /*
     * 第一个参数是程序
     * 第二个参数是采样器id 在shader中定义的 名称必须保持一致
     * */
    m_ySamplerLoc = glGetUniformLocation (m_ProgramObj, "y_texture" );
    m_uvSamplerLoc = glGetUniformLocation(m_ProgramObj, "uv_texture");

    /*声明两个纹理ID 并给纹理Id 赋值为0*/
    GLuint textureIds[2] = {0};

    /*创建两个纹理*/
    glGenTextures(2, textureIds);

    /*给c++中声明的两个纹理id 赋值*/
    m_yTextureId = textureIds[0];
    m_uvTextureId = textureIds[1];


}

/**
 * 最关键的还是绘制
 * @param screenW
 * @param screenH
 */
void NV21TextureMapSample::Draw(int screenW, int screenH) {
    LOGD("NV21TextureMapSample::Draw()------------------------");
    if(m_ProgramObj == GL_NONE || m_yTextureId == GL_NONE || m_uvTextureId == GL_NONE) {
        return;
    }

    /*上传Y平面数据*/

    /*glBindTexture是OpenGL核心函数库中的一个函数。函数说明：允许建立一个绑定到目标纹理的有名称的纹理
     * glBindTexture可以让你创建或使用一个已命名的纹理 */
    glBindTexture(GL_TEXTURE_2D, m_yTextureId);
    /*glTexImage2D，功能是根据指定的参数，生成一个2D纹理（Texture）*/
    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, m_RenderImage.width, m_RenderImage.height,
                 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, m_RenderImage.ppPlane[0]);

    /*glTexParameter*，是OpenGL纹理过滤函数
     * 图象从纹理图象空间映射到帧缓冲图象空间时，需要重新构造纹理图像，
     * 就会造成应用到多边形上的图像失真。这些函数相当于进行优化，以解决这类问题。
     *
     * GL_TEXTURE_WRAP_S:表示x轴
     * GL_TEXTURE_WRAP_T：表示y轴
     * */
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);  //GL_CLAMP_TO_EDGE 纹理坐标超出部分取边界颜色。
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);

   /*更新 UV 平面数据*/
    glBindTexture(GL_TEXTURE_2D, m_uvTextureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE_ALPHA, m_RenderImage.width >> 1,
                 m_RenderImage.height >> 1, 0, GL_LUMINANCE_ALPHA, GL_UNSIGNED_BYTE, m_RenderImage.ppPlane[1]);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glBindTexture(GL_TEXTURE_2D, GL_NONE);

    /*GL 坐标系*/
    GLfloat verticesCoords[] = {
            -1.0f,  1.0f, 0.0f,  // Position 0   左上顶点
            -1.0f, -1.0f, 0.0f,  // Position 1   左下顶点
            1.0f,  -1.0f, 0.0f,  // Position 2   右下顶点
            1.0f,   1.0f, 0.0f,  // Position 3   右上
    };

    /*手机坐标系*/
    GLfloat textureCoords[] = {
            0.0f,  0.0f,        // TexCoord 0        左上顶点
            0.0f,  1.0f,        // TexCoord 1       左下顶点
            1.0f,  1.0f,        // TexCoord 2       右下顶点
            1.0f,  0.0f         // TexCoord 3       右上
    };

    GLushort indices[] = { 0, 1, 2, 0, 2, 3 };

    /*在绘制的时候使用程序*/
    glUseProgram (m_ProgramObj);


    /*
     * 加载顶点位置
     * glVertexAttribPointer 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置。
     * 1.指定要修改的顶点属性的索引值
     * 2.指定每个顶点属性的组件数量
     * 3.指定数组中每个组件的数据类型。可用的符号常量有GL_BYTE, GL_UNSIGNED_BYTE,
     * 4.normalized 指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）。
     * 5.stride 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0。
     * 6.pointer 指定第一个组件在数组的第一个顶点属性中的偏移量。该数组与GL_ARRAY_BUFFER绑定，储存于缓冲区中。初始值为0；
     * */
    glVertexAttribPointer (0, 3, GL_FLOAT,
                           GL_FALSE, 3 * sizeof (GLfloat), verticesCoords);

    /*
     * 加载纹理坐标
     *
     * */
    glVertexAttribPointer (1, 2, GL_FLOAT,
                           GL_FALSE, 2 * sizeof (GLfloat), textureCoords);

    /*glEnableVertexAttribArray - 启用或禁用通用顶点属性数组
     * 指定要启用或禁用的通用顶点属性的索引
     *
     * glEnableVertexAttribArray启用index指定的通用顶点属性数组。 glDisableVertexAttribArray禁用index指定的通用顶点属性数组。
     * 默认情况下，禁用所有客户端功能，包括所有通用顶点属性数组。 如果启用，
     * 将访问通用顶点属性数组中的值，并在调用顶点数组命令（如glDrawArrays或glDrawElements）时用于呈现。
     * */
    glEnableVertexAttribArray (0);
    glEnableVertexAttribArray (1);

    /*绑定Y平面图*/
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, m_yTextureId);

    // Set the Y plane sampler to texture unit to 0
    /*将 Y 平面采样器设置为纹理单元为 0*/
    glUniform1i(m_ySamplerLoc, 0);


    // Bind the UV plane map
    /*绑定UV平面图*/
    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, m_uvTextureId);

    // Set the UV plane sampler to texture unit to 1
    /*将 UV 平面采样器设置为纹理单元为 1*/
    glUniform1i(m_uvSamplerLoc, 1);

    /*glDrawElements是一个OPENGL的图元绘制函数，从数组中获得数据渲染图元。
     * 可以在显示列表中包含glDrawElements，当glDrawElements被包含进显示列表时，相应的顶点、法线、
     * 颜色数组数据也得进入显示列表的，因为那些数组指针是ClientSideState的变量，在显示列表创建的时候而不是在显示列表执行的时候，
     * 这些数组指针的值影响显示列表。glDrawElements只能用在OPENGL1.1或者更高的版本。
     * https://baike.baidu.com/item/glDrawElements/4172299?fr=aladdin
     * */
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, indices);
}

/**
 * 在析构函数中删除程序以及纹理数据
 */
void NV21TextureMapSample::Destroy() {
    if (m_ProgramObj){
        glDeleteProgram(m_ProgramObj);
        glDeleteTextures(1,&m_yTextureId);
        glDeleteTextures(1,&m_uvTextureId);
        m_ProgramObj=GL_NONE;
    }
}
