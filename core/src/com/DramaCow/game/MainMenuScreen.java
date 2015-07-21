package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter {

	GDXgame game;
	OrthographicCamera cam;
	Rectangle goToGameScreen;
	Vector3 touchPoint;

	public MainMenuScreen (GDXgame game) {
		this.game = game;

		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		touchPoint = new Vector3();
		goToGameScreen = new Rectangle(20, 100, 50, 50);
	}


	public void update() {
		if(Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if(goToGameScreen.contains(touchPoint.x, touchPoint.y)){
				game.setScreen(new GameScreen(game));
			}
		}
		
	}

	public void	draw () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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