package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.List;

import com.DramaCow.maths.Rect;

public class WorldRenderer {
	
	private final GDXgame game;
	private World world;
	private OrthographicCamera cam;

	private ShapeRenderer shapeRenderer;

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
		// Do nothing
	}

	public void render() {
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);

		switch (world.getState()) {
			case LOADING:
				game.batch.begin();
					if (world.player == null) renderLoadingScreen();
					else 					  renderPlayer();					
				game.batch.end();
				break;
			
			case START:
				renderLevel();
	
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
		}
	}

	private void renderLoadingScreen() {
		game.batch.draw(TextureManager.getTexture("loading"), world.getCamBounds().x, world.getCamBounds().y, 
			world.getCamBounds().w, world.getCamBounds().h);
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

		final float camx = world.getCamBounds().x;
		final float camw = world.getCamBounds().w;

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
		int c0 = (int) world.getCamBounds().x; 
			c0 = c0 >= 0 ? c0 : 0;
		int cmax = (int) (world.getCamBounds().x + world.getCamBounds().w + 1);
			cmax = cmax <= world.getCurrentLevel().LEVEL_WIDTH ? cmax : world.getCurrentLevel().LEVEL_WIDTH; 

		int r0 = (int) world.getCamBounds().y; r0 = r0 >= 0 ? r0 : 0;
			r0 = r0 >= 0 ? r0 : 0;
		int rmax = (int) (world.getCamBounds().y + world.getCamBounds().h + 1);
			rmax = rmax <= world.getCurrentLevel().LEVEL_HEIGHT ? rmax : world.getCurrentLevel().LEVEL_HEIGHT; 

		float width = world.tileset.TILE_X / 32;		// Where 32px == 1.0m
		float height = world.tileset.TILE_Y / 32;
		int tile = 0;

		for (int r = r0; r < rmax; r++) {
			for (int c = c0; c < cmax; c++) {
				// Get current tile
				if(world.getCurrentLevel().getMap()[r][c] == 1) tile = 1; else tile = 0;
				// Get surrounding tiles
				int tileUp = 0, tileDown = 0, tileLeft = 0, tileRight = 0;
				if (r > world.getCurrentLevel().LEVEL_HEIGHT - 2 || world.getCurrentLevel().getMap()[r+1][c] == 1) tileUp = 1;
				if (r < 1 || world.getCurrentLevel().getMap()[r-1][c] == 1) tileDown = 1;
				if (c >	world.getCurrentLevel().LEVEL_WIDTH - 2	|| world.getCurrentLevel().getMap()[r][c+1] == 1) tileRight = 1;
				if (c < 1 || world.getCurrentLevel().getMap()[r][c-1] == 1) tileLeft = 1;
				// Draw the tile
				if (tile != 0) {
					int index = 1 * tileUp + 2 * tileRight + 4 * tileDown + 8 * tileLeft;
					game.batch.draw(world.tileset.getTile(index), c * width, r * height, width, height);
				} else {
					// Draw carpet tiles
					if (tileDown == 1 && r > 0) 	
						game.batch.draw(world.tileset.getTile(16), c * width, r * height, width, height);
					if (tileLeft == 1 && c > 0) 
						game.batch.draw(world.tileset.getTile(17), c * width, r * height, width, height);
					if (tileUp == 1 && r < world.getCurrentLevel().LEVEL_HEIGHT - 1) 	
						game.batch.draw(world.tileset.getTile(18), c * width, r * height, width, height);
					if (tileRight == 1 && c < world.getCurrentLevel().LEVEL_WIDTH - 1) 
						game.batch.draw(world.tileset.getTile(19), c * width, r * height, width, height);
				}
				if(r > 0 && r < world.getCurrentLevel().LEVEL_HEIGHT - 1 && tile == 1){
					// Draw inner corner
					int tileUpLeft = 0; int tileUpRight = 0; int tileDownLeft = 0; int tileDownRight = 0;
					if (r>0 && c>0 && world.getCurrentLevel().getMap()[r-1][c-1] == 1) tileDownLeft = 1;
					if (r<world.getCurrentLevel().LEVEL_HEIGHT - 1 && c>0 && world.getCurrentLevel().getMap()[r+1][c-1] == 1) tileUpLeft = 1;
					if (r>0 && c<world.getCurrentLevel().LEVEL_WIDTH - 1 && world.getCurrentLevel().getMap()[r-1][c+1] == 1) tileDownRight = 1;
					if (r<world.getCurrentLevel().LEVEL_HEIGHT - 1 && c<world.getCurrentLevel().LEVEL_WIDTH - 1 
						&& world.getCurrentLevel().getMap()[r+1][c+1] == 1) tileUpRight = 1;

					if (tileRight == 1 && tileDown == 1 && tileDownRight == 0) 
						game.batch.draw(world.tileset.getTile(20), c * width, r * height, width, height);
					if (tileDown == 1 && tileLeft == 1 && tileDownLeft == 0) 
						game.batch.draw(world.tileset.getTile(21), c * width, r * height, width, height);
					if (tileLeft == 1 && tileUp == 1 && tileUpLeft == 0) 
						game.batch.draw(world.tileset.getTile(22), c * width, r * height, width, height);
					if (tileUp == 1 && tileRight == 1 && tileUpRight ==0) 
						game.batch.draw(world.tileset.getTile(23), c * width, r * height, width, height);
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
			//if (bounds.overlaps(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
				game.batch.draw(AnimationManager.getAnimation(object.id).getKeyFrame(object.getTime(), 0), object.getX(), 
					object.getY(), object.getWidth(), object.getHeight());
			//}
			//else if (object.getX() < bounds.getX() || object.getX() > world.getCurrentLevel().LEVEL_WIDTH) {
			//	continue;
			//}
			//else break;
		}
	}

	private void renderLevelBorder() {
		game.batch.draw(TextureManager.getTexture("start"), 0.0f, 0.0f, 2.0f, 16.0f);
		game.batch.draw(TextureManager.getTexture("end"), world.getCurrentLevel().LEVEL_WIDTH - 2.0f, 0.0f, 2.0f, 16.0f);
	}

	private void renderLevelBounds() {
		final float camx = world.getCamBounds().x;
		final float camw = world.getCamBounds().w;
	
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
		//game.batch.draw(AnimationManager.getAnimation(world.player.getStateID()).getKeyFrame(world.player.getTime(),0), world.player.getX(),
				//world.player.getY(), world.player.getWidth(), world.player.getHeight());
		game.batch.draw(TextureManager.getTexture("player"), world.player.getX(),
				world.player.getY(), world.player.getWidth(), world.player.getHeight());
	}

	public void resize(int w, int h) {
		cam.viewportWidth = 16.0f * ((float) w/h);
		cam.viewportHeight = 16.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		world.getCamBounds().setSize(cam.viewportWidth, cam.viewportHeight);
	}

	private void updateCamPosition(){
		// Change to translate 
		cam.position.set(world.getCamBounds().x + cam.viewportWidth/2, world.getCamBounds().y + cam.viewportHeight/2, 0.0f);
	}
}
