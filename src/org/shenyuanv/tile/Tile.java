package org.shenyuanv.tile;

import org.shenyuanv.core.GridMap;

import android.graphics.Canvas;
import android.graphics.Point;

public interface Tile extends java.io.Serializable{

	public void focus();
	
	public void blur();
	
	public Tile clone(int x,int y,GridMap gridMap);
	
	public void draw(Canvas canvas,int offsetX,int offsetY);
	
	public void update(long elapsedTime);
	
	public int getTileX();
	
	public int getTileY();
	
	public int getType();
	
	public int getId();
	
	public Point getSize();
	
	public void setHealth(float health);	
	
	public float getHealth();	
	
	public boolean operate(int x,int y,String uuid );
	 
	public float getDefence();
	
	public void setUUID(String i);
	
	public String getUUID();
	
}
