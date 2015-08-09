package com.DramaCow.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class EffectFadeIn extends ScreenEffect {

	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	private final float r, g, b; 
	
	public EffectFadeIn(float duration) {
		super(duration);
		r = g = b = 0.0f;
	}

	public EffectFadeIn(float r, float g, float b, float duration) {
		super(duration);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void draw() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, 1.0f - elapsedTime/duration);
			shapeRenderer.rect(0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);	
	}

	@Override 
	public String toString() {
		return "fade in";
	} 
} 
