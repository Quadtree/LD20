package info.quadtree.rv.graphics;

import com.badlogic.gdx.Input;

public class KeyEvent {
	public static int VK_1 = Input.Keys.NUM_1;
	public static int VK_2 = Input.Keys.NUM_2;
	public static int VK_3 = Input.Keys.NUM_3;
	public static int VK_4 = Input.Keys.NUM_4;
	public static int VK_A = Input.Keys.A;
	public static int VK_D = Input.Keys.D;
	public static int VK_ENTER = Input.Keys.ENTER;
	public static int VK_F1 = Input.Keys.F1;
	public static int VK_I = Input.Keys.I;
	public static int VK_O = Input.Keys.O;
	public static int VK_P = Input.Keys.P;
	public static int VK_R = Input.Keys.R;
	public static int VK_S = Input.Keys.S;
	public static int VK_W = Input.Keys.W;

	int keyCode;

	public KeyEvent(int keyCode) {
		super();
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
