package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen extends ScreenAdapter {

	private final GDXgame game;
	private OrthographicCamera cam;
	private Rectangle goToGameScreen;
	private Vector3 touchPoint;
	private Button startButton;

	public MainMenuScreen (GDXgame game) {
		this.game = game;

		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();

		TextureManager.loadTexture("startButtonTiles","tempStartButton.png");
		startButton = new Button("startButtonTiles",32,32,64,32,cam);
	}

	public void update() {
		if(startButton.isClicked()) game.setScreen(new GameScreen(game));
		startButton.update();
	}

	public void	draw () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.draw(startButton.getTexture(),startButton.getX(),startButton.getY());
		game.batch.end();
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void pause () {

	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = (float) width;
		cam.viewportHeight = (float) height;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}
}
