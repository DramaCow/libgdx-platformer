// Sam Coward - 2015

package com.DramaCow.maths;

// Axis Aligned Bounding Box (used for collision detection/reaction)
public class AABB {
	
	// Offset (by center)
	public Vector2D position;

	// Dimensions
	public Vector2D halfExtents; // extents = half of dimensions

	public AABB(float x, float y, float halfWidth, float halfHeight) {
		this.position = new Vector2D(x, y);
		this.halfExtents = new Vector2D(halfWidth, halfHeight);
	}

	public AABB(Vector2D position, Vector2D halfExtents) {
		this.position = position;
		this.halfExtents = halfExtents;
	}

	public AABB(Rect r) {
		this.halfExtents = new Vector2D(r.w/2, r.h/2);
		this.position = new Vector2D(r.x + halfExtents.x, r.y + halfExtents.y);
	}

	public AABB(AABB box) {
		this.position = new Vector2D(box.position);
		this.halfExtents = new Vector2D(box.halfExtents);
	}

	public Rect toRect() {
		return new Rect(position.x - halfExtents.x, position.y - halfExtents.y/2, halfExtents.x * 2, halfExtents.y * 2);
	}

	@Override
	public String toString() {
		return "position=" + position.toString() + " " + "halfExtents=" + halfExtents.toString();
	}
}
