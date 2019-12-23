package retrocar;
/**
 * @author Jonathan Guéhenneux
 */
public class RetroCar {

	private double x;
	private double y;

	private RetroScreen screen;

	/**
	 * @param x
	 * @param y
	 * @param screen
	 */
	public RetroCar(double x, double y, RetroScreen screen) {

		this.x = x;
		this.y = y;

		this.screen = screen;

		draw(true);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void move(double dX, double dY) {

		draw(false);

		x += dX;
		y += dY;

		draw(true);
	}

	/**
	 * @param pixel
	 */
	private void draw(boolean pixel) {

		int roundedX = (int) Math.round(x);
		int roundedY = (int) Math.round(y);
		
		screen.setPixel(roundedX, roundedY + 3, pixel);
		screen.setPixel(roundedX, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY + 2, pixel);
		screen.setPixel(roundedX + 1, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY + 0, pixel);
		screen.setPixel(roundedX + 2, roundedY + 3, pixel);
		screen.setPixel(roundedX + 2, roundedY + 1, pixel);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
}