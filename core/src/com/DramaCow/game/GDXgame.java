package com.DramaCow.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class GDXgame implements ApplicationListener {	

	public SpriteBatch batch;
	private Screen screen;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		screen.update(Gdx.graphics.getDeltaTime());
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
}
