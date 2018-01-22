package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import java.util.Arrays;

public class Face {
    private final int[] indexs;

    public Face(int[] indexs) {
//        if(indexs.length < 3){
//            throw new Exception("Face order less than 3"); TODO: Handle this
//        }
        this.indexs = indexs;
    }

    public Face(
            int iA,
            int iB,
            int iC
    ) {
        this(
                new int[]{
                        iA,
                        iB,
                        iC
                }
        );
    }

    public Face(
            int iA,
            int iB,
            int iC,
            int iD
    ) {
        this(
                new int[]{
                        iA,
                        iB,
                        iC,
                        iD
                }
        );
    }

    public int[] getIndexs() {
        return indexs;
    }

    public int getFaceOrder(){
        return indexs.length;
    }

    public int getPrimtiveFaceCount(){
        return getFaceOrder() - 2;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indexs);
    }
}
