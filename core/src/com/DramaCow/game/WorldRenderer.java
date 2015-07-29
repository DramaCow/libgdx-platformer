package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.List;

public class WorldRenderer {
	
	private final GDXgame game;
	private World world;
	private OrthographicCamera cam;

	private ShapeRenderer shapeRenderer;

	private Tileset tileset;

	public WorldRenderer(GDXgame game, World world) {
		this.world = world;
	
		int w = game.getScreenWidth(), h = game.getScreenHeight();
		this.cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		this.cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		this.cam.update();
		this.world.resize(16.0f * ((float) w/h), 16.0f);

		this.game = game;

		this.shapeRenderer = new ShapeRenderer();
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
				game.batch.begin();
					renderPlayer();					
				game.batch.end();
				break;
			
			case START:
				game.batch.begin();
					renderPlayer();					
				game.batch.end();
				break;

			case RUNNING:
				renderLevel();

				game.batch.begin();
					renderPlayer();					
				game.batch.end();
				break;

			case END:
				renderLevel();

				game.batch.begin();
					renderPlayer();					
				game.batch.end();
				break;

			case TRANSITION:
				// Render fade in/out sequence here (keep player animation on screen)
				break;

		}
	}

	private void renderLevel() {
		updateCamPosition();

		game.batch.begin();
			game.batch.disableBlending();
			renderLevelBackground();

			game.batch.enableBlending();
			renderLevelTiles();
			renderLevelObjects();
			renderLevelBorder();
		game.batch.end();

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
			renderLevelBounds();	
		shapeRenderer.end();		
	}

	private void renderLevelBackground() {
		// To be optimised

		final float p = 2.0f; // Background moves p times slower than foreground

		final float camx = world.getCamBounds().getX();
		final float camw = world.getCamBounds().getW();

		final float LEVEL_WIDTH  = world.getCurrentLevel().LEVEL_WIDTH;
		final float LEVEL_HEIGHT = world.getCurrentLevel().LEVEL_HEIGHT;

		final float w = TextureManager.getTextureWidth("background") / 32.0f; // Where 32px == 1.0m	

		final float base = camx < 0.0f ? 0.0f : camx;								// Where we want the background to start
		final float limit = LEVEL_WIDTH < camx + camw ? LEVEL_WIDTH : camx + camw;	// Where we want the background to end

		final float dx = Math.abs( (camx / p) % w ); 								// Background distance (absolute) 
																					//     caused by camera offset from level start
		final float x  = base - dx;													// Where background start at if we weren't clipping

		for (int i = 0; x + (i * w) < limit; i++) {
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

	private void renderLevelBorder() {
		game.batch.draw(TextureManager.getTexture("start"), 0.0f, 0.0f, 2.0f, 16.0f);
		game.batch.draw(TextureManager.getTexture("end"), world.getCurrentLevel().LEVEL_WIDTH - 2.0f, 0.0f, 2.0f, 16.0f);
	}

	private void renderLevelBounds() {
		final float camx = world.getCamBounds().getX();
		final float camw = world.getCamBounds().getW();
	
		final float LEVEL_WIDTH = world.getCurrentLevel().LEVEL_WIDTH;
		final float LEVEL_HEIGHT = world.getCurrentLevel().LEVEL_HEIGHT;

		shapeRenderer.setColor(0, 0, 0, 1);

		if (camx < 0.0f) {
			shapeRenderer.rect(camx, 0.0f, -camx, LEVEL_HEIGHT);
		}

		if (camx + camw > LEVEL_WIDTH) {
			shapeRenderer.rect(LEVEL_WIDTH, 0.0f, camx + camw - LEVEL_WIDTH, LEVEL_HEIGHT);
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
		// Change to translate later
		cam.position.set(world.getCamBounds().getX() + cam.viewportWidth/2, world.getCamBounds().getY() + cam.viewportHeight/2, 0.0f);
	}
}
