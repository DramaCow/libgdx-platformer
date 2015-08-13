package com.DramaCow.game;

public class Coin extends GameObject{

	public Coin(float x, float y){
	 	super("coin", x, y, 1.0f, 1.0f);
		//box.halfExtents.scalar(0.5f); // reduce the size of the bounding box
	}

	@Override
	public void update(float dt) {
		super.update(dt);	
	};
}
