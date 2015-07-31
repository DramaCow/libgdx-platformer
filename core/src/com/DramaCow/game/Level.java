package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.lang.Math;

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

		Random rn = new Random();
		float jumpSpeed = player.JUMP_SPEED;
		float runSpeed = player.RUN_SPEED;
		float gravity = -9.81f;
		float maxJumpHeight = (-jumpSpeed*jumpSpeed)/(2*gravity);

		final int MAX_PLATFORM_HEIGHT = 10;
		final int MIN_PLATFORM_HEIGHT = 1;
		final int MAX_PLATFORM_WIDTH = 12;
		final int START_HEIGHT = 3;

		int previousPlatformWidth = 8;

		//X and Y are used to mark the top right block of each platform
		//R and C are used to traverse the array
		int x = 0, y = 0, r = 0, c = 0;

		//Always generate first platform at height 3.0f
		for (r = 0; r < START_HEIGHT; r++){
			for (c = 0; c < previousPlatformWidth; c++){
				REGION_MAP[r][c] = 1;
			}
		}
		//Set pointers
		x = c;
		y = r;

		//Create other platforms
		while(x < LEVEL_WIDTH){
			//Generate new height of platform
			int newHeight = rn.nextInt((int)(maxJumpHeight + y) - MIN_PLATFORM_HEIGHT) + MIN_PLATFORM_HEIGHT;
			if(newHeight > MAX_PLATFORM_HEIGHT) newHeight = MAX_PLATFORM_HEIGHT;
			int heightDifference = newHeight - y;
			//Work out time to reach that height
			float timeToReachHeight = (float)((-jumpSpeed-Math.sqrt((double)(jumpSpeed*jumpSpeed+2*gravity*heightDifference)))/gravity);			//Work out distance travelled in X in that time
			//Work out max and min distance to new platform
			int maxDistance = (int)(runSpeed * timeToReachHeight);
			int minDistance = maxDistance - (previousPlatformWidth - 1);
			if(minDistance < 0) minDistance = 0;
			//Generate new x of platform
			int newX = maxDistance == minDistance ? x + maxDistance : x + rn.nextInt(maxDistance - minDistance) + minDistance;
			//Generate the width of the platform - normally distributed
			int newWidth = rn.nextInt(MAX_PLATFORM_WIDTH/2) + rn.nextInt(MAX_PLATFORM_WIDTH/2) + 1;
			//Place platform in array
			for (r = 0; r < newHeight; r++){
				for (c = newX; c < newX+newWidth; c++){
					if(c>=LEVEL_WIDTH || r>=LEVEL_HEIGHT) break;
					REGION_MAP[r][c] = 1;
				}
			}
			//Update pointers again
			x = c;
			y = r;
			previousPlatformWidth = newWidth;
		}

		//Always generate end platform
		for (r = 0; r < LEVEL_HEIGHT; r++){
			for (c = LEVEL_WIDTH-5; c < LEVEL_WIDTH; c++){
				REGION_MAP[r][c] = r < START_HEIGHT ? 1 : 0;
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

		// RUn through and mark 'safe zones' needed to keep the level solvable


		// Fill in the non-safe areas with random static obstacles


		// Place enemys

		/*int pick; GameObject obstacle;
		for (int i = 0; i < 50; i++) {
			pick = rn.nextInt(obstacleHat.size());
			obstacle = obstacleBlueprints.get(obstacleHat.get(pick));
			if (obstacle instanceof Enemy) {
				objects.add(new Enemy((Enemy) obstacle, 10.0f + i*1.0f, 5.0f));
			}
		}*/

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
