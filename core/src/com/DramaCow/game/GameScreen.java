package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameScreen implements Screen {

	public static enum GameState {
		PLAY,
		PAUSE,
		GAME_OVER
	}

	private final GDXgame game;
	private GameState state;
	private OrthographicCamera cam;

	private final World world;
	private final WorldRenderer worldRenderer;

	public GameScreen(GDXgame game){
		this.game = game;

		this.state = GameState.PLAY;

		float w = game.getScreenWidth(), h = game.getScreenHeight();
		cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();

		this.world = new World();
		this.worldRenderer = new WorldRenderer(game, this.world);
	}

	@Override
	public void show() {
		world.init();
		worldRenderer.init();

		game.runEffects(new EffectFadeIn(2.0f));
	}

	@Override
	public void update(float deltaTime){
		switch (state) {
			case PLAY:
				world.update(deltaTime);
				if (world.isGameover()) state = GameState.GAME_OVER;
				break;

			case PAUSE:
				// don't update world 
				// Display pause text and dull screen

				// or

				// go back to PLAY state
				// NOTE: INPUT NEEDS TO BE DEFINED IN THIS CLASS BEFORE PAUSE STATE CAN BE ACCESSED
				break;

			case GAME_OVER:
				// pass the score and goto game over screen 

				game.setScreen(new MainMenuScreen(game));
				break;
		}
	}

	@Override
	public void draw(){
		worldRenderer.render();
	}

	@Override
	public void resize(int w, int h) {
		worldRenderer.resize(w, h);

		cam.viewportWidth = 16.0f * ((float) w/h);
		cam.viewportHeight = 16.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		world.dispose();
	}
}
