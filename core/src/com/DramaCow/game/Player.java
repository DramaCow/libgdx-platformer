package com.DramaCow.game;

public class Player extends DynamicGameObject{

	static public enum PlayerState {
		RUN, JUMP, FALL, ATTACK, HURT, DEAD
	}

	public static final float HURT_DELAY = 1.0f;

	private final float RUN_SPEED = 8.0f;
	private final float HURT_SPEED = -1.0f;
	private final float JUMP_SPEED = 10.0f;

	private PlayerState state;
	private int health;

	public Player(String id, float x, float y, float w, float h){
		super(id,x,y,w,h);
		setState(PlayerState.RUN); 
	}

	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		switch (state){
			case RUN:
				velocity.x = RUN_SPEED;
				break;
			case JUMP:
				velocity.y = JUMP_SPEED;
				break;
			case FALL:
				break;
			case ATTACK:
				break;
			case HURT:
				// If enough time passed leave hurt state
				velocity.x = HURT_SPEED;
				if(t > HURT_DELAY) setState(PlayerState.RUN); 
				break;
			case DEAD:
				break;
		}
	}

	public void hurt(int damage){
		health -= damage;
		setState(PlayerState.HURT); 
	}

	public void heal(int amount){
		health += amount;
	}

	//Sets state a resets state timer
	public void setState (PlayerState s){
		state = s;
		t = 0;
	}

	public String getStateID(){
		switch (state){
			case RUN:
				return "Run";
			case JUMP:
				return "Jump";
			case FALL:
				return "Fall";
			case ATTACK:
				return "Attack";
			case HURT:
				return "Hurt";
			case DEAD:
				return "Dead";
			default:
				return "Run";
		}
	}
}
