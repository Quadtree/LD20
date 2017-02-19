package rv;

import java.applet.Applet;
import java.awt.Image;

import sgf.ImageSource;
import sgf.SGF;

@SuppressWarnings("serial")
public class RobotVillage extends Applet implements ImageSource {

	public static RobotVillage s;
	
	@Override
	public void init() {
		//System.out.println("Applet starting...");
		s = this;
		
		SGF.getInstance().start(this, this, new Game());
		
		super.init();
	}

	@Override
	public Image getImageByName(String arg0) {
		return getImage(getDocumentBase(), "./media/" + arg0 + ".png");
	}

	@Override
	public void destroy() {
		//System.out.println("Applet shutting down...");
		SGF.getInstance().stop();
	}
}
