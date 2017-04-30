package info.quadtree.rve;

import javax.swing.JFrame;

import info.quadtree.rv.graphics.SGF;

public class RobotVillageEditor {
	public static JFrame mw;
	
	public static void main(String[] args)
	{
		mw = new JFrame();
		mw.setSize(1024, 768);
		mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mw.setVisible(true);
		
		SGF.getInstance().start(mw, new ImageLoader(), new Editor());
	}
}
