package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Map;
import java.util.HashMap;

// Static class
// This class is needed for dynamic loading of textures from files
public class TextureManager {

	// Private constructor prevents instantiation of the class
	private TextureManager() {}

	private static Map<String, Texture> textures = new HashMap<String, Texture>();

	public static boolean loadTexture(String texId, String texture) {
		if (textures.containsKey(texId)) {
			return false;
		}
		textures.put(texId, new Texture(Gdx.files.internal(texture)));
		return true;
	}

	public static boolean disposeTexture(String texId) {
		Texture tex = getTexture(texId);
		if (tex == null) return false;
		tex.dispose();
		textures.remove(texId);
		return true;
	}
	
	public static Texture getTexture(String texId) {
		return textures.containsKey(texId) ? textures.get(texId) : null;
	}

	// Are these functions necessary?
	public static int getTextureWidth(String texId) {
		Texture tex = getTexture(texId);
		return tex != null ? tex.getWidth() : 0;
	}

	public static int getTextureHeight(String texId) {
		Texture tex = getTexture(texId);
		return tex != null ? tex.getHeight() : 0;
	}
}
