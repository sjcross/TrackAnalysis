package wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component;

import java.util.Arrays;

public class FaceSI {
    private final int[] indexs;

    public FaceSI(int[] indexs) {
        this.indexs = indexs;
    }

    public FaceSI(
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

    public FaceSI(
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

    public int getFaceOrder() {
        return indexs.length;
    }

    public int getPrimtiveFaceCount() {
        return getFaceOrder() - 2;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indexs);
    }
}
