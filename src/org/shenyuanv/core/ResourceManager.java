package org.shenyuanv.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shenyuanv.activity.R;
import org.shenyuanv.tile.Headquarter;
import org.shenyuanv.tile.House;
import org.shenyuanv.tile.Mine;
import org.shenyuanv.tile.Scv;
import org.shenyuanv.tile.Sprite;
import org.shenyuanv.tile.Sprite.Animation;
import org.shenyuanv.tile.Tile;
import org.shenyuanv.ui.icon.BaseIcon;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;


 

public class ResourceManager {

	private GridMap gridMap; 
	
	public static Resources res;
	
	public static ResourceManager resourceManager;
	
	private static Map<Integer,Bitmap> IMAGE_CACHE = new HashMap<Integer, Bitmap>();
	
	public static Bitmap loadBitmap(int resourceId) {
		 
		if(!IMAGE_CACHE.containsKey(resourceId)){
			IMAGE_CACHE.put(resourceId, BitmapFactory.decodeResource(res, resourceId));
		} 
		return IMAGE_CACHE.get(resourceId);
 
	}
 
	public static GridMapRender load(int type,List<Integer> types){
		 
		resourceManager = new ResourceManager(type,types);
		return resourceManager.gridMap.getTileMapRender();
	}
	
	public GridMap getGridMap() {
		return gridMap;
	}

	/**
	 * 初始化缩略图，将地图块图片缩小并返回，以便画出小地图，暂不实现
	 * @return
	 */
/*	private Bitmap initMapBg(){
		
		BufferedBitmap buffer = new BufferedBitmap(128,128,BufferedBitmap.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) buffer.getGraphics();
		for (int y = 0; y < gridMap.getHeight(); ++y) {
			for (int x = 0; x < gridMap.getWidth(); ++x) {
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.setToTranslation(x*10, y*5);
				affineTransform.scale(0.2, 0.2);
				g2.drawBitmap(Constant.IMAGE_BG, affineTransform, null);
				
			}
		}
		return buffer;
	}*/
	private ResourceManager(int type,List<Integer> types) {
		Constant.load();
		gridMap = loadMap(type,types);
		//Constant.IMAGE_MAP_BG = initMapBg();
		Constant.progress = Constant.TOTAL;
	}
	
	 
	private GridMap loadMap(int type,List<Integer> types) {
	 
		List<String> list = new ArrayList<String>();
		int width = readMap(list, "startmap1.map");
		GridMap map = new GridMap(width, list.size());
		GridMapRender mapRender = new GridMapRender(map);
		mapRender.type = type;
		map.setTileMapRender(mapRender);
		map.setIconMap(Constant.ICON_MAP);
		for (int y = 0; y < list.size(); ++y) {
			// 读取每一行
			String s = list.get(y);
			for (int x = 0; x < s.length(); ++x) {
					
				int code = (int)s.charAt(x)-40;
				if(code<0||code>Constant.TILE_TABLE.length)
					continue;
				if(code<80&&!types.contains((code/20)))
					continue;
				Tile tile = Constant.TILE_TABLE[code];
				if(null == tile){
					continue;
				}
				//test
				if(code == 0){
					
				}
				//test
				tile = tile.clone(x, y,map);
				tile.setHealth(1.0f);
				map.add(tile);
				
				//确定当前视觉范围
				if(tile.getType()==type&&tile instanceof Headquarter){
					
					//获取当前基地坐标
					int hqX = GridMapRender.tileXToPx(tile.getTileX());
					int hqY = GridMapRender.tileYToPx(tile.getTileY());
					Point size = tile.getSize();
					//获取屏幕中心坐标
					int centerX = (800-GridMapRender.tileXToPx(size.x))/2;
					int centerY = (600-GridMapRender.tileYToPx(size.y))/2;
					
					mapRender.setOffset(Math.max(hqX-centerX, 0),Math.max(hqY-centerY, 0)); 
					
				}
				
			}

		}
		return map;
	}

