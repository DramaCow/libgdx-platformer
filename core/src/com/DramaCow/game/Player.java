package com.DramaCow.game;

public class Player extends DynamicGameObject{

	static public enum PlayerState {
		RUN, JUMP, FALL, ATTACK, HURT, DEAD
	}

	public static final float PLAYER_WIDTH = 32.0f;
	public static final float PLAYER_HEIGHT = 48.0f;

	private PlayerState state;
	private int health;
	private float timeInState;

	public Player(float x, float y){
		super("Player",x,y,PLAYER_WIDTH,PLAYER_HEIGHT);
		setState(PlayerState.RUN); 
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		timeInState += deltaTime;

		// If enough time passed leave hurt state
		if(state == PlayerState.HURT && timeInState > 2.0f) setState(PlayerState.RUN); 
	}

	public void hurt(int damage){
		health -= damage;
		setState(PlayerState.HURT); 
	}

	public void heal(int amount){
		health += amount;
	}

	//Sets state a resets state timer
	private void setState (PlayerState s){
		state = s;
		timeInState = 0;
	}

	private float getTimeInState(){
		return timeInState;
	}

	//CLASS IS WORK IN PROGRESS

}