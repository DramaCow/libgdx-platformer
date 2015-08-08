	package com.DramaCow.game;

import com.DramaCow.maths.Vector2D;
import com.DramaCow.maths.Rect;

public class Player extends DynamicGameObject{

	static public enum PlayerState {
		RUN, JUMP, FALL, ATTACK, HURT, DEAD
	}
	
	// Used to toggle whether or not the player can interact with the level they are in
	private boolean exists = true;

	public static final float HURT_DELAY = 1.0f;
	private float hurtTimer = 0.0f;
	private boolean hurtTrigger = false;

	private final float MAX_RUN_SPEED = Player.getMaxRunSpeed(); // Could vary by level
	private final float JUMP_SPEED = Player.getJumpSpeed();
	private final Vector2D ACCELERATION = new Vector2D(10.0f, 0.0f);

	private final float gy;

	private PlayerState state;
	private int health;

	public volatile boolean up = false;
	public volatile boolean left = false;
	public volatile boolean down = false;
	public volatile boolean right = false;

	public Player(String id, float x, float y, float w, float h, final Level level){
		super(id,x,y,w,h,level);
		collidable = true;
		setState(PlayerState.RUN);
		gy = level.getGravity().y; 
	}

	@Override
	public void update(float dt) {
		if (!exists) {
			acceleration.x 	= 0.0f;
			acceleration.y 	= 0.0f;
			velocity.x 	   	= MAX_RUN_SPEED; 
			velocity.y 	   	= (level.LEVEL_HEIGHT/2 - bounds.w/2 - position.y) * 5;

			super.update(dt);
			return;
		}
	
		acceleration.x 	= 0.0f;
		acceleration.y 	= gy; //grounded ? 0.0f : gy;
		//velocity.y 	   	= grounded ? 0.0f : velocity.y;

		updateState(dt);

		// Hurt can occur in any state but hurt and death
		if (hurtTrigger) { 
			hurtTimer = 0.0f; 
			hurtTrigger = false;

			velocity.x *= -1.0; 

			// Hurt damage dependant on speed
			if (velocity.x <= -20.0f) hurt(2);
			else hurt(1);
		}

		// Cap the velocity
		velocity.x = velocity.x > MAX_RUN_SPEED ? MAX_RUN_SPEED : velocity.x;

		super.update(dt);	
	}

	public void printbools() {
		System.out.println(acceleration);
		System.out.println(velocity);
		System.out.println(position);
	}

	private void updateState(float dt) {
		switch (state){
			case RUN:
				acceleration.x += ACCELERATION.x;
				if (up && grounded) { 
					velocity.y += JUMP_SPEED; 
					setState(PlayerState.JUMP); 
				}
				break;

			case JUMP:
				acceleration.x += ACCELERATION.x;
				if (up) acceleration.y += ACCELERATION.y;
				if (velocity.y <= 0.0f) state = PlayerState.FALL;
				break;

			case FALL:
				acceleration.x += ACCELERATION.x;
				if (grounded) state = PlayerState.RUN;
				break;

			case ATTACK:
				break;

			case HURT:
				acceleration.x += ACCELERATION.x;

				hurtTimer += dt;

				if(hurtTimer > HURT_DELAY || velocity.x >= 0.0f) {
					setState(PlayerState.RUN); 
				}
				break;

			case DEAD:
				break;
		}
	}

	@Override
	protected void eastCollision(boolean touching) {
		if (state != PlayerState.HURT && velocity.x >= 10.0f) {
			hurtTrigger = touching;
		}
	}	

	public void toggleExistence(boolean exists) {
		this.exists = exists;
		this.collidable = exists;
		// toggle scoring
	}

	public Rect bounds() {
		return new Rect(bounds);
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

	public static String getStateID(PlayerState state){
		switch (state){
			case RUN:
				return "run";
			case JUMP:
				return "jump";
			case FALL:
				return "fall";
			case ATTACK:
				return "attack";
			case HURT:
				return "hurt";
			case DEAD:
				return "dead";
			default:
				return "run";
		}
	}

	public String getStateID(){
		return getStateID(state);
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public static float getJumpSpeed() {
		return 16.0f;
	}

	public static float getMaxRunSpeed() {
		return 12.0f;
	}
}
