package com.DramaCow.game;

import java.util.Set;

public class LevelTemplate{
	private final int[][] map;
	private final Set<LevelTemplateObject> objects;
	private int lx,ly,rx,ry;

	public LevelTemplate(int[][] map, Set<LevelTemplateObject> objects){
		this.map = map;
		this.objects = objects;

		//Calculate marker positions
		for(int r = 0; r < this.getHeight(); r++){
			for(int c = 0; c < this.getWidth(); c++){
				if(this.map[r][c] == 2) {
					this.ly = r; this.lx = c;
				}
				if(this.map[r][c] == 3) {
					this.ry = r; this.rx = c;
				}
			}
		}
	}

	public int[][] getMap(){
		return map;
	}

	public Set<LevelTemplateObject> getObjects(){
		return objects;
	}

	public int getWidth(){
		return map[0].length;
	}

	public int getHeight(){
		return map.length;
	}

	public int getRightX(){
		return rx;
	}

	public int getRightY(){
		return ry;
	}

	public int getLeftX(){
		return lx;
	}

	public int getLeftY(){
		return ly;
	}

}