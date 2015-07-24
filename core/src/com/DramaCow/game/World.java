package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;

public class World {

	public static enum WorldState {
		INIT, 
		READY,			// \ 
		RUNNING, 		// |-- Loop between these states
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
		this.DEFAULT_BIOME = XReader.getDefault("levelmaster.xml");
		//Set world listener here
		this.BIOMES = XReader.getLevels("levelmaster.xml"); //Done it m8 // <-- Replace with reading biomes from file
		// this.player = new Player();
 
		levelNumber = 0;
		elapsedTime = 0L;
		score = 0; 
	}

	public WorldState getState() {
		return state;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	//May or may not need this
	public String getNextBiome(){
		if(BIOMES.containsKey(levelNumber+1)) return BIOMES.get(levelNumber+1); 
		else return DEFAULT_BIOME;
	}

	public void update(float dt) {
		// Call each update method in here

		switch (state) {
			case INIT:
				nextLevel = new Level("id", 1024, 16);
				Level.generateMap(nextLevel);
				state = WorldState.READY;
				System.out.println("ready");
				break;
			
			case READY:
				levelNumber++;
				currentLevel = nextLevel;
				nextLevel = new Level("id", 1024, 16);
				Level.generateMapInBackground(nextLevel);
				state = WorldState.RUNNING;
				break;

			case RUNNING:
				currentLevel.update(dt);
				break;
		
			case TRANSITION:
				// Stay in transition if next level isn't ready
				if (nextLevel.isReady()) state = WorldState.READY;	
				break;

			case GAME_OVER:
				break;
		}
	}
}
