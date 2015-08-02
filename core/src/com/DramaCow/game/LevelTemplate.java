package com.DramaCow.game;

import java.util.Set;

public class LevelTemplate{
	private final int[][] map;
	private final Set<LevelTemplateObject> objects;
	private final int width;
	private final int height;

	public LevelTemplate(int[][] map, Set<LevelTemplateObject> objects){
		this.map = map;
		this.objects = objects;
		this.width = map[0].length;
		this.height = map.length;
	}

	public int[][] getMap(){
		return map;
	}

	public Set<LevelTemplateObject> getObjects(){
		return objects;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}
}