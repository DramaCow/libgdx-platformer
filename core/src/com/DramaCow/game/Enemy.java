package com.DramaCow.game;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicGameObject{

	private Ai ai;

	public Enemy (String id, float x, float y, float width, float height, Ai ai) {
		super(id, x, y, width, height);
		this.ai = ai;
		this.ai.create(this);
	}

	@Override
	public void update(float dt){
		super.update(dt);
		ai.update(this, dt);
	}

	// Copies the enemy to a new position
	public Enemy copy(float x, float y){
		return new Enemy(this.id, x, y, this.getWidth(), this.getHeight(), this.ai);
	}

	public Enemy copy(Vector2 loc){
		return new Enemy(this.id, loc.x, loc.y, this.getWidth(), this.getHeight(), this.ai);
	}
}
