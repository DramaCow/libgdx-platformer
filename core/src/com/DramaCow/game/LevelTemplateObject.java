package com.DramaCow.game;

public class LevelTemplateObject {
	private final String name;
	private final float probability;
	private float x;
	private float y;

	public LevelTemplateObject(String name, float x, float y, float probability){
		this.name = name;
		this.x = x;
		this.y = y;
		this.probability = probability;
	}

	public void transpose(float x, float y){
		this.x += x;
		this.y += y;
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