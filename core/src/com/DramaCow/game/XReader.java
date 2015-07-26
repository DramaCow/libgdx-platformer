package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class XReader {
	
	private XReader() {}

	public static XmlReader.Element getRoot(String filename) {
		XmlReader.Element root = null;
		try{
			XmlReader reader = new XmlReader();
			root = reader.parse(Gdx.files.internal(filename));
		}	
		catch(Exception e) {
			System.out.println(e);
		}

		return root;
	}

	// Level master parser
	public static String getLoadingScreen(String filename) {
		return getRoot(filename).getChildByName("default").getAttribute("loading");
	}

	public static String getFilenameOfLevel(String filename, String levelname) {
		return getRoot(filename).getChildByName("levels")
			.getChildByName(levelname).getAttribute("level_file");
	}

	public static Map<Integer, String> getAllLevels(String filename) {
		XmlReader.Element levels = getRoot(filename).getChildByName("levels");
	
		Map<Integer, String> levelMap = new HashMap<Integer, String>();
		for(int i = 0; i < levels.getChildCount(); i++) {
			levelMap.put(levels.getChild(i).getIntAttribute("level_number"), levels.getChild(i).getName());
		}
		return levelMap;
	}

	public static String getDefaultLevel(String filename) {
		return getRoot(filename).getChildByName("default").getAttribute("id");
	}

	// Level parser
	public static String getLevelTileset(String filename) {
		return getRoot(filename).getChildByName("assets").getAttribute("tileset");
	}

	public static String getLevelBackground(String filename) {
		return getRoot(filename).getChildByName("assets").getAttribute("background");
	}

	public static String getLevelBGM(String filename) {
		return getRoot(filename).getChildByName("assets").getAttribute("bgm");
	}

	public static Map<String, GameObject> getLevelObstacles(String filename) {
		XmlReader.Element node = getRoot(filename).getChildByName("obstacles");

		Map<String, GameObject> obstacles = new HashMap<String, GameObject>();
		for(int i = 0; i < node.getChildCount(); i++) {
			String obstacleId = node.getChild(i).getAttribute("id");
			obstacles.put(obstacleId, getObstacleAttributes(Terms.OBSTACLES, obstacleId));
		}
		return obstacles;
	}

	// Enemy parser
	public static void loadObstacleAssets(String filename, String obstacleId) {
		// Load assets into memory (uses obstacleId for id in all managers)
		XmlReader.Element assets = getRoot(filename).getChildByName(obstacleId).getChildByName("assets");

		TextureManager.loadTexture(obstacleId, assets.getAttribute("spritesheet"));
		AnimationManager.loadAnimation(obstacleId, new Animation(assets.getFloatAttribute("animspeed"),
			(new Tileset(TextureManager.getTexture(obstacleId), assets.getIntAttribute("width"), 
						 assets.getIntAttribute("height"))).getTiles()));
		//SoundManager.loadSound(obstacleId, assets.getAttribute("sound"));
	}

	public static GameObject getObstacleAttributes(String filename, String obstacleId) {
		XmlReader.Element attributes = getRoot(filename).getChildByName(obstacleId).getChildByName("attributes");

		return new Enemy(obstacleId, 0.0f, 0.0f, attributes.getFloatAttribute("width"), attributes.getFloatAttribute("height"),
			Ai.getAI(attributes.getAttribute("ai"), attributes.getIntAttribute("difficulty")));
	}
}
