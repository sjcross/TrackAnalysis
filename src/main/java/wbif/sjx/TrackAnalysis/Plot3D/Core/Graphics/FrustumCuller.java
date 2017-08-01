package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics;


import wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics.Item.Entity;
import wbif.sjx.TrackAnalysis.Plot3D.Math.Matrix4f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector4f;

/**
 * Created by Jordan Fisher on 02/07/2017.
 */
public class FrustumCuller {
    private Matrix4f clipMatrix;
    private Vector4f[] frustumPlanes;

    public FrustumCuller(){
        clipMatrix = new Matrix4f();
        frustumPlanes = new Vector4f[6];
        for (int i = 0; i < frustumPlanes.length; i++){
            frustumPlanes[i] = new Vector4f();
        }
    }

    public void setClipMatrix(Matrix4f projectionMatrix, Matrix4f cameraMatrix) {
        clipMatrix = Matrix4f.multiply(cameraMatrix, projectionMatrix);
        update();
    }

    public void update(){
        float[][] E = clipMatrix.elements;

        frustumPlanes[0].set(E[3][0] + E[0][0], E[3][1] + E[0][1], E[3][2] + E[0][2], E[3][3] + E[0][3]);
        frustumPlanes[1].set(E[3][0] - E[0][0], E[3][1] - E[0][1], E[3][2] - E[0][2], E[3][3] - E[0][3]);
        frustumPlanes[2].set(E[3][0] + E[1][0], E[3][1] + E[1][1], E[3][2] + E[1][2], E[3][3] + E[1][3]);
        frustumPlanes[3].set(E[3][0] - E[1][0], E[3][1] - E[1][1], E[3][2] - E[1][2], E[3][3] - E[1][3]);
        frustumPlanes[4].set(E[3][0] + E[2][0], E[3][1] + E[2][1], E[3][2] + E[2][2], E[3][3] + E[2][3]);
        frustumPlanes[5].set(E[3][0] - E[2][0], E[3][1] - E[2][1], E[3][2] - E[2][2], E[3][3] - E[2][3]);

        for (int i = 0; i < frustumPlanes.length; i++){
            frustumPlanes[i].normalize3();
        }
    }


    public boolean isInsideFrustum(Entity e) {
        boolean result = true;
//        for (int i = 0; i < frustumPlanes.length; i++) {
//            Vector4f plane = frustumPlanes[i];
//            if (plane.x * e.getPosition().getX() + plane.y * e.getPosition().getY() + plane.z * e.getPosition().getZ() + plane.w <= - e.getMesh().getBoundingSphereRadius() * e.getScale()) {
//                result = false;
//                return result;
//            }
//        }
        return result;
    }

    public void dispose(){
        clipMatrix = null;
        frustumPlanes = null;
    }
}
