package com.DramaCow.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

abstract public class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;
	public final String id;

	private float t = 0.0f;
	
	public GameObject (String id, float x, float y, float width, float height) {
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x, y, width, height);
		this.id = id;
	}

	public void update(float dt) {
		t += dt;
	}

	public Vector2 getPosition() {
		return position;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return bounds.getWidth();
	}

	public float getHeight() {
		return bounds.getHeight();
	}

	public float getTime() {
		return t;
	}
}
