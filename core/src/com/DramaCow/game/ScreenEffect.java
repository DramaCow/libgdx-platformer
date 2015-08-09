package com.DramaCow.game;

abstract public class ScreenEffect {
	protected final float duration;
	protected float elapsedTime = 0.0f;

	protected boolean over = false;

	public ScreenEffect(float duration) {
		this.duration = duration;
	}

	public void update(float dt) {
		elapsedTime += dt;
		over = duration <= elapsedTime;
	}

	public void draw() {}

	public final boolean over() { return over; }
}
