//package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;
//
//import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
//import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
//import static org.lwjgl.opengl.GL11.glDrawElements;
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
//
//public class RenderData {
//
//    private final int indexedVertexCount;
//    private final VertexArray vao;
//    private final Buffer vbo;
//    private final Buffer ibo;
//
//    public RenderData(int indexedVertexCount) {
//        this(indexedVertexCount, new VertexArray(), new Buffer(GL_ARRAY_BUFFER), new Buffer(GL_ELEMENT_ARRAY_BUFFER));
//    }
//
//    public RenderData(int indexedVertexCount, VertexArray vao, Buffer vbo, Buffer ibo) {
//        this.indexedVertexCount = indexedVertexCount;
//        this.vao = vao;
//        this.vbo = vbo;
//        this.ibo = ibo;
//    }
//
//    public void render(){
//        vao.bind();
//        vbo.bind();
//        ibo.bind();
//
//        vao.enableAttributes();
//
//        glDrawElements(GL_TRIANGLES, indexedVertexCount, GL_UNSIGNED_INT, 0);
//
//        vao.disableAttributes();
//
//        vao.unbind();
//        vbo.unbind();
//        ibo.unbind();
//    }
//
//    public void dispose(){
//        vao.dispose();
//        vbo.dispose();
//        ibo.dispose();
//    }
//
//    public int getIndexedVertexCount() {
//        return indexedVertexCount;
//    }
//
//    public VertexArray getVao() {
//        return vao;
//    }
//
//    public Buffer getVbo() {
//        return vbo;
//    }
//
//    public Buffer getIbo() {
//        return ibo;
//    }
//}
