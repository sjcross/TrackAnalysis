//package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;
//
//import org.lwjgl.system.CallbackI;
//
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//
//import static org.lwjgl.opengl.GL15.*;
//
//public class Buffer {
//
//    private final int target;
//    private final int id;
//
//    public Buffer(int target) {
//        this(target, glGenBuffers());
//    }
//
//    public Buffer(int target, int id) {
//        this.target = target;
//        this.id = id;
//    }
//
//    public void bind(){
//        glBindBuffer(target, id);
//    }
//
//    public void unbind(){
//        glBindBuffer(target, 0);
//    }
//
//    public void bufferData(ByteBuffer buffer, int usage){
//        glBufferData(target, buffer, usage);
//    }
//
//    public void bufferData(IntBuffer buffer, int usage){
//        glBufferData(target, buffer, usage);
//    }
//
//    public void bufferData(FloatBuffer buffer, int usage){
//        glBufferData(target, buffer, usage);
//    }
//
//    public void dispose(){
//        unbind();
//        glDeleteBuffers(id);
//    }
//}
