package com.DramaCow.game;

import java.lang.Math;

abstract public class Ai {

	int difficulty;

	abstract public void update(Enemy enemy, float deltaTime);

	abstract public void create(Enemy enemy);

	public static Ai getAI(String aiType, int difficulty){
		if (aiType == "test") return new TestAi(difficulty);
		else if (aiType == "wave") return new WaveAI(difficulty);
		return null;
	}

	//Test AIs
	public static class TestAi extends Ai{
		public TestAi(int difficulty){
			this.difficulty = difficulty;
		}

		@Override
		public void create(Enemy enemy){
			enemy.velocity.x = -1.0f * difficulty;
		}

		@Override
		public void update(Enemy enemy, float deltaTime){};
	}

	// EXPERIMENTAL - Do not function precisely due to loss of accuracy using floating-point values
	// PERFORMANCE HEAVY
	public static class WaveAI extends Ai{
		float t = 0.0f;	   	// time
		final double A; 	// amplitude of displacement	
		final double T; 	// period of wave
		final double B;		// amplitude of velocity

		public WaveAI(int difficulty){
			this.difficulty = difficulty;

			A = 3.0 + ((float) difficulty/10);
			T = difficulty < 775 ? 8.0f - ((float) difficulty/100) : 0.25f; // Have at least 4 frames per wave
			B = (2 * Math.PI * A) / T; // where T * sin(pi/2) == T
		}

		@Override
		public void create(Enemy enemy){
			enemy.velocity.x = -0.1f * difficulty;
		}

		@Override
		public void update(Enemy enemy, float dt){
			enemy.velocity.y = (float)( B * Math.cos((2 * Math.PI * t)/T) );
			t = (float)( (t + dt) % T );
		};
	}
}
