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
	
	private Map<Integer, String> BIOMES;
	// private final Player player;

	private Level currentLevel;
	private /*volatile*/ Level nextLevel;

	private int levelNumber;
	private long elapsedTime;
	private int score;

	public World() {
		this.state = WorldState.INIT;

		//Set world listener here
		this.BIOMES = new HashMap<Integer, String>(); // <-- Replace with reading biomes from file
		// this.player = new Player();
 
		levelNumber = 1;
		elapsedTime = 0L;
		score = 0; 
	}

	public WorldState getState() {
		return state;
	}

	public Level getCurrentLevel() {
		return currentLevel;
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
