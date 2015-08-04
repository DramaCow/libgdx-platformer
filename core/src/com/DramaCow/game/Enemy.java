package com.DramaCow.game;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicGameObject{

	private Ai ai;

	public Enemy(String id, float x, float y, float width, float height, Ai ai) {
		super(id, x, y, width, height);
		this.ai = ai;
		this.ai.create(this);
	}

	@Override
	public void update(float dt){
		super.update(dt);
		ai.update(this, dt);
	}

	// Allow repositioning cloned enemy
	public Enemy(Enemy that, float x, float y) {
		super(that.id, x, y, that.bounds.getWidth(), that.bounds.getHeight());
		this.ai = Ai.getAI(that.ai.ID(), that.ai.difficulty);
		this.ai.create(this);
	}

	public String getAiID(){
		return ai.ID();
	}

	@Override 
	public String toString() {
		return this.getAiID() + ": " + Float.toString(position.x); 
	}
}
