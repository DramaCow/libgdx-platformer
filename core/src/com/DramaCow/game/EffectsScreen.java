package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class EffectsScreen implements Screen {

	private final GDXgame game;
	private final Screen current;
	private final Screen next;

	private List<ScreenEffect> effects = new ArrayList<ScreenEffect>();

	private EffectsScreen(GDXgame game, Screen current, Screen next, ScreenEffect... effects) {
		this.game = game;
		this.current = current;
		this.next = next;
		this.effects = new ArrayList<ScreenEffect>( Arrays.asList(effects) );
	}	

	public static Screen applyEffects(GDXgame game, Screen current, ScreenEffect... effects) {
		if (effects.length == 0) {
			return current;
		}
		return new EffectsScreen(game, current, null, effects);
	}

	public static Screen transition(GDXgame game, Screen current, Screen next, ScreenEffect... effects) {
		if (effects.length == 0) {
			return current;
		}
		return new EffectsScreen(game, current, next, effects);
	}

	@Override
	public void show() {
		// Do nothing
	}

	@Override
	public void update(float dt) {
		current.update(dt);

		if (!effects.isEmpty()) {
			effects.get(0).update(dt);

			if (effects.get(0).over()) {
				effects.remove(0);
				if (effects.isEmpty()) {
					setScreen();
				}
			}
		}
		else {
			setScreen();
		}
	}

	private void setScreen() {
		if (next != null) {
			System.out.println("called");
			game.setScreen(next);
		}
		else {
			System.out.println("called2");
			game.assignScreen(current);
		}
	}

	@Override
	public void	draw () {
		current.draw();
		effects.get(0).draw();
	}

	@Override
	public void resize(int w, int h) {
		current.resize(w, h);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		// Do nothing
		current.hide();
	}

	@Override
	public void dispose() {
		// Do nothing
	}
}
