package com.DramaCow.game;

public class Enemy extends DynamicGameObject{

	private Ai ai;

	public Enemy (String id, float x, float y, float width, float height, Ai ai) {
		super(id, x, y, width, height);
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		ai.update(this, deltaTime);
	}

	// Copies the enemy to a new position
	public Enemy copy(float x, float y){
		return new Enemy(this.id, x, y, this.getWidth(), this.getHeight(), this.ai);
	}
}