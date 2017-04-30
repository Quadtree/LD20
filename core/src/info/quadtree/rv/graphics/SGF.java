package info.quadtree.rv.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class SGF {
	class QueuedImage {
		String imgName;
		public float rot;
		public float x, y, w, h;

		public QueuedImage(float x, float y, float w, float h, float rot, String imgName) {
			super();
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.rot = rot;
			this.imgName = imgName;
		}

	}

	class QueuedText {
		public int fontSize;
		public String text;
		public float x, y, cr, cg, cb;

		public QueuedText(String text, float x, float y, float cr, float cg, float cb, int fontSize) {
			super();
			this.text = text;
			this.x = x;
			this.y = y;
			this.cr = cr;
			this.cg = cg;
			this.cb = cb;
			this.fontSize = fontSize;
		}

	}

	private static SGF sgf = new SGF();

	public static SGF getInstance() {
		return sgf;
	}

	SpriteBatch batch;

	GameInterface game;

	Map<String, Texture> loadedImages = new HashMap<String, Texture>();

	long milisUpdated = 0;

	Array<QueuedImage> normalRenderQueue = new Array<QueuedImage>();

	Array<QueuedText> textRenderQueue = new Array<QueuedText>();

	SpriteBatch uiBatch;
	Array<QueuedImage> uiRenderQueue = new Array<QueuedImage>();

	Array<QueuedText> uiTextRenderQueue = new Array<QueuedText>();

	public void addKeyListener(KeyListener keyListener) {
		// @todo: Implment
	}

	public void addMouseListener(MouseListener mouseListener) {
		// @todo: Implment
	}

	public void addMouseMotionListener(MouseMotionListener mouseListener) {
		// @todo: Implment
	}

	private void doDrawImage(QueuedImage toRender, boolean useCamera) {
		if (!loadedImages.containsKey(toRender.imgName))
			loadedImages.put(toRender.imgName, new Texture(Gdx.files.internal(toRender.imgName + ".png")));

		(useCamera ? batch : uiBatch).draw(new TextureRegion(loadedImages.get(toRender.imgName)), toRender.x, toRender.y, toRender.w / 2, toRender.h / 2, toRender.w, toRender.h, 1, 1, toRender.rot * (180.f / (float) Math.PI));
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

		normalRenderQueue.clear();
		uiRenderQueue.clear();

		game.render();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (QueuedImage tr : normalRenderQueue)
			doDrawImage(tr, true);
		batch.end();

		uiBatch.begin();
		for (QueuedImage tr : uiRenderQueue)
			doDrawImage(tr, false);
		uiBatch.end();
	}

	public void renderImage(String imgName, float x, float y, float w, float h, float rot, boolean useCamera) {
		QueuedImage tr = new QueuedImage(x, y, w, h, rot, imgName);

		if (useCamera)
			normalRenderQueue.add(tr);
		else
			uiRenderQueue.add(tr);
	}

	public void renderText(String text, float x, float y, int cr, int cg, int cb, boolean useCamera, int fontSize) {
		QueuedText tr = new QueuedText(text, x, y, cr, cg, cb, fontSize);

		if (useCamera)
			textRenderQueue.add(tr);
		else
			uiTextRenderQueue.add(tr);
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

		batch.setProjectionMatrix(proj);
	}

	public void start(GameInterface game) {
		log("Starting up...");
		this.game = game;
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		milisUpdated = System.currentTimeMillis();

		game.init();
	}

	public void update() {
		game.update();
	}
}
