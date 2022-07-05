cmake_minimum_required(VERSION 3.18.1)
project("msopengles")
#加载源文件
file(GLOB file_all *.cpp  *.c)

#引用头文件
include_directories(opencv)
include_directories(include)

#指定库查找路径
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/")

add_library(
        msopengles
        SHARED
        ${file_all})

find_library(
        log-lib
        log)

target_link_libraries(
        msopengles
        assimp                  #一个解析3D模型的库
        freetype                #彻底开源的、可扩展、可定制且可移植的字体引擎
        opencv_java3            #图像人脸追踪 图片处理等
        GLESv3                  #OpenGL ES 2.0版本
        EGL                     #自定义EGL FBO会用到这个
        android                 #ANativeWindow 使用这个库
        ${log-lib})