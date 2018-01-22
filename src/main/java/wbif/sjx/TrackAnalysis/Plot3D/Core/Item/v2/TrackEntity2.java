//package wbif.sjx.TrackAnalysis.Plot3D.Core.Item.v2;
//
//import org.lwjgl.BufferUtils;
//import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
//import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;
//import wbif.sjx.TrackAnalysis.Plot3D.Utils.DataTypeUtils;
//import wbif.sjx.common.Object.Track;
//
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.util.ArrayList;
//
//import static edu.mines.jtk.ogl.Gl.glEnableVertexAttribArray;
//import static org.lwjgl.opengl.GL11.GL_FLOAT;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
//import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
//import static org.lwjgl.opengl.GL30.glBindVertexArray;
//import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
//
//public class TrackEntity2 {
//    private static final int FLOAT_SIZE = 4;
//    private static final int VEC4_SIZE = 4;
//
//    private final int igmbo;
//
//    public TrackEntity2(Track track) {
//
//        ArrayList<Matrix4f> matrix4fs = new ArrayList<>();
//        ArrayList<Float> pipeLengths = new ArrayList<>();
//
//        ArrayList<Vector3f> globalPositionList = new ArrayList<>();
//
//        for(int frame : track.keySet()){
//            globalPositionList.add(frame, DataTypeUtils.toVector3f(track.get(frame)));
//        }
//
//        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(globalPositionList.size() * Matrix4f.ORDER * Matrix4f.ORDER);
//
//        for(int i = 0; i < globalPositionList.size() - 1; i++){
//            Vector3f currentPosition = globalPositionList.get(i);
//            Vector3f nextPosition = globalPositionList.get(i + 1);
//            Vector3f pipeVector = Vector3f.Subtract(nextPosition, currentPosition);
//
//            Matrix4f globalMatrix = Matrix4f.Translation(currentPosition);
//            globalMatrix.multiply(Matrix4f.EulerRotation(pipeVector.getPhi(),90,0));
//
//            matrix4fs.add(globalMatrix);
//            pipeLengths.add(pipeVector.getLength());
//        }
//
//        matrix4fs.add(Matrix4f.Translation(globalPositionList.get(globalPositionList.size() - 1)));
//
//        igmbo = glGenBuffers();
//
//        glBindBuffer(GL_ARRAY_BUFFER, igmbo);
//        glBufferData(GL_ARRAY_BUFFER, (ByteBuffer) null, GL_STATIC_DRAW);
//
//
////        int VAO = 0;
////        glBindVertexArray(VAO);
////        // vertex Attributes
////        glEnableVertexAttribArray(3);
////        glVertexAttribPointer(3, VEC4_SIZE, GL_FLOAT, false, VEC4_SIZE * FLOAT_SIZE,0);
////        glEnableVertexAttribArray(4);
////        glVertexAttribPointer(4, VEC4_SIZE, GL_FLOAT, false, VEC4_SIZE * FLOAT_SIZE, VEC4_SIZE);
////        glEnableVertexAttribArray(5);
////        glVertexAttribPointer(5, VEC4_SIZE, GL_FLOAT, false, VEC4_SIZE * FLOAT_SIZE,2 * VEC4_SIZE);
////        glEnableVertexAttribArray(6);
////        glVertexAttribPointer(6, VEC4_SIZE, GL_FLOAT, false, VEC4_SIZE * FLOAT_SIZE, 3 * VEC4_SIZE);
////
////        glVertexAttribDivisor(3, 1);
////        glVertexAttribDivisor(4, 1);
////        glVertexAttribDivisor(5, 1);
////        glVertexAttribDivisor(6, 1);
////
////        glBindVertexArray(0);
//    }
//
//    public void render(){
//
//    }
//}
