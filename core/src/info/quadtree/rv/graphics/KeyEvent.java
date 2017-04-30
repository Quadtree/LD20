package info.quadtree.rv.graphics;

import com.badlogic.gdx.Input;

public class KeyEvent {
	public static int VK_ENTER = Input.Keys.ENTER;
	public static int VK_F1 = Input.Keys.F1;
	public static int VK_I = Input.Keys.I;
	public static int VK_O = Input.Keys.O;

	int keyCode;

	public KeyEvent(int keyCode) {
		super();
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
