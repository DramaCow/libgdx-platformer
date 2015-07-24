package com.DramaCow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;
import java.util.ArrayList;

public class Tileset {
	
	public final int TILE_X;
	public final int TILE_Y;

	private Texture tileset;
	private List<TextureRegion> tiles;

	public Tileset(Texture tileset, final int TILE_X, final int TILE_Y) {
		this.TILE_X = TILE_X;
		this.TILE_Y = TILE_Y;
		this.tileset = tileset;
		this.tiles = new ArrayList<TextureRegion>();

		int rowSize = tileset.getHeight() / TILE_Y;
		int columnSize = tileset.getWidth() / TILE_X;

		for (int j = 0; j < rowSize; j++) {
			for (int i = 0; i < columnSize; i++) {
				tiles.add(new TextureRegion(tileset, i * TILE_X, j * TILE_Y, 
					TILE_X, TILE_Y));
			}
		}
	}

	public TextureRegion getTile(int i) {
		return tiles.get(i);
	}

	public TextureRegion[] getTiles() {
		return tiles.toArray(new TextureRegion[tiles.size()]);
	}

	public int getNumberOfTiles() {
		return tiles.size();
	}
}
