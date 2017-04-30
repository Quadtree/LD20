package info.quadtree.rv.graphics;

import java.awt.event.MouseListener;

public class SGF {
	private static SGF sgf = new SGF();

	public static SGF getInstance() {
		return sgf;
	}

	public void addKeyListener(KeyListener keyListener) {
		// @todo: Implment
	}

	public void addMouseListener(MouseListener mouseListener) {
		// @todo: Implment
	}

	public void playAudio(String name) {

	}

	public void renderImage(String imgName, float x, float y, float w, float h, float rot, boolean useCamera) {

	}

	public void renderText(String text, float x, float y, int cr, int cg, int cb, boolean useCamera, int fontSize) {

	}

	public Point2D screenToReal(Point2D pt) {
		return null;
	}

	public void setCamera(float x, float y, float zoom) {

	}
}
