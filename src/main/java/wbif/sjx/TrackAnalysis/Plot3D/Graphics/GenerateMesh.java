package wbif.sjx.TrackAnalysis.Plot3D.Graphics;

import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Face;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Mesh;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.Texture;
import wbif.sjx.TrackAnalysis.Plot3D.Graphics.Component.TexturedFace;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.ArrayList;

public class GenerateMesh {
    private GenerateMesh() {}

    public static Mesh cube(float length){
        return cuboid(length, length, length);
    }

    public static Mesh cuboid(float width, float height, float length) {
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-width / 2, length / 2,height / 2),   //V0
                new Vector3f(-width / 2, -length / 2, height / 2),  //V1
                new Vector3f(width / 2, -length / 2, height / 2),   //V2
                new Vector3f(width / 2, length / 2, height / 2),    //V3
                new Vector3f(-width / 2, length / 2, -height / 2),  //V4
                new Vector3f(-width / 2, -length / 2, -height / 2), //V5
                new Vector3f(width / 2, -length / 2, -height / 2),  //V6
                new Vector3f(width / 2, length / 2, -height / 2)    //V7
        };

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f(1f / 4, 0f),     //T0
                new Vector2f(2f / 4, 0f),     //T1
                new Vector2f(0f, 1f / 4),     //T2
                new Vector2f(1f / 4, 1f / 4), //T3
                new Vector2f(2f / 4, 1f / 4), //T4
                new Vector2f(3f / 4, 1f / 4), //T5
                new Vector2f(0f, 2f / 4),     //T6
                new Vector2f(1f / 4, 2f / 4), //T7
                new Vector2f(2f / 4, 2f / 4), //T8
                new Vector2f(3f / 4, 2f / 4), //T9
                new Vector2f(1f / 4, 3f / 4), //T10
                new Vector2f(2f / 4, 3f / 4), //T11
                new Vector2f(1f / 4, 1f),     //T12
                new Vector2f(2f / 4, 1f)      //T13
        };

        Face[] faces = new Face[]{
                new Face(//Front1
                        vertices[3],
                        vertices[0],
                        vertices[4]
                ),
                new Face(//Front2
                        vertices[4],
                        vertices[7],
                        vertices[3]
                ),
                new Face(//Left1
                        vertices[0],
                        vertices[1],
                        vertices[5]
                ),
                new Face(//Left2
                        vertices[5],
                        vertices[4],
                        vertices[0]
                ),
                new Face(//Right1
                        vertices[2],
                        vertices[3],
                        vertices[7]
                ),
                new Face(//Right2
                        vertices[7],
                        vertices[6],
                        vertices[2]
                ),
                new Face(//Back1
                        vertices[1],
                        vertices[2],
                        vertices[6]
                ),
                new Face(//Back2
                        vertices[6],
                        vertices[5],
                        vertices[1]
                ),
                new Face(//Top1
                        vertices[2],
                        vertices[1] ,
                        vertices[0]
                ),
                new Face(//Top2
                        vertices[0],
                        vertices[3],
                        vertices[2]
                ),
                new Face(//Bottom1
                        vertices[7],
                        vertices[4],
                        vertices[5]
                ),
                new Face(//Bottom2
                        vertices[5],
                        vertices[6],
                        vertices[7]
                ),
        };

        return new Mesh(faces);
    }

    public static Mesh cuboidFrame(float width, float height, float length, float thickness){

        float halfThickness = thickness/2;

        float xi = width/2;
        float yi = height/2;
        float zi = length/2;

        float xe = xi + halfThickness;
        float ye = yi + halfThickness;
        float ze = zi + halfThickness;

        //e is for exterior, i is for interior
        //u are the sub vertexes of the vertexes v of the main cube

        Vector3f[][] verticies = new Vector3f[][]{
                new Vector3f[]{//v0
                        new Vector3f(-xe, ye, ze),//u0
                        new Vector3f(-xe, ye, zi),//u1
                        new Vector3f(-xi, ye, zi),//u2
                        new Vector3f(-xi, ye, ze),//u3
                        new Vector3f(-xe, yi, ze),//u0
                        new Vector3f(-xe, yi, zi),//u1
                        new Vector3f(-xi, yi, zi),//u2
                        new Vector3f(-xi, yi, ze),//u3
                },
                new Vector3f[]{//v1
                        new Vector3f(-xe, ye, -ze),//u0
                        new Vector3f(-xe, ye, -zi),//u1
                        new Vector3f(-xi, ye, -zi),//u2
                        new Vector3f(-xi, ye, -ze),//u3
                        new Vector3f(-xe, yi, -ze),//u0
                        new Vector3f(-xe, yi, -zi),//u1
                        new Vector3f(-xi, yi, -zi),//u2
                        new Vector3f(-xi, yi, -ze),//u3
                },
                new Vector3f[]{//v2
                        new Vector3f(xe, ye, -ze),//u0
                        new Vector3f(xe, ye, -zi),//u1
                        new Vector3f(xi, ye, -zi),//u2
                        new Vector3f(xi, ye, -ze),//u3
                        new Vector3f(xe, yi, -ze),//u0
                        new Vector3f(xe, yi, -zi),//u1
                        new Vector3f(xi, yi, -zi),//u2
                        new Vector3f(xi, yi, -ze),//u3
                },
                new Vector3f[]{//v3
                        new Vector3f(xe, ye, ze),//u0
                        new Vector3f(xe, ye, zi),//u1
                        new Vector3f(xi, ye, zi),//u2
                        new Vector3f(xi, ye, ze),//u3
                        new Vector3f(xe, yi, ze),//u0
                        new Vector3f(xe, yi, zi),//u1
                        new Vector3f(xi, yi, zi),//u2
                        new Vector3f(xi, yi, ze),//u3
                },
                new Vector3f[]{//v4
                        new Vector3f(-xe, -ye, ze),//u0
                        new Vector3f(-xe, -ye, zi),//u1
                        new Vector3f(-xi, -ye, zi),//u2
                        new Vector3f(-xi, -ye, ze),//u3
                        new Vector3f(-xe, -yi, ze),//u0
                        new Vector3f(-xe, -yi, zi),//u1
                        new Vector3f(-xi, -yi, zi),//u2
                        new Vector3f(-xi, -yi, ze),//u3
                },
                new Vector3f[]{//v5
                        new Vector3f(-xe, -ye, -ze),//u0
                        new Vector3f(-xe, -ye, -zi),//u1
                        new Vector3f(-xi, -ye, -zi),//u2
                        new Vector3f(-xi, -ye, -ze),//u3
                        new Vector3f(-xe, -yi, -ze),//u0
                        new Vector3f(-xe, -yi, -zi),//u1
                        new Vector3f(-xi, -yi, -zi),//u2
                        new Vector3f(-xi, -yi, -ze),//u3
                },
                new Vector3f[]{//v6
                        new Vector3f(xe, -ye, -ze),//u0
                        new Vector3f(xe, -ye, -zi),//u1
                        new Vector3f(xi, -ye, -zi),//u2
                        new Vector3f(xi, -ye, -ze),//u3
                        new Vector3f(xe, -yi, -ze),//u0
                        new Vector3f(xe, -yi, -zi),//u1
                        new Vector3f(xi, -yi, -zi),//u2
                        new Vector3f(xi, -yi, -ze),//u3
                },
                new Vector3f[]{//v7
                        new Vector3f(xe, -ye, ze),//u0
                        new Vector3f(xe, -ye, zi),//u1
                        new Vector3f(xi, -ye, zi),//u2
                        new Vector3f(xi, -ye, ze),//u3
                        new Vector3f(xe, -yi, ze),//u0
                        new Vector3f(xe, -yi, zi),//u1
                        new Vector3f(xi, -yi, zi),//u2
                        new Vector3f(xi, -yi, ze),//u3
                }
        };

        Face[] faces = new Face[]{
                //v0 to v1

                //interior
                new Face(
                        verticies[1][3],
                        verticies[0][2],
                        verticies[0][6]
                ),
                new Face(
                        verticies[1][3],
                        verticies[0][6],
                        verticies[1][7]
                ),

                //downward
                new Face(
                        verticies[1][7],
                        verticies[0][6],
                        verticies[0][5]
                ),
                new Face(
                        verticies[1][7],
                        verticies[0][5],
                        verticies[1][4]
                ),

                //exterior
                new Face(
                        verticies[0][1],
                        verticies[1][0],
                        verticies[1][4]
                ),
                new Face(
                        verticies[0][1],
                        verticies[1][4],
                        verticies[0][5]
                ),

                //upward
                new Face(
                        verticies[1][0],
                        verticies[0][1],
                        verticies[0][2]
                ),
                new Face(
                        verticies[1][0],
                        verticies[0][2],
                        verticies[1][3]
                ),

                //v2 to v3

                //interior
                new Face(
                        verticies[3][3],
                        verticies[2][2],
                        verticies[2][6]
                ),
                new Face(
                        verticies[3][3],
                        verticies[2][6],
                        verticies[3][7]
                ),

                //downward
                new Face(
                        verticies[3][7],
                        verticies[2][6],
                        verticies[2][5]
                ),
                new Face(
                        verticies[3][7],
                        verticies[2][5],
                        verticies[3][4]
                ),

                //exterior
                new Face(
                        verticies[2][1],
                        verticies[3][0],
                        verticies[3][4]
                ),
                new Face(
                        verticies[2][1],
                        verticies[3][4],
                        verticies[2][5]
                ),

                //upward
                new Face(
                        verticies[3][0],
                        verticies[2][1],
                        verticies[2][2]
                ),
                new Face(
                        verticies[3][0],
                        verticies[2][2],
                        verticies[3][3]
                ),

                //v5 to v4

                //interior
                new Face(
                        verticies[4][3],
                        verticies[5][2],
                        verticies[5][6]
                ),
                new Face(
                        verticies[4][3],
                        verticies[5][6],
                        verticies[4][7]
                ),

                //downward
                new Face(
                        verticies[4][7],
                        verticies[5][6],
                        verticies[5][5]
                ),
                new Face(
                        verticies[4][7],
                        verticies[5][5],
                        verticies[4][4]
                ),

                //exterior
                new Face(
                        verticies[5][1],
                        verticies[4][0],
                        verticies[4][4]
                ),
                new Face(
                        verticies[5][1],
                        verticies[4][4],
                        verticies[5][5]
                ),

                //upward
                new Face(
                        verticies[4][0],
                        verticies[5][1],
                        verticies[5][2]
                ),
                new Face(
                        verticies[4][0],
                        verticies[5][2],
                        verticies[4][3]
                ),

                //v7 to v6

                //interior
                new Face(
                        verticies[6][3],
                        verticies[7][2],
                        verticies[7][6]
                ),
                new Face(
                        verticies[6][3],
                        verticies[7][6],
                        verticies[6][7]
                ),

                //downward
                new Face(
                        verticies[6][7],
                        verticies[7][6],
                        verticies[7][5]
                ),
                new Face(
                        verticies[6][7],
                        verticies[7][5],
                        verticies[6][4]
                ),

                //exterior
                new Face(
                        verticies[7][1],
                        verticies[6][0],
                        verticies[6][4]
                ),
                new Face(
                        verticies[7][1],
                        verticies[6][4],
                        verticies[7][5]
                ),

                //upward
                new Face(
                        verticies[6][0],
                        verticies[7][1],
                        verticies[7][2]
                ),
                new Face(
                        verticies[6][0],
                        verticies[7][2],
                        verticies[6][3]
                ),

                //v1 to v2

                //interior
                new Face(
                        verticies[2][0],
                        verticies[1][3],
                        verticies[1][7]
                ),
                new Face(
                        verticies[2][0],
                        verticies[1][7],
                        verticies[2][4]
                ),

                //downward
                new Face(
                        verticies[2][4],
                        verticies[1][7],
                        verticies[1][6]
                ),
                new Face(
                        verticies[2][4],
                        verticies[1][6],
                        verticies[2][5]
                ),

                //exterior
                new Face(
                        verticies[2][5],
                        verticies[1][6],
                        verticies[1][2]
                ),
                new Face(
                        verticies[2][5],
                        verticies[1][2],
                        verticies[2][1]
                ),

                //upward
                new Face(
                        verticies[2][1],
                        verticies[1][2],
                        verticies[1][3]
                ),
                new Face(
                        verticies[2][1],
                        verticies[1][3],
                        verticies[2][0]
                ),
        };




        return new Mesh(faces);



    }

    public static Mesh cylinder(float radius, int resolution, float length){
        resolution = resolution < 3 ? 3 : resolution;

        ArrayList<Face> faces = new ArrayList<>();
        Vector3f[] topVertices = new Vector3f[resolution];
        Vector3f[] botVertices = new Vector3f[resolution];
        Vector3f topCentreVertex = new Vector3f(0,length / 2,0);
        Vector3f botCentreVertex = new Vector3f(0,-length / 2,0);

        double deltaTheta = 2 * FastMath.PI/(resolution);
        double theta = 0;

        for(int i = 0; i < resolution; i++){
            float rSinθ = radius * (float) FastMath.sin(theta);
            float rCosθ = radius * (float) FastMath.cos(theta);
            topVertices[i] = new Vector3f(rCosθ,length / 2, rSinθ);
            botVertices[i] = new Vector3f(rCosθ,-length / 2, rSinθ);
            theta -= deltaTheta;
        }

        for(int i = 0; i < resolution -1; i++){
            //top face fragments
            faces.add(new Face(
                    topCentreVertex,
                    topVertices[i],
                    topVertices[i + 1]
            ));

            //bot face fragments
            faces.add(new Face(
                    botVertices[i],
                    botCentreVertex,
                    botVertices[i + 1]
            ));

            //side face fragment 1
            faces.add(new Face(
                    topVertices[i + 1],
                    topVertices[i],
                    botVertices[i]
            ));

            //side face fragment 2
            faces.add(new Face(
                    topVertices[i + 1],
                    botVertices[i],
                    botVertices[i + 1]
            ));
        }

        //top face filler fragments
        faces.add(new Face(
                topCentreVertex,
                topVertices[resolution - 1],
                topVertices[0]
        ));

        //bot face filler fragments
        faces.add(new Face(
                botVertices[resolution - 1],
                botCentreVertex,
                botVertices[0]
        ));

        //side face filler fragment 1
        faces.add(new Face(
                topVertices[0],
                topVertices[resolution - 1],
                botVertices[resolution - 1]
        ));

        //side face fragment 2
        faces.add(new Face(
                topVertices[0],
                botVertices[resolution - 1],
                botVertices[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh pipe(float radius, int resolution, float length){
        resolution = resolution < 3 ? 3 : resolution;

        ArrayList<Face> faces = new ArrayList<>();
        Vector3f[] topVertices = new Vector3f[resolution];
        Vector3f[] botVertices = new Vector3f[resolution];

        double deltaTheta = 2 * FastMath.PI/(resolution);
        double theta = 0;

        for(int i = 0; i < resolution; i++){
            float rSinθ = radius * (float) FastMath.sin(theta);
            float rCosθ = radius * (float) FastMath.cos(theta);
            topVertices[i] = new Vector3f(rCosθ,length, rSinθ);
            botVertices[i] = new Vector3f(rCosθ, 0, rSinθ);
            theta -= deltaTheta;
        }

        for(int i = 0; i < resolution -1; i++){
            //side face fragment 1
            faces.add(new Face(
                    topVertices[i + 1],
                    topVertices[i],
                    botVertices[i]
            ));

            //side face fragment 2
            faces.add(new Face(
                    topVertices[i + 1],
                    botVertices[i],
                    botVertices[i + 1]
            ));
        }

        //side face filler fragment 1
        faces.add(new Face(
                topVertices[0],
                topVertices[resolution - 1],
                botVertices[resolution - 1]
        ));

        //side face fragment 2
        faces.add(new Face(
                topVertices[0],
                botVertices[resolution - 1],
                botVertices[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh sphere(float radius, int resolution){
        resolution = resolution < 3 ? 3 : resolution;
        Vector3f[][] vertices = new Vector3f[resolution][resolution];
        Vector3f northPole = new Vector3f(0,radius,0);
        Vector3f southPole = new Vector3f(0,-radius,0);

        double deltaTheta = 2 * FastMath.PI / resolution;
        double deltaAlpha = FastMath.PI / (resolution + 1);
        double theta;
        double alpha;

        alpha = - deltaAlpha;
        for(int ring = 0; ring < resolution; ring++){
            theta = 0;
            for(int meridian = 0; meridian < resolution; meridian++){
                float sinθ = (float)FastMath.sin(theta);
                float cosθ = (float)FastMath.cos(theta);
                float sinφ = (float)FastMath.sin(alpha);
                float cosφ = (float)FastMath.cos(alpha);

                vertices[ring][meridian] = new Vector3f(
                        radius * sinφ * cosθ,
                        radius * cosφ,
                        radius * sinφ * sinθ
                );
                theta -= deltaTheta;
            }
            alpha -= deltaAlpha;
        }

        ArrayList<Face> faces = new ArrayList<>();


        for(int ring = 0; ring < resolution - 1; ring++){
            for(int meridian = 0; meridian < resolution - 1; meridian++){
                //main faces fragment 1
                faces.add(new Face(
                        vertices[ring][meridian + 1],
                        vertices[ring][meridian],
                        vertices[ring + 1][meridian]
                ));
                //main faces fragment 2
                faces.add(new Face(
                        vertices[ring][meridian + 1],
                        vertices[ring + 1][meridian],
                        vertices[ring + 1][meridian + 1]
                ));
            }

            //main faces filler fragment 1
            faces.add(new Face(
                    vertices[ring][0],
                    vertices[ring][resolution - 1],
                    vertices[ring + 1][resolution - 1]
            ));
            //main faces filler fragment 2
            faces.add(new Face(
                    vertices[ring][0],
                    vertices[ring + 1][resolution - 1],
                    vertices[ring + 1][0]
            ));
        }

        for(int meridian = 0; meridian < resolution - 1; meridian++){
            //top faces
            faces.add(new Face(
                    northPole,
                    vertices[0][meridian],
                    vertices[0][meridian + 1]
            ));

            //bottom faces
            faces.add(new Face(
                    southPole,
                    vertices[resolution - 1][meridian + 1],
                    vertices[resolution - 1][meridian]
            ));
        }

        //top faces filler
        faces.add(new Face(
                northPole,
                vertices[0][resolution - 1],
                vertices[0][0]
        ));

        //bottom faces filler
        faces.add(new Face(
                southPole,
                vertices[resolution - 1][0],
                vertices[resolution - 1][resolution - 1]
        ));

        return new Mesh(faces);
    }

    public static Mesh texturedPlane(float width, float length){
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(width / 2,0,-length / 2),
                new Vector3f(-width / 2,0,-length / 2),
                new Vector3f(-width / 2,0,length / 2),
                new Vector3f(width / 2,0,length / 2)
        };

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(0,1),
                new Vector2f(1,1)
        };

        TexturedFace[] faces = new TexturedFace[]{
                new TexturedFace(
                        vertices[0], textureCoords[1],
                        vertices[1], textureCoords[0],
                        vertices[2], textureCoords[2]
                ),
                new TexturedFace(
                        vertices[2], textureCoords[2],
                        vertices[3], textureCoords[3],
                        vertices[0], textureCoords[1]
                )
        };

        return new Mesh(faces);
    }


}
