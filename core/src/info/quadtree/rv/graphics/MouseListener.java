package info.quadtree.rv.graphics;

public interface MouseListener {

	/**
	 * Invoked when the mouse button has been clicked (pressed and released) on
	 * a component.
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e);

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e);

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e);
}