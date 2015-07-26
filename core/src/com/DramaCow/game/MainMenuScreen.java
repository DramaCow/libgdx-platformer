package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Music;
import java.util.Map;
import java.util.HashMap;

public class MainMenuScreen implements Screen {

	private final GDXgame game;
	private OrthographicCamera cam;
	private Rectangle goToGameScreen;
	private Vector3 touchPoint;
	private Button startButton;
	private Button musicButton;
	private Tileset menuTiles;
	private float tileOffset;
	private float bgOffset;
	private Tileset runAnimationTiles;
	private Animation runAnimation;
	private float animationTime;

	private final float bgWidth = 20.0f;
	private final float bgHeight = 10.0f;
	private final float buttonsX = 5.0f;

	public MainMenuScreen (GDXgame game) {
		this.game = game;

		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(10.0f * ((float) w/h), 10.0f);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		System.out.println(cam.viewportWidth + ", " + cam.viewportHeight);

		touchPoint = new Vector3();

		//Init offsets
		tileOffset = 0.0f;
		bgOffset = 0.0f;
	}	

	@Override
	public void show() {
		TextureManager.loadTexture("button","startbutton.png");
		startButton = new Button("button",64,32,buttonsX,7.0f,4.0f,2.0f) {
			@Override 
			public void onClick() {
				game.setScreen(new GameScreen(game));
			}
		};

		musicButton = new Button("button",64,32,buttonsX,3.0f,4.0f,2.0f) {
			private boolean musicOff = false;

			@Override 
			public void onClick() {
				if (!musicOff) {
					SoundManager.getMusic("bgm").pause();
					musicOff = true;
				}
				else {
					SoundManager.getMusic("bgm").play();
					musicOff = false;
				}
			}
		};

		//Load temp menu tiles and bg
		TextureManager.loadTexture("tiles","tileset.png");
		menuTiles = new Tileset(TextureManager.getTexture("tiles"),32,32);
		TextureManager.loadTexture("background","background2.png");

		//Load and create running animation
		TextureManager.loadTexture("run","frog.png");
		runAnimationTiles = new Tileset(TextureManager.getTexture("run"),70,52);
		runAnimation = new Animation(0.15625f, runAnimationTiles.getTiles());

		//Menu music
		SoundManager.loadMusic("bgm","bgm.ogg", true);
		SoundManager.getMusic("bgm").play();
	}

	@Override
	public void update(float delta) {
		// Get mouse/touch point
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		// Update buttons
		startButton.update(touchPoint.x, touchPoint.y, Gdx.input.isTouched());
		musicButton.update(touchPoint.x, touchPoint.y, Gdx.input.isTouched());

		//Update tile and background offsets
		tileOffset -= delta * 2.0f;
		if(tileOffset < -1.0f) tileOffset = 0.0f;
		bgOffset -= delta * 1.0f;
		if(bgOffset < -bgWidth) bgOffset = 0.0f;

		animationTime += delta;
	}

	@Override
	public void	draw () {
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.disableBlending();

		//Draw background
		for(float i = 0.0f; i < cam.viewportWidth + bgWidth ; i += bgWidth) {
			game.batch.draw(TextureManager.getTexture("background"),i+bgOffset,0.0f,bgWidth,bgHeight);
		}

		//Draw tiles
		for(float i = 0.0f; i < cam.viewportWidth + 1.0f ; i += 1.0f) {
			game.batch.draw(menuTiles.getTile(5),i+tileOffset,0.0f,1.0f,1.0f);
			game.batch.draw(menuTiles.getTile(1),i+tileOffset,1.0f,1.0f,1.0f);
		}

		//Draw buttons
		game.batch.draw(startButton.getTexture(),startButton.getX(),startButton.getY(), startButton.getW(), startButton.getH());
		game.batch.draw(musicButton.getTexture(),musicButton.getX(),musicButton.getY(), musicButton.getW(), musicButton.getH());

		//Draw running animation
		game.batch.enableBlending();
		game.batch.draw(runAnimation.getKeyFrame(animationTime,0),2.0f,2.0f,(float) 70/32,(float) 52/32);

		game.batch.end();
	}

	@Override
	public void resize(int w, int h) {
		cam.viewportWidth = 10.0f * ((float) w/h);
		cam.viewportHeight = 10.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		startButton.setX(cam.viewportWidth - buttonsX);
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
		dispose();
	}

	@Override
	public void dispose() {
		SoundManager.getMusic("bgm").stop();

		TextureManager.disposeTexture("button");
		TextureManager.disposeTexture("tiles");
		TextureManager.disposeTexture("background");
		TextureManager.disposeTexture("run");
		SoundManager.disposeMusic("bgm");
	}
}
