// Sam Coward - 2015

package com.DramaCow.maths;

public class CollisionObject {
	public AABB box;
	public Vector2D velocity;
	public Vector2D acceleration;
	public Vector2D posCorrect; // Used for correcting penetration distance

	// What faces of object are in contact with a surface
	public boolean north 	= false;
	public boolean south 	= false;
	public boolean east 	= false;
	public boolean west 	= false;

	public CollisionObject(AABB box, Vector2D velocity, Vector2D acceleration, Vector2D posCorrect) {
		this.box = box;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.posCorrect = posCorrect;
	}

	public CollisionObject(AABB box, Vector2D velocity, Vector2D acceleration) {
		this.box = box;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.posCorrect = new Vector2D(0.0f, 0.0f);
	}
}
