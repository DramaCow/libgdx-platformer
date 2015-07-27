package com.DramaCow.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ShopScreen implements Screen{
	private ShopCostume currentCostume;
	private Map<String, ShopCostume> itemIDs;
	private List<String> shopItems;
	private List<String> hiddenItems;
	private List<String> lockedItems;

	public ShopScreen(){
		itemIDs = new HashMap<String, ShopCostume>();
		shopItems = new ArrayList<String>();           //Shop items to be loaded from xml files 
		hiddenItems = new ArrayList<String>();
		lockedItems = new ArrayList<String>();
	}

	public void show(){

	}
	public void update(float dt){

	}
	public void draw(){

	}
	public void resize(int w, int h){

	}
	public void pause(){

	}
	public void resume(){

	}
	public void hide(){

	}
	public void dispose(){

	}

}