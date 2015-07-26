package com.DramaCow.game;

public interface Screen {
	public void show();
	public void update(float dt);
	public void draw();
	public void resize(int w, int h);
	public void pause();
	public void resume();
	public void hide();
	public void dispose();
}
