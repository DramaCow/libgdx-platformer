package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;

public class AnimationManager {

	private AnimationManager() {}

	public static Map<String, Animation> animations = new HashMap<String, Animation>();

	public static boolean loadAnimation(String id, Animation animation){
		if (animations.containsKey(id)) return false;
		animations.put(id, animation);
		return true;
	}

	public static Animation getAnimation(String id){
		if(animations.containsKey(id)) return animations.get(id);
		else return null;
	}
}
