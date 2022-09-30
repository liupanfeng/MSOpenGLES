
#ifndef MSOPENGLES_MSVAOEXAMPLE_H
#define MSOPENGLES_MSVAOEXAMPLE_H

#include "GLSampleBase.h"
#include "GLUtils.h"

class MSVAOExample : public GLSampleBase{

public:
    MSVAOExample();
    virtual  ~MSVAOExample();
    virtual void LoadImage(NativeImage * image);
    virtual void Init();
    virtual void Draw(int width,int height);
    virtual void Destroy();

private:
    GLuint m_VAO_ID;
    GLuint m_VAO_IDS[2];

};


#endif //MSOPENGLES_MSVAOEXAMPLE_H
