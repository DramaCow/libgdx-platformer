package com.DramaCow.game;

import com.DramaCow.maths.Vector2D;
import com.DramaCow.maths.AABB;

abstract public class GameObject implements Comparable<GameObject>{
	protected final Vector2D position; 	// Position for graphic placement
	protected final AABB box; 			// Bounding box and center position
	public final String id;

	protected float t = 0.0f;
	
	public GameObject (String id, float x, float y, float w, float h) {
		this.position = new Vector2D(x, y);
		this.box = new AABB(x + w/2, y + h/2, w/2, h/2);
		this.id = id;
	}

	public void update(float dt) {
		t += dt;
	}

	public Vector2D getPosition() {
		return position;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return box.halfExtents.x * 2;
	}

	public float getHeight() {
		return box.halfExtents.y * 2;
	}

	public float getTime() {
		return t;
	}

	public int compareTo(GameObject compareObject) {
		return Math.round(this.box.position.x - compareObject.box.position.x);
	}
}
