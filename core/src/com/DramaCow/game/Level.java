package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.lang.Math;
import java.util.Collections;

import com.DramaCow.maths.Rect;
import com.DramaCow.maths.Vector2D;

public class Level {
	public final String BIOME_ID; 

	public final int LEVEL_WIDTH, LEVEL_HEIGHT;
	private final int[][] REGION_MAP;

	private Map<String, GameObject> obstacleBlueprints;
	private List<GameObject> objects;
	private List<LevelTemplateObject> templateObjects;
	private Player player;
	private Vector2D gravity = new Vector2D(0.0f,-50.0f);

	private boolean isReady;

	public static final int MAX_HEIGHT = 12;	//Max height of left markers
	public static final int MIN_HEIGHT = 3;		//Min height of left markers
	public static final int START_HEIGHT = 5;	//Height of start and end platform
	public static final int START_WIDTH = 8;	//Width of start platform
	public static final int END_WIDTH = 5;		//Width of end platform

	public Level(String biome, int w, int h) {
		this.BIOME_ID = biome;

		this.LEVEL_WIDTH = w; this.LEVEL_HEIGHT = h;
		this.REGION_MAP = new int[h][w];

		this.isReady = false;
	}

	private boolean generate() {
		//Get test template and put them in a list
		List<String> folders = XReader.getTemplateFolders(XReader.getFilenameOfLevel(Terms.LEVEL_MASTER, this.BIOME_ID));
		List<LevelTemplate> levelTemplates = new ArrayList<LevelTemplate>();
		for(String folder: folders){
			levelTemplates.addAll(XReader.getLevelTemplates(folder));
		}
		templateObjects = new ArrayList<LevelTemplateObject>();	

		//Make sure there are templates, otherwise just draw a flat surface
		if(levelTemplates.size() == 0){
			for (int r = 0; r < START_HEIGHT; r++){
				for (int c = 0; c < LEVEL_WIDTH; c++){
					REGION_MAP[r][c] = 1;
				}
			}

			return false;
		}

		Random rn = new Random();
		float jumpSpeed = Player.getJumpSpeed();
		float runSpeed = Player.getMaxRunSpeed();
		float gy = gravity.y;
		float maxJumpHeight = (-jumpSpeed*jumpSpeed)/(2*gy);

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
		while(rx < LEVEL_WIDTH - END_WIDTH){
			//Pick a random template to place, marking sure there is space
			LevelTemplate template = null;		Boolean selectingTemplate = true;
			int newX = 0, newY = 0;
			while(selectingTemplate){
				//Generate new height of template
				newY = rn.nextInt((int)(maxJumpHeight + ry) - MIN_HEIGHT) + MIN_HEIGHT;
				if(newY > MAX_HEIGHT) newY = MAX_HEIGHT;
				int heightDifference = newY - ry;
				//Work out time to reach that height
				float timeToReachHeight = (float)((-jumpSpeed-Math.sqrt(
					(double)(jumpSpeed*jumpSpeed+2*gy*heightDifference)))/gy);
				//Work out max and min distance to new template
				int maxDistance = (int)(runSpeed * timeToReachHeight);
				int minDistance = 1;
				//Generate new x of template
				newX = rx + rn.nextInt(maxDistance - minDistance) + minDistance;

				template = levelTemplates.get(rn.nextInt(levelTemplates.size()));
				//Ensure that the L and R markers are within MIN_HEIGHT and MAX_HEIGHT
				//And ensure that the whole template is within the level
				if(newY + template.getRightY() - template.getLeftY() <= MAX_HEIGHT 
					&& newY + template.getRightY() - template.getLeftY() >= MIN_HEIGHT
					&& newY - template.getLeftY() >= 0 
					&& newY + template.getHeight() - template.getLeftY() < LEVEL_HEIGHT) selectingTemplate = false;
			}	
			//Find left marker and work out template coordinates
			int templateX = newX - template.getLeftX();
			int templateY = newY - template.getLeftY();

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
			//Join the top row to the ceiling
			for(int r = templateY + template.getHeight() - 1; r < LEVEL_WIDTH; r++){
				for(int c = templateX; c < templateX + template.getWidth(); c++){
					if(c>=LEVEL_WIDTH || r>=LEVEL_HEIGHT || c<0 || r<0) break;
					REGION_MAP[r][c] = template.getMap()[template.getHeight() - 1][c - templateX];
				}
			}

			//Update right marker
			rx = templateX + template.getRightX();
			ry = templateY + template.getRightY();

			//Update positions of the objects for the template and add them to the list
			Set<LevelTemplateObject> currentTemplateObjects = template.getObjects();
			for(LevelTemplateObject object : currentTemplateObjects){
				if(object.getX() + templateX < LEVEL_WIDTH - END_WIDTH) templateObjects.add(new LevelTemplateObject(object.getName(),
					object.getX() + templateX, object.getY() + templateY, object.getProbability()));
			}
		}

		//Always generate end platform
		for (int r = 0; r < LEVEL_HEIGHT; r++){
			for (int c = LEVEL_WIDTH-END_WIDTH; c < LEVEL_WIDTH; c++){
				REGION_MAP[r][c] = r < START_HEIGHT ? 1 : 0;
			}
		}

		return true;
	}

