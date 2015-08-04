package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;

import com.DramaCow.maths.Vector2D;
import com.DramaCow.maths.Rect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class World {

	public static enum WorldState {
		LOADING,		// \ 
		START,			// |-- Loop between these states
		RUNNING, 		// |
		END				// /
	}
	
	private final Map<Integer, String> BIOMES;
	private final String DEFAULT_BIOME; 

	private WorldState state;
	private Rect cambounds; 

	public Player player;
	private Level currentLevel;
	private Level nextLevel;

	public Tileset tileset;

	public int levelNumber;
	private long elapsedTime;
	private int score;

	private boolean gameover = false;

	// ========================================
	// =============== SETUP ==================
	// ========================================

	public World() {
		this.cambounds = new Rect(0.0f, 0.0f, 16.0f, 16.0f);

		this.DEFAULT_BIOME = XReader.getDefaultLevel(Terms.LEVEL_MASTER);
		this.BIOMES = XReader.getAllLevels(Terms.LEVEL_MASTER);

		startGeneratingNextLevel();
 
		levelNumber = 0;
		elapsedTime = 0L;
		score = 0; 

		this.state = WorldState.LOADING;
	}

	public void init() {
		// Load player assets here
		TextureManager.loadTexture("player","frog.png");
		Tileset playerTiles = new Tileset(TextureManager.getTexture("player"),70,52);

		for (Player.PlayerState state : Player.PlayerState.values()) {
			AnimationManager.loadAnimation(player.getStateID(state), new Animation(0.0625f,playerTiles.getTiles()));
		}

		// Loading screen image should be loaded from a file
		TextureManager.loadTexture("loading", "mark.png");

		// Borders should be loaded from a file
		TextureManager.loadTexture("start", "jagged2.png");
		TextureManager.loadTexture("end", "jagged.png");
	}

	// ========================================
	// ========== STATE FUNCTIONS =============
	// ========================================

	public void update(float dt) {
		switch (state) {	
			case LOADING:	loading(); 		break;
			case START:		start(dt); 		break;
			case RUNNING:	running(dt); 	break;
			case END:		end(dt); 		break; 
		}
	}

	// LOADING STATE FUNCTIONS --------------------
	private void loading() {
		if (nextLevel.isReady()) {
			loadNextLevelAssets(); // should be done in background to avoid frame blip

			setUpCurrentLevel();
			setUpInputControls();
			startGeneratingNextLevel();

			state = WorldState.START;
		}	
	}

	private boolean loadNextLevelAssets(){
		// Boolean check needed to see if current biomeId matches next level biomeId to prevent reloading of the same assets

		String levelFilename = XReader.getFilenameOfLevel(Terms.LEVEL_MASTER, getNextBiome());
		
		TextureManager.loadTexture("tiles", XReader.getLevelTileset(levelFilename));
			tileset = new Tileset(TextureManager.getTexture("tiles"), 32, 32);
		TextureManager.loadTexture("background", XReader.getLevelBackground(levelFilename));
		SoundManager.loadMusic("bgm", XReader.getLevelBGM(levelFilename), true);

		for (String obstacleId : nextLevel.getBlueprintIDs()) {
			XReader.loadObstacleAssets(Terms.OBSTACLES, obstacleId);
		}

		return true;
	}

	private void setUpCurrentLevel() {
		//Assign next level to current level and begin generating next level
		levelNumber++;
		currentLevel = nextLevel;
		player = Level.generatePlayer(currentLevel, cambounds);
		trackPlayer();
		if (player == null) System.out.println("Player is null for some raisin");
	}

	private void setUpInputControls() {
		// TEMPORARY FUNCTION
		Gdx.input.setInputProcessor(new InputAdapter () {
			@Override
			public boolean keyDown (int keycode) {
				switch (keycode) {
					case Keys.W:
						player.up = true;
						break;
					case Keys.A:
						player.left = true;
						break;
					case Keys.S:
						player.down = true;
						break;
					case Keys.D:
						player.right = true;
						break;
					case Keys.P:
						player.printbools();
						break;
				}
				return false;
			}

			@Override
			public boolean keyUp (int keycode) {
				switch (keycode) {
					case Keys.W:
						player.up = false;
						break;
					case Keys.A:
						player.left = false;
						break;
					case Keys.S:
						player.down = false;
						break;
					case Keys.D:
						player.right = false;
						break;
				}
				return false;
			}
		});
	}

	private void startGeneratingNextLevel() {
		nextLevel = new Level(getNextBiome(), 64, 16);
		Level.generateMapInBackground(nextLevel);
	}
	// ----------------------------------------

	// START STATE FUNCTIONS --------------------
	private void start(float dt) {
		currentLevel.update(cambounds, dt);
		trackPlayer();

		if (0.0f <= player.getX() + player.getWidth()) { 
			player.toggleExistence(true);
			state = WorldState.RUNNING;
		}
	}
	// ----------------------------------------

	// RUNNING STATE FUNCTIONS --------------------
	private void running(float dt) {
		currentLevel.update(cambounds, dt);
		trackPlayer();
	
		if (currentLevel.LEVEL_WIDTH <= player.getX() + player.getWidth()) {
			state = WorldState.END;
		}
	}

	private void trackPlayer(){
		float camx = player.getX() + player.getWidth() / 2  - cambounds.w / 4;
		float camy = player.getY() + player.getHeight() / 2 - cambounds.h / 2;

		float ymax = currentLevel.LEVEL_HEIGHT - cambounds.h;
 
		cambounds.x = camx;
		cambounds.y = camy >= 0.0f ? ( camy <= ymax ? camy : ymax ) : 0.0f;
	}
	// ----------------------------------------

	// END STATE FUNCTIONS --------------------
	private void end(float dt) {
		player.toggleExistence(false);
		currentLevel.update(cambounds, dt);
		trackPlayer();

		if (cambounds.x >= currentLevel.LEVEL_WIDTH) {
			disposeCurrentLevelAssets();
			state = WorldState.LOADING;
		}
	}

	private void disposeCurrentLevelAssets() {
		for (String assetId : currentLevel.getBlueprintIDs()) {
			TextureManager.disposeTexture(assetId);
			AnimationManager.disposeAnimation(assetId);
			SoundManager.disposeSound(assetId);
		}
	}
	// ----------------------------------------

	// ========================================
	// ============ GET FUNCTIONS =============
	// ========================================

	public Rect getCamBounds(){
		return cambounds;
	}

	public boolean isGameover() {
		return gameover;
	}

	public WorldState getState() {
		return state;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public String getNextBiome(){
		if(BIOMES.containsKey(levelNumber+1)) return BIOMES.get(levelNumber+1); 
		else return DEFAULT_BIOME;
	}

	public void resize(float w, float h) {
		cambounds.setSize(w, h);
	}

	// ========================================
	// ========== OTHER FUNCTIONS =============
	// ========================================

	public void dispose() {
		disposeCurrentLevelAssets();

		// CHECK IF DISPOSING OF FOLLOWING ASSETS CAUSES EXCEPTIONS
		TextureManager.disposeTexture("player");

		for (Player.PlayerState state : Player.PlayerState.values()) {
			AnimationManager.disposeAnimation(player.getStateID(state));
		}

		TextureManager.disposeTexture("loading");		

		TextureManager.disposeTexture("start");
		TextureManager.disposeTexture("end");
	}
}
