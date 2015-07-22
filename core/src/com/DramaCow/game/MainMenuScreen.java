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
		cam = new OrthographicCamera(10.0f * ((float) w/h), 10.0f);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		System.out.println(cam.viewportWidth + ", " + cam.viewportHeight);

		touchPoint = new Vector3();

		TextureManager.loadTexture("startBtnTiles","tempStartButton.png");
		startButton = new Button("startBtnTiles",64,32,0.0f,0.0f,5.0f,2.5f);
	}

	public void update() {
		if(startButton.isClicked()) game.setScreen(new GameScreen(game));

		// Get mouse/touch point
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		// Get whether screen has been clicked/touched
		boolean down = Gdx.input.justTouched();
		
		// Update buttons
		startButton.update(touchPoint.x, touchPoint.y, down);
	}

	public void	draw () {
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.draw(startButton.getTexture(),startButton.getX(),startButton.getY(), startButton.getW(), startButton.getH());
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
	public void resize(int w, int h) {
		cam.viewportWidth = 10.0f * ((float) w/h);
		cam.viewportHeight = 10.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}
}
