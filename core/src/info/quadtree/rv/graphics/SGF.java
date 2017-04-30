package info.quadtree.rv.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class SGF implements InputProcessor {
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

	Set<KeyListener> keyListeners = new HashSet<KeyListener>();

	Map<String, BitmapFont> loadedFonts = new HashMap<String, BitmapFont>();

	Map<String, Texture> loadedImages = new HashMap<String, Texture>();

	long milisUpdated = 0;

	Set<MouseListener> mouseListeners = new HashSet<MouseListener>();
	Set<MouseMotionListener> mouseMotionListeners = new HashSet<MouseMotionListener>();

	Array<QueuedImage> normalRenderQueue = new Array<QueuedImage>();

	Matrix4 screenToReal = new Matrix4();

	Array<QueuedText> textRenderQueue = new Array<QueuedText>();
	SpriteBatch uiBatch;
	Array<QueuedImage> uiRenderQueue = new Array<QueuedImage>();

	Array<QueuedText> uiTextRenderQueue = new Array<QueuedText>();

	public void addKeyListener(KeyListener keyListener) {
		keyListeners.add(keyListener);
	}

	public void addMouseListener(MouseListener mouseListener) {
		mouseListeners.add(mouseListener);
	}

	public void addMouseMotionListener(MouseMotionListener mouseListener) {
		mouseMotionListeners.add(mouseListener);
	}

	private void doDrawImage(QueuedImage toRender, boolean useCamera) {
		if (!loadedImages.containsKey(toRender.imgName))
			loadedImages.put(toRender.imgName, new Texture(Gdx.files.internal(toRender.imgName + ".png")));

		(useCamera ? batch : uiBatch).draw(new TextureRegion(loadedImages.get(toRender.imgName)), toRender.x - toRender.w / 2, toRender.y - toRender.h / 2, toRender.w / 2, toRender.h / 2, toRender.w, toRender.h, 1, 1,
				toRender.rot * (180.f / (float) Math.PI));
	}

	private void doDrawText(QueuedText qt, boolean useCamera) {

		String fontName = "fnt_consolas_" + qt.fontSize;

		if (!loadedFonts.containsKey(fontName))
			loadedFonts.put(fontName, new BitmapFont(Gdx.files.internal(fontName + ".fnt")));

		loadedFonts.get(fontName).draw(useCamera ? batch : uiBatch, qt.text, qt.x, qt.y);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (KeyListener kl : keyListeners)
			kl.keyPressed(new KeyEvent(keycode));
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (KeyListener kl : keyListeners)
			kl.keyReleased(new KeyEvent(keycode));
		return true;
	}

	public void log(String msg) {
		Gdx.app.log("SGF", msg);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (MouseMotionListener mml : mouseMotionListeners)
			mml.mouseMoved(new MouseEvent(screenX, screenY, 0));
		return true;
	}

	public void playAudio(String name) {

	}

	public void removeKeyListener(KeyListener keyListener) {
		keyListeners.remove(keyListener);
	}

	public void removeMouseListener(MouseListener mouseListener) {
		mouseListeners.remove(mouseListener);
	}

	public void removeMouseMotionListener(MouseMotionListener mouseListener) {
		mouseMotionListeners.remove(mouseListener);
	}

	public void render() {

		while (milisUpdated < System.currentTimeMillis()) {
			update();
			milisUpdated += 16;
		}

		normalRenderQueue.clear();
		uiRenderQueue.clear();
		textRenderQueue.clear();
		uiTextRenderQueue.clear();

		game.render();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		for (QueuedImage tr : normalRenderQueue)
			doDrawImage(tr, true);
		batch.end();

		// uiBatch.setProjectionMatrix(new Matrix4().idt().scl(-2.f /
		// Gdx.graphics.getWidth(), -2.f / Gdx.graphics.getHeight(),
		// 1).translate(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight()
		// / 2, 0));

		uiBatch.begin();
		for (QueuedImage tr : uiRenderQueue)
			doDrawImage(tr, false);
		for (QueuedText qt : uiTextRenderQueue)
			doDrawText(qt, false);
		uiBatch.end();
	}

	public void renderImage(String imgName, float x, float y, float w, float h, float rot, boolean useCamera) {
		if (useCamera)
			normalRenderQueue.add(new QueuedImage(x, -y, w, h, rot, imgName));
		else
			uiRenderQueue.add(new QueuedImage(x, Gdx.graphics.getHeight() - y, w, h, rot, imgName));
	}

	public void renderText(String text, float x, float y, int cr, int cg, int cb, boolean useCamera, int fontSize) {
		if (useCamera)
			textRenderQueue.add(new QueuedText(text, x, -y, cr, cg, cb, fontSize));
		else
			uiTextRenderQueue.add(new QueuedText(text, x, Gdx.graphics.getHeight() - (y - fontSize / 2), cr, cg, cb, fontSize));
	}

	public Point2D screenToReal(Point2D pt) {
		Vector3 v3 = new Vector3(pt.x, pt.y, 1);

		// Matrix4 m4 = new Matrix4(batch.getProjectionMatrix());
		// m4.inv();
		v3.mul(screenToReal);

		// SGF.getInstance().log(pt.x + "," + pt.y + " -> " + v3);

		return new Point2D(v3.x, v3.y);
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCamera(float x, float y, float zoom) {
		// System.out.println(x + " " + y + " " + zoom);

		Matrix4 proj = new Matrix4();
		proj.idt();
		proj.scl(64);
		proj.scl(1.f / Gdx.graphics.getWidth(), 1.f / Gdx.graphics.getHeight(), 1);
		proj.translate(-x, y, 0);

		screenToReal.idt();
		// screenToReal.scl(Gdx.graphics.getWidth() / 2,
		// Gdx.graphics.getHeight() / 2, 1);
		screenToReal.translate(x, y, 0);
		screenToReal.scl(1.f / 32.f);
		screenToReal.translate(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, 0);

		batch.setProjectionMatrix(proj);
	}

	public void start(GameInterface game) {
		log("Starting up...");
		this.game = game;
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		milisUpdated = System.currentTimeMillis();

		Gdx.input.setInputProcessor(this);

		game.init();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (MouseListener ml : mouseListeners) {
			ml.mousePressed(new MouseEvent(screenX, screenY, button));
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (MouseMotionListener mml : mouseMotionListeners)
			mml.mouseDragged(new MouseEvent(screenX, screenY, 0));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (MouseListener ml : mouseListeners) {
			ml.mouseReleased(new MouseEvent(screenX, screenY, button));
		}
		return true;
	}

	public void update() {
		game.update();
	}
}
