package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class WorldRenderer {
	
	private final GDXgame game;
	private World world;
	private OrthographicCamera cam;

	private Tileset tileset;

	public WorldRenderer(GDXgame game, World world) {
		this.world = world;
	
		int w = game.getScreenWidth(), h = game.getScreenHeight();
		this.cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		this.cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		this.cam.update();
		this.world.resize(16.0f * ((float) w/h), 16.0f);

		this.game = game;
	}	

	public void init() {
		tileset = new Tileset(TextureManager.getTexture("tiles"), 32, 32);
	}

	public void render() {
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);

		switch (world.getState()) {
			case READY:
				// Display same stuff as transition state?
				break;

			case RUNNING:
				updateCamPosition();

				game.batch.disableBlending();
				game.batch.begin();
					renderLevelBackground();
				game.batch.end();

				game.batch.enableBlending();
				game.batch.begin();
					renderLevelTiles();
					renderLevelObjects();
					renderLevelEnd();
					renderPlayer();					
				game.batch.end();
				break;

			case TRANSITION:
				// Render fade in/out sequence here (keep player animation on screen)
				break;

			case END:
				break;

		}
	}

	private void renderLevelBackground() {
		final float p = 2.0f; // Background moves p times slower than foreground

		final float bx = world.getCamBounds().getX();
		final float bw = world.getCamBounds().getW();

		final float LEVEL_WIDTH  = world.getCurrentLevel().LEVEL_WIDTH;
		final float LEVEL_HEIGHT = world.getCurrentLevel().LEVEL_HEIGHT;

		final float w = TextureManager.getTextureWidth("background") / 32.0f; // Where 32px == 1.0m
		final float h = TextureManager.getTextureHeight("background") / 32.0f;			

		final float dx = (bx / p) % w;
		final float x  = bx - dx;

		//Exceptional background drawing cases
		if (bx >= LEVEL_WIDTH) {
			return;
		}
		else if (bx + bw > LEVEL_WIDTH) {
			int i;
			for (i = 0; (x + w) + (i * w) < LEVEL_WIDTH; i++) {
				game.batch.draw(TextureManager.getTexture("background"), x + (i * w), 0.0f, w, LEVEL_HEIGHT);
			}
			float d = LEVEL_WIDTH - (x + (i * w));
			game.batch.draw(TextureManager.getTexture("background"), x + (i * w), 0.0f, d, LEVEL_HEIGHT,
				0.0f, 1.0f, d/w, 0.0f);
			return;
		}

		for (int i = 0; x + (i * w) < bx + bw; i++) {
			game.batch.draw(TextureManager.getTexture("background"), x + (i * w), 0.0f, w, LEVEL_HEIGHT);
		}
	}

	private void renderLevelTiles() {	
		int c0 = (int) world.getCamBounds().getX(); 
			c0 = c0 >= 0 ? c0 : 0;
		int cmax = (int) (world.getCamBounds().getX() + world.getCamBounds().getW() + 1);
			cmax = cmax <= world.getCurrentLevel().LEVEL_WIDTH ? cmax : world.getCurrentLevel().LEVEL_WIDTH; 

		int r0 = (int) world.getCamBounds().getY(); r0 = r0 >= 0 ? r0 : 0;
			r0 = r0 >= 0 ? r0 : 0;
		int rmax = (int) (world.getCamBounds().getY() + world.getCamBounds().getH() + 1);
			rmax = rmax <= world.getCurrentLevel().LEVEL_HEIGHT ? rmax : world.getCurrentLevel().LEVEL_HEIGHT; 

		float width = tileset.TILE_X / 32;		// Where 32px == 1.0m
		float height = tileset.TILE_Y / 32;
		int tile = 0;

		for (int r = r0; r < rmax; r++) {
			for (int c = c0; c < cmax; c++) {
				tile = world.getCurrentLevel().getMap()[r][c];
				if (tile != 0) {
					game.batch.draw(tileset.getTile(tile-1), c * width, r * height, 
						width, height);
				}
			}
		}
	}

	private void renderLevelObjects() {
		List<GameObject> objects = world.getCurrentLevel().getObjects();
		Rect bounds = world.getCamBounds();

		// Regular loop needed to remove elements from map with concurrency exception
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if (bounds.overlaps(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
				game.batch.draw(AnimationManager.getAnimation(object.id).getKeyFrame(object.getTime(), 0), object.getX(), 
					object.getY(), object.getWidth(), object.getHeight());
			}
			else if (object.getX() < bounds.getX() || object.getX() > world.getCurrentLevel().LEVEL_WIDTH) {
				continue;
			}
			else break;
		}
	}

	private void renderLevelEnd() {
		game.batch.draw(TextureManager.getTexture("end"), world.getCurrentLevel().LEVEL_WIDTH - 2.0f, 0.0f, 2.0f, 16.0f);
	}

	private void renderPlayer() {
		game.batch.draw(AnimationManager.getAnimation(world.PLAYER.getStateID()).getKeyFrame(world.PLAYER.getTime(),0), world.PLAYER.getX(),
				world.PLAYER.getY(), world.PLAYER.getWidth(), world.PLAYER.getHeight());
	}

	public void resize(int w, int h) {
		cam.viewportWidth = 16.0f * ((float) w/h);
		cam.viewportHeight = 16.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		world.getCamBounds().setSize(cam.viewportWidth, cam.viewportHeight);
	}

	private void updateCamPosition(){
		// Change to translate later
		cam.position.set(world.getCamBounds().getX() + cam.viewportWidth/2, world.getCamBounds().getY() + cam.viewportHeight/2, 0.0f);
	}
}
