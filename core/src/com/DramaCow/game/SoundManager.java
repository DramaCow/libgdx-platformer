package com.DramaCow.game;

import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager{
	private SoundManager(){}

	public static Map<String, Music> musicMap = new HashMap<String, Music>();
	public static Map<String, Sound> soundMap = new HashMap<String, Sound>();

	public static boolean loadSound(String id, String fileName){
		if (soundMap.containsKey(id)) return false;
		soundMap.put(id, Gdx.audio.newSound(Gdx.files.internal(fileName)));
		return true;
	}

	public static boolean loadMusic(String id, String fileName){
		if(musicMap.containsKey(id)) return false;
		musicMap.put(id, Gdx.audio.newMusic(Gdx.files.internal(fileName)));
		return true;
	}

	public static Sound getSound(String id){
		if(soundMap.containsKey(id)) return soundMap.get(id);
		else return null;
	}

	public static Music getMusic(String id){
		if(musicMap.containsKey(id)) return musicMap.get(id);
		else return null;
	}

	public static boolean disposeMusic(String id){
		Music music = getMusic(id);
		if(music == null) return false;
		music.dispose();
		return true;
	}

	public static boolean disposeSound(String id){
		Sound sound = getSound(id);
		if(sound == null) return false;
		sound.dispose();
		return true;
	}

	public static boolean disposeAllMusic(){
		for(Music music : musicMap.values()){
			music.dispose();
		}
		return true;
	}

	public static boolean diposeAllSound(){
		for(Sound sound : soundMap.values()){
			sound.dispose();
		}
		return true;
	}


}