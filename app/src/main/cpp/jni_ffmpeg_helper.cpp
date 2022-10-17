#include <jni.h>

extern "C" { //ffmpeg 的引入必须要放置到{}内部
    #include <libavcodec/avcodec.h>
    #include <libavformat/avformat.h>
    #include <libavdevice/avdevice.h>
    #include <libavfilter/avfilter.h>
    #include <libavutil/avutil.h>
}



extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_FFmpegConfigInfoActivity_getFFmpegConfigInfo(JNIEnv *env, jobject thiz) {
    char buffer[10240] = {0};
    sprintf(buffer, "%s", avcodec_configuration());
    return env->NewStringUTF(buffer);
}