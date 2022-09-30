#version 300 es

precision mediump float;
in vec4 v_color;
in vec4 v_position;
out vec4 o_frag_Color;

void main() {
    float n=10.0;
    float span=1.0;
    int i = int((v_position.x+0.5)/span);
    int j = int((v_position.y+0.5)/span);

    int grayColor=int(mod(float(i+j),2.0));
    if(grayColor==1){
        float luminance = v_color.r*0.299+v_color.g*0.587+v_color.b*0.144;
        o_frag_Color = vec4(vec3(luminance),v_color.a);
    }else{
        o_frag_Color = v_color;
    }
}
