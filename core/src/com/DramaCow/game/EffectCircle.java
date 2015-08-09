package com.DramaCow.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class EffectCircle extends ScreenEffect {
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	private final float r, g, b; 

	public EffectCircle(float duration) {
		super(duration);
		r = g = b = 0.0f;
	}

	public EffectCircle(float r, float g, float b, float duration) {
		super(duration);
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public void draw() {
		float w = Gdx.graphics.getWidth()/2.0f;
		float h = Gdx.graphics.getHeight()/2.0f;

		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, elapsedTime/duration);
			shapeRenderer.circle(w, h, (float)Math.sqrt(w*w + h*h) * (elapsedTime/duration));
		shapeRenderer.end();	
	}

	@Override 
	public String toString() {
		return "circle";
	} 
} 
