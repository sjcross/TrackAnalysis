package io.github.sjcross.TrackAnalysis.Plot3D.Graphics.Component;

import java.util.Objects;

public class FaceMI {
    private final IndexSet[] indexSets;

    public FaceMI(IndexSet[] indexSets) {
        this.indexSets = indexSets;
    }

    public FaceMI(
            int vA,
            int vB,
            int vC
    ) {
        this(
                new IndexSet[]{
                        new IndexSet(vA),
                        new IndexSet(vB),
                        new IndexSet(vC)
                }
        );
    }

    public FaceMI(
            int vA,
            int vB,
            int vC,
            int vD
    ) {
        this(
                new IndexSet[]{
                        new IndexSet(vA),
                        new IndexSet(vB),
                        new IndexSet(vC),
                        new IndexSet(vD)
                }
        );
    }

    public FaceMI(
            int vA, int tA,
            int vB, int tB,
            int vC, int tC
    ) {
        this(
                new IndexSet[]{
                        new IndexSet(vA, tA),
                        new IndexSet(vB, tB),
                        new IndexSet(vC, tC)
                }
        );
    }

    public FaceMI(
            int vA, int tA,
            int vB, int tB,
            int vC, int tC,
            int vD, int tD
    ) {
        this(
                new IndexSet[]{
                        new IndexSet(vA, tA),
                        new IndexSet(vB, tB),
                        new IndexSet(vC, tC),
                        new IndexSet(vD, tD)
                }
        );
    }


    public IndexSet[] getIndexSets() {
        return indexSets;
    }

    public int getFaceOrder() {
        return indexSets.length;
    }

    public int getPrimtiveFaceCount() {
        return getFaceOrder() - 2;
    }

    public static class IndexSet {
        public static final int NULL_INDEX = -1;

        private final int vertexPositionIndex;
        private final int textureCoordinateIndex;

        public IndexSet(int vertexPositionIndex) {
            this(vertexPositionIndex, NULL_INDEX);
        }

        public IndexSet(int vertexPositionIndex, int textureCoordinateIndex) {
            this.vertexPositionIndex = vertexPositionIndex;
            this.textureCoordinateIndex = textureCoordinateIndex;
        }

        public int getVertexPositionIndex() {
            return vertexPositionIndex;
        }

        public int getTextureCoordinateIndex() {
            return textureCoordinateIndex;
        }

        @Override
        public boolean equals(Object obj) {
            IndexSet indexSet = (IndexSet) obj;
            return indexSet.vertexPositionIndex == vertexPositionIndex && indexSet.textureCoordinateIndex == textureCoordinateIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertexPositionIndex, textureCoordinateIndex);
        }
    }
}
