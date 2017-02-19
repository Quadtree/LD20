package sgf;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.swing.SwingUtilities;

public class SGF implements Runnable {
	Canvas c;
	Graphics2D g;
	Color clearColor;
	HashMap<String, Image> images;
	
	ImageSource imgSrc;
	
	float camX, camY, camZoom;
	
	GameInterface game;
	
	Queue<RenderOperation> ops;
	Queue<RenderOperation> UIops;
	
	boolean run = true;
	
	AffineTransform cameraTransform;
	
	RenderThread renderThread;
	
	boolean renderingInProgress = false;
	
	private static SGF singleton;
	
	private AffineTransform UIScale;
	
	public static SGF getInstance()
	{
		if(singleton == null)
			singleton = new SGF();
		
		return singleton;
	}
	
	private SGF()
	{
	}
	
	private class RenderThread implements Runnable
	{
		int framesLastSecond, framesThisSecond;
		long lastSecond = 0;
		
		@Override
		public void run() {
			try {
				g = (Graphics2D)c.getBufferStrategy().getDrawGraphics();
					
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
				g.setBackground(clearColor);
				g.clearRect(0, 0, c.getWidth(), c.getHeight());
					
				g.setTransform(cameraTransform);
				
					
				while(ops.peek() != null)
				{
					ops.poll().execute(g);
				}
					
				g.setTransform(UIScale);
					
				while(UIops.peek() != null)
				{
					UIops.poll().execute(g);
				}
				
				//g.setColor(new Color(255,255,255));
				//g.setFont(new Font("Consolas", 0, 12));
				//g.drawString("FPS: " + framesLastSecond, 20, 20);
				
				framesThisSecond++;
				
				if(Calendar.getInstance().getTimeInMillis() / 1000 != lastSecond)
				{
					lastSecond = Calendar.getInstance().getTimeInMillis() / 1000;
					
					framesLastSecond = framesThisSecond;
					framesThisSecond = 0;
				}
					
				g.dispose();
				c.getBufferStrategy().show();
					
				renderingInProgress = false;
			}
			catch(Exception e) // errors while rendering aren't considered fatal, so just ignore them
			{
			}
		}
		
	}
	
	public void addKeyListener(KeyListener l)
	{
		c.addKeyListener(l);
	}
	
	public void removeKeyListener(KeyListener l)
	{
		c.removeKeyListener(l);
	}
	
	public void addMouseListener(MouseListener l)
	{
		c.addMouseListener(l);
	}
	
	public void addMouseMotionListener(MouseMotionListener l)
	{
		c.addMouseMotionListener(l);
	}
	
	public void addMouseWheelListener(MouseWheelListener l)
	{
		c.addMouseWheelListener(l);
	}
	
	public void start(Container parent, ImageSource imgSrc, GameInterface game)
	{
		images = new HashMap<String, Image>();
		
		this.imgSrc = imgSrc;
		this.game = game;
		
		c = new Canvas();
		c.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		parent.add(c);
		
		c.createBufferStrategy(2);
		
		setCamera(0,0,1);
		
		clearColor = new Color(0,0,0);
		
		ops = new ArrayBlockingQueue<RenderOperation>(16384);
		UIops = new ArrayBlockingQueue<RenderOperation>(16384);
		
		UIScale = new AffineTransform();
		UIScale.scale(parent.getWidth() / 1024.f, parent.getWidth() / 1024.f);
		
		renderThread = new RenderThread();
		
		Thread logicThread = new Thread(this);
		logicThread.start();
	}
	
	public void stop()
	{
		run = false;
		singleton = null;
	}

	@Override
	public void run() {
		game.init();
		
		long ticks = Calendar.getInstance().getTimeInMillis();
		try {
			while(run)
			{
				while(Calendar.getInstance().getTimeInMillis() > ticks)
				{
					//System.out.println("Updating");
					game.update();
					ticks += 16;
				}
				if(!renderingInProgress)
				{
					renderingInProgress = true;
					game.render();
					SwingUtilities.invokeLater(renderThread);
				}
			}
		} catch(Exception e)
		{
			run = false;
			e.printStackTrace();
		}
		
		game.shutdown();
	}
	
	public Image getImage(String name)
	{
		Image ret = images.get(name);
		if(ret == null) images.put(name, imgSrc.getImageByName(name));
		ret = images.get(name);
		if(ret == null) throw new RuntimeException("./media/" + name + ".png not found!");
		return ret;
	}
	
	public void renderImage(Image img, float x, float y, float w, float h, float rot, boolean useCamera)
	{
		if(useCamera && (Math.abs(x - camX) > c.getWidth() / camZoom || Math.abs(y - camY) > c.getHeight() / camZoom)) return;
		
		RenderOperation op = new RenderImageOperation(x,y,rot,w,h,null,img);
		if(useCamera)
			ops.add(op);
		else
			UIops.add(op);
	}
	
	public void renderImage(String imageName, float x, float y, float w, float h, float rot, boolean useCamera)
	{
		if(useCamera && (Math.abs(x - camX) > c.getWidth() / camZoom || Math.abs(y - camY) > c.getHeight() / camZoom)) return;
		
		RenderOperation op = new RenderImageOperation(x,y,rot,w,h,imageName,null);
		if(useCamera)
			ops.add(op);
		else
			UIops.add(op);
	}
	
	public Point2D screenToReal(Point2D pt)
	{
		Point2D result = new Point2D.Float();
		try {
			return cameraTransform.inverseTransform(pt, result);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void setCamera(float x, float y, float zoom)
	{
		camX = x;
		camY = y;
		camZoom = zoom;
		
		cameraTransform = new AffineTransform();
		cameraTransform.translate(c.getWidth() / 2, c.getHeight() / 2);
		cameraTransform.scale(zoom, zoom);
		cameraTransform.translate(-x, -y);
	}
	
	public void renderText(String text, float x, float y, int cr, int cg, int cb, boolean useCamera, int fontSize)
	{
		if(useCamera && (Math.abs(x - camX) > c.getWidth() / camZoom || Math.abs(y - camY) > c.getHeight() / camZoom)) return;
		
		RenderOperation op = new RenderTextOperation(cr,cg,cb, text, fontSize, x,y);
		if(useCamera)
			ops.add(op);
		else
			UIops.add(op);
	}
}



















