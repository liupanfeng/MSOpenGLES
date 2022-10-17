cmake_minimum_required(VERSION 3.18.1)
project("msopengles")

#增加FFmpeg 支持  静态库导入方式
set(FFMPEG_DIR  ${CMAKE_CURRENT_SOURCE_DIR}/libFFmpeg/armeabi-v7a)
include_directories(${FFMPEG_DIR}/include)
link_directories(${FFMPEG_DIR}/lib)

#加载源文件
file(GLOB file_all *.cpp  *.c)


#引用头文件
include_directories(opencv)
include_directories(include)
include_directories(glm)




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
        GLESv3                  #OpenGL ES 3.0版本
        GLESv2                  #OpenGL ES 2.0版本
        GLESv1_CM
        EGL                     #自定义EGL FBO会用到这个
        android                 #ANativeWindow 使用这个库
        avformat avcodec avdevice avfilter avutil fdk-aac x264 z c m dl
        ${log-lib})