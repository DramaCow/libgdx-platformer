package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class CharSelButton {

	private float x, y;
	private float w, h;

	private Rectangle bounds;
	private ShopCostume costume;

	boolean hasClicked; // Used to prevent repeated onClick calls when button is held

	public CharSelButton(ShopCostume costume, float x, float y, float w, float h) {
		this.x = x; this.y = y;
		this.w = w; this.h = h;
		this.costume = costume;
		this.bounds = new Rectangle(x,y,w,h);
		this.hasClicked = false;
	}

	//To be called in the update function of the screen class
	public final void update(float x, float y, boolean down) {
		if (hasClicked && !down) hasClicked = false;
		if (bounds.contains(x, y) && down && !hasClicked) {
				hasClicked = true;
				onClick();
		}	
	} 
			

		


	public final Texture getTexture() {
		return costume.getTexture();
	}

	public ShopCostume getShopCostume(){
		return costume;
	}

	// To be overidden
	public void onClick() {}

	public final float getX() {
		return x;
	}

	public final float getY() {
		return y;
	}
	
	public final float getW() {
		return w;
	}

	public final float getH() {
		return h;
	}

	public final void setX(float x) {
		this.x = x;
		bounds.setPosition(x, this.y);
	}

	public final void setY(float y) {
		this.y = y;
		bounds.setPosition(this.x,y);
	}
}
