package org.shenyuanv.ui.icon;

import java.util.ArrayList;
import java.util.List;

import org.shenyuanv.core.GridMapRender;
import org.shenyuanv.tile.Tile;
import org.shenyuanv.util.Resource;

import android.graphics.Bitmap;

public abstract class BaseIcon {
	
	protected Bitmap iconImage;
	
	protected List<IconBean> tileImages = new ArrayList<IconBean>();
	
	protected Resource resource;
	
	 
	public BaseIcon(Bitmap iconImage) {
		super();
		this.iconImage = iconImage;
	}

	public abstract void onClicked(GridMapRender gridMapRender);
	
	public void add(Tile house,Bitmap tileImage,Resource resource){
		this.tileImages.add(new IconBean(house,tileImage));
		this.resource=resource;
	}
	
	public Bitmap getTileImage(int type){
		return tileImages.get(type).image;
	}
	 

	/**
	 * 返回一个原始Tile
	 * @param type
	 * @return
	 */
	public Tile getTile(int type){
		return tileImages.get(type).tile;
	}
	
	public Resource getResource() {
		return resource;
	}

	public Bitmap getIconImage() {
		return iconImage;
	}
	
	public float getBuildSpeed(){return 0.0f;}
	
	public static class IconBean{
		
		public Bitmap image;
		public Tile tile;
		
		public IconBean(Tile tile,Bitmap image) {
			super();
			this.image = image;
			this.tile = tile;
		}
		
	}
}
