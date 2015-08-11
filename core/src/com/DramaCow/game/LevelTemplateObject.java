package com.DramaCow.game;

public class LevelTemplateObject {
	public final String name;
	public final float probability;
	public final float x;
	public final float y;
	public final float gdirx;
	public final float gdiry;

	public LevelTemplateObject(String name, float x, float y, float probability, float gdirx, float gdiry){
		this.name = name;
		this.x = x;
		this.y = y;
		this.probability = probability;
		this.gdirx = gdirx;
		this.gdiry = gdiry;
	}
}