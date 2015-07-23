package com.DramaCow.game;

import com.badlogic.gdx.utils.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;

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
		Set<String> enemyIds = new HashSet<String>();
		String levelFile = getFileName(filename, level);
		XmlReader.Element root = parsing(levelFile);
		XmlReader.Element enemys = root.getChildByName("ENEMIES");
		int childCount = enemys.getChildCount();
		for(int i = 0;i<childCount;i++){
			enemys.getChild(i).getText();
			System.out.println(enemys.getChild(i).getText());
		}
		

		return enemies;
	}
}
