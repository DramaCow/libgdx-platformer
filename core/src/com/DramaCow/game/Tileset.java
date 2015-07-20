package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;
import java.util.ArrayList;

public class Tileset {
	
	public final int TILE_SIZE;

	private Texture tileset;
	private List<TextureRegion> tiles;

	public Tileset(Texture tileset, final int TILE_SIZE) {
		this.TILE_SIZE = TILE_SIZE;
		this.tileset = tileset;
		this.tiles = new ArrayList<TextureRegion>();

		int rowSize = tileset.getHeight() / TILE_SIZE;
		int columnSize = tileset.getWidth() / TILE_SIZE;

		for (int j = 0; j < rowSize; j++) {
			for (int i = 0; i < columnSize; i++) {
				tiles.add(new TextureRegion(tileset, i * TILE_SIZE, j * TILE_SIZE, 
					TILE_SIZE, TILE_SIZE));
			}
		}
	}

	public TextureRegion getTile(int i) {
		return tiles.get(i);
	}

	public int getNumberOfTiles() {
		return tiles.size();
	}
}
