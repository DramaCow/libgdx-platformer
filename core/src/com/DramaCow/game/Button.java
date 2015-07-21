package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Button {

	public static enum State {
		IDLE, HOVER, CLICK
	}

	private String textureId;
	private Rectangle bounds;
	private OrthographicCamera cam;
	private State state;
	private	Vector3 touchPoint;
	private Tileset tiles;
	private int x;
	private int y;

	public Button(String textureId, int x, int y, int width, int height, OrthographicCamera cam){
		this.bounds = new Rectangle(x,y,width,height);
		this.textureId = textureId;
		this.cam = cam;
		this.state = State.IDLE;
		this.tiles = new Tileset(TextureManager.getTexture(textureId),width,height);
		this.x = x;
		this.y = y;
		this.touchPoint = new Vector3();
	}

	//To be called in the update function of the screen class
	public void update(){
		//Get mouse/ touch point
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		//If the mouse/touchscreen is on the button
		if(bounds.contains(touchPoint.x, touchPoint.y)){
			//Check if it was just clicked
			if(Gdx.input.justTouched()){		//CHECK IF THIS WORKS
				state = State.CLICK;
			} else {
				state = State.HOVER;
			}
		} else {
			state = State.IDLE;
		}
	}

	public TextureRegion getTexture(){
		switch(state){
			case IDLE: return tiles.getTile(0);
			case HOVER: return tiles.getTile(1);
			case CLICK: return tiles.getTile(2);
		}
		return tiles.getTile(0);
	}

	public Boolean isClicked(){
		return state == State.CLICK ? true : false;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
}
