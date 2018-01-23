package wbif.sjx.TrackAnalysis.Plot3D.Math.vectors;

public class Vector2f {
	public float x, y;

	public Vector2f() {
		x = 0;
		y = 0;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void scale(float scaler){
        this.x *= scaler;
        this.y *= scaler;
    }

	@Override
	public String toString(){
		return String.format("Vec2f: [ %f, %f ]", x, y);
	}
}
