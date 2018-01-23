package wbif.sjx.TrackAnalysis.Plot3D.Math.vectors;

public class Vector2d {
	public double x, y;

	public Vector2d() {
		x = 0;
		y = 0;
	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString(){
		return String.format("Vec2f: [ %f, %f ]", x, y);
	}

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector2d Subtract(Vector2d vec1, Vector2d vec2){
		return new Vector2d(
				vec1.x - vec2.x,
				vec1.y - vec2.y
		);
	}
}
