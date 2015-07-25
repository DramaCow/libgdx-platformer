package com.DramaCow.game;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;


public class XReader {
	
	private XReader() {}

	public static XmlReader.Element parsing(String filename){
		XmlReader.Element root = null;
		try{
			XmlReader reader = new XmlReader();
			root = reader.parse(Gdx.files.internal(filename));
		}	
		catch(Exception e){
			System.out.println(e);
		}

		return root;
	}

	public static String getFileName(String filename, String levelname){
		XmlReader.Element root = parsing(filename);
		Array<Element> levels = root.getChildrenByName("levels");
		Array<Element> level = levels.first().getChildrenByName(levelname);
		return level.get(0).getAttribute("level_file");
	}

	public static Map<Integer, String> getLevels(String filename){
		Map<Integer, String> levelMap = new HashMap<Integer, String>();
		XmlReader.Element root = parsing(filename);
		XmlReader.Element levels = root.getChildByName("levels");
		for(int i = 0; i<levels.getChildCount(); i++){
			Integer levelNumber = Integer.parseInt(levels.getChild(i).getAttribute("level_number"));
			String levelName = levels.getChild(i).getName();
			levelMap.put(levelNumber, levelName);
		}

		return levelMap;
	}

	public static String getDefault(String filename){
		XmlReader.Element root = parsing(filename);
		Array<Element> defs = root.getChildrenByName("defaults");
		Element def = defs.first().getChildByName("default"); 
		return def.getAttribute("ID");
	}

	public static String getLevelSongName(String filename, String levelname){
		String levelFile = getFileName(filename, levelname);
		XmlReader.Element root = parsing(levelFile);
		String musicName = root.getChildByName("music").getText();
		return musicName;
	}

	public static String getLevelTileSet(String filename, String levelname){
		String levelFile = getFileName(filename, levelname);
		XmlReader.Element root = parsing(levelFile);
		String tileSetName = root.getChildByName("tileset").getText();
		return tileSetName;
	}

	public static Map<String, Enemy> getLevelEnemies(String filename, String level){
		Map<String, Enemy> enemies = new HashMap<String, Enemy>();
		String levelFile = getFileName(filename, level);
		XmlReader.Element root = parsing(levelFile);
		XmlReader.Element enemys = root.getChildByName("ENEMIES");
		int childCount = enemys.getChildCount();
		for(int i = 0;i<childCount;i++){
			String enemyID = enemys.getChild(i).getText();
			Enemy enemy = getEnemy("enemies.xml", enemyID);
			enemies.put(enemyID, enemy);
		}
		return enemies;
	}

	public static Enemy getEnemy(String enemyFile, String enemyID){
		XmlReader.Element root = parsing(enemyFile);
		XmlReader.Element enemy = root.getChildByName("ENEMY" + enemyID);
		String aiType = enemy.getChildByName("AI").getText();
		Integer diff = Integer.parseInt(enemy.getChildByName("DIFF").getText());
		Ai ai = Ai.getAI("test", diff);													//This code needs to be replaced when multiple ai's are created
		Texture enemySprite = new Texture(Gdx.files.internal("tempEnemy.png"));         //This code needs to be replaced when multiple enemy sprites are created
		float enemyWidth = enemySprite.getWidth();
		float enemyHeight = enemySprite.getHeight();
		enemySprite.dispose();
		return new Enemy(enemyID, 0, 0, enemyWidth/32, enemyHeight/32, ai);
	}

	public static String getEnemySprite(String enemyFile, String enemyID){
		XmlReader.Element root = parsing(enemyFile);
		XmlReader.Element enemy = root.getChildByName("ENEMY" + enemyID);
		String sheetName = enemy.getChildByName("SpriteSheet").getText();
		return "tempEnemy.png";
	}


}
