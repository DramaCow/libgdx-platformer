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
	private List<LevelTemplateObject> templateObjects;
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
		//Get test template and put them in a list
		LevelTemplate template1 = XReader.getLevelTemplate("tmx/testTemplate1.tmx");
		LevelTemplate template2 = XReader.getLevelTemplate("tmx/testTemplate2.tmx");
		LevelTemplate template3 = XReader.getLevelTemplate("tmx/testTemplate3.tmx");
		List<LevelTemplate> levelTemplates = new ArrayList();
		levelTemplates.add(template1);
		levelTemplates.add(template2);
		levelTemplates.add(template3);

		templateObjects = new ArrayList<LevelTemplateObject>();

		Random rn = new Random();
		float jumpSpeed = player.JUMP_SPEED;
		float runSpeed = player.RUN_SPEED;
		float gravity = -9.81f;
		float maxJumpHeight = (-jumpSpeed*jumpSpeed)/(2*gravity);

		final int MAX_HEIGHT = 10;
		final int MIN_HEIGHT = 5;
		final int START_HEIGHT = 5;
		final int START_WIDTH = 8;

		//rx and ry are keep track of the position of the right marker of a template
		int rx = 0, ry = 0;

		//Always generate first platform at height 3.0f
		for (int r = 0; r < START_HEIGHT; r++){
			for (int c = 0; c < START_WIDTH; c++){
				REGION_MAP[r][c] = 1;
			}
		}
		//Set pointers
		rx = START_WIDTH;
		ry = START_HEIGHT;

		//Place templates in level
		while(rx < LEVEL_WIDTH){
			//Generate new height of template
			int newY = rn.nextInt((int)(maxJumpHeight + ry) - MIN_HEIGHT) + MIN_HEIGHT;
			if(newY > MAX_HEIGHT) newY = MAX_HEIGHT;
			int heightDifference = newY - ry;
			//Work out time to reach that height
			float timeToReachHeight = (float)((-jumpSpeed-Math.sqrt((double)(jumpSpeed*jumpSpeed+2*gravity*heightDifference)))/gravity);
			//Work out max and min distance to new template
			int maxDistance = (int)(runSpeed * timeToReachHeight);
			int minDistance = 1;
			//Generate new x of template
			int newX = rx + rn.nextInt(maxDistance - minDistance) + minDistance;

			//Pick a random template to place
			LevelTemplate template = levelTemplates.get(rn.nextInt(levelTemplates.size()));
			//Find left marker and work out template coordinates
			int lx = 0, ly = 0;
			for(int r = 0; r < template.getHeight(); r++){
				for(int c = 0; c < template.getWidth(); c++){
					if(template.getMap()[r][c] == 2){
						lx = c;
						ly = r;
					}
				}
			}
			int templateX = newX - lx;
			int templateY = newY - ly;

			//Place platform in array
			for(int r = templateY; r < templateY + template.getHeight(); r++){
				for(int c = templateX; c < templateX + template.getWidth(); c++){
					if(c>=LEVEL_WIDTH || r>=LEVEL_HEIGHT || c<0 || r<0) break;
					REGION_MAP[r][c] = template.getMap()[r - templateY][c - templateX];
				}
			}
			//Join the bottom row to the ground
			for(int r = 0; r < templateY; r++){
				for(int c = templateX; c < templateX + template.getWidth(); c++){
					if(c>=LEVEL_WIDTH || r>=LEVEL_HEIGHT || c<0 || r<0) break;
					REGION_MAP[r][c] = template.getMap()[0][c - templateX];
				}
			}

			//Update right marker
			for(int r = 0; r < template.getHeight(); r++){
				for(int c = 0; c < template.getWidth(); c++){
					if(template.getMap()[r][c] == 3){
						rx = templateX + c;
						ry = templateY + r;
					}
				}
			}

			//Update positions of the objects for the template and add them to the list
			Set<LevelTemplateObject> currentTemplateObjects = template.getObjects();
			for(LevelTemplateObject object : currentTemplateObjects){
				object.transpose(templateX, templateY);
				templateObjects.add(object);
			}
		}

		//Always generate end platform
		for (int r = 0; r < LEVEL_HEIGHT; r++){
			for (int c = LEVEL_WIDTH-5; c < LEVEL_WIDTH; c++){
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
