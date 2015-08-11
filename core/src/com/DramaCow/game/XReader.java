package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.io.File;

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
		return getRoot(filename).getChildByName("assets").getAttribute("loading");
	}

	public static String getBorderLeft(String filename){
		return getRoot(filename).getChildByName("assets").getAttribute("borderLeft");
	}

	public static String getBorderRight(String filename){
		return getRoot(filename).getChildByName("assets").getAttribute("borderRight");
	}

	public static String getHeartGraphic(String filename){
		return getRoot(filename).getChildByName("assets").getAttribute("heart");
	}

	public static String getCoinGraphic(String filename){
		return getRoot(filename).getChildByName("assets").getAttribute("coin");
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

	public static List<Background> getLevelBackgrounds(String filename) {
		List<Background> bgs = new ArrayList<Background>();
		XmlReader.Element node = getRoot(filename).getChildByName("backgrounds");
		for(int i = 0; i < node.getChildCount(); i++){
			if(TextureManager.loadTexture(node.getChild(i).getAttribute("id"),node.getChild(i).getAttribute("imgfile"))){
				bgs.add(new Background(node.getChild(i).getAttribute("id"), node.getChild(i).getFloatAttribute("p")));
			}
		}
		return bgs;
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

	public static List<String> getTemplateFolders(String filename){
		XmlReader.Element node = getRoot(filename).getChildByName("templateFolders");

		List<String> folders = new ArrayList<String>();
		for(int i = 0; i < node.getChildCount(); i++) {
			folders.add(node.getChild(i).getAttribute("path"));
		}
		return folders;
	}

	// Level template parser
	public static List<LevelTemplate> getLevelTemplates(String folderString){
		List<LevelTemplate> levelTemplates = new ArrayList<LevelTemplate>();
		File folder = new File(folderString);
		for (File fileEntry : folder.listFiles()) {
			String fileName = fileEntry.getName();
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if(extension.equals("tmx")) levelTemplates.add(XReader.getLevelTemplate(fileEntry.getAbsolutePath()));
    	}
    	return levelTemplates;	
	}

	public static LevelTemplate getLevelTemplate(String templateFile){
		int[][] map = getTemplateMap(templateFile);
		int height = map.length;
		return new LevelTemplate(map, getTemplateObjects(templateFile, height));
	}

	public static int[][] getTemplateMap(String templateFile) {
		int width = getRoot(templateFile).getIntAttribute("width");
		int height = getRoot(templateFile).getIntAttribute("height");
		XmlReader.Element node = getRoot(templateFile).getChildByName("layer").getChildByName("data");
		int[][] templateMap = new int[height][width];
		int i = 0;
		for(int r = height - 1; r >= 0; r--){
			for(int c = 0; c < width; c++){
				templateMap[r][c] = node.getChild(i).getIntAttribute("gid");
				i++;
			}
		}
		return templateMap;
	}

	public static Set<LevelTemplateObject> getTemplateObjects(String templateFile, int height){
		Set<LevelTemplateObject> objects = new HashSet<LevelTemplateObject>();
		XmlReader.Element node = getRoot(templateFile).getChildByName("objectgroup");
		for(int i = 0; i < node.getChildCount(); i++){
			//get the properties
			XmlReader.Element properties = node.getChild(i).getChildByName("properties");
			float probability = 0.0f, gdirx = 0.0f, gdiry = 0.0f;
			for(int j = 0; j < properties.getChildCount(); j++){
				if(properties.getChild(j).get("name").equals("probability")) 
					probability = properties.getChild(j).getFloatAttribute("value");
				if(properties.getChild(j).get("name").equals("g-dir-x")) 
					gdirx = properties.getChild(j).getFloatAttribute("value");
				if(properties.getChild(j).get("name").equals("g-dir-y")) 
					gdiry = properties.getChild(j).getFloatAttribute("value");
			}
			//create the object
			objects.add(new LevelTemplateObject(node.getChild(i).get("name"), 
				node.getChild(i).getFloatAttribute("x")/32.0f, height - node.getChild(i).getFloatAttribute("y")/32.0f, 
				probability, gdirx, gdiry));
		}
		return objects;
	}

}
