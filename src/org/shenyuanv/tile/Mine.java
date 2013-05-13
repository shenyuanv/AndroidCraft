package org.shenyuanv.tile;

import java.lang.reflect.Constructor;

import org.shenyuanv.core.GridMap;
import org.shenyuanv.core.GridMapRender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

public class Mine extends AbstractTile{
	
	Bitmap currentBitmap;
	int width;
	int height;
	
	public Mine(Bitmap Bitmap,int id) {
		super(id);
		this.currentBitmap = Bitmap;
		width = currentBitmap.getWidth();
		height =currentBitmap.getHeight(); 
	}

	public void draw(Canvas g,int offsetX,int offsetY) {
		
		int x = Math.round(this.x-offsetX);
		int y = Math.round(this.y-offsetY);
		
		if(isSelected()){
			Paint paint = new Paint();
			paint.setColor(Color.YELLOW);
			//g.drawArc(x-Math.round(width*0.27f), y , Math.round(width*1.5f), height , 0, 360);
			//g.drawArc(x-Math.round(width*0.18f), y+10 , Math.round(width*1.3f), Math.round(height*0.7f) , 0, 360);
			RectF oval = new RectF(x-Math.round(width*0.18f), y+10 , Math.round(width*1.3f), Math.round(height*0.7f));
			g.drawArc(oval, 0, 360, true, paint);
			 
		}
		g.drawBitmap(currentBitmap, x, y, new Paint());
	}
	
	public Tile clone(int x, int y, GridMap gridMap) {
		Constructor constructor = getClass().getConstructors()[0];
		try {
			Mine mine = (Mine) constructor.newInstance(new Object[]{currentBitmap,id});
			mine.x = x* GridMapRender.TILE_WIDTH;
			mine.y = y* GridMapRender.TILE_HEIGHT;
			mine.tileX = x;
			mine.tileY = y;
			 
			return mine;
		} catch (Exception e) {
			e.printStackTrace();
	 
		}
		return null;
	}
	private static final float MINE_SPEED = 0.0008f;
	private float mine;
	public boolean mining(long elapsedTime) {
		mine = mine += elapsedTime*MINE_SPEED;
		return mine>=1;
	}
	
	public void reset(){
		mine=0;
	}
	
	private final static Point SIZE = new Point(1,1);
	public Point getSize(){
		return SIZE;
	}
	
	public int getType() {
		return -1;
	}

	public float getDefence() {
		// TODO Auto-generated method stub
		return 0;
	}

}
