//package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;
//
//import java.util.HashMap;
//
//import static edu.mines.jtk.ogl.Gl.glEnableVertexAttribArray;
//import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
//import static org.lwjgl.opengl.ARBVertexArrayObject.glDeleteVertexArrays;
//import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
//import static org.lwjgl.opengl.GL11.GL_FLOAT;
//import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
//import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
//import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
//
//public class VertexArray{
//    private static final int FLOAT_BYTE_COUNT = 4;
//
//    private final int id;
//    private final HashMap<Integer, Atribute> atributes;
//
//    public VertexArray(HashMap<Integer, Atribute> atributes) {
//        this(atributes, glGenVertexArrays());
//    }
//
//    public VertexArray(HashMap<Integer, Atribute> atributes, int id) {
//        this.id = id;
//        this.atributes = new HashMap<>(atributes);
//
//        bind();
//
//        int floatCount = 0;
//
//        for(Atribute atribute : atributes.values()){
//            floatCount += atribute.size;
//        }
//
//        final int stride = floatCount * FLOAT_BYTE_COUNT;
//
//        floatCount = 0;
//
//        for(int atributeLocation : atributes.keySet()){
//            Atribute atribute = atributes.get(atributeLocation);
//
//            glEnableVertexAttribArray(atributeLocation);
//
//            glVertexAttribPointer(atributeLocation, atribute.size, GL_FLOAT, false, stride,floatCount * FLOAT_BYTE_COUNT);
//
//            glVertexAttribDivisor(atributeLocation, atribute.perInstance ? 1 : 0);
//
//            floatCount += atribute.size;
//        }
//
//        disableAttributes();
//        unbind();
//    }
//
//    public void bind(){
//        glBindVertexArray(id);
//    }
//
//    public void enableAttributes(){
//        for(int atributeLocation : atributes.keySet()){
//            glEnableVertexAttribArray(atributeLocation);
//        }
//    }
//
//    public void unbind(){
//        glBindVertexArray(0);
//    }
//
//    public void disableAttributes(){
//        for(int atributeLocation : atributes.keySet()){
//            glDisableVertexAttribArray(atributeLocation);
//        }
//    }
//
//    public void dispose(){
//        disableAttributes();
//        unbind();
//        glDeleteVertexArrays(id);
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    private static final class Atribute{
//        private int size;
//        private boolean perInstance;
//
//        public Atribute(int size){
//            this(size, false);
//        }
//
//        public Atribute(int size, boolean perInstance) {
//            this.size = size;
//            this.perInstance = perInstance;
//        }
//    }
//}
