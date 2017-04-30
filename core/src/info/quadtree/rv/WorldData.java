package info.quadtree.rv;

import java.io.Serializable;

import sgf.SGF;

public class WorldData implements Serializable {
	public transient static boolean[] isPassable = { true, true, false, true };

	/**
	 *
	 */
	private static final long serialVersionUID = 5478728883871820758L;

	public transient static String[] tileGraphics = { "grass1", "floor", "wall", "dirt" };
	public byte[][] tiles;

	public WorldData() {
		tiles = new byte[1024][1024];
	}

	public void render() {
		for (int x = 0; x < tiles.length; x++)
			for (int y = 0; y < tiles[0].length; y++)
				SGF.getInstance().renderImage(tileGraphics[tiles[x][y]], x, y, 1, 1, 0, true);
	}
}
