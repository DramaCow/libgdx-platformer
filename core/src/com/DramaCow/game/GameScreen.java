package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter{
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

	OrthographicCamera cam;
	GDXgame game;

	public GameScreen(GDXgame game){
		cam = new OrthographicCamera(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
		cam.position.set(320 / 2, 480 / 2, 0);
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
