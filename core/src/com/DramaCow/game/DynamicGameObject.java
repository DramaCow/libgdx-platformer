package com.DramaCow.game;

import com.badlogic.gdx.math.Vector2;

abstract public class DynamicGameObject extends GameObject {
	public final Vector2 velocity;
	public final Vector2 acceleration;
	
	public DynamicGameObject (String id, float x, float y, float width, float height) {
		super(id, x, y, width, height);
		velocity = new Vector2();
		acceleration = new Vector2();
	}

	@Override
	public void update(float dt){
		super.update(dt);

		velocity.add(acceleration.x * dt, acceleration.y * dt);
		position.add(velocity.x * dt, velocity.y * dt);
		bounds.x = position.x;
		bounds.y = position.y;
	}

	public void setVelocity(Vector2 v){
		velocity.set(v);
	}

	public Vector2 getVelocity(){
		return velocity;
	}

	public void setAcceleration(Vector2 a){
		acceleration.set(a);
	}

	public Vector2 getAcceleration(){
		return acceleration;
	}

	public void setPosition(Vector2 p){
		position.set(p);
		bounds.x = position.x;
		bounds.y = position.y;
	}
}
