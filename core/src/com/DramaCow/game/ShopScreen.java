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
	private List<CharSelButton> buttons;
	private GDXgame game;
	private Button toMenu;
	private Vector3 touchPoint;
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private BitmapFont current;
	private Texture currentTexture;
	private List<String> shopItems;
	private List<String> hiddenItems;
	private List<String> lockedItems;

	public ShopScreen(GDXgame game){
		float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
		TextureManager.loadTexture("default", "player.png");
		currentTexture = TextureManager.getTexture("default");
		buttons = new ArrayList<CharSelButton>();
		this.game = game;
		shopItems = new ArrayList<String>();           //Shop items to be loaded from xml files 
		hiddenItems = new ArrayList<String>();
		lockedItems = new ArrayList<String>();
		cam = new OrthographicCamera(16.0f * ((float) w/h), 16.0f);
		cam.position.set(cam.viewportWidth / 2.0f, cam.viewportHeight / 2.0f, 0.0f);
		cam.update();
		current = new BitmapFont();
		batch = new SpriteBatch();	
		touchPoint = new Vector3();
	}

	public void populateSet(){
		
		//hiddenItems.add("1");
		//lockedItems.add("default");
	}

	@Override
	public void show(){
		TextureManager.loadTexture("backgroundfs", "gameShopBackground.png");
		TextureManager.loadTexture("default", "player.png");
		TextureManager.loadTexture("hidden", "questionmark.png");
		TextureManager.loadTexture("1", "ex1.png");
		TextureManager.loadTexture("2", "ex2.png");
		TextureManager.loadTexture("shading", "shading.png");
		CharSelButton btn1 = new CharSelButton(new ShopCostume("default", TextureManager.getTexture("default"), 0.0f), 0.0f, 0.0f, 30.0f, 60.0f) {
			@Override
			public void onClick(){
				System.out.println("hey there");
				currentTexture = this.getTexture();
			}
		};
		CharSelButton btn2 = new CharSelButton(new ShopCostume("1", TextureManager.getTexture("1"), 0.0f), 0.0f, 40.0f, 30.0f, 60.0f){
			@Override
			public void onClick(){
				System.out.println("hey there1");
				currentTexture = this.getTexture();
			}
		};
		CharSelButton btn3 = new CharSelButton(new ShopCostume("2", TextureManager.getTexture("2"), 0.0f), 0.0f, 80.0f, 30.0f, 60.0f){
			@Override
			public void onClick(){
				System.out.println("hey there2");
				currentTexture = this.getTexture();
			}
		};
		buttons.add(btn1);
		buttons.add(btn2);
		buttons.add(btn3);
    	populateSet();
	}

	@Override
	public void update(float dt){
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		for(CharSelButton button : buttons){
			//System.out.println(touchPoint.x + " " + touchPoint.y);
			button.update(touchPoint.x, touchPoint.y, Gdx.input.isTouched());
		}

		
		

		//UPDATING CHARACTER SELECTION SHOULD HAPPEN HERE WHEN BUTTONS ARE INCORPORATED
	}

	@Override
	public void draw(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);       //Clearing frame
		batch.begin();
		if(Gdx.input.isTouched())System.out.println(touchPoint.x +" " + " " +touchPoint.y);
		batch.draw(TextureManager.getTexture("backgroundfs"), 0, 0, 480.0f * ((float) Gdx.graphics.getWidth()/Gdx.graphics.getHeight()), 480.0f); //Drawing Background
		batch.draw(currentTexture, 30, 180, 60, 120);
		current.draw(batch, "Your Current Selection", 20 ,320);
		Integer x = 300, y = 300;
		for(CharSelButton button: buttons){
			button.setX(x);
			button.setY(y);
			//System.out.println(button.getX() + " " + button.getY() + " " + button.getW() + " " +" " +button.getH());
			BitmapFont price = new BitmapFont();
			BitmapFont id = new BitmapFont();													//Looping through map, drawing costume textures, price and ID
			price.draw(batch, Float.toString(button.getShopCostume().getPrice()), x, y-15);
			id.draw(batch, button.getShopCostume().getId(), x, y+70);
			batch.draw(button.getShopCostume().getTexture(), button.getX(), button.getY(), button.getW(), button.getH());

			List<Integer> newLocs = updateLoc(x, y, button.getTexture());
			if(hiddenItems.contains(button.getShopCostume().getId())) {
				batch.draw(TextureManager.getTexture("hidden"), x, y, 30, 60);					//If item hidden draw hidden texture and update x and y
				x = newLocs.get(0);
				y = newLocs.get(1);	
				continue;
			}	
			
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
		TextureManager.disposeTexture("shading");
		TextureManager.disposeTexture("default");
		TextureManager.disposeTexture("1");
		TextureManager.disposeTexture("2");
		TextureManager.disposeTexture("hidden");
	}

}