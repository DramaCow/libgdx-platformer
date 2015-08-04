package com.DramaCow.game;

import com.DramaCow.maths.Vector2D;
import com.DramaCow.maths.Rect;

abstract public class GameObject implements Comparable<GameObject>{
	public final Vector2D position;
	public final Rect bounds;
	public final String id;

	protected float t = 0.0f;
	
	public GameObject (String id, float x, float y, float width, float height) {
		this.position = new Vector2D(x, y);
		this.bounds = new Rect(x, y, width, height);
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
		return bounds.w;
	}

	public float getHeight() {
		return bounds.h;
	}

	public float getTime() {
		return t;
	}

	public int compareTo(GameObject compareObject) {
		return Math.round(this.getX() - compareObject.getX());
	}
}