	private boolean populate(final Level level) {
		// Proper population to be implemented here
		objects = new ArrayList<GameObject>();

		List<String> obstacleHat = new ArrayList<String>(getBlueprintIDs());

		if (obstacleHat.isEmpty()) return false;

		List<Enemy> staticEnemies = new ArrayList<Enemy>();
		List<Enemy> linearEnemies = new ArrayList<Enemy>();
		List<Enemy> waveEnemies = new ArrayList<Enemy>();
		// Sort enemies into AI types
		for(int i = 0; i < obstacleHat.size(); i++){
			GameObject obstacle = obstacleBlueprints.get(obstacleHat.get(i));
			if (obstacle instanceof Enemy) {
				if(((Enemy)obstacle).getAiID().equals("static")) staticEnemies.add((Enemy)obstacle);
				else if (((Enemy)obstacle).getAiID().equals("linear")) linearEnemies.add((Enemy)obstacle);
				else if (((Enemy)obstacle).getAiID().equals("wave")) waveEnemies.add((Enemy)obstacle);
			}
		}

		// Go through all template objects and covert them to game objects
		Random rn = new Random();

		for(LevelTemplateObject templateObject: templateObjects){
			if(rn.nextFloat() <= templateObject.getProbability()){
				String name = templateObject.getName();

				/*
				if(name.equals("coin")){
					objects.add(new Coin(templateObject.getX(), templateObject.getY()));
				}
				else if(name.equals("heart")){
					objects.add(new Heart(templateObject.getX(), templateObject.getY()));
				}*/
				if(name.equals("static")){
					if(staticEnemies.size() == 0) continue;
					objects.add(new Enemy(staticEnemies.get(rn.nextInt(staticEnemies.size())),
						templateObject.getX(), templateObject.getY(), this));
				}
				else if(name.equals("linear")){
					if(linearEnemies.size() == 0) continue;
					objects.add(new Enemy(linearEnemies.get(rn.nextInt(linearEnemies.size())),
						templateObject.getX(), templateObject.getY(), this));
				}
				else if(name.equals("wave")){
					if(waveEnemies.size() == 0) continue;
					objects.add(new Enemy(waveEnemies.get(rn.nextInt(waveEnemies.size())),
						templateObject.getX(), templateObject.getY(), this));
				}
			}
		}

		//Sort the objects by X position
		Collections.sort(objects);

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
			float w = 1.0f;
			float h = 1.5f;

			level.player = new Player("Player", 3.0f - cambounds.w, level.START_HEIGHT, w, h, level);
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
		//System.out.println(objects.size());
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if (bounds.overlaps(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
				//System.out.println("Bounds in");
				object.update(dt);
			}
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

	public Vector2D getGravity(){
		return gravity;
	}	
}
