package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Level {
	public final String BIOME_ID; 

	public final int LEVEL_WIDTH, LEVEL_HEIGHT;
	private final int[][] REGION_MAP;

	private Map<String, GameObject> obstacleBlueprints;
	private List<GameObject> objects;
	private Player player;

	private boolean isReady;

	public Level(String biome, Player player, int w, int h) {
		this.player = player;
		this.BIOME_ID = biome;

		this.LEVEL_WIDTH = w; this.LEVEL_HEIGHT = h;
		this.REGION_MAP = new int[h][w];

		this.isReady = false;
	}

	private boolean generate() {
		// Proper map generation will go here
		Random rn = new Random();
		for (int r = 0; r < LEVEL_HEIGHT; r++) {
			for (int c = 0; c < LEVEL_WIDTH; c++) {
				if(r<3) REGION_MAP[r][c] = 1;
				else{
					int rand = rn.nextInt(4);
					REGION_MAP[r][c] = rand == 0 ? 1 : 0;
				}
			}
		}
		return true;
	}

	private boolean populate() {
		// Proper population to be implemented here
		objects = new ArrayList<GameObject>();

		List<String> obstacleHat = new ArrayList<String>(getBlueprintIDs());
		System.out.println("Obstacle Hat: " + obstacleHat);

		if (obstacleHat.isEmpty()) return false;

		Random rn = new Random();

		int pick; GameObject obstacle;
		for (int i = 0; i < 50; i++) {
			pick = rn.nextInt(obstacleHat.size());
			obstacle = obstacleBlueprints.get(obstacleHat.get(pick));
			if (obstacle instanceof Enemy) {
				objects.add(new Enemy((Enemy) obstacle, 10.0f + i*1.0f, 5.0f));
			}
		}

		return true;
	}

	// INTERFACES FOR GENERATING LEVEL MAP
	public static void generateMap(final Level level) {
		System.out.println("Generating map");

		level.isReady = false;
		level.generate();
		level.obstacleBlueprints = XReader.getLevelObstacles(XReader.getFilenameOfLevel(Terms.LEVEL_MASTER, level.BIOME_ID));
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
			if (object.getX() + object.getWidth() < bounds.getX() 	|| 
				object.getX() + object.getWidth() > LEVEL_WIDTH		||
				object.getX() + object.getWidth() < 0.0f) {
				objects.remove(i);
			}
			if (bounds.overlaps(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
				//System.out.println("Bounds in");
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
