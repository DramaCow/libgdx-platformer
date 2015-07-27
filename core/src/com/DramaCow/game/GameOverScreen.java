package com.DramaCow.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont; 
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Music;

public class GameOverScreen implements Screen{
	private OrthographicCamera cam;
	private final GDXgame game;
	private SpriteBatch batch;
	private boolean beatHighScore;
	private BitmapFont message;
	private Button tryAgain;
	private Button mainMenu;
	private int score;
	private Vector3 touchPoint;

	public GameOverScreen(final GDXgame game, boolean beatHighScore, int score){
		this.game = game;
		this.beatHighScore = beatHighScore;
		this.score = score;
		this.batch = new SpriteBatch();
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		System.out.println(w +" " +h);
		this.cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		message = new BitmapFont();			
		message.setColor(Color.GREEN);
		touchPoint = new Vector3();	
	}

	@Override
	public void show(){
		SoundManager.loadMusic("endMusic", "tempEndMusic.ogg", false);
		SoundManager.getMusic("endMusic").play();
		TextureManager.loadTexture("gameoverTemp", "gameoverTemp.png");
		TextureManager.loadTexture("tryAgainBtnTiles","replaying.png");
		TextureManager.loadTexture("mainMenu", "startbutton.png");
		tryAgain = new Button("tryAgainBtnTiles",32,32,400.0f,200.0f,64.0f,64.0f){
			@Override 
			public void onClick(){
				//dispose();
				game.setScreen(new GameScreen(game));
			}
		};

		mainMenu = new Button("mainMenu",64, 32, 400.0f, 120.0f,128.0f,64.0f){
			@Override
			public void onClick(){
				System.out.println("disposing");
				//dispose();
				System.out.println("setting up game screen");
				game.setScreen(new MainMenuScreen(game));
			}
		};
	}

	@Override
	public void update(float delta){
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		boolean down = Gdx.input.justTouched();
		tryAgain.update(touchPoint.x, touchPoint.y, down);
		mainMenu.update(touchPoint.x, touchPoint.y, down);
	}

	@Override
	public void draw(){
		cam.update();
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		System.out.println("trying to draw");
		batch.draw(TextureManager.getTexture("gameoverTemp"), 0, 0, 480.0f * ((float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight()), 480.0f);
		batch.draw(tryAgain.getTexture(),tryAgain.getX(),tryAgain.getY(), tryAgain.getW(), tryAgain.getH());
		batch.draw(mainMenu.getTexture(),mainMenu.getX(),mainMenu.getY(), mainMenu.getW(), mainMenu.getH());

		if(beatHighScore) message.draw(batch, "New Highscore " + score, 400.0f, 100.0f);

		else message.draw(batch, "You Got " + score, 400.0f, 100.0f);
		
		batch.end();
	}

	@Override
	public void resize(int w, int h) {
		cam.viewportWidth = 480.0f * ((float) w/h);
		cam.viewportHeight = 480.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}

	@Override
	public void pause(){

	}

	@Override
	public void resume(){

	}
	
	@Override
	public void hide(){
		dispose();

	}

	@Override
	public void dispose(){
		SoundManager.getMusic("endMusic").stop();
		SoundManager.disposeMusic("endMusic");
		TextureManager.disposeTexture("tryAgainBtnTiles");
		TextureManager.disposeTexture("gameOverBackground");
		TextureManager.disposeTexture("mainMenu");
	}

	

	

	


}