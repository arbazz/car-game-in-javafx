package retrocar;


/**
 * @author Jonathan Guéhenneux
 */
public class RetroBorder {

	private double borderY;

	private int on;
	private int off;

	private RetroScreen screen;

	/**
	 * @param screen
	 * @param on
	 * @param off
	 */
	public RetroBorder(RetroScreen screen, int on, int off) {

		this.screen = screen;

		this.on = on;
		this.off = off;

		borderY = 0;

		draw();
	}

	/**
	 * @param dy
	 */
	public void move(double dY) {

		borderY += dY;

		draw();
	}

	/**
	 * 
	 */
	private void draw() {

		int screenWidth = screen.getWidth();
		int screenHeight = screen.getHeight();

		boolean pixel;

		for (int y = 0; y < screenHeight; y++) {

			pixel = (borderY - y) % (on + off) < on;

			screen.setPixel(0, y, pixel);
			screen.setPixel(screenWidth - 1, y, pixel);
		}
	}
}