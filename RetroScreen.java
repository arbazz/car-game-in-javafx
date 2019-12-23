package retrocar;

/**
 * @author Jonathan Guéhenneux
 */
public class RetroScreen {

	private int width;
	private int height;

	private boolean[][] pixels;

	/**
	 * @param width
	 * @param height
	 */
	public RetroScreen(int width, int height) {

		this.width = width;
		this.height = height;

		pixels = new boolean[width][height];
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean getPixel(int x, int y) {
		return pixels[x][y];
	}

	/**
	 * @param x
	 * @param y
	 * @param pixel
	 */
	public void setPixel(int x, int y, boolean pixel) {

		if (x >= 0 && x < width && y >= 0 && y < height) {
			pixels[x][y] = pixel;
		}
	}
}