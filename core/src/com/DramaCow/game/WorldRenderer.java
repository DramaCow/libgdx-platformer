package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldRenderer {
	
	private World world;
	private OrthographicCamera cam;
	private SpriteBatch batch;

	private Tileset tileset;

	public WorldRenderer(SpriteBatch batch, World world) {
		this.world = world;
	
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		this.cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		this.cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		this.cam.update();

		this.batch = batch;

		TextureManager.loadTexture("loading", "loading.png");

		TextureManager.loadTexture("tiles", "tempGrassTileSet.png");
		this.tileset = new Tileset(TextureManager.getTexture("tiles"), 32, 32);
	}	
	
	public void render() {
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Depending on world state, call different render routines here

		switch (world.getState()) {
			case INIT:
				batch.disableBlending();
				batch.begin();
					batch.draw(TextureManager.getTexture("loading"), 0.0f, 0.0f, cam.viewportWidth, cam.viewportHeight);
				batch.end();
				break;

			case READY:
				// Display same stuff as transition state?
				break;

			case RUNNING:
				System.out.println("Draw shit");

				batch.disableBlending();
				batch.begin();
					renderLevelBackground();
				batch.end();

				batch.enableBlending();
				batch.begin();
					renderLevelTiles();
					renderLevelObjects();
				batch.end();
				break;

			case TRANSITION:
				// Render fade in/out sequence here (keep player animation on screen)
				break;

			case GAME_OVER:
				// Dispose of textures and sounds

				// Undecided, this state is currently going to be used for notifying game screen of a game over
				break;
		}
	}

	private void renderLevelBackground() {
		// TODO
	}

	private void renderLevelTiles() {
		// EXAMPLE CODE
		int tile = 0;
		float width = tileset.TILE_X / 32;		// Where 32px == 1.0m
		float height = tileset.TILE_Y / 32;

		for (int r = 0; r < world.getCurrentLevel().LEVEL_HEIGHT; r++) {
			for (int c = 0; c < world.getCurrentLevel().LEVEL_WIDTH; c++) {
				tile = world.getCurrentLevel().getMap()[r][c];
				if (tile != 0) {
					batch.draw(tileset.getTile(tile - 1), c * width, r * height, 
						width, height);
				}
			}
		}
	}

	private void renderLevelObjects() {
		// TODO
	}

	public void resize(int w, int h) {
		cam.viewportWidth = 16.0f * ((float) w/h);
		cam.viewportHeight = 16.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}
}
