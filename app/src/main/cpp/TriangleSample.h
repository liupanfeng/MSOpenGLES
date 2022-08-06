//
// Created by ms on 2022/7/5.
//

#ifndef MSOPENGLES_TRIANGLESAMPLE_H
#define MSOPENGLES_TRIANGLESAMPLE_H

#include <GLES3/gl3.h>
#include "GLUtils.h"
#include "android_log_util.h"



class TriangleSample  {

public:
    GLuint m_ProgramObj;
    GLuint m_VertexShader;
    GLuint m_FragmentShader;

public:
    TriangleSample();
    ~TriangleSample();

public:
    void Init();
    void Draw();

};


#endif //MSOPENGLES_TRIANGLESAMPLE_H
