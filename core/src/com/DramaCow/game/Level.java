package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;

public class Level {
	public final String BIOME_ID; 

	public final int LEVEL_WIDTH, LEVEL_HEIGHT;
	private final int[][] REGION_MAP;

	private List<GameObject> objects;
	// private Player player;

	private boolean isReady;

	public Level(String biome, int w, int h) {
		this.BIOME_ID = biome;

		this.LEVEL_WIDTH = w; this.LEVEL_HEIGHT = h;
		this.REGION_MAP = new int[h][w];

		this.isReady = false;

		this.objects = new ArrayList<GameObject>();
	}

	private boolean generate() {
		// Proper map generation will go here
		for (int r = 0; r < LEVEL_HEIGHT; r++) {
			for (int c = 0; c < LEVEL_WIDTH; c++) {
				REGION_MAP[r][c] = r < 3 ? 1 : 0;
			}
		}

		return true;
	}

	private boolean populate() {
		// Proper population to be implemented here
		for(int i = 0; i<5; i++){
			Ai test = Ai.getAI("wave",i*10);
			Enemy testEnemy = new Enemy("testEnemy",10.0f + i*10.0f,5.0f,2.0f,2.0f,test);
			objects.add(testEnemy);
		}

		return true;
	}

	// INTERFACES FOR GENERATING LEVEL MAP
	public static void generateMap(final Level level) {
		System.out.println("Generating map");

		level.isReady = false;
		level.generate();
		level.populate();
		level.isReady = true;
	}

	public static void generateMapInBackground(final Level level) {
		Thread t = new Thread(new Runnable() {
		  public void run() {
			Level.generateMap(level);
		  }
		});
		t.start();
	}

	public int[][] getMap() {	
		return REGION_MAP;
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	public boolean isReady() {
		return isReady;
	}

	public void update(float dt) {
		// TODO
		/*	Check collisions???
		 *	Update enemies
		 */
		for(GameObject object: objects){
			object.update(dt);
		}
	}		
}