	private static int readMap(List list, String fileName) {

		BufferedReader br = null;
		int width = 0;
		try {
			File file = new File(Environment.getExternalStorageDirectory()+"/map/",fileName);
			Log.d("readmap: ", file.getAbsolutePath());
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {

				if (line.startsWith("#"))
					continue;
				list.add(line);
				width = Math.max(width, line.length());
			}

			return width;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class Constant {
		
		public static String IP;
		
		
		
		//背景
		public static Bitmap IMAGE_BG;
		//控制台
		public static Bitmap IMAGE_CONTROL;
		//光照效果
		public static Bitmap SCV_SPARK;
		//矿产
		public static Bitmap ICON_MINE;
		//人口
		public static Bitmap ICON_MAN;
		//矿产错误信息
		public static String MINE_ERROR="我们需要更多晶矿。";
		//人口错误信息
		public static String MAN_ERROR="太挤啦，快造点房子出来吧";
		
		public static String BUILD_ERROR="地基不稳，不能修在这里";
		
		public static Bitmap IMAGE_MAP_BG;
		
		
		//ICONS
		public static final BaseIcon[][] SCV_ICONS = new BaseIcon[2][3];
		public static final BaseIcon[][] HQ_ICONS = new BaseIcon[2][3];
		public static final BaseIcon[][] BACK_ICONS = new BaseIcon[2][3];
		
		//ICON资源表
		public static final Map<Integer,BaseIcon[][]> ICON_TABLE = new HashMap<Integer, BaseIcon[][]>();
		
		//Tile资源表
		public static final Tile[] TILE_TABLE = new Tile[82];
		
		private static final int TYPE  = 4;
		
		public static final int TYPE_SIZE = 20;
		
		//进度控制
		private static float progress;
		
		private static final float TOTAL=19;
		
		static void load(){
			
			IMAGE_BG = loadBitmap(R.drawable.bg1);
			SCV_SPARK =  loadBitmap(R.drawable.scv_spark);
			
/*			IMAGE_CONTROL = loadBitmap("panel/main.gif");
			
			SCV_SPARK =  loadBitmap("unit/0_scv_spark.gif");
			
			ICON_MINE =  loadBitmap("panel/mine.gif");
			
			ICON_MAN =  loadBitmap("panel/man.gif");*/
			
			
			initTile();
			 
			
		}
		
		private static final Map<String, BaseIcon> ICON_MAP = new HashMap<String, BaseIcon>();
		private static int count = 0;
		private static BaseIcon createIcon(BaseIcon icon){
			ICON_MAP.put("icon"+(++count), icon);
			return icon;
		} 
		private static void initTile(){
			
			//加载图片
/*			Bitmap scv  = loadBitmap("unit/0_scv_red.gif");
			Bitmap marine  = loadBitmap("unit/0_marine_red.gif");
			Bitmap marineFight  = loadBitmap("unit/0_fight_marine_red.png");
			
			Bitmap tank  = loadBitmap("unit/0_tank.gif");
			Bitmap supply  = loadBitmap("build/0_supply_red.gif");
			Bitmap barrack  = loadBitmap("build/0_barrack_red.gif");
			Bitmap mine  = loadBitmap("block/mine.gif");*/

			Bitmap base  = loadBitmap(R.drawable.hq_red);
			Bitmap mine  = loadBitmap(R.drawable.mine);
			Bitmap scv  = loadBitmap(R.drawable.scv_red);
			//转换成buffer
/*			BufferedBitmap scv_buffer=imageToBuffer(scv);
			BufferedBitmap marine_buffer=imageToBuffer(marine);
			BufferedBitmap marineFight_buffer=imageToBuffer(marineFight);
			BufferedBitmap supply_buffer=imageToBuffer(supply);
			BufferedBitmap barrack_buffer=imageToBuffer(barrack);
			BufferedBitmap tank_buffer=imageToBuffer(tank);
			*/
			
			
			//初始化矿产资源
			TILE_TABLE[80] = new Mine(mine,80);
			
			//ICON
/*			HQ_ICONS[0][0]  = createIcon(new HouseIcon(loadBitmap("ico/0_scv.gif")));
			SCV_ICONS[0][0] = createIcon(new ScvIcon(loadBitmap("ico/0_supply.gif")));
			SCV_ICONS[0][1] = createIcon(new ScvIcon(loadBitmap("ico/0_barrack.gif")));
			BACK_ICONS[0][0] = createIcon(new HouseIcon(loadBitmap("ico/0_marine.gif")));*/
			//BACK_ICONS[0][1] = new HouseIcon(loadBitmap("ico/0_tanke.gif"));
			
			
			
			//初始化比较复杂的Tile
			for(int i=0;i<TYPE;i++){
				
				//农民
				TILE_TABLE[TYPE_SIZE*i] = createScv(scv,TYPE_SIZE*i);
				Log.d("createScv","i:"+i);
				scv = Bitmap.createBitmap(createBitmap(scv,i));
				
				//ICON_TABLE.put(TILE_TABLE[TYPE_SIZE*i].getId(), SCV_ICONS);
				//scv_buffer =  imageToBuffer(createBitmap(scv, i));
	/*			
				//步兵
				TILE_TABLE[TYPE_SIZE*i+01] = createMarine(marine_buffer,marineFight_buffer,TYPE_SIZE*i+01);
				marine_buffer =  imageToBuffer(createBitmap(marine, i));
				marineFight_buffer =  imageToBuffer(createBitmap(marineFight, i));*/
				
				//喷火兵
//				TILE_TABLE[TYPE_SIZE*i+02] = createMarine(marine_buffer,TYPE_SIZE*i+02);
//				marine_buffer =  imageToBuffer(createBitmap(marine, i));
				
				//坦克
//				TILE_TABLE[TYPE_SIZE*i+04] = createTank(tank_buffer,TYPE_SIZE*i+04);
//				tank_buffer =  imageToBuffer(createBitmap(tank, i));
				
				//人口
/*				TILE_TABLE[TYPE_SIZE*i+10] = createSupply(supply_buffer,TYPE_SIZE*i+10);
				supply_buffer =  imageToBuffer(createBitmap(supply, i));*/
				
				//基地
				//TILE_TABLE[TYPE_SIZE*i+11] = createBase(base,TYPE_SIZE*i+11);
				TILE_TABLE[TYPE_SIZE*i+11] = createBase(base,TYPE_SIZE*i+11);
				base = createBitmap(base,i);
				//ICON_TABLE.put(TILE_TABLE[TYPE_SIZE*i+11].getId(), HQ_ICONS);
				//base_buffer =  imageToBuffer(createBitmap(base, i));
				
				//兵营
/*				TILE_TABLE[TYPE_SIZE*i+12] = createBarrack(barrack_buffer,TYPE_SIZE*i+12);
				ICON_TABLE.put(TILE_TABLE[TYPE_SIZE*i+12].getId(), BACK_ICONS);
				barrack_buffer =  imageToBuffer(createBitmap(barrack, i));*/
			}
		}
		
		
		/**
		 * 创建Supply
		 * @param buffer
		 * @return
		 */
/*		private static House createSupply(BufferedBitmap buffer,int id){
			Bitmap[] images = new Bitmap[3];
			int w = buffer.getWidth() / 3;
			int h = buffer.getHeight();
			for(int i=0;i<images.length;++i){
				images[i] = buffer.getSubimage(w * i, 0, w, h);
				
			}
			House house = new Supply(images,id); 
			SCV_ICONS[0][0].add(house,images[0],house.getResource());
			return house;
		}*/
		
/*		private static House createBarrack(BufferedBitmap buffer,int id){
			Bitmap[] images = new Bitmap[3];
			int w = buffer.getWidth() / 3;
			int h = buffer.getHeight();
			for(int i=0;i<images.length;++i){
				images[i] = buffer.getSubimage(w * i, 0, w, h);
				
			}
			House house = new Barrack(images,id); 
			SCV_ICONS[0][1].add(house,images[0],house.getResource());
			return house;
		}*/
		
		/**
		 * 创建Base
		 * @param buffer
		 * @return
		 */
		private static House createBase(Bitmap buffer,int id){
			Bitmap[] images = new Bitmap[1];
			int w = buffer.getWidth();
			int h = buffer.getHeight();
			for(int i=0;i<images.length;++i){
				//images[i] = buffer.getSubimage(w * i, 0, w, h);
				images[i] = buffer;
			}
			Headquarter hq = new Headquarter(images,id);
			return hq;
		}
		
		/*
		private static Sprite createMarine(BufferedBitmap buffer,BufferedBitmap marineFight_buffer, int id) {
			
			BufferedBitmap marineBuffer = buffer;
			int w = marineBuffer.getWidth() / 3;
			int h = marineBuffer.getHeight() / 5;
		
			Animation[] moveAnima = new Animation[8];
			Bitmap[][] images = new Bitmap[8][3];
			
			for (int y = 0,z=3; y < 8; ++y) {
				moveAnima[y] = new Animation();
				 
				if(y>=5){
					
					for(int i=0;i<images[z].length;++i){
						Bitmap image = BitmapManager.getMirror(images[z][i]);
						images[y][i]=image;
						moveAnima[y].addFrame(image, 200);
					}
					--z;
					
				}else{
					for (int x = 0; x < 3; ++x) {
						Bitmap image = marineBuffer.getSubimage(x*w, y*h, w, h);
						images[y][x] = image;
						moveAnima[y].addFrame(image, 200);
					}
				}
				 
			}
			
			Bitmap[][] antiBitmaps = new Bitmap[8][2];
			Animation[] fightAnima = new Animation[8];
			w = marineFight_buffer.getWidth()/2;
			h = marineFight_buffer.getHeight()/5;
			
			for (int y = 0,z=3; y < 8; ++y) {
				fightAnima[y] = new Animation();
				 
				if(y>=5){
					
					for(int i=0;i<antiBitmaps[z].length;++i){
						Bitmap image = BitmapManager.getMirror(antiBitmaps[z][i]);
						images[y][i]=image;
						fightAnima[y].addFrame(image, 200);
					}
					--z;
					 
				}else{
					 
					Bitmap image1 = marineFight_buffer.getSubimage(0, y*h, w, h);
					Bitmap image2 = marineFight_buffer.getSubimage(w, y*h, w, h);
					antiBitmaps[y][0]=image1;
					antiBitmaps[y][1]=image2;              
					fightAnima[y].addFrame(image1, 200);
					fightAnima[y].addFrame(image2, 200);
					fightAnima[y].addFrame(image1, 200);
					fightAnima[y].addFrame(image2, 200);
					 
					
				}
			}
			Sprite sprite = new Marine(new Animation[][] { moveAnima,fightAnima}, id);
			BACK_ICONS[0][0].add(sprite, null,sprite.getResource());
			return sprite;
		}
		*/
		/**
		 * create a scv prototype,every scv object is cloned by this prototype.
		 * a scv prototype has a single id,namely index in the tile_table.
		 * 
		 * @param buffer
		 *            BitmapBuffer
		 * @param id
		 *            index in the tile_table
		 * @return
		 */
		private static Sprite createScv(Bitmap buffer,int id) {
			//Log.d("createScv", buffer.getWidth()+","+buffer.getHeight());
			Bitmap scvBuffer = buffer; 
			Animation[][] anima = new Animation[3][8];
			int w = scvBuffer.getWidth() / 3;
			int h = scvBuffer.getHeight() / 5;
			
			for (int x = 0; x < anima.length; ++x) {

				for (int y = 0; y < 5; ++y) {
					anima[x][y] = new Animation();
					Bitmap image = Bitmap.createBitmap(scvBuffer, x*w, y*h,w, h);
					anima[x][y].addFrame(image, 200);
				}

				Matrix matrix = new Matrix();
				Bitmap src = anima[x][3].getBitmap();
				matrix.setScale(-1, 1);
				anima[x][5] = new Animation(
						Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true), 200);
				src = anima[x][2].getBitmap();
				matrix.setScale(-1, 1);
				anima[x][6] = new Animation(
						Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true), 200);
				src = anima[x][1].getBitmap();
				matrix.setScale(-1, 1);
				anima[x][7] = new Animation(
						Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true), 200);
				
				
/*				anima[x][6] = new Animation(BitmapManager.getMirror(anima[x][2]
						.getBitmap()), 200);
				anima[x][7] = new Animation(BitmapManager.getMirror(anima[x][1]
						.getBitmap()), 200);*/

			}
			Sprite sprite = new Scv(anima,id);
			//HQ_ICONS[0][0].add(sprite, null,sprite.getResource());
			return sprite;
		}
/*		
		private static Sprite createTank(BufferedBitmap buffer,int id) {

			BufferedBitmap scvBuffer = buffer;
			Animation[][] anima = new Animation[1][8];
			int w = scvBuffer.getWidth() / 1;
			int h = scvBuffer.getHeight() / 5;
			
			for (int x = 0; x < anima.length; ++x) {

				for (int y = 0; y < 5; ++y) {

					anima[x][y] = new Animation();
					Bitmap image = scvBuffer.getSubimage(x*w, y * h, w, h);
					anima[x][y].addFrame(image, 200);
				}

				anima[x][5] = new Animation(BitmapManager.getMirror(anima[x][3]
						.getBitmap()), 200);
				anima[x][6] = new Animation(BitmapManager.getMirror(anima[x][2]
						.getBitmap()), 200);
				anima[x][7] = new Animation(BitmapManager.getMirror(anima[x][1]
						.getBitmap()), 200);

			}
			Tank sprite = new Tank(anima,id);
			BACK_ICONS[0][1].add(sprite, null,sprite.getResource());
			return sprite;
		}*/
		
/*		private static BufferedBitmap imageToBuffer(Bitmap image) {

			int w = image.getWidth(null);
			int h = image.getHeight(null);

			BufferedBitmap buffer = new BufferedBitmap(w, h,
					BufferedBitmap.TYPE_INT_ARGB);
			Graphics gh = buffer.getGraphics();
			gh.drawBitmap(image, 0, 0, null);
			gh.dispose();
			return buffer;
		}*/
		
