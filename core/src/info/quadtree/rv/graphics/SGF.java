package info.quadtree.rv.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class SGF {
	private static SGF sgf = new SGF();

	public static SGF getInstance() {
		return sgf;
	}

	SpriteBatch batch;

	GameInterface game;

	Map<String, Texture> loadedImages = new HashMap<String, Texture>();

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

	public void log(String msg) {
		Gdx.app.log("SGF", msg);
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

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		game.render();

		batch.end();
	}

	public void renderImage(String imgName, float x, float y, float w, float h, float rot, boolean useCamera) {
		if (!loadedImages.containsKey(imgName))
			loadedImages.put(imgName, new Texture(Gdx.files.internal(imgName + ".png")));

		batch.draw(new TextureRegion(loadedImages.get(imgName)), x, y, w / 2, h / 2, w, h, 1, 1, rot * (180.f / (float) Math.PI));
	}

	public void renderText(String text, float x, float y, int cr, int cg, int cb, boolean useCamera, int fontSize) {

	}

	public Point2D screenToReal(Point2D pt) {
		return new Point2D(0, 0);
	}

	public void setCamera(float x, float y, float zoom) {
		// System.out.println(x + " " + y + " " + zoom);

		Matrix4 proj = new Matrix4();
		proj.idt();
		proj.scl(64);
		proj.scl(1.f / Gdx.graphics.getWidth(), 1.f / Gdx.graphics.getHeight(), 1);
		proj.translate(-x, -y, 0);
		// proj.translate(x, y, 0);

		batch.setProjectionMatrix(proj);
	}

	public void start(GameInterface game) {
		log("Starting up...");
		this.game = game;
		batch = new SpriteBatch();
		milisUpdated = System.currentTimeMillis();

		game.init();
	}

	public void update() {
		game.update();
	}
}
