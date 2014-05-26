package lunding.robotArm;

/**
 * The class point create a point containing x,y,z & angle.
 * @author Rasmus Lunding Henriksen
 * @version 1.0.0
 * @since 11. July 2012
 *
 */
public class Point {
	private int x;
	private int y;
	private int z;
	private int angle;
	
	/**
	 * Create the point and set the coordinates and angle at the same time.
	 * @param X - x coordinate
	 * @param Y - y coordinate
	 * @param Z	- z coordinate
	 * @param Angle - angle
	 */
	public Point(int X, int Y, int Z, int Angle){
		this.setX(X);
		this.setY(Y);
		this.setZ(Z);
		this.setAngle(Angle);
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public double distanceBetween(Point p){
		double xd = 0, yd = 0, zd = 0, result = 0;
		xd = x - p.getX();
		yd = y - p.getY();
		zd = z - p.getZ();
		result = Math.sqrt(xd*xd + yd*yd + zd*zd);
		
		return result;
	}
	
}
