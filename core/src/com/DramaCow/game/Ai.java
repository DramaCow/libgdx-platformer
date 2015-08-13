package com.DramaCow.game;

import java.lang.Math;

abstract public class Ai {

	protected int difficulty;

	abstract public String ID();
	abstract public void update(Enemy enemy, float dt);
	abstract public void create(Enemy enemy);

	public static Ai getAI(String aiType, int difficulty){
		// Josh, you must use equals() to test string value rather than reference value -Sam
		// Thank you Sam, that was very silly of me! I will remember now - Josh
		if 		(aiType.equals(LinearAI.ID))	return new LinearAI(difficulty);
		else if (aiType.equals(WaveAI.ID))		return new WaveAI(difficulty);
		else if (aiType.equals(StaticAI.ID))	return new StaticAI(difficulty);

		return null;
	}

	// Test AIs
	public static class LinearAI extends Ai {
		public LinearAI(int difficulty){
			this.difficulty = difficulty;
		}
		
		@Override
		public String ID() { return ID; }
		public static String ID = "linear";

		@Override
		public void create(Enemy enemy) {
			enemy.getVelocity().x = -1.0f * difficulty;
		}

		@Override
		public void update(Enemy enemy, float deltaTime){
			enemy.getVelocity().x = -1.0f * difficulty;
		};
	}

	// EXPERIMENTAL - Do not function precisely due to loss of accuracy using floating-point values
	// PERFORMANCE HEAVY
	public static class WaveAI extends Ai {
		float t = 0.0f;	   	// time
		final double A; 	// amplitude of displacement	
		final double T; 	// period of wave
		final double B;		// amplitude of velocity

		public WaveAI(int difficulty){
			this.difficulty = difficulty;

			A = 3.0 + ((float) difficulty/10);
			T = difficulty < 125 ? 1.5f - ((float) difficulty/100) : 0.25f; // Have at least 4 frames per wave
			B = (2 * Math.PI * A) / T; // where T * sin(pi/2) == T
		}

		@Override
		public String ID() { return ID; }
		public static String ID = "wave";

		@Override
		public void create(Enemy enemy){
			enemy.velocity.x = -1.0f * difficulty;
		}

		@Override
		public void update(Enemy enemy, float dt){
			enemy.velocity.y = (float)( B * Math.cos((2 * Math.PI * t)/T) );
			t = (float)( (t + dt) % T );
		};
	}

	// Used for objects such as spikes
	public static class StaticAI extends Ai {
		public StaticAI(int difficulty){
			this.difficulty = difficulty;
		}
		
		@Override
		public String ID() { return ID; }
		public static String ID = "static";

		@Override
		public void create(Enemy enemy) {}

		@Override
		public void update(Enemy enemy, float deltaTime){};
	}
}
