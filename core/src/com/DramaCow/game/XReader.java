package com.DramaCow.game;

import java.util.Iterator;
import com.badlogic.gdx.utils.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.files.FileHandle;



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

	/*public static Map<Integer, String> getMap(String filename){
		Map<Integer, String> map = new HashMap<Integer, String>();
		XmlReader.Element root = parsing(filename);
		Array<Element> levels = root.getChildrenByName("levels");

		System.out.println(levels.first().getChildCount());
		for(int i = 0; i<levels.first().getChildCount(); i++){
     		XmlReader.Element level_element = levels.first().getChild(i);
     		Integer level_number = Integer.parseInt(level_element.getAttribute("level_number"));
     		String level_name = level_element.getName();
     		System.out.println(level_name);
     		System.out.println(level_number);
     		map.put(level_number, level_name);
 		}

		return map;
	}*/

	public static String getFileName(String filename, String levelname){
		XmlReader.Element root = parsing(filename);
		Array<Element> levels = root.getChildrenByName("levels");
		Array<Element> level = levels.first().getChildrenByName(levelname);
		return level.get(0).getAttribute("level_file");
	}



}