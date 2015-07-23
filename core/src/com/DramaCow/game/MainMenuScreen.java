package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;

public class MainMenuScreen extends ScreenAdapter {

	private final GDXgame game;
	private OrthographicCamera cam;
	private Rectangle goToGameScreen;
	private Vector3 touchPoint;
	private Button startButton;
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

	public void update(float delta) {
		if(startButton.isClicked()) game.setScreen(new GameScreen(game));

		// Get mouse/touch point
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		// Get whether screen has been clicked/touched
		boolean down = Gdx.input.justTouched();
		
		// Update buttons
		startButton.update(touchPoint.x, touchPoint.y, down);

		//Update tile and background offsets
		tileOffset -= delta * 2.0f;
		if(tileOffset < -1.0f) tileOffset = 0.0f;
		bgOffset -= delta * 1.0f;
		if(bgOffset < -bgWidth) bgOffset = 0.0f;

		animationTime += delta;
	}

	public void	draw () {
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.disableBlending();

		//Draw background
		for(float i = 0.0f; i < cam.viewportWidth + bgWidth ; i += bgWidth) {
			game.batch.draw(TextureManager.getTexture("tempBackground"),i+bgOffset,0.0f,bgWidth,bgHeight);
		}

		//Draw tiles
		for(float i = 0.0f; i < cam.viewportWidth + 1.0f ; i += 1.0f) {
			game.batch.draw(menuTiles.getTile(5),i+tileOffset,0.0f,1.0f,1.0f);
			game.batch.draw(menuTiles.getTile(1),i+tileOffset,1.0f,1.0f,1.0f);
		}

		//Draw buttons
		game.batch.draw(startButton.getTexture(),startButton.getX(),startButton.getY(), startButton.getW(), startButton.getH());

		//Draw running animation
		game.batch.enableBlending();
		game.batch.draw(runAnimation.getKeyFrame(animationTime,0),2.0f,2.0f,1.0f,1.0f);

		game.batch.end();
	}

	@Override
	public void render (float delta) {
		update(delta);
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
		startButton.setX(cam.viewportWidth - buttonsX);
		cam.update();
	}

	@Override
	public void show() {
		TextureManager.loadTexture("startBtnTiles","tempStartButton.png");
		startButton = new Button("startBtnTiles",64,32,buttonsX,5.0f,4.0f,2.0f);

		//Load temp menu tiles and bg
		TextureManager.loadTexture("tempGrassTiles","tempGrassTileSet.png");
		menuTiles = new Tileset(TextureManager.getTexture("tempGrassTiles"),32,32);
		TextureManager.loadTexture("tempBackground","tempBackground.png");

		//Load and create running animation
		TextureManager.loadTexture("runAnimationTiles","tempAnimation.png");
		runAnimationTiles = new Tileset(TextureManager.getTexture("runAnimationTiles"),32,32);
		runAnimation = new Animation(0.2f,runAnimationTiles.getTile(0),runAnimationTiles.getTile(1));

		//Menu music
		SoundManager.loadMusic("menuMusic","tempMenuLoop.ogg", true);
		SoundManager.getMusic("menuMusic").play();
	}

	@Override
	public void hide() {
		SoundManager.disposeAllMusic();
		TextureManager.disposeAllTextures();
	}
}
