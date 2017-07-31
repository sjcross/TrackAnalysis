package wbif.sjx.TrackAnalysis.Plot3D.Core.Graphics;

import org.apache.commons.math3.util.FastMath;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector2f;
import wbif.sjx.TrackAnalysis.Plot3D.Math.vectors.Vector3f;

import java.util.ArrayList;

/**
 * Created by Jordan Fisher on 20/05/2017.
 */
public class GenerateMesh {
    private GenerateMesh() {}

    public static Mesh tetrahedron(float length) {
        float x = length/(float) FastMath.sqrt(2);

        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(0, length, x), //V0
                new Vector3f(-length,0, -x),//V1
                new Vector3f(0, -length, x),//V2
                new Vector3f(length,0, -x), //V3
        };

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f(1f / 4, 0f),     //T0
        };

        Face[] faces = new Face[]{
                new Face(//base
                        vertices[1], textureCoords[0],
                        vertices[3], textureCoords[0],
                        vertices[2], textureCoords[0]
                ),
                new Face(//back
                        vertices[0], textureCoords[0],
                        vertices[3], textureCoords[0],
                        vertices[1], textureCoords[0]
                ),
                new Face(//frontLeft
                        vertices[0], textureCoords[0],
                        vertices[1], textureCoords[0],
                        vertices[2], textureCoords[0]
                ),
                new Face(//frontright
                        vertices[0], textureCoords[0],
                        vertices[2], textureCoords[0],
                        vertices[3], textureCoords[0]
                )
        };

        return new Mesh(faces);
    }

    public static Mesh cuboid(float width, float height, float length) {
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-width / 2, height / 2, length / 2),   //V0
                new Vector3f(-width / 2, height / 2, -length / 2),  //V1
                new Vector3f(width / 2, height / 2, -length / 2),   //V2
                new Vector3f(width / 2, height / 2, length / 2),    //V3
                new Vector3f(-width / 2, -height / 2, length / 2),  //V4
                new Vector3f(-width / 2, -height / 2, -length / 2), //V5
                new Vector3f(width / 2, -height / 2, -length / 2),  //V6
                new Vector3f(width / 2, -height / 2, length / 2)    //V7
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
                        vertices[3], textureCoords[4],
                        vertices[0], textureCoords[3],
                        vertices[4], textureCoords[7]
                ),
                new Face(//Front2
                        vertices[4], textureCoords[7],
                        vertices[7], textureCoords[8],
                        vertices[3], textureCoords[4]
                ),
                new Face(//Left1
                        vertices[0], textureCoords[3],
                        vertices[1], textureCoords[2],
                        vertices[5], textureCoords[6]
                ),
                new Face(//Left2
                        vertices[5], textureCoords[6],
                        vertices[4], textureCoords[7],
                        vertices[0], textureCoords[3]
                ),
                new Face(//Right1
                        vertices[2], textureCoords[5],
                        vertices[3], textureCoords[4],
                        vertices[7], textureCoords[8]
                ),
                new Face(//Right2
                        vertices[7], textureCoords[8],
                        vertices[6], textureCoords[9],
                        vertices[2], textureCoords[5]
                ),
                new Face(//Back1
                        vertices[1], textureCoords[12],
                        vertices[2], textureCoords[13],
                        vertices[6], textureCoords[11]
                ),
                new Face(//Back2
                        vertices[6], textureCoords[11],
                        vertices[5], textureCoords[10],
                        vertices[1], textureCoords[12]
                ),
                new Face(//Top1
                        vertices[2], textureCoords[1],
                        vertices[1], textureCoords[0],
                        vertices[0], textureCoords[3]
                ),
                new Face(//Top2
                        vertices[0], textureCoords[3],
                        vertices[3], textureCoords[4],
                        vertices[2], textureCoords[1]
                ),
                new Face(//Bottom1
                        vertices[7], textureCoords[8],
                        vertices[4], textureCoords[7],
                        vertices[5], textureCoords[10]
                ),
                new Face(//Bottom2
                        vertices[5], textureCoords[10],
                        vertices[6], textureCoords[11],
                        vertices[7], textureCoords[8]
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
            theta -= deltaTheta; //beacause faces defined anticlockwise
        }

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f()
        };

        for(int i = 0; i < resolution -1; i++){
            //top face fragments
            faces.add(new Face(
                    topCentreVertex, textureCoords[0],
                    topVertices[i], textureCoords[0],
                    topVertices[i + 1], textureCoords[0]
            ));

            //bot face fragments
            faces.add(new Face(
                    botVertices[i], textureCoords[0],
                    botCentreVertex, textureCoords[0],
                    botVertices[i + 1], textureCoords[0]
            ));

            //side face fragment 1
            faces.add(new Face(
                    topVertices[i + 1], textureCoords[0],
                    topVertices[i], textureCoords[0],
                    botVertices[i], textureCoords[0]
            ));

            //side face fragment 2
            faces.add(new Face(
                    topVertices[i + 1], textureCoords[0],
                    botVertices[i], textureCoords[0],
                    botVertices[i + 1], textureCoords[0]
            ));
        }

        //top face filler fragments
        faces.add(new Face(
                topCentreVertex, textureCoords[0],
                topVertices[resolution - 1], textureCoords[0],
                topVertices[0], textureCoords[0]
        ));

        //bot face filler fragments
        faces.add(new Face(
                botVertices[resolution - 1], textureCoords[0],
                botCentreVertex, textureCoords[0],
                botVertices[0], textureCoords[0]
        ));

        //side face filler fragment 1
        faces.add(new Face(
                topVertices[0], textureCoords[0],
                topVertices[resolution - 1], textureCoords[0],
                botVertices[resolution - 1], textureCoords[0]
        ));

        //side face fragment 2
        faces.add(new Face(
                topVertices[0], textureCoords[0],
                botVertices[resolution - 1], textureCoords[0],
                botVertices[0], textureCoords[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh tube(float outerRadius, float innerRadius, int resolution, float length){
        resolution = resolution < 3 ? 3 : resolution;

        if(innerRadius > outerRadius){
            float temp = innerRadius;
            innerRadius = outerRadius;
            outerRadius = temp;
        }else if(innerRadius == outerRadius){
            innerRadius = outerRadius - 0.0001f;
        }

        ArrayList<Face> faces = new ArrayList<>();
        Vector3f[] outerTopVertices = new Vector3f[resolution];
        Vector3f[] outerBotVertices = new Vector3f[resolution];
        Vector3f[] innerTopVertices = new Vector3f[resolution];
        Vector3f[] innerBotVertices = new Vector3f[resolution];

        double deltaTheta = 2 * FastMath.PI/(resolution);
        double theta = 0;

        for(int i = 0; i < resolution; i++){
            float Sinθ = (float) FastMath.sin(theta);
            float Cosθ = (float) FastMath.cos(theta);
            outerTopVertices[i] = new Vector3f(outerRadius * Cosθ,length / 2, outerRadius * Sinθ);
            outerBotVertices[i] = new Vector3f(outerRadius * Cosθ,-length / 2, outerRadius * Sinθ);
            innerTopVertices[i] = new Vector3f(innerRadius * Cosθ,length / 2, innerRadius * Sinθ);
            innerBotVertices[i] = new Vector3f(innerRadius * Cosθ,-length / 2, innerRadius * Sinθ);
            theta -= deltaTheta; //beacause faces defined anticlockwise
        }

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f()
        };

        for(int i = 0; i < resolution -1; i++){
            //top face fragments 1
            faces.add(new Face(
                    innerTopVertices[i + 1], textureCoords[0],
                    innerTopVertices[i], textureCoords[0],
                    outerTopVertices[i], textureCoords[0]
            ));

            //top face fragments 2
            faces.add(new Face(
                    innerTopVertices[i + 1], textureCoords[0],
                    outerTopVertices[i], textureCoords[0],
                    outerTopVertices[i + 1], textureCoords[0]
            ));

            //bottom face fragments 1
            faces.add(new Face(
                    innerBotVertices[i], textureCoords[0],
                    innerBotVertices[i + 1], textureCoords[0],

                    outerBotVertices[i], textureCoords[0]
            ));

            //bottom face fragments 2
            faces.add(new Face(
                    outerBotVertices[i], textureCoords[0],
                    innerBotVertices[i + 1], textureCoords[0],

                    outerBotVertices[i + 1], textureCoords[0]
            ));

            //outer side face fragment 1
            faces.add(new Face(
                    outerTopVertices[i + 1], textureCoords[0],
                    outerTopVertices[i], textureCoords[0],
                    outerBotVertices[i], textureCoords[0]
            ));

            //outer side face fragment 2
            faces.add(new Face(
                    outerTopVertices[i + 1], textureCoords[0],
                    outerBotVertices[i], textureCoords[0],
                    outerBotVertices[i + 1], textureCoords[0]
            ));

            //inner side face fragment 1
            faces.add(new Face(
                    innerTopVertices[i], textureCoords[0],
                    innerTopVertices[i + 1], textureCoords[0],
                    innerBotVertices[i], textureCoords[0]
            ));

            //inner side face fragment 2
            faces.add(new Face(
                    innerBotVertices[i], textureCoords[0],
                    innerTopVertices[i + 1], textureCoords[0],
                    innerBotVertices[i + 1], textureCoords[0]
            ));
        }

        //top face filler fragments 1
        faces.add(new Face(
                innerTopVertices[0], textureCoords[0],
                innerTopVertices[resolution - 1], textureCoords[0],
                outerTopVertices[resolution - 1], textureCoords[0]
        ));

        //top face filler fragments 2
        faces.add(new Face(
                innerTopVertices[0], textureCoords[0],
                outerTopVertices[resolution - 1], textureCoords[0],
                outerTopVertices[0], textureCoords[0]
        ));

        //bottom face filler fragments 1
        faces.add(new Face(
                innerBotVertices[resolution - 1], textureCoords[0],
                innerBotVertices[0], textureCoords[0],

                outerBotVertices[resolution - 1], textureCoords[0]
        ));

        //bottom face filler fragments 2
        faces.add(new Face(
                outerBotVertices[resolution - 1], textureCoords[0],
                innerBotVertices[0], textureCoords[0],

                outerBotVertices[0], textureCoords[0]
        ));

        //outer side face filler fragment 1
        faces.add(new Face(
                outerTopVertices[0], textureCoords[0],
                outerTopVertices[resolution - 1], textureCoords[0],
                outerBotVertices[resolution - 1], textureCoords[0]
        ));

        //outer side face filler fragment 2
        faces.add(new Face(
                outerTopVertices[0], textureCoords[0],
                outerBotVertices[resolution - 1], textureCoords[0],
                outerBotVertices[0], textureCoords[0]
        ));

        //inner side face filler fragment 1
        faces.add(new Face(
                innerTopVertices[resolution - 1], textureCoords[0],
                innerTopVertices[0], textureCoords[0],
                innerBotVertices[resolution - 1], textureCoords[0]
        ));

        //inner side face filler fragment 2
        faces.add(new Face(
                innerBotVertices[resolution - 1], textureCoords[0],
                innerTopVertices[0], textureCoords[0],
                innerBotVertices[0], textureCoords[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh cone(float radius, int resolution, float height){
        resolution = resolution < 3 ? 3 : resolution;

        ArrayList<Face> faces = new ArrayList<>();
        Vector3f peakVertex = new Vector3f(0,height / 2,0);
        Vector3f[] baseVertices = new Vector3f[resolution];
        Vector3f baseCentreVertex = new Vector3f(0,-height / 2,0);

        double deltaTheta = 2 * FastMath.PI/(resolution);
        double theta = 0;

        for(int i = 0; i < resolution; i++){
            float rSinθ = radius * (float) FastMath.sin(theta);
            float rCosθ = radius * (float) FastMath.cos(theta);
            baseVertices[i] = new Vector3f(rCosθ,-height / 2, rSinθ);
            theta -= deltaTheta;
        }

        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f()
        };

        for(int i = 0; i < resolution -1; i++){
            //base face fragments
            faces.add(new Face(
                    baseVertices[i], textureCoords[0],
                    baseCentreVertex, textureCoords[0],
                    baseVertices[i + 1], textureCoords[0]
            ));

            //side face fragment
            faces.add(new Face(
                    peakVertex, textureCoords[0],
                    baseVertices[i], textureCoords[0],
                    baseVertices[i + 1], textureCoords[0]
            ));
        }

        //bot face filler fragments
        faces.add(new Face(
                baseVertices[resolution - 1], textureCoords[0],
                baseCentreVertex, textureCoords[0],
                baseVertices[0], textureCoords[0]
        ));

        //side face filler fragment
        faces.add(new Face(
                peakVertex, textureCoords[0],
                baseVertices[resolution - 1], textureCoords[0],
                baseVertices[0], textureCoords[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh torus(float majorRadius, int lon, float minorRadius, int lat){ // WIP
//        Vector3f[][] vertices = new Vector3f[lat][lon];
//
//        double deltaTheta = 2 * FastMath.PI/lat;
//        double deltaAlpha = 2 * FastMath.PI/lon;
//        double theta;
//        double alpha;
//
//        theta = 0;
//        for(int major = 0; major < lon; major++) {
//            alpha = 0;
//            for (int minor = 0; minor < lat; minor++) {
//                float sinφ = (float)FastMath.sin(alpha);
//                float cosφ = (float)FastMath.cos(alpha);
//                float sinθ = (float)FastMath.sin(theta);
//                float cosθ = (float)FastMath.cos(theta);
//
//                vertices[minor][major] = new Vector3f(
//                        (majorRadius + (minorRadius * cosφ)) * cosθ,
//                        minorRadius * sinφ,
//                        (majorRadius + (minorRadius * cosφ)) * sinφ
//                );
//
//                alpha -= deltaAlpha;
//            }
//            theta -= deltaTheta;
//        }
//
//
//        Vector2f[] textureCoords = new Vector2f[]{
//                new Vector2f()
//        };
//
//        ArrayList<Face> faces = new ArrayList<>();
//
//        for(int major = 0; major < lon -1; major++) {
//            for (int minor = 0; minor < lat -1; minor++) {
//                faces.add(new Face(
//                        vertices[minor][major + 1], textureCoords[0],
//                        vertices[minor][major], textureCoords[0],
//                        vertices[minor + 1][major], textureCoords[0]
//                ));
//            }
//        }


        Vector3f[][] vertices = new Vector3f[lat][lon];

        double deltaTheta = 2 * FastMath.PI/lon; //xz plane
        double deltaAlpha = 2 * FastMath.PI/lat;
        double theta;
        double alpha;

        theta = 0;
        for(int meridian = 0; meridian < lon; meridian++) {
            alpha = 0;
            for (int ring = 0; ring < lat; ring++) {
                float sinθ = (float) FastMath.sin(theta);
                float cosθ = (float) FastMath.cos(theta);
                float sinφ = (float) FastMath.sin(alpha);
                float cosφ = (float) FastMath.cos(alpha);

                vertices[meridian][ring] = new Vector3f(
                        (majorRadius + (minorRadius * cosφ)) * cosθ,
                        minorRadius * sinφ,
                        (majorRadius + (minorRadius * cosφ)) * sinφ
                );

                alpha -= deltaAlpha;
            }
            theta -= deltaTheta;
        }


        Vector2f[] textureCoords = new Vector2f[]{
                new Vector2f()
        };

        ArrayList<Face> faces = new ArrayList<>();

        for(int meridian = 0; meridian < lon -1; meridian++) {
            for (int ring = 0; ring < lat -1; ring++) {
                faces.add(new Face(
                        vertices[meridian + 1][ring], textureCoords[0],
                        vertices[meridian][ring], textureCoords[0],
                        vertices[meridian][ring + 1], textureCoords[0]
                ));
            }
        }


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
                float sinθ = (float) FastMath.sin(theta);
                float cosθ = (float) FastMath.cos(theta);
                float sinφ = (float) FastMath.sin(alpha);
                float cosφ = (float) FastMath.cos(alpha);

                vertices[ring][meridian] = new Vector3f(
                        radius * sinφ * cosθ,
                        radius * cosφ,
                        radius * sinφ * sinθ
                );
                theta -= deltaTheta;
            }
            alpha -= deltaAlpha;
        }


        Vector2f[] textCoords = new Vector2f[]{
                new Vector2f(0,0),
                new Vector2f(1,0),
                new Vector2f(0,1),
                new Vector2f(1,1)
        };

        ArrayList<Face> faces = new ArrayList<>();


        for(int ring = 0; ring < resolution - 1; ring++){
            for(int meridian = 0; meridian < resolution - 1; meridian++){
                //main faces fragment 1
                faces.add(new Face(
                        vertices[ring][meridian + 1], textCoords[1],
                        vertices[ring][meridian], textCoords[0],
                        vertices[ring + 1][meridian], textCoords[2]
                ));
                //main faces fragment 2
                faces.add(new Face(
                        vertices[ring][meridian + 1], textCoords[1],
                        vertices[ring + 1][meridian], textCoords[2],
                        vertices[ring + 1][meridian + 1], textCoords[3]
                ));
            }

            //main faces filler fragment 1
            faces.add(new Face(
                    vertices[ring][0], textCoords[1],
                    vertices[ring][resolution - 1], textCoords[0],
                    vertices[ring + 1][resolution - 1], textCoords[2]
            ));
            //main faces filler fragment 2
            faces.add(new Face(
                    vertices[ring][0], textCoords[1],
                    vertices[ring + 1][resolution - 1], textCoords[2],
                    vertices[ring + 1][0], textCoords[3]
            ));
        }

        for(int meridian = 0; meridian < resolution - 1; meridian++){
            //top faces
            faces.add(new Face(
                    northPole, textCoords[0],
                    vertices[0][meridian], textCoords[0],
                    vertices[0][meridian + 1], textCoords[0]
            ));

            //bottom faces
            faces.add(new Face(
                    southPole, textCoords[0],
                    vertices[resolution - 1][meridian + 1], textCoords[0],
                    vertices[resolution - 1][meridian], textCoords[0]
            ));
        }

        //top faces filler
        faces.add(new Face(
                northPole, textCoords[0],
                vertices[0][resolution - 1], textCoords[0],
                vertices[0][0], textCoords[0]
        ));

        //bottom faces filler
        faces.add(new Face(
                southPole, textCoords[0],
                vertices[resolution - 1][0], textCoords[0],
                vertices[resolution - 1][resolution - 1], textCoords[0]
        ));

        return new Mesh(faces);
    }

    public static Mesh rectangle(float width, float height) {
        Vector3f v0 = new Vector3f(-width / 2, height / 2, 0);
        Vector3f v1 = new Vector3f(-width / 2, -height / 2, 0);
        Vector3f v2 = new Vector3f(width / 2, height / 2, 0);
        Vector3f v3 = new Vector3f(width / 2, -height / 2, 0);

        Vector2f t0 = new Vector2f(0,0);
        Vector2f t1 = new Vector2f(0,1);
        Vector2f t2 = new Vector2f(1,0);
        Vector2f t3 = new Vector2f(1,1);

        Face[] faces = new Face[]{
                new Face(
                        v1, t1,
                        v0, t0,
                        v2, t2
                ),
                new Face(
                        v2, t2,
                        v3, t3,
                        v1, t1
                ),
        };

        return new Mesh(faces);
    }
}
