package org.shenyuanv.ui.icon;

import org.shenyuanv.core.GridMapRender;

import android.graphics.Bitmap;

public class ScvIcon extends BaseIcon{

	public ScvIcon(Bitmap iconBitmap) {
		super(iconBitmap);
	}
	
	public void onClicked(GridMapRender gridMapRender){
/*		 
		Scv tile = (Scv)gridMapRender.getCurrentTile();
		if(gridMapRender.checkResource(resource)){
			tile.readyBuild(this);
			gridMapRender.getNetWorkManager().readyBuild(new IconInfo(tile.getUUID(),gridMapRender.getGridMap().getIconKey(this)));
		}*/
	}
	
}
