package sgf.demo;

import sgf.GameInterface;
import sgf.SGF;

public class SGFDemoGame implements GameInterface {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		SGF.getInstance().renderImage("x", 200, 200, 150, 150, 0.25f, true);
		SGF.getInstance().renderText("Test!", 0, 0, 255, 0, 0, true, 32);
		
		SGF.getInstance().setCamera(0, 0, 1);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
