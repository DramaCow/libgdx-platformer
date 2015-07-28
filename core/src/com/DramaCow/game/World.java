package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;

public class World {

	public static enum WorldState {
		READY,			// \ 
		RUNNING, 		// |-- Loop between these states
		END,			// |
		TRANSITION		// /
	}

	private WorldState state;

	private Rect cambounds; 
	
	private final Map<Integer, String> BIOMES;
	private final String DEFAULT_BIOME; 
	public final Player PLAYER;

	private Level currentLevel;
	private /*volatile*/ Level nextLevel;

	private int levelNumber;
	private long elapsedTime;
	private int score;

	private boolean gameover = false;

	public World() {
		this.cambounds = new Rect(0.0f, 0.0f, 16.0f, 16.0f);

		this.DEFAULT_BIOME = XReader.getDefaultLevel(Terms.LEVEL_MASTER);
		this.BIOMES = XReader.getAllLevels(Terms.LEVEL_MASTER);

		this.PLAYER = new Player("Player",3.0f,3.0f,(float) 70/32,(float) 52/32);

		nextLevel = new Level(getNextBiome(), PLAYER, 64, 16);
		Level.generateMap(nextLevel);	
 
		levelNumber = 0;
		elapsedTime = 0L;
		score = 0; 

		this.state = WorldState.READY;
	}

	public void init() {
		TextureManager.loadTexture("Player","frog.png");
		Tileset playerTiles = new Tileset(TextureManager.getTexture("Player"),70,52);
		AnimationManager.loadAnimation("Run", new Animation(0.0625f,playerTiles.getTiles()));

		TextureManager.loadTexture("end", "jagged.png");

		loadNextLevelAssets();
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

	public void update(float dt) {
		switch (state) {			
			case READY:
				levelNumber++;
				currentLevel = nextLevel;
				nextLevel = new Level(getNextBiome(), PLAYER, 64, 16);
				Level.generateMapInBackground(nextLevel);
				state = WorldState.RUNNING;
				break;

			case RUNNING:
				currentLevel.update(cambounds, dt);
				trackPlayer();
				// set game over here
				break;

			case END:
				break;
		
			case TRANSITION:
				// Stay in transition if next level isn't ready
				//disposeLevelAssets(); // Should be in world???
				if (nextLevel.isReady()) state = WorldState.READY;	
				break;

		}
	}

	private boolean loadNextLevelAssets(){
		// Boolean check needed to see if current biomeId matches next level biomeId to prevent reloading of the same assets

		String levelFilename = XReader.getFilenameOfLevel(Terms.LEVEL_MASTER, getNextBiome());
		
		TextureManager.loadTexture("tiles", XReader.getLevelTileset(levelFilename));
		TextureManager.loadTexture("background", XReader.getLevelBackground(levelFilename));
		SoundManager.loadMusic("bgm", XReader.getLevelBGM(levelFilename), true);

		for (String obstacleId : nextLevel.getBlueprintIDs()) {
			XReader.loadObstacleAssets(Terms.OBSTACLES, obstacleId);
		}

		return true;
	}

	public void dispose() {
		for (String assetId : currentLevel.getBlueprintIDs()) {
			TextureManager.disposeTexture(assetId);
			//AnimationManager.disposeAnimation(assetId);
			SoundManager.disposeSound(assetId);
		}
	}

	public void resize(float w, float h) {
		cambounds.setSize(w, h);
	}

	public Rect getCamBounds(){
		return cambounds;
	}

	private void trackPlayer(){
		float camx = PLAYER.getX() + PLAYER.getWidth() / 2  - cambounds.getW() / 4;
		float camy = PLAYER.getY() + PLAYER.getHeight() / 2 - cambounds.getH() / 2;

		float ymax = currentLevel.LEVEL_HEIGHT - cambounds.getH();

		cambounds.setX( camx >= 0.0f ? camx : 0.0f ); 
		cambounds.setY( camy >= 0.0f ? ( camy <= ymax ? camy : ymax ) : 0.0f );
	}
}
