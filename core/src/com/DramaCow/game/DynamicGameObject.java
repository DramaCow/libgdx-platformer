package com.DramaCow.game;

import com.badlogic.gdx.math.Vector2;

public class DynamicGameObject extends GameObject {
	public final Vector2 velocity;
	public final Vector2 accel;

	public DynamicGameObject (float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		accel = new Vector2();
	}

	public void update(float deltaTime){
		velocity.add(accel.x * deltaTime, accel.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x;
		bounds.y = position.y;
	}

	public void setVelocity(Vector2 v){
		velocity.set(v);
	}

	public Vector2 getVelocity(){
		return velocity;
	}

	public void setAccel(Vector2 a){
		accel.set(a);
	}

	public Vector2 getAccel(){
		return accel;
	}

	public void setPosition(Vector2 p){
		position.set(p);
		bounds.x = position.x;
		bounds.y = position.y;
	}
}