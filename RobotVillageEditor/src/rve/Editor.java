package info.quadtree.rve;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import info.quadtree.rv.NPCDesc;
import info.quadtree.rv.WorldData;
import sgf.GameInterface;
import info.quadtree.rv.graphics.SGF;

public class Editor implements GameInterface, WindowListener, KeyListener, MouseListener, MouseMotionListener {

	WorldData world;
	
	ArrayList<NPCDesc> npcs;
	
	byte brush;
	
	float camX, camY;
	
	boolean mouseState = false;
	
	Point2D mPos;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:\\Users\\quadtree\\ludumdare\\RobotVillage\\media\\world.dat"));
			world = (WorldData)in.readObject();
			in.close();
		} catch(Exception e)
		{
			System.out.println("world.dat missing or corrupted, regenerating!");
			world = new WorldData();
		}
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:\\Users\\quadtree\\ludumdare\\RobotVillage\\media\\npcs.dat"));
			npcs = (ArrayList<NPCDesc>)in.readObject();
			in.close();
		} catch(Exception e)
		{
			System.out.println("npcs.dat missing or corrupted, regenerating!");
			npcs = new ArrayList<NPCDesc>();
		}
		
		RobotVillageEditor.mw.addWindowListener(this);
		SGF.getInstance().addKeyListener(this);
		SGF.getInstance().addMouseListener(this);
		SGF.getInstance().addMouseMotionListener(this);
		
		camX = 512;
		camY = 512;
		
		SGF.getInstance().setCamera(camX, camY, 32);
	}

	@Override
	public void render() {
		world.render();
		
		SGF.getInstance().renderText("CAMERA: " + camX + " " + camY, 20, 40, 255, 255, 255, false, 14);
		
		SGF.getInstance().renderImage(WorldData.tileGraphics[brush], 100, 100, 64, 64, 0, false);
		
		for(NPCDesc nd : npcs)
		{
			nd.render();
		}
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void update() {
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\quadtree\\ludumdare\\RobotVillage\\media\\world.dat"));
			out.writeObject(world);
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\quadtree\\ludumdare\\RobotVillage\\media\\npcs.dat"));
			out.writeObject(npcs);
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT) camX -= 4.3f;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) camX += 4.3f;
		if(e.getKeyCode() == KeyEvent.VK_UP) camY -= 4.3f;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) camY += 4.3f;
		
		if(e.getKeyCode() == KeyEvent.VK_1) brush = 0;
		if(e.getKeyCode() == KeyEvent.VK_2) brush = 1;
		if(e.getKeyCode() == KeyEvent.VK_3) brush = 2;
		if(e.getKeyCode() == KeyEvent.VK_4) brush = 3;
		if(e.getKeyCode() == KeyEvent.VK_5) brush = 4;
		
		if(e.getKeyCode() == KeyEvent.VK_U) npcs.add(new NPCDesc(NPCDesc.NPCType.Shop1, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_I) npcs.add(new NPCDesc(NPCDesc.NPCType.Shop2, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_O) npcs.add(new NPCDesc(NPCDesc.NPCType.Shop3, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_P) npcs.add(new NPCDesc(NPCDesc.NPCType.Tech, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_J) npcs.add(new NPCDesc(NPCDesc.NPCType.Mayor, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_K) npcs.add(new NPCDesc(NPCDesc.NPCType.Villager, (float)mPos.getX(), (float)mPos.getY()));
		
		if(e.getKeyCode() == KeyEvent.VK_Q) npcs.add(new NPCDesc(NPCDesc.NPCType.BasicEnemy, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_W) npcs.add(new NPCDesc(NPCDesc.NPCType.ShieldEnemy, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_E) npcs.add(new NPCDesc(NPCDesc.NPCType.EliteEnemy, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_R) npcs.add(new NPCDesc(NPCDesc.NPCType.BossEnemy, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_T) npcs.add(new NPCDesc(NPCDesc.NPCType.Beacon, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_Y) npcs.add(new NPCDesc(NPCDesc.NPCType.FastEnemy, (float)mPos.getX(), (float)mPos.getY()));
		if(e.getKeyCode() == KeyEvent.VK_A) npcs.add(new NPCDesc(NPCDesc.NPCType.MissileEnemy, (float)mPos.getX(), (float)mPos.getY()));
		
		if(e.getKeyCode() == KeyEvent.VK_N) npcs.add(new NPCDesc(NPCDesc.NPCType.PlotBarrier, (float)mPos.getX(), (float)mPos.getY()));
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			NPCDesc closest = null;
			float closestDist = Float.MAX_VALUE;
			
			for(NPCDesc nd : npcs)
			{
				float dist = (float)Math.sqrt(Math.pow(nd.x - mPos.getX(), 2) + Math.pow(nd.y - mPos.getY(), 2));
				
				if(dist < closestDist)
				{
					closestDist = dist;
					closest = nd;
				}
			}
			
			if(closest != null)
				npcs.remove(closest);
		}
		
		SGF.getInstance().setCamera(camX, camY, 32);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		mPos = SGF.getInstance().screenToReal(new Point2D.Float(e.getX(), e.getY()));
		
		int tx = (int)(mPos.getX() + 0.5f);
		int ty = (int)(mPos.getY() + 0.5f);
		
		if(!e.isShiftDown())
			world.tiles[tx][ty] = brush;
		else
			recursiveFill(tx,ty,world.tiles[tx][ty]);
	}

	private void recursiveFill(int tx, int ty, byte fillTarget) {
		if(tx < 0 || ty < 0 || tx >= 1024 || ty >= 1024 || world.tiles[tx][ty] != fillTarget) return;
		
		world.tiles[tx][ty] = brush;
		
		recursiveFill(tx - 1, ty, fillTarget);
		recursiveFill(tx + 1, ty, fillTarget);
		recursiveFill(tx, ty - 1, fillTarget);
		recursiveFill(tx, ty + 1, fillTarget);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mPos = SGF.getInstance().screenToReal(new Point2D.Float(e.getX(), e.getY()));
		
		int tx = (int)(mPos.getX() + 0.5f);
		int ty = (int)(mPos.getY() + 0.5f);
		
		world.tiles[tx][ty] = brush;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mPos = SGF.getInstance().screenToReal(new Point2D.Float(e.getX(), e.getY()));
	}

}
