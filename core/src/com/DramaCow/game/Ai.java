package com.DramaCow.game;

abstract public class Ai {

	int difficulty;

	abstract public void update(Enemy enemy, float deltaTime);

	abstract public void create(Enemy enemy);

	public static Ai getAI(String aiType, int difficulty){
		if(aiType == "test") return new TestAi(difficulty);
		return null;
	}

	//Test AI
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

}