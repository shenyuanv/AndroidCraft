package org.shenyuanv.ui.icon;

import org.shenyuanv.core.GridMapRender;
import org.shenyuanv.tile.House;

import android.graphics.Bitmap;

public class HouseIcon extends BaseIcon{

	public HouseIcon(Bitmap iconBitmap) {
		super(iconBitmap);
	}

	public void onClicked(GridMapRender gridMapRender){
	
/*		House house = (House)gridMapRender.getCurrentTile();
		if(gridMapRender.checkResource(resource)){
			house.readyBuild(this);
		}
	 */
		
	}
	
	public float getBuildSpeed(){
		return BUILD_SPEED;
	}
	
	private static final float BUILD_SPEED = 0.0003f;
}
