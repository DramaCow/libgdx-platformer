package com.DramaCow.game;

import com.badlogic.gdx.graphics.Texture;

public class ShopCostume{
	private String id;
	private Texture image;
	private int price;

	public ShopCostume(Texture image, int price){
		this.image = image;
		this.price = price;
		this.id = id;
	}
}