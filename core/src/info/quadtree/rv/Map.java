package rv;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import rv.actor.Actor;
import sgf.SGF;

public class Map extends Actor{
	WorldData map;
	
	byte[][] permutation;
	
	Body body;
	
	public boolean isPassable(int x, int y)
	{
		if(x < 0 || y < 0 || x >= map.tiles.length || y >= map.tiles[0].length) return false;
		return WorldData.isPassable[map.tiles[x][y]];
	}
	
	protected boolean isPossiblyReachable(int x, int y)
	{
		if(isPassable(x - 1, y)) return true;
		if(isPassable(x + 1, y)) return true;
		if(isPassable(x, y - 1)) return true;
		if(isPassable(x, y + 1)) return true;
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void load()
	{
		try {
			ArrayList<NPCDesc> npcsToCreate;
			
			boolean boxFilled[][] = new boolean[1024][1024];
			permutation = new byte[1024][1024];
			
			InputStream worldIn = null;
			
			worldIn = getClass().getResourceAsStream("/media/world.dat");
			
			if(worldIn == null) worldIn = new FileInputStream(".\\media\\world.dat");
			
			ObjectInputStream in = new ObjectInputStream(worldIn);
			map = (WorldData)in.readObject();
			in.close();
			
			InputStream npcsIn = null;
			
			npcsIn = getClass().getResourceAsStream("/media/npcs.dat");
			
			if(npcsIn == null) npcsIn = new FileInputStream(".\\media\\npcs.dat");
		
			in = new ObjectInputStream(npcsIn);
			npcsToCreate = (ArrayList<NPCDesc>)in.readObject();
			in.close();
			
			BodyDef bd = new BodyDef();
			body = Game.s.physicsWorld.createBody(bd);
			
			FilterData fd = new FilterData();
			fd.categoryBits = 1;
			fd.maskBits = 0xffffffff;
			
			for(int x=0;x<map.tiles.length;x++){
				for(int y=0;y<map.tiles[0].length;y++){
					permutation[x][y] = (byte)Game.s.rand.nextInt(4*8);
					if(!boxFilled[x][y] && !isPassable(x,y) && isPossiblyReachable(x,y))
					{
						PolygonDef pd = new PolygonDef();
						
						int boxHeight = 0;
						
						for(int i=y;i<1024;i++)
						{
							if(!isPassable(x,i))
							{
								boxFilled[x][i] = true;
								boxHeight++;
							} else
								break;
						}
						
						pd.setAsBox(0.5f, 0.5f*boxHeight, new Vec2(x,y + boxHeight/2.f - 0.5f), 0);
						
						//if(x == 512)
						//	System.out.println(0.5f + " " + 0.5f*boxHeight + " " + new Vec2(x,y + boxHeight/2.f - 0.5f));
						
						body.createShape(pd);
					}
				}
			}
			
			for(NPCDesc nd : npcsToCreate)
			{
				nd.create(Game.s.actors);
			}
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
		
		int minX = (int)(Game.s.player.getPosition().x - 16);
		int minY = (int)(Game.s.player.getPosition().y - 16);
		int maxX = (int)(Game.s.player.getPosition().x + 18);
		int maxY = (int)(Game.s.player.getPosition().y + 16);
		
		for(int x=minX;x<maxX;x++)
		{
			for(int y=minY;y<maxY;y++)
			{
				if(map.tiles[x][y] != 0)
					SGF.getInstance().renderImage(WorldData.tileGraphics[map.tiles[x][y]], x, y, 1, 1, 1.5707963267948966192313216916398f * (permutation[x][y] % 4), true);
				else
					SGF.getInstance().renderImage("grass" + ((permutation[x][y] / 4) + 1), x, y, 1, 1, 1.5707963267948966192313216916398f * (permutation[x][y] % 4) * 2, true);
			}
		}
	
		
		super.render();
	}
}
