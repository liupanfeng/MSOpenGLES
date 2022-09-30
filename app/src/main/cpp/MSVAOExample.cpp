
#include "MSVAOExample.h"


#define VERTEX_POS_SIZE       3 // x, y and z
#define VERTEX_COLOR_SIZE     4 // r, g, b, and a

#define VERTEX_POS_INDX       0 //shader layout loc
#define VERTEX_COLOR_INDX     1 //shader layout loc

#define VERTEX_STRIDE         (sizeof(GLfloat)*(VERTEX_POS_SIZE+VERTEX_COLOR_SIZE))

MSVAOExample::MSVAOExample() {
    m_VAO_ID=0;
}

MSVAOExample::~MSVAOExample() {

}

void MSVAOExample::LoadImage(NativeImage *image) {

}

void MSVAOExample::Init() {

    // 4 vertices, with(x,y,z) ,(r, g, b, a) per-vertex
    GLfloat vertices[4 *(VERTEX_POS_SIZE + VERTEX_COLOR_SIZE )] =
            {
                    -0.5f,  0.5f, 0.0f,       // v0
                    1.0f,  0.0f, 0.0f, 1.0f,  // c0
                    -0.5f, -0.5f, 0.0f,       // v1
                    0.0f,  1.0f, 0.0f, 1.0f,  // c1
                    0.5f, -0.5f, 0.0f,        // v2
                    0.0f,  0.0f, 1.0f, 1.0f,  // c2
                    0.5f,  0.5f, 0.0f,        // v3
                    0.5f,  1.0f, 1.0f, 1.0f,  // c3
            };
    // Index buffer data
    GLushort indices[6] = { 0, 1, 2, 0, 2, 3};

    m_ProgramObj = GLUtils::CreateProgram(vShaderStr, fShaderStr, m_VertexShader, m_FragmentShader);

    /*创建VBO*/
    glGenBuffers(2, m_VAO_IDS);
    /*绑定VBO*/
    glBindBuffer(GL_ARRAY_BUFFER, m_VAO_IDS[0]);
    /*给VBO赋值*/
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_VAO_IDS[1]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

    glGenVertexArrays(1, &m_VAO_ID);
    glBindVertexArray(m_VAO_ID);

    glBindBuffer(GL_ARRAY_BUFFER, m_VAO_IDS[0]);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_VAO_IDS[1]);

    glEnableVertexAttribArray(VERTEX_POS_INDX);
    glEnableVertexAttribArray(VERTEX_COLOR_INDX);

    glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE, GL_FLOAT, GL_FALSE, VERTEX_STRIDE, (const void *)0);
    glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE, GL_FLOAT, GL_FALSE, VERTEX_STRIDE, (const void *)(VERTEX_POS_SIZE *sizeof(GLfloat)));

    glBindVertexArray(GL_NONE);

}

void MSVAOExample::Draw(int width, int height) {
    if(m_ProgramObj == 0) {
        return;
    }

    glUseProgram(m_ProgramObj);
    glBindVertexArray(m_VAO_ID);

    // Draw with the VAO settings
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, (const void *)0);

    // Return to the default VAO
    glBindVertexArray(GL_NONE);

}

void MSVAOExample::Destroy() {
    if(m_ProgramObj){
        glDeleteProgram(m_ProgramObj);
        glDeleteBuffers(2,m_VAO_IDS);
        glDeleteVertexArrays(1,&m_VAO_ID);
        m_ProgramObj= GL_NONE;
    }
}
