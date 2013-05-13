package org.shenyuanv.net;

import java.util.List;

import org.shenyuanv.net.datagram.PlayerInfo;
import org.shenyuanv.net.datagram.PlayerList;
import org.shenyuanv.tile.Sprite;
import org.shenyuanv.tile.Tile;

/**
 * 用于网络对战，暂不实现
 * 
 */
public interface NetWorkManager {

	public void establishServer(String serverName);

	public void join(PlayerInfo clientInfo, PlayerInfo serverInfo);

	public void listen();

	public void closeListen();

	public void select(String name, int index);

	public PlayerList startGame();

	//public void addPlayerListener(PlayerListener playerListener);

	//public void addClientListener(ClientListener clientListener);

	public void move(List<Sprite> sprites, int tx, int ty);

	/**
	 * 
	 * @param tile
	 * @param tx
	 * @param ty
	 * @param newTileUUID 新tileUUID
	 */
	public void operate(Tile tile, int tx, int ty,String newTileUUID);

/*	public void build(IconInfo iconInfo);
	
	public void readyBuild(IconInfo iconInfo);
	
	*//**
	 * 为了避免数据不同步，需要通知每个Player删除tile
	 * @param iconInfo
	 *//*
	public void remove(SpriteInfo spriteInfo);*/
	public void close();

}
