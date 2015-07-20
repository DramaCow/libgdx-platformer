package com.DramaCow.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GDXgame extends Game {	

	private SpriteBatch batch;
	private Tileset tileset;
	private OrthographicCamera cam;

	@Override
	public void create () {
		batch = new SpriteBatch();
		
		TextureManager.loadTexture("tiles", "tileset.png");
		tileset = new Tileset(TextureManager.getTexture("tiles"), 32);

		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(w, h);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}

	@Override
	public void render () {
		super.render();

		// Handle input here...
		cam.translate(1.0f, 0.0f);

		cam.update();
		batch.setProjectionMatrix(cam.combined);
	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw Tilemap
		//batch.enableBlending(true);
		batch.begin();
		for (int j = 0; j < (Gdx.graphics.getHeight() / tileset.TILE_SIZE) * 3; j++) {
			for (int i = 0; i < (Gdx.graphics.getWidth() / tileset.TILE_SIZE) * 3; i++) {
				batch.draw(tileset.getTile((i + j) % tileset.getNumberOfTiles()), 
					i * tileset.TILE_SIZE, j * tileset.TILE_SIZE, tileset.TILE_SIZE, tileset.TILE_SIZE);
			}
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = (float) width;
		cam.viewportHeight = (float) height;
			cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
	}

	@Override
	public void pause() {
		// If in game mode, enter pause state
	}

	@Override
	public void resume() {
		// Resume stuff here
	}

	@Override 
	public void dispose() {
		TextureManager.disposeAllTextures();
	}	
}
