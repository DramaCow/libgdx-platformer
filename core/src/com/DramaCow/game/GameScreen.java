package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter{

	public static enum GameState {
		INIT, 
		RUNNING, 
		PAUSE,
		GAME_OVER
	}

	private final GDXgame game;
	private GameState state;
	private OrthographicCamera cam;

	public GameScreen(GDXgame game){
		this.game = game;

		this.state = GameState.INIT;

		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();

		// define: WORLD, WORLD_RENDERER, WORLD_LISTENER, RECTs, SCORE; here
	}

	public void update(){
		// WHAT SHOULD THE DELTA TIME COMPARISON BE???
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		switch (state) {
			case INIT:
				// initialise world here
				// when world has been initialised, goto RUNNING state
				break;
			case RUNNING:
				// deal with game input here and adjust world state accordingly
				// otherwise let world state decide what is happening (has its own states)
				break;
			case PAUSE:
				// don't update world 
				// Display pause text and dull screen

				// or

				// go back to RUNNING state
				break;
			case GAME_OVER:
				// pass the score and goto game over screen 
				break;
		}
	}

	public void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void render(float delta){
		update();
		draw();
	}
}
