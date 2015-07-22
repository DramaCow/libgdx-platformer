package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button {

	public static enum ButtonState {
		IDLE, 
		HOVER, 
		CLICK
	}

	private ButtonState state;
	private float x, y;
	private float w, h;

	private Rectangle bounds;

	private String texId;
	private Tileset tiles;

	public Button(String texId, int texw, int texh, float x, float y, float w, float h) {
		this.state = ButtonState.IDLE;
		this.x = x; this.y = y;
		this.w = w; this.h = h;

		this.bounds = new Rectangle(x,y,w,h);

		this.texId = texId;
		this.tiles = new Tileset(TextureManager.getTexture(texId), texw, texh);
	}

	//To be called in the update function of the screen class
	public void update(float x, float y, boolean down) {
		//If the mouse/touchscreen is on the button
		if(bounds.contains(x, y)) {
			//Check if touched or hovered
			if(down) {
				state = ButtonState.CLICK;
			} else {
				state = ButtonState.HOVER;
			}
		} 
		else {
			state = ButtonState.IDLE;
		}
	}

	public TextureRegion getTexture() {
		switch(state) {
			case IDLE: return tiles.getTile(0);
			case HOVER: return tiles.getTile(1);
			case CLICK: return tiles.getTile(2);
		}
		return tiles.getTile(0);
	}

	public Boolean isClicked() {
		return state == ButtonState.CLICK ? true : false;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}
}
