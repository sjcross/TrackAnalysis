package wbif.sjx.TrackAnalysis.Plot3D.Graphics;

import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.*;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.ArrayList;

/**
 * Created by JDJFisher on 31/07/2017.
 */
public class MeshFactory {

    private MeshFactory() {
    }

    public static Mesh plane(float width, float length) {
        float halfWidth = Math.abs(width / 2);
        float halfLength = Math.abs(length / 2);

        Vector3f[] vertexPositions = new Vector3f[]{
                new Vector3f(halfWidth, 0, -halfLength),
                new Vector3f(-halfWidth, 0, -halfLength),
                new Vector3f(-halfWidth, 0, halfLength),
                new Vector3f(halfWidth, 0, halfLength)
        };

        Vector2f[] textureCoordinates = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(0, 1),
                new Vector2f(0, 0),
                new Vector2f(1, 0)
        };

        FaceMI[] faces = new FaceMI[]{
                new FaceMI(
                        0, 0,
                        1, 1,
                        2, 2,
                        3, 3
                )
        };

        return new Mesh(new MIMeshData(vertexPositions, textureCoordinates, faces));
    }

    public static Mesh cube(float sideLength) {
        sideLength = Math.abs(sideLength);
        return cuboid(sideLength, sideLength, sideLength);
    }

    public static Mesh cuboid(float width, float height, float length) {
        float halfWidth = Math.abs(width / 2);
        float halfHeight = Math.abs(height / 2);
        float halfLength = Math.abs(length / 2);

        Vertex[] vertices = new Vertex[]{
                new Vertex(new Vector3f(-halfWidth, halfHeight, halfLength)),   //V0
                new Vertex(new Vector3f(-halfWidth, halfHeight, -halfLength)),  //V1
                new Vertex(new Vector3f(halfWidth, halfHeight, -halfLength)),   //V2
                new Vertex(new Vector3f(halfWidth, halfHeight, halfLength)),    //V3
                new Vertex(new Vector3f(-halfWidth, -halfHeight, halfLength)),  //V4
                new Vertex(new Vector3f(-halfWidth, -halfHeight, -halfLength)), //V5
                new Vertex(new Vector3f(halfWidth, -halfHeight, -halfLength)),  //V6
                new Vertex(new Vector3f(halfWidth, -halfHeight, halfLength))    //V7
        };

        FaceSI[] faces = new FaceSI[]{
                new FaceSI(//front
                        3,
                        0,
                        4,
                        7
                ),
                new FaceSI(//left
                        0,
                        1,
                        5,
                        4
                ),
                new FaceSI(//right
                        2,
                        3,
                        7,
                        6
                ),
                new FaceSI(//back
                        1,
                        2,
                        6,
                        5
                ),
                new FaceSI(//top
                        2,
                        1,
                        0,
                        3
                ),
                new FaceSI(//bottom
                        7,
                        4,
                        5,
                        6
                ),
        };

        return new Mesh(vertices, faces);
    }

    public static Mesh cuboidFrame(float width, float height, float length, float thickness) {
        float xi = width / 2;
        float yi = height / 2;
        float zi = length / 2;

        float xe = xi + thickness;
        float ye = yi + thickness;
        float ze = zi + thickness;

        //e is for exterior, i is for interior
        //u are the sub vertexes of the vertexes v of the main cube

        Vertex[] vertices = new Vertex[]{
                //v0
                new Vertex(new Vector3f(-xe, ye, ze)),//u0
                new Vertex(new Vector3f(-xe, yi, ze)),//u1
                new Vertex(new Vector3f(-xi, yi, ze)),//u2
                new Vertex(new Vector3f(-xi, ye, ze)),//u3
                new Vertex(new Vector3f(-xe, ye, zi)),//u4
                new Vertex(new Vector3f(-xe, yi, zi)),//u5
                new Vertex(new Vector3f(-xi, yi, zi)),//u6
                new Vertex(new Vector3f(-xi, ye, zi)),//u7
                //v1
                new Vertex(new Vector3f(-xe, -yi, ze)),//u0
                new Vertex(new Vector3f(-xe, -ye, ze)),//u1
                new Vertex(new Vector3f(-xi, -ye, ze)),//u2
                new Vertex(new Vector3f(-xi, -yi, ze)),//u3
                new Vertex(new Vector3f(-xe, -yi, zi)),//u4
                new Vertex(new Vector3f(-xe, -ye, zi)),//u5
                new Vertex(new Vector3f(-xi, -ye, zi)),//u6
                new Vertex(new Vector3f(-xi, -yi, zi)),//u7
                //v2
                new Vertex(new Vector3f(xi, -yi, ze)),//u0
                new Vertex(new Vector3f(xi, -ye, ze)),//u1
                new Vertex(new Vector3f(xe, -ye, ze)),//u2
                new Vertex(new Vector3f(xe, -yi, ze)),//u3
                new Vertex(new Vector3f(xi, -yi, zi)),//u4
                new Vertex(new Vector3f(xi, -ye, zi)),//u5
                new Vertex(new Vector3f(xe, -ye, zi)),//u6
                new Vertex(new Vector3f(xe, -yi, zi)),//u7
                //v3
                new Vertex(new Vector3f(xi, ye, ze)),//u0
                new Vertex(new Vector3f(xi, yi, ze)),//u1
                new Vertex(new Vector3f(xe, yi, ze)),//u2
                new Vertex(new Vector3f(xe, ye, ze)),//u3
                new Vertex(new Vector3f(xi, ye, zi)),//u4
                new Vertex(new Vector3f(xi, yi, zi)),//u5
                new Vertex(new Vector3f(xe, yi, zi)),//u6
                new Vertex(new Vector3f(xe, ye, zi)),//u7
                //v4
                new Vertex(new Vector3f(-xe, ye, -zi)),//u0
                new Vertex(new Vector3f(-xe, yi, -zi)),//u1
                new Vertex(new Vector3f(-xi, yi, -zi)),//u2
                new Vertex(new Vector3f(-xi, ye, -zi)),//u3
                new Vertex(new Vector3f(-xe, ye, -ze)),//u4
                new Vertex(new Vector3f(-xe, yi, -ze)),//u5
                new Vertex(new Vector3f(-xi, yi, -ze)),//u6
                new Vertex(new Vector3f(-xi, ye, -ze)),//u7
                //v5
                new Vertex(new Vector3f(-xe, -yi, -zi)),//u0
                new Vertex(new Vector3f(-xe, -ye, -zi)),//u1
                new Vertex(new Vector3f(-xi, -ye, -zi)),//u2
                new Vertex(new Vector3f(-xi, -yi, -zi)),//u3
                new Vertex(new Vector3f(-xe, -yi, -ze)),//u4
                new Vertex(new Vector3f(-xe, -ye, -ze)),//u5
                new Vertex(new Vector3f(-xi, -ye, -ze)),//u6
                new Vertex(new Vector3f(-xi, -yi, -ze)),//u7
                //v6
                new Vertex(new Vector3f(xi, -yi, -zi)),//u0
                new Vertex(new Vector3f(xi, -ye, -zi)),//u1
                new Vertex(new Vector3f(xe, -ye, -zi)),//u2
                new Vertex(new Vector3f(xe, -yi, -zi)),//u3
                new Vertex(new Vector3f(xi, -yi, -ze)),//u4
                new Vertex(new Vector3f(xi, -ye, -ze)),//u5
                new Vertex(new Vector3f(xe, -ye, -ze)),//u6
                new Vertex(new Vector3f(xe, -yi, -ze)),//u7
                //v7
                new Vertex(new Vector3f(xi, ye, -zi)),//u0
                new Vertex(new Vector3f(xi, yi, -zi)),//u1
                new Vertex(new Vector3f(xe, yi, -zi)),//u2
                new Vertex(new Vector3f(xe, ye, -zi)),//u3
                new Vertex(new Vector3f(xi, ye, -ze)),//u4
                new Vertex(new Vector3f(xi, yi, -ze)),//u5
                new Vertex(new Vector3f(xe, yi, -ze)),//u6
                new Vertex(new Vector3f(xe, ye, -ze)),//u7
        };

        ArrayList<FaceSI> faces = new ArrayList<>();

        //v1 to v0
        faces.add(new FaceSI(//top
                2,
                1,
                8,
                11
        ));
        faces.add(new FaceSI(//left
                1,
                5,
                12,
                8
        ));
        faces.add(new FaceSI(//right
                6,
                2,
                11,
                15
        ));
        faces.add(new FaceSI(//bottom
                5,
                6,
                15,
                12
        ));

        //v2 to v3
        faces.add(new FaceSI(//top
                26,
                25,
                16,
                19
        ));
        faces.add(new FaceSI(//left
                25,
                29,
                20,
                16
        ));
        faces.add(new FaceSI(//right
                30,
                26,
                19,
                23
        ));
        faces.add(new FaceSI(//bottom
                29,
                30,
                23,
                20
        ));

        //v5 to v4
        faces.add(new FaceSI(//top
                34,
                33,
                40,
                43
        ));
        faces.add(new FaceSI(//left
                33,
                37,
                44,
                40
        ));
        faces.add(new FaceSI(//right
                38,
                34,
                43,
                47
        ));
        faces.add(new FaceSI(//bottom
                37,
                38,
                47,
                44
        ));

        //v5 to v4
        faces.add(new FaceSI(//top
                58,
                57,
                48,
                51
        ));
        faces.add(new FaceSI(//left
                57,
                61,
                52,
                48
        ));
        faces.add(new FaceSI(//right
                62,
                58,
                51,
                55
        ));
        faces.add(new FaceSI(//bottom
                61,
                62,
                55,
                52
        ));

        //v2 to v1
        faces.add(new FaceSI(//top
                11,
                10,
                17,
                16
        ));
        faces.add(new FaceSI(//left
                10,
                14,
                21,
                17
        ));
        faces.add(new FaceSI(//right
                15,
                11,
                16,
                20
        ));
        faces.add(new FaceSI(//bottom
                14,
                15,
                20,
                21
        ));

        //v3 to v0
        faces.add(new FaceSI(//top
                3,
                2,
                25,
                24
        ));
        faces.add(new FaceSI(//left
                2,
                6,
                29,
                25
        ));
        faces.add(new FaceSI(//right
                7,
                3,
                24,
                28
        ));
        faces.add(new FaceSI(//bottom
                6,
                7,
                28,
                29
        ));

        //v6 to v5
        faces.add(new FaceSI(//top
                43,
                42,
                49,
                48
        ));
        faces.add(new FaceSI(//left
                42,
                46,
                53,
                49
        ));
        faces.add(new FaceSI(//right
                47,
                43,
                48,
                52
        ));
        faces.add(new FaceSI(//bottom
                46,
                47,
                52,
                53
        ));

        //v7 to v4
        faces.add(new FaceSI(//top
                35,
                34,
                57,
                56
        ));
        faces.add(new FaceSI(//left
                34,
                38,
                61,
                57
        ));
        faces.add(new FaceSI(//right
                39,
                35,
                56,
                60
        ));
        faces.add(new FaceSI(//bottom
                38,
                39,
                60,
                61
        ));

        //v5 to v1
        faces.add(new FaceSI(//top
                14,
                13,
                41,
                42
        ));
        faces.add(new FaceSI(//left
                13,
                12,
                40,
                41
        ));
        faces.add(new FaceSI(//right
                15,
                14,
                42,
                43
        ));
        faces.add(new FaceSI(//bottom
                12,
                15,
                43,
                40
        ));

        //v6 to v2
        faces.add(new FaceSI(//top
                22,
                21,
                49,
                50
        ));
        faces.add(new FaceSI(//left
                21,
                20,
                48,
                49
        ));
        faces.add(new FaceSI(//right
                23,
                22,
                50,
                51
        ));
        faces.add(new FaceSI(//bottom
                20,
                23,
                51,
                48
        ));

        //v4 to v0
        faces.add(new FaceSI(//top
                6,
                5,
                33,
                34
        ));
        faces.add(new FaceSI(//left
                5,
                4,
                32,
                33
        ));
        faces.add(new FaceSI(//right
                7,
                6,
                34,
                35
        ));
        faces.add(new FaceSI(//bottom
                4,
                7,
                35,
                32
        ));

        //v7 to v3
        faces.add(new FaceSI(//top
                30,
                29,
                57,
                58
        ));
        faces.add(new FaceSI(//left
                29,
                28,
                56,
                57
        ));
        faces.add(new FaceSI(//right
                31,
                30,
                58,
                59
        ));
        faces.add(new FaceSI(//bottom
                28,
                31,
                59,
                56
        ));

        //v0
        faces.add(new FaceSI(//0
                4,
                5,
                1,
                0
        ));
        faces.add(new FaceSI(//1
                7,
                4,
                0,
                3
        ));
        faces.add(new FaceSI(//2
                3,
                0,
                1,
                2
        ));

        //v1
        faces.add(new FaceSI(//0
                12,
                13,
                9,
                8
        ));
        faces.add(new FaceSI(//1
                13,
                14,
                10,
                9
        ));
        faces.add(new FaceSI(//2
                11,
                8,
                9,
                10
        ));

        //v2
        faces.add(new FaceSI(//0
                22,
                23,
                19,
                18
        ));
        faces.add(new FaceSI(//1
                21,
                22,
                18,
                17
        ));
        faces.add(new FaceSI(//2
                19,
                16,
                17,
                18
        ));

        //v3
        faces.add(new FaceSI(//0
                30,
                31,
                27,
                26
        ));
        faces.add(new FaceSI(//1
                31,
                28,
                24,
                27
        ));
        faces.add(new FaceSI(//2
                27,
                24,
                25,
                26
        ));

        //v4
        faces.add(new FaceSI(//0
                36,
                37,
                33,
                32
        ));
        faces.add(new FaceSI(//1
                39,
                36,
                32,
                35
        ));
        faces.add(new FaceSI(//2
                39,
                38,
                37,
                36
        ));

        //v5
        faces.add(new FaceSI(//0
                44,
                45,
                41,
                40
        ));
        faces.add(new FaceSI(//1
                45,
                46,
                42,
                41
        ));
        faces.add(new FaceSI(//2
                47,
                46,
                45,
                44
        ));

        //v6
        faces.add(new FaceSI(//0
                54,
                55,
                51,
                50
        ));
        faces.add(new FaceSI(//1
                53,
                54,
                50,
                49
        ));
        faces.add(new FaceSI(//2
                55,
                54,
                53,
                52
        ));

        //v7
        faces.add(new FaceSI(//0
                62,
                63,
                59,
                58
        ));
        faces.add(new FaceSI(//1
                63,
                60,
                56,
                59
        ));
        faces.add(new FaceSI(//2
                63,
                62,
                61,
                60
        ));

        return new Mesh(vertices, faces);
    }

    public static Mesh pipe(float radius, int resolution, float length) {
        resolution = resolution < 3 ? 3 : resolution;
        radius = Math.abs(radius);
        length = Math.abs(length);

        Vertex[] vertices = new Vertex[resolution * 2];

        double deltaTheta = 2 * Math.PI / (resolution);
        double theta = 0;

        for (int i = 0; i < resolution; i++) {
            float rSinTheta = radius * (float) Math.sin(theta);
            float rCosTheta = radius * (float) Math.cos(theta);
            vertices[i] = new Vertex(new Vector3f(rCosTheta, length, rSinTheta));
            vertices[resolution + i] = new Vertex(new Vector3f(rCosTheta, 0, rSinTheta));
            theta -= deltaTheta;
        }

        ArrayList<FaceSI> faces = new ArrayList<>();

        for (int i = 0; i < resolution - 1; i++) {
            faces.add(new FaceSI(
                    i + 1,
                    i,
                    i + resolution,
                    i + 1 + resolution
            ));
        }

        faces.add(new FaceSI(
                0,
                resolution - 1,
                (resolution * 2) - 1,
                resolution
        ));

        return new Mesh(vertices, faces);
    }

    public static Mesh sphere(float radius, int resolution) {
        resolution = resolution < 3 ? 3 : resolution;
        radius = Math.abs(radius);

        final int ringCount = resolution - 1;
        final int meridianCount = resolution;

        Vertex[] vertices = new Vertex[ringCount * meridianCount + 2];

        final int northPoleIndex = vertices.length - 2;
        final int southPoleIndex = vertices.length - 1;

        vertices[northPoleIndex] = new Vertex(new Vector3f(0, radius, 0));
        vertices[southPoleIndex] = new Vertex(new Vector3f(0, -radius, 0));

        double deltaAlpha = Math.PI / resolution;
        double deltaTheta = 2 * Math.PI / resolution;
        double alpha = 0;
        double theta;

        for (int ring = 0; ring < ringCount; ring++) {
            alpha -= deltaAlpha;
            theta = 0;
            for (int meridian = 0; meridian < meridianCount; meridian++) {
                float sinTheta = (float) Math.sin(theta);
                float cosTheta = (float) Math.cos(theta);
                float sinPhi = (float) Math.sin(alpha);
                float cosPhi = (float) Math.cos(alpha);

                vertices[(ring * meridianCount) + meridian] = new Vertex(new Vector3f(
                        radius * sinPhi * cosTheta,
                        radius * cosPhi,
                        radius * sinPhi * sinTheta
                ));
                theta -= deltaTheta;
            }
        }

        ArrayList<FaceSI> faces = new ArrayList<>();

        for (int ring = 0; ring < ringCount - 1; ring++) {

            final int currentRingIndexOffset = ring * meridianCount;
            final int nextRingIndexOffset = (ring + 1) * meridianCount;

            for (int meridian = 0; meridian < meridianCount - 1; meridian++) {

                //faces
                faces.add(new FaceSI(
                        currentRingIndexOffset + meridian + 1,
                        currentRingIndexOffset + meridian,
                        nextRingIndexOffset + meridian,
                        nextRingIndexOffset + meridian + 1
                ));
            }

            //filler faces
            faces.add(new FaceSI(
                    currentRingIndexOffset,
                    currentRingIndexOffset + meridianCount - 1,
                    nextRingIndexOffset + meridianCount - 1,
                    nextRingIndexOffset
            ));
        }

        final int bottomRingIndexOffset = (ringCount - 1) * meridianCount;

        for (int meridian = 0; meridian < resolution - 1; meridian++) {
            //top faces
            faces.add(new FaceSI(
                    northPoleIndex,
                    meridian,
                    meridian + 1
            ));

            //bottom faces
            faces.add(new FaceSI(
                    southPoleIndex,
                    bottomRingIndexOffset + meridian + 1,
                    bottomRingIndexOffset + meridian
            ));
        }

        //top faces filler
        faces.add(new FaceSI(
                northPoleIndex,
                meridianCount - 1,
                0
        ));

        //bottom faces filler
        faces.add(new FaceSI(
                southPoleIndex,
                bottomRingIndexOffset,
                bottomRingIndexOffset + meridianCount - 1
        ));

        return new Mesh(vertices, faces);
    }
}
