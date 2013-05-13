package org.shenyuanv.activity;

import org.shenyuanv.core.GridMap;
import org.shenyuanv.core.GridMapRender;
import org.shenyuanv.core.ResourceManager;
import org.shenyuanv.net.MockNetWorkManager;
import org.shenyuanv.net.NetWorkManager;
import org.shenyuanv.net.datagram.PlayerList;
import org.shenyuanv.view.GameView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class AndroidCraftActivity extends Activity implements Runnable {
	
	private View gameView;	
	private GridMap gridmap;
	private GridMapRender gridMapRender;
	private NetWorkManager netWorkManager = new MockNetWorkManager();
	
	private Handler mHandler=new Handler(){  
		  public void handleMessage(Message msg) {  
			  gameView.postInvalidate();  
		  }  
		 }; 
		 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGame();
		gameView = new GameView(this,gridmap,gridMapRender,netWorkManager);
		setContentView(gameView);
		Thread thread = new Thread(this);
		thread.start();
	}
	private void initGame() {
		netWorkManager.select("Computer", 1);
		netWorkManager.select("Computer", 2);
		netWorkManager.select("Computer", 3);		
		ResourceManager.res = getResources();	
		PlayerList playerList = netWorkManager.startGame();
		gridMapRender = ResourceManager.load(playerList.getType(),playerList.getPlayers());
		gridMapRender.setNetWorkManager(netWorkManager);
		gridmap = ResourceManager.resourceManager.getGridMap();
		gridMapRender = gridmap.getTileMapRender();
		
	}

	@Override
	public void run() {
		gameLoop();		
	}
	private void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (true) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

			// 发送消息更新canvas
			Message message = new Message();
			AndroidCraftActivity.this.mHandler.sendMessage(message);

            try {
                Thread.sleep(5);
            }
            catch (InterruptedException ex) { }
        }
	}
	private void update(long elapsedTime) {
		gridMapRender.update(elapsedTime);
		
	}
}