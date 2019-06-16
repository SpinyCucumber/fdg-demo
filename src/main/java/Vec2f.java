import org.lwjgl.opengl.GL11;

/**
 * My own vector class, mostly for GUI stuff.
 * @author SpinyQ
 *
 */
public class Vec2f {

	public float x, y;
	
	public Vec2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2f(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructs a vector using an angle.
	 * @param angle The angle between the vector and the x-axis (The vector <1,0>)
	 * @return
	 */
	public static Vec2f fromAngle(float angle) {
		return new Vec2f((float) Math.cos(angle), (float) Math.sin(angle));
	}
	
	/**
	 * Returns a vector going in a random direction with a given magnitude.
	 * @param mag
	 * @return
	 */
	public static Vec2f randomRadialVector(float mag) {
		return Vec2f.fromAngle((float) (Math.random() * 2.0f * Math.PI)).scaledBy(mag);
	}
	
	public static Vec2f randomVector(float minX, float maxX, float minY, float maxY) {
		return new Vec2f((float) (Math.random() * (maxX - minX) + minX),
				(float) (Math.random() * (maxY - minY) + minY));
	}
	
	public Vec2f copy() {
		return new Vec2f(x, y);
	}

	/**
	 * Calculates length of vector
	 * @return
	 */
	public float length() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	/**
	 * Adds another vector in-place, changing the values of this vector.
	 * @param other
	 */
	public void add(Vec2f other) {
		x += other.x;
		y += other.y;
	}
	
	/**
	 * Adds two vectors, returning the result.
	 * @param other
	 */
	public Vec2f plus(Vec2f other) {
		return new Vec2f(x + other.x, y + other.y);
	}
	
	/**
	 * Subtracts another vector in-place
	 * @param other
	 */
	public void sub(Vec2f other) {
		x -= other.x;
		y -= other.y;
	}
	
	public Vec2f minus(Vec2f other) {
		return new Vec2f(x - other.x, y - other.y);
	}
	
	/**
	 * Scales this vector in-place by a scalar
	 * @param s
	 */
	public void scale(float s) {
		x *= s;
		y *= s;
	}
	
	/**
	 * Returns the result of scaling this vector by s
	 * @param s
	 * @return
	 */
	public Vec2f scaledBy(float s) {
		return new Vec2f(x * s, y * s);
	}
	
	/**
	 * Calculates the dot product of this vector and another
	 * @param other
	 * @return
	 */
	public float dot(Vec2f other) {
		return x * other.x + y * other.y;
	}
	
	public void glVertex() {
		GL11.glVertex2f(x, y);
	}

	@Override
	public String toString() {
		return "Vec2f [x=" + x + ", y=" + y + "]";
	}

}
