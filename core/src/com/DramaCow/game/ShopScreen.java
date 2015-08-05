package com.DramaCow.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import com.badlogic.gdx.graphics.Texture;

public class ShopScreen implements Screen{
	private final GDXgame game;
	private List<ShopButton> buttons;
	private Button toMenu;
	private Vector3 touchPoint;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private BitmapFont current;
	private BitmapFont price;
	private	BitmapFont id;
	private String currentId = "default";
	private List<ShopCostume> shopItems;
	private List<String> hiddenItems;
	private List<String> lockedItems;

	public ShopScreen(final GDXgame game){
		this.game = game;
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		buttons = new ArrayList<ShopButton>();
		shopItems = XReader.getShopItems("xml/shop.xml");    
		hiddenItems = XReader.getHiddenItems("xml/shop.xml");   //Loading shop items, hidden and locked from xml files
		lockedItems = XReader.getLockedItems("xml/shop.xml");
		current = new BitmapFont();
		price = new BitmapFont();    //Various statements required to be printed
		id = new BitmapFont();
		batch = new SpriteBatch();	
		touchPoint = new Vector3();    //For updating buttons
	}

	@Override
	public void show(){
		TextureManager.loadTexture("default", "player.png");
		TextureManager.loadTexture("toMenu", "menucharacterset.png");
		TextureManager.loadTexture("backgroundfs", "gameShopBackground.png");
		TextureManager.loadTexture("hidden", "questionmark.png");
		TextureManager.loadTexture("shading", "shading.png");
		for(ShopCostume item : shopItems){
			if(hiddenItems.contains(item.getId())) item.setImage(TextureManager.getTexture("hidden"));
			ShopButton btn = new ShopButton(item, 0.0f, 0.0f, 30.0f, 60.0f){
				@Override
				public void onClick(){
					if(!(hiddenItems.contains(this.getShopCostume().getId()) || lockedItems.contains(this.getShopCostume().getId()))) currentId = this.getShopCostume().getId();
				}
			};
			buttons.add(btn);
		}
		
		toMenu = new Button("toMenu", 64, 32, 50, 50, 64, 64){
			@Override
			public void onClick(){
				game.setScreen(new MainMenuScreen(game));
			}
		};
	}

	@Override
	public void update(float dt){
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		for(ShopButton button : buttons){
			button.update(touchPoint.x, touchPoint.y, Gdx.input.isTouched());
		}
		toMenu.update(touchPoint.x, touchPoint.y, Gdx.input.isTouched());
	}

	@Override
	public void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);       //Clearing frame
		batch.begin();
		batch.draw(TextureManager.getTexture("backgroundfs"), 0, 0, 480.0f * ((float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight()), 480.0f); //Drawing Background
		batch.draw(TextureManager.getTexture(currentId), 30, 180, 60, 120);
		current.draw(batch, "Your Current Selection", 20 ,320);
		batch.draw(toMenu.getTexture(),toMenu.getX(),toMenu.getY(), toMenu.getW(), toMenu.getH());  //Drawing to menu button
		Integer x = 300, y = 300;
		for(ShopButton button: buttons){
			button.setX(x);
			button.setY(y);
			price.draw(batch, "£" + Float.toString(button.getShopCostume().getPrice()), x, y-15);   //Looping through set, drawing costume textures, price and ID
			id.draw(batch, button.getShopCostume().getId(), x, y+70);

			List<Integer> newLocs = updateLoc(x, y, button.getTexture());
			batch.draw(button.getShopCostume().getTexture(), button.getX(), button.getY(), button.getW(), button.getH());

			if(lockedItems.contains(button.getShopCostume().getId())) batch.draw(TextureManager.getTexture("shading"), x, y, 30, 60);     //If item locked shade it
			x = newLocs.get(0);
			y = newLocs.get(1);	
		}
		batch.end();
	}

	public List<Integer> updateLoc(Integer x, Integer y, Texture tex){
		List<Integer> newLocs = new ArrayList<Integer>();
		x = x + tex.getWidth()+ 30;
		if(x>=(int)Gdx.graphics.getWidth()-50) {
			x = 300;										//Updates x, y positions for costumes
			y = y - tex.getHeight()-70;
		}
		newLocs.add(x);
		newLocs.add(y);	
		return newLocs;
	}

	public String getSelectedId(){
		return currentId;
	}

	@Override
	public void resize(int w, int h){
		cam.viewportWidth = 480.0f * ((float) w/h);
		cam.viewportHeight = 480.0f;
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
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
		TextureManager.disposeTexture("shading");
		TextureManager.disposeTexture("hidden");
		TextureManager.disposeTexture("toMenu");
		TextureManager.disposeTexture("backgroundfs");
	}

}