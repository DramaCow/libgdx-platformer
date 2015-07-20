package com.DramaCow.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GDXgame extends Game {	

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
