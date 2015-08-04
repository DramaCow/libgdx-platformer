// Sam Coward - 2015

package com.DramaCow.maths;

public class Contact {
	
	public Vector2D normal;
	public float distance;
	public Vector2D point;

	public Contact() {}

	public Contact(Vector2D normal, float distance, Vector2D point) {
		this.normal = new Vector2D(normal);
		this.distance = distance;
		this.point = new Vector2D(point);
	}

	public void set(Vector2D normal, float distance, Vector2D point) {
		this.normal = new Vector2D(normal);
		this.distance = distance;
		this.point = new Vector2D(point);
	}

}
