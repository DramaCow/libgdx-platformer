package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Texture;

public class ShopScreen implements Screen{
	private GDXgame game;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private BitmapFont current;
	private String currentID = "default";
	private Map<String, ShopCostume> itemIDs;
	private List<String> shopItems;
	private List<String> hiddenItems;
	private List<String> lockedItems;
	private BitmapFont title;

	public ShopScreen(GDXgame game){
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		this.game = game;
		itemIDs = new HashMap<String, ShopCostume>();
		shopItems = new ArrayList<String>();           //Shop items to be loaded from xml files 
		hiddenItems = new ArrayList<String>();
		lockedItems = new ArrayList<String>();
		cam = new OrthographicCamera(10.0f * ((float) w/h), 10.0f);
		current = new BitmapFont();
		title = new BitmapFont();
		batch = new SpriteBatch();	
	
	}

	public void populateMap(Map<String, ShopCostume> itemIDs){
		itemIDs.put("default", new ShopCostume("default", TextureManager.getTexture("default"), 0.0f));
		itemIDs.put("1", new ShopCostume("1", TextureManager.getTexture("1"), 0.0f));
		itemIDs.put("2", new ShopCostume("2", TextureManager.getTexture("2"), 0.0f));
		hiddenItems.add("1");
	}

	@Override
	public void show(){
		TextureManager.loadTexture("default", "player.png");
		TextureManager.loadTexture("hidden", "questionmark.png");
		TextureManager.loadTexture("1", "ex1.png");
		TextureManager.loadTexture("2", "ex2.png");
    	populateMap(itemIDs);
	}

	@Override
	public void update(float dt){

	}

	@Override
	public void draw(){
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		batch.draw(itemIDs.get("default").getTexture(), 30, 180, 60, 120);
		title.draw(batch, "Character Selection", 250, 460);
		current.draw(batch, "Your Current Selection", 20 ,320);
		Integer x = 300, y = 300;
		for(Map.Entry<String, ShopCostume> entry : itemIDs.entrySet()){
			List<Integer> newLocs = updateLoc(x, y, entry.getValue().getTexture());
			if(hiddenItems.contains(entry.getValue().getId())) {
				batch.draw(TextureManager.getTexture("hidden"), x, y, 30, 60);
				x = newLocs.get(0);
				y = newLocs.get(1);	
				continue;
			}	
			BitmapFont price = new BitmapFont();
			BitmapFont id = new BitmapFont();
			price.draw(batch, Float.toString(entry.getValue().getPrice()), x, y-15);
			id.draw(batch, entry.getValue().getId(), x, y+70);
			batch.draw(entry.getValue().getTexture(), x, y, 30, 60);
			x = newLocs.get(0);
			y = newLocs.get(1);	
		}
		batch.end();
	}

	public List<Integer> updateLoc(Integer x, Integer y, Texture tex){
		List<Integer> newLocs = new ArrayList<Integer>();
		x = x + tex.getWidth()+ 30;
		if(x==(int)Gdx.graphics.getWidth()) {
			x = 300;
			y = y - tex.getHeight();
		}
		newLocs.add(x);
		newLocs.add(y);	
		return newLocs;
	}



	@Override
	public void resize(int w, int h){

	}

	@Override
	public void pause(){

	}

	@Override
	public void resume(){

	}

	@Override
	public void hide(){

	}

	@Override
	public void dispose(){
		TextureManager.disposeTexture("default");
		TextureManager.disposeTexture("1");
		TextureManager.disposeTexture("2");
		TextureManager.disposeTexture("hidden");
	}

}