package com.DramaCow.game;

public class Level {
	public final String BIOME_ID; 

	public final int LEVEL_WIDTH, LEVEL_HEIGHT;
	private final int[][] REGION_MAP;

	// private List<GameObjects> enemies;
	// private Player player;

	private boolean isReady;

	public Level(String biome, int w, int h) {
		this.BIOME_ID = biome;

		this.LEVEL_WIDTH = w; this.LEVEL_HEIGHT = h;
		this.REGION_MAP = new int[h][w];

		this.isReady = false;
	}

	private boolean generate() {
		// Proper map generation will go here
		for (int r = 0; r < LEVEL_HEIGHT; r++) {
			for (int c = 0; c < LEVEL_WIDTH; c++) {
				REGION_MAP[r][c] = r < 5 ? 1 : 0;
			}
		}
		return true;
	}

	// INTERFACES FOR GENERATING LEVEL MAP
	public static void generateMap(final Level level) {
		level.isReady = level.generate();
	}

	public static void generateMapInBackground(final Level level) {
		level.isReady = false;
		Thread t = new Thread(new Runnable() {
		  public void run() {
			level.generate();
			level.isReady = true;
		  }
		});
		t.start();
	}

	public int[][] getMap() {	
		return REGION_MAP;
	}

	public boolean isReady() {
		return isReady;
	}

	public void update() {
		// TODO
		/*	Check collisions???
		 *	Update enemies
		 */
	}		
}
