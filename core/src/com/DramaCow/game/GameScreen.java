package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter{

	public static enum State {
		INIT, 
		RUNNING, 
		PAUSE,
		GAME_OVER
	}

	OrthographicCamera cam;
	GDXgame game;

	public GameScreen(GDXgame game){
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();

		this.game = game;
	}

	public void update(){

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
