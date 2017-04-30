package info.quadtree.rv.graphics;

public interface MouseMotionListener {

	/**
	 * Invoked when a mouse button is pressed on a component and then dragged.
	 * <code>MOUSE_DRAGGED</code> events will continue to be delivered to the
	 * component where the drag originated until the mouse button is released
	 * (regardless of whether the mouse position is within the bounds of the
	 * component).
	 * <p>
	 * Due to platform-dependent Drag&Drop implementations,
	 * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
	 * Drag&Drop operation.
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Invoked when the mouse cursor has been moved onto a component but no
	 * buttons have been pushed.
	 */
	public void mouseMoved(MouseEvent e);

}
