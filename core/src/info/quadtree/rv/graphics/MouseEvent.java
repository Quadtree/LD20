package info.quadtree.rv.graphics;

import com.badlogic.gdx.Input;

public class MouseEvent {
	public static final int BUTTON1 = Input.Buttons.LEFT;
	public static final int BUTTON3 = Input.Buttons.RIGHT;

	int button;

	int x, y;

	public MouseEvent(int x, int y, int button) {
		super();
		this.button = button;
		this.x = x;
		this.y = y;
	}

	public int getButton() {
		return button;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
