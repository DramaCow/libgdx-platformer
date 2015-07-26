package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;

public class World {

	public static enum WorldState {
		INIT, 
		READY,			// \ 
		RUNNING, 		// |-- Loop between these states
		END,			// |
		TRANSITION,		// /
		GAME_OVER
	}

	private /*volatile*/ WorldState state;
	
	private final Map<Integer, String> BIOMES;
	private final String DEFAULT_BIOME; 
	// private final Player player;

	private Level currentLevel;
	private /*volatile*/ Level nextLevel;
	private int levelNumber;
	private long elapsedTime;
	private int score;

	public World() {
		this.state = WorldState.INIT;

		this.DEFAULT_BIOME = XReader.getDefaultLevel(Terms.LEVEL_MASTER);
		this.BIOMES = XReader.getAllLevels(Terms.LEVEL_MASTER);

		// this.player = new Player();

		nextLevel = new Level(getNextBiome(), 1024, 16);
		Level.generateMap(nextLevel);	
 
		levelNumber = 0;
		elapsedTime = 0L;
		score = 0; 
	}

	public void init() {
		TextureManager.loadTexture("loading", XReader.getLoadingScreen(Terms.LEVEL_MASTER)); // loading screen texture	
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
			case INIT:
				// nextLevel = new Level(getNextBiome(), 1024, 16);
				// Level.generateMap(nextLevel);
				// loadNextLevelAssets();	
				state = WorldState.READY;
				System.out.println("ready");
				break;
			
			case READY:
				levelNumber++;
				currentLevel = nextLevel;
				nextLevel = new Level(getNextBiome(), 1024, 16);
				Level.generateMapInBackground(nextLevel);
				state = WorldState.RUNNING;
				break;

			case RUNNING:
				currentLevel.update(dt);
				break;

			case END:
				break;
		
			case TRANSITION:
				// Stay in transition if next level isn't ready
				//disposeLevelAssets(); // Should be in world???
				if (nextLevel.isReady()) state = WorldState.READY;	
				break;

			case GAME_OVER:
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
	

	private void disposeLevelAssets() {
		for (String assetId : currentLevel.getBlueprintIDs()) {
			TextureManager.disposeTexture(assetId);
			//AnimationManager.disposeAnimation(assetId);
			SoundManager.disposeSound(assetId);
		}
	}
}
