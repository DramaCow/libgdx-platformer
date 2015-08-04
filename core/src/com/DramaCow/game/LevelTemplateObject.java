package com.DramaCow.game;

public class LevelTemplateObject {
	private final String name;
	private final float probability;
	private final float x;
	private final float y;

	public LevelTemplateObject(String name, float x, float y, float probability){
		this.name = name;
		this.x = x;
		this.y = y;
		this.probability = probability;
	}

	public String getName(){
		return name;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public float getProbability(){
		return probability;
	}
}