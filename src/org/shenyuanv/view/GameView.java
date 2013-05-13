package org.shenyuanv.view;

import org.shenyuanv.core.GridMap;
import org.shenyuanv.core.GridMapRender;
import org.shenyuanv.net.NetWorkManager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View{

	GridMap gridmap;
	GridMapRender gridMapRender;
	NetWorkManager netWorkManager;

	private GestureDetector gestureDetector;
	public GameView(Context context,GridMap gridmap,GridMapRender gridMapRender,NetWorkManager netWorkManager) {
		super(context);
		// TODO Auto-generated constructor stub
		this.gridmap = gridmap;
		this.gridMapRender = gridMapRender;
		this.netWorkManager = netWorkManager;
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);	

		gestureDetector = new GestureDetector(context, new InnerGestureListener());
		gestureDetector.setIsLongpressEnabled(false);
		
	}
	


	@Override
	protected void onDraw(Canvas canvas) {
		//Log.d("onDraw", "draw");
		GridMapRender.screenWidth = getWidth();
		GridMapRender.screenHeight = getHeight();
		gridMapRender.draw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextScaleX(1);
		canvas.drawText(String.valueOf(gridMapRender.offsetX), 50, 50, paint);
		canvas.drawText(String.valueOf(gridMapRender.offsetY), 100, 50, paint);
		super.onDraw(canvas);
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	class InnerGestureListener extends GestureDetector.SimpleOnGestureListener {  
		  
	        @Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
	        int maxX = GridMapRender.tileXToPx(107);
			if(gridMapRender.offsetX<=maxX && gridMapRender.offsetX>=0){
				gridMapRender.offsetX += distanceX;
				if(gridMapRender.offsetX<0){
					gridMapRender.offsetX = 0;
				}
				if(gridMapRender.offsetX>maxX){
					gridMapRender.offsetX = maxX;
				}
			}
			int maxY = GridMapRender.tileXToPx(110);
			if(gridMapRender.offsetY<=maxY && gridMapRender.offsetY>=0){
				gridMapRender.offsetY += distanceY;		
				if(gridMapRender.offsetY<0){
					gridMapRender.offsetY = 0;
				}
				if(gridMapRender.offsetY > maxY){
					gridMapRender.offsetY = maxY;
				}
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

			@Override  
	        public boolean onDown(MotionEvent e) {  
	            return true;  
	        }  
	          
	        @Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				return true;
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				Log.d("onDoubleTap",(int)e.getX()+","+(int)e.getY());
				//gridMapRender.operate((int)e.getX(), (int)e.getY());
				gridMapRender.focus(0, 0, (int)e.getX()+2, (int)e.getY()+2);
				return super.onDoubleTap(e);
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				super.onLongPress(e);
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				super.onShowPress(e);
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				Log.d("onSingleTapConfirmed",(int)e.getX()+","+(int)e.getY());
				gridMapRender.move((int)e.getX(), (int)e.getY());
				return super.onSingleTapConfirmed(e);
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return super.onSingleTapUp(e);
			}
	        
	  }



	
}
