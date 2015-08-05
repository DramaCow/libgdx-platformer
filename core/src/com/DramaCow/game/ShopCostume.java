package com.DramaCow.game;

import com.badlogic.gdx.graphics.Texture;

public class ShopCostume{
	private String id;
	private Texture image;
	private float price;

	public ShopCostume(String id, Texture image, float price){
		this.id = id;
		this.image = image;
		this.price = price;
	}

	public Texture getTexture(){
		return image;
	}

	public float getPrice(){
		return price;
	}

	public String getId(){
		return id;
	}

	public void setImage(Texture image){
		this.image = image;
	}

}