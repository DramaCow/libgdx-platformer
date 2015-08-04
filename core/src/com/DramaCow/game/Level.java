package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

import com.DramaCow.maths.Rect;

public class Level {
	public final String BIOME_ID; 

	public final int LEVEL_WIDTH, LEVEL_HEIGHT;
	private final int[][] REGION_MAP;

	private Map<String, GameObject> obstacleBlueprints;
	private List<GameObject> objects;
	private Player player;

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
				REGION_MAP[r][c] = r < 3 || (r == 6 && c <= 40 && c >= 20) ? 1 : 0;
			}
		}

		return true;
	}

	private boolean populate(final Level level) {
		// Proper population to be implemented here
		objects = new ArrayList<GameObject>();

		List<String> obstacleHat = new ArrayList<String>(getBlueprintIDs());

		if (obstacleHat.isEmpty()) return false;

		Random rn = new Random();

		int pick; GameObject obstacle;
		for (int i = 0; i < 50; i++) {
			pick = rn.nextInt(obstacleHat.size());
			obstacle = obstacleBlueprints.get(obstacleHat.get(pick));
			if (obstacle instanceof Enemy) {
				objects.add(new Enemy((Enemy) obstacle, 10.0f + i*1.0f, 5.0f, level));
			}
		}

		return true;
	}

	// INTERFACES FOR GENERATING LEVEL MAP
	public static void generateMap(final Level level) {
		level.isReady = false;
		level.generate();
		level.obstacleBlueprints = XReader.getLevelObstacles(XReader.getFilenameOfLevel(Terms.LEVEL_MASTER, level.BIOME_ID));
		level.populate(level);
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

	public static Player generatePlayer(final Level level, Rect cambounds) {
		if (level.player == null) {
			float w = 2.1875f;
			float h = 1.625f;

			level.player = new Player("Player", 3.0f - cambounds.w, level.LEVEL_HEIGHT/2 - h/2, w, h, level);
			level.player.toggleExistence(false);

			return level.player;
		}
		return null;
	}

	// --------------------------------------------------

	public int[][] getMap() {	
		return REGION_MAP;
	}

	public Set<String> getBlueprintIDs() {
		return obstacleBlueprints.keySet();
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	public boolean isReady() {
		return isReady;
	}

	public void update(Rect bounds, float dt) {
		// TODO
		/*	Check collisions???
		 *	Update enemies
		 */
		
		// Regular loop needed to remove elements from map with concurrency exception
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if (object.getX() + object.getWidth() < bounds.x 		|| 
				object.getX() + object.getWidth() > LEVEL_WIDTH		||
				object.getX() + object.getWidth() < 0.0f) {
				objects.remove(i);
			}
			if (bounds.overlaps(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {		
				object.update(dt);
			}
			else break; //Assumes list near linearly ordered by objects x position (excluding those already on screen)
		}
		
		player.update(dt);
	}	

	public static void printmap(Level level) {
		for (int r = 0; r < level.LEVEL_HEIGHT; r++) {
			for (int c = 0; c < level.LEVEL_WIDTH; c++) {
				System.out.print(level.REGION_MAP[r][c]);
			}
			System.out.println();
		}
	}	
}
