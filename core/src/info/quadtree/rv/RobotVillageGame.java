package info.quadtree.rv;

import com.badlogic.gdx.ApplicationAdapter;

import info.quadtree.rv.graphics.SGF;

public class RobotVillageGame extends ApplicationAdapter {
	// SpriteBatch batch;
	// Texture img;

	@Override
	public void create() {
		// batch = new SpriteBatch();
		// img = new Texture("badlogic.jpg");

		SGF.getInstance().log("Create");
		SGF.getInstance().start(new Game());
	}

	@Override
	public void dispose() {
		// batch.dispose();
		// img.dispose();
	}

	@Override
	public void render() {
		// Gdx.gl.glClearColor(1, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// batch.begin();
		// batch.draw(img, 0, 0);
		// batch.end();

		SGF.getInstance().render();
	}
}
