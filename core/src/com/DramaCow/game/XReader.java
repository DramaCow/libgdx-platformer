package com.DramaCow.game;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.files.FileHandle;



public class XReader {
	
	private XReader() {}

	public static void parsing(String filename){
		try{
			XmlReader reader = new XmlReader();
			System.out.println(Gdx.files.internal(filename));
			XmlReader.Element e = reader.parse(Gdx.files.internal(filename));

			System.out.println(e.getName());
		}	
		catch(Exception e){
			System.out.println(e);
		}
	}

}