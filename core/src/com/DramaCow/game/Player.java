package com.DramaCow.game;

import com.badlogic.gdx.math.Vector2;

public class Player extends DynamicGameObject{

	static public enum PlayerState {
		RUN, JUMP, FALL, ATTACK, HURT, DEAD
	}

	public static final float PLAYER_WIDTH = 32;
	public static final float PLAYER_HEIGHT = 32;

	private PlayerState state;
	private int health;
	private float timeInState;

	public Player(float x, float y){
		super(x,y,PLAYER_WIDTH,PLAYER_HEIGHT);
		state = PlayerState.RUN;
	}

	@Override
	public void update(float deltaTime){
		velocity.add(accel.x * deltaTime, accel.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x;
		bounds.y = position.y;
		timeInState += deltaTime;
	}

	public void hurt(int damage){
		health -= damage;
		state = PlayerState.HURT;
	}

	public void heal(int amount){
		health += amount;
	}

	private void setState (PlayerState s){
		state = s;
		timeInState = 0;
	}

	//CLASS IS WORK IN PROGRESS

}