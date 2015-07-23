package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter{

	public static enum GameState {
		WORLD,
		PAUSE,
		GAME_OVER
	}

	private final GDXgame game;
	private GameState state;
	//private OrthographicCamera cam;

	private final World world;
	private final WorldRenderer worldRenderer;

	public GameScreen(GDXgame game){
		this.game = game;

		this.state = GameState.WORLD;

		//float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		//cam = new OrthographicCamera(10.0f * ((float) w/h), 10.0f);
		//cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		//cam.update();

		this.world = new World();
		this.worldRenderer = new WorldRenderer(game.batch, this.world);
	}

	public void update(float deltaTime){
		// WHAT SHOULD THE DELTA TIME COMPARISON BE???
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		switch (state) {
			case WORLD:
				// Let world deal with its state
				world.update(deltaTime);
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

		worldRenderer.render();
	}

	@Override
	public void render(float delta){
		update(delta);
		draw();
	}

	@Override
	public void resize(int w, int h) {
		worldRenderer.resize(w, h);
	}
}