		/**
		 * 根据不同队伍变换颜色
		 * 
		 * */
		private static  Bitmap createBitmap(Bitmap image,int color)  {

			int w = image.getWidth();
			int h = image.getHeight();
			int[] pix = new int[w*h];
			int i=0;
			for (int y = 0; y < h; ++y) {
				for (int x = 0; x < w; ++x) {
					pix[i] = image.getPixel(x, y);
					int pixel = pix[i];
					int a = (pixel >> 24) & 0xff;
					int r = (pixel >> 16) & 0xff;
					int g = (pixel >> 8) & 0xff;
					int b = (pixel) & 0xff;
					int c = Color.argb(a, r, g, b);
					//Log.d("createBitmap", "case:"+color);

					switch (color) {
					case 0:
						if (r > b + 50 && r > g + 50) {
							if (r > b + 70 && r > g + 70) {
								c = Color.argb(a, 230, 100, 33);
							} else {
								c = Color.argb(a,100, 50, 33);
							}

						}
						break;

					case 1:
						if (r > b + 50 && r > g + 50) {
							if (r > b + 70 && r > g + 70) {
								c = Color.argb(a, 33, 100, 230);
							} else {
								c = Color.argb(a,30, 50, 100);
							}
						}
						break;
					case 2:
						if (r > b + 50 && r > g + 50) {
							if (r > b + 70 && r > g + 70) {
								c = Color.argb(a, 33, 146, 255);
							} else {
								c = Color.argb(a,16, 85, 57);
							}
							
						}
						break;
					}

					pix[i] = c;
					++i;
				}
			}
			Bitmap result = Bitmap.createBitmap(pix, w, h, Bitmap.Config.ARGB_8888);
			return result;
		}
		
		private static Bitmap loadBitmap(int resourceId) {
			if(!IMAGE_CACHE.containsKey(resourceId)){
				Bitmap bitmap = BitmapFactory.decodeResource(res, resourceId);
				IMAGE_CACHE.put(resourceId, bitmap);
				++progress;
			} 
			return IMAGE_CACHE.get(resourceId);
		}
		
		public static int getProgress() {
//			System.out.println(progress+","+TOTAL);
			return Math.min(Math.round(progress/TOTAL*600),600);
		}
		
	}
	
	 
}
