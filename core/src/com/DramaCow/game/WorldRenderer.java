package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

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
					//renderLevelBackground();
				game.batch.end();

				game.batch.enableBlending();
				game.batch.begin();
					renderLevelTiles();
					renderLevelObjects();
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
		game.batch.draw(TextureManager.getTexture("background"), cam.position.x - cam.viewportWidth/2, cam.position.y - cam.viewportHeight/2, 
				cam.viewportWidth, cam.viewportHeight);
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
					game.batch.draw(tileset.getTile(tile-1), c * width, r * height, 
						width, height);
				}
			}
		}
	}

	private void renderLevelObjects() {
		List<GameObject> objects = world.getCurrentLevel().getObjects();
		for(GameObject object: objects){
			game.batch.draw(AnimationManager.getAnimation(object.id).getKeyFrame(object.getTime(), 0), object.getX(), 
				object.getY(), object.getWidth(), object.getHeight());
		}
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
		cam.position.set(world.getCamBounds().getX() + cam.viewportWidth/2, world.getCamBounds().getY() + cam.viewportHeight/2, 0.0f);
	}
}
