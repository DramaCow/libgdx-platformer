package com.DramaCow.game;

public class Coin extends GameObject{

	public Coin(float x, float y){
	 	super("coin", x, y, 1.0f, 1.0f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);	
	};
}
