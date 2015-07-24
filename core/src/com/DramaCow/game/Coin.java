package com.DramaCow.game;

public class Coin extends GameObject{

	public Coin(int x, int y){
	 	super("Coin", x, y, 1.0f, 1.0f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);	
	};
}
