package info.quadtree.rv.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SGF {
	private static SGF sgf = new SGF();

	public static SGF getInstance() {
		return sgf;
	}

	SpriteBatch batch;

	GameInterface game;

	long milisUpdated = 0;

	public void addKeyListener(KeyListener keyListener) {
		// @todo: Implment
	}

	public void addMouseListener(MouseListener mouseListener) {
		// @todo: Implment
	}

	public void addMouseMotionListener(MouseMotionListener mouseListener) {
		// @todo: Implment
	}

	public void playAudio(String name) {

	}

	public void removeKeyListener(KeyListener keyListener) {
		// @todo: Implment
	}

	public void removeMouseListener(MouseListener mouseListener) {
		// @todo: Implment
	}

	public void removeMouseMotionListener(MouseMotionListener mouseListener) {
		// @todo: Implment
	}

	public void render() {

		while (milisUpdated < System.currentTimeMillis()) {
			update();
			milisUpdated += 16;
		}

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		game.render();

		batch.end();
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

	public void start(GameInterface game) {
		this.game = game;
		batch = new SpriteBatch();
		milisUpdated = System.currentTimeMillis();

		game.init();
	}

	public void update() {
		game.update();
	}
}
