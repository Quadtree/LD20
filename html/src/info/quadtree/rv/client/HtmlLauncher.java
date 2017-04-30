package info.quadtree.rv.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import info.quadtree.rv.RobotVillageGame;

public class HtmlLauncher extends GwtApplication {

	@Override
	public ApplicationListener createApplicationListener() {
		return new RobotVillageGame();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(1024, 768);
	}
}