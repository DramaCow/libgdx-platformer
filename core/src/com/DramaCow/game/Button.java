package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.DramaCow.maths.Rect;

public class Button {

	public static enum ButtonState {
		IDLE, 
		HOVER, 
		CLICK
	}

	private ButtonState state;
	private float x, y;
	private float w, h;

	private Rect bounds;

	private String texId;
	private Tileset tiles;

	boolean hasClicked; // Used to prevent repeated onClick calls when button is held

	public Button(String texId, int texw, int texh, float x, float y, float w, float h) {
		this.state = ButtonState.IDLE;
		this.x = x; this.y = y;
		this.w = w; this.h = h;

		this.bounds = new Rect(x,y,w,h);

		this.texId = texId;
		this.tiles = new Tileset(TextureManager.getTexture(texId), texw, texh);

		this.hasClicked = false;
	}

	//To be called in the update function of the screen class
	public final void update(float x, float y, boolean down) {
		if (hasClicked && !down) hasClicked = false;
		
		//If the mouse/touchscreen is on the button
		if (bounds.contains(x, y)) {
			//Check if touched or hovered
			if (down) {
				state = ButtonState.CLICK;
				if (!hasClicked) {
					hasClicked = true;
					onClick();
				}
			} 
			else {
				state = ButtonState.HOVER;
			}
		} 
		else {
			state = ButtonState.IDLE;
		}
	}

	public final TextureRegion getTexture() {
		switch(state) {
			case IDLE: 	return tiles.getTile(0);
			case HOVER: return tiles.getTile(1);
			case CLICK: return tiles.getTile(2);
		}
		return tiles.getTile(0);
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
