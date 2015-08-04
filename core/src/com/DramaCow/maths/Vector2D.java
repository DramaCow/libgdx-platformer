// Sam Coward - 2015

package com.DramaCow.maths;

public class Vector2D {
	
	// Attributes
	public float x, y;

	// Constructors and setters
	public Vector2D() {
		this.x = this.y = 0.0f;
	}

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D that) {
		this.x = that.x;
		this.y = that.y;
	}

	public Vector2D set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2D set(Vector2D v) {
		return set(v.x, v.y);
	}

	// Static standard vector operations
	public static Vector2D add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2D sub(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x - v2.x, v1.y - v2.y);
	}

	public static float dot(Vector2D v1, Vector2D v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static Vector2D scalar(float a, Vector2D v) {
		return new Vector2D(a * v.x, a * v.y);
	}

	public static Vector2D unit(Vector2D v) {
		if (v.x != 0.0f || v.y != 0.0f) {
			float invLen = 1 / (float) Math.sqrt(v.x*v.x + v.y*v.y);
			return new Vector2D( v.x * invLen, v.y * invLen ); 
		}

		return new Vector2D(0.0f, 0.0f);
	}

	// Standard vector operations
	public Vector2D add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Vector2D add(Vector2D v) {
		return add(v.x, v.y);
	}

	public Vector2D sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2D sub(Vector2D v) {
		return sub(v.x, v.y);
	}

	public Vector2D scalar(float a) {
		this.x *= a;
		this.y *= a;
		return this;
	}

	// Static obscure vector operations
	public static Vector2D min(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x <= v2.x ? v1.x : v2.x, v1.y <= v2.y ? v1.y : v2.y);
	}

	public static Vector2D max(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x >= v2.x ? v1.x : v2.x, v1.y >= v2.y ? v1.y : v2.y);
	}

	public static Vector2D mult(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2D abs(Vector2D v) {
		return new Vector2D( (v.x <= 0.0f ? 0.0f - v.x : v.x), (v.y <= 0.0f ? 0.0f - v.y : v.y) );
	}

	public static float sqrLen(Vector2D v) {
		return (v.x * v.x) + (v.y * v.y);
	}

	public static Vector2D swap(Vector2D v) {
		return new Vector2D(v.y, v.x);
	}

	public static Vector2D majorAxis(Vector2D v) {
		// abs(x) >= abs(y)
		if ( (v.x <= 0.0f ? 0.0f - v.x : v.x) >= (v.y <= 0.0f ? 0.0f - v.y : v.y)) {
			return new Vector2D(v.x >= 0.0f ? 1.0f : -1.0f, 0.0f);
		}
		else {
			return new Vector2D(0.0f, v.y >= 0.0f ? 1.0f : -1.0f);
		}	
	}	

	// Obscure vector operations
	public Vector2D mult(float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	public Vector2D mult(Vector2D v) {
		return mult(v.x, v.y);
	}

	public float maxComponent() {
		return this.y <= this.x ? this.x : this.y;
	}
	
	public Vector2D neg() {
		this.x = 0.0f - this.x;
		this.y = 0.0f - this.y;
		return this;
	}

	// Other
	@Override 
	public String toString() {
		return "(" + x + " " + y + ")";
	}
}
