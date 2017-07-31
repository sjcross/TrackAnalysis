package wbif.sjx.TrackAnalysis.Plot3D.Math.vectors;

public class Vector3i {
	public int x, y, z;

	public Vector3i() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vector3i vec){
		this.set(vec.x, vec.y, vec.z);
	}

	public void set(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void change(Vector3i vec){
		this.change(vec.x, vec.y, vec.z);
	}

	public void change(int x, int y, int z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
}
