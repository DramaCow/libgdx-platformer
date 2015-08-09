package com.DramaCow.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GDXgame implements ApplicationListener {	

	public SpriteBatch batch;
	private Screen screen;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setColour(0.0f, 0.0f, 0.0f, 1.0f);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		screen.update(Gdx.graphics.getDeltaTime());
		clear();
		screen.draw();
	}

	@Override	
	public void pause() {
		screen.pause();
	}

	@Override	
	public void resume() {
		screen.resume();
	}

	@Override
	public void resize(int w, int h) {
		screen.resize(w, h);
	}

	@Override	
	public void dispose() {
		screen.dispose();
	}

	public void setScreen(Screen s) {
		if (screen != null) {
            screen.hide(); 													// dispose of assets
        }
        screen = s;
        screen.show(); 														// load assets (MAKE SURE TO NOT INITIALISE LOGIC HERE)
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	// DANGEROUS METHOD, only usable inside package
	void assignScreen(Screen s) {
        screen = s;
	}

	public void runEffects(ScreenEffect... effects) {
		screen = EffectsScreen.applyEffects(this, screen, effects);
	}

	public void transition(Screen next, ScreenEffect... effects) {
		screen = EffectsScreen.transition(this, screen, next, effects);
	}

	public void setColour(float r, float g, float b, float a) {
		Gdx.gl.glClearColor(r, g, b, a);
	}

	// Only needs to be called once per frame
	public void clear() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public int getScreenWidth() {
		return Gdx.graphics.getWidth();
	}

	public int getScreenHeight() {
		return Gdx.graphics.getHeight();
	}

	public float getInputX() {
		return Gdx.input.getX();
	}

	public float getInputY() {
		return Gdx.input.getY();
	}	

	public boolean isInputTouched() {
		return Gdx.input.isTouched();
	}
}
