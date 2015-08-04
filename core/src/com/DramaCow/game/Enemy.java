package com.DramaCow.game;

import com.DramaCow.maths.Vector2D;

public class Enemy extends DynamicGameObject{

	private Ai ai;

	public Enemy(String id, float x, float y, float width, float height, final Level level, Ai ai) {
		super(id, x, y, width, height, level);
		this.ai = ai;
		this.ai.create(this);
	}

	public Enemy(String id, float x, float y, float width, float height, Ai ai) {
		super(id, x, y, width, height, null);
		this.ai = ai;
		this.ai.create(this);
	}

	@Override
	public void update(float dt){
		super.update(dt);
		ai.update(this, dt);
	}

	// Allow repositioning cloned enemy
	public Enemy(Enemy that, float x, float y, final Level level) {
		super(that.id, x, y, that.bounds.w, that.bounds.h, level);
		this.ai = Ai.getAI(that.ai.ID(), that.ai.difficulty);
		this.ai.create(this);
	}
}
