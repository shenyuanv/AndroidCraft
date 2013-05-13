package org.shenyuanv.tile;

import java.lang.reflect.Constructor;

import org.shenyuanv.core.GridMap;
import org.shenyuanv.core.GridMapRender;
import org.shenyuanv.ui.icon.BaseIcon;
import org.shenyuanv.util.Resource;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class House extends AbstractTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Bitmap currentBitmap;
	
	protected Bitmap[] Bitmaps;
	
	protected int status = 0;
	
	protected BaseIcon icon;
	
	protected float complete;
	
	public House(Bitmap[] Bitmaps,int id) {
		super(id);
		this.Bitmaps = Bitmaps;
	}

	public void draw(Canvas g,int offsetX,int offsetY) {

		int x = Math.round(this.x-offsetX);
		int y = Math.round(this.y-offsetY);
		if(isSelected()){
		 //画选择后的圆圈以及血条
/*			if(getType()!=gridMap.getTileMapRender().getCurrentType()){
				g.setColor(Color.RED);
				g.drawArc(x-Math.round(getWidth()*0.27f), y , Math.round(getWidth()*1.5f), getHeight() , 0, 360);
			}else{
				g.setColor(Constant.GREEN);
				g.drawArc(x-Math.round(getWidth()*0.27f), y , Math.round(getWidth()*1.5f), getHeight() , 0, 360);
			}
			
			g.fillRect(x, y + getHeight()+5,  Math.round(getWidth()*(health)), 3);
			g.setColor(Color.black);
			g.drawRect(x, y + getHeight()+5, getWidth(), 3);*/
		}
		
		g.drawBitmap(currentBitmap, x, y, new Paint());
	}
	
/*	protected void buiding(long elapsedTime){
		//如果在building
		if(status==1){
			complete+= elapsedTime*icon.getBuildSpeed();
			//当build完毕
			if(complete>=1){
				Tile tile = icon.getTile(getType());
				GridMapRender gm = gridMap.getTileMapRender();
				Point location = AStarSearch.findNeighborNode(gridMap, x, y);
				tile = tile.clone(location.x, location.y, gridMap);
				tile.setUUID(java.util.UUID.randomUUID().toString());
				gm.addBuildTile(tile);
				complete=0;
				status=0;
				tile.setHealth(1.0f);
				//最后通知work_panel已经build完毕
				gm.getConsolePanel().work_panel.build(null);
				//通知网络build 需要ICON,TYP
				gm.getNetWorkManager().build(new IconInfo(getUUID(),tile.getUUID(),gridMap.getIconKey(icon)));
			}
		}
	} 
	*/
	public void update(long elapsedTime) {
		if(health<0.6){
			this.currentBitmap = this.Bitmaps[2]; 
		}
		else if(health<0.98){
			this.currentBitmap = this.Bitmaps[1]; 
		}else{
			this.currentBitmap = this.Bitmaps[0];
		}
		//buiding(elapsedTime);
	}
	
 
	
	public Tile clone(int x,int y,GridMap gridMap) {
		Constructor constructor = getClass().getConstructors()[0];
		try {
			House house = (House) constructor.newInstance(new Object[]{Bitmaps,id});
			house.x = x* GridMapRender.TILE_WIDTH;
			house.y = y* GridMapRender.TILE_HEIGHT;
			house.tileX = x;
			house.tileY = y;
			house.gridMap = gridMap;
			house.gm = gridMap.getTileMapRender();
			return house;
		} catch (Exception e) {
			e.printStackTrace();
	 
		}
		return null;
	}

	public int getHeight() {
		return currentBitmap.getHeight();
	}

	public int getWidth() {
		return currentBitmap.getWidth();
	}

	
/*	public void readyBuild(BaseIcon icon){
		WorkPanel workPanel = gridMap.getTileMapRender().getConsolePanel().work_panel;
		workPanel.build(this);
		this.status = 1;
		this.icon = icon;
	}*/
	
	public boolean isBuilding(){
		return status==1;
	}
	
	public float getComplete() {
		return complete;
	}
	
	
	public abstract boolean build(long elapsedTime);
	
	public abstract Point getSize();
	
	public abstract Resource getResource();
	
	/**
	 * 防御
	 */
	public  abstract float getDefence();
}
