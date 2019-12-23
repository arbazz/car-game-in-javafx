package retrocar;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Jonathan Guéhenneux
 */
public class RetroScreenGui extends Canvas {

	private static final Paint PIXEL_ON = Color.rgb(70, 74, 70);
	private static final Paint PIXEL_OFF = Color.rgb(175, 182, 175);
	private static final Paint BACKGROUND = Color.rgb(181, 188, 181);

	private static final int PIXEL_WIDTH = 12;
	private static final int PIXEL_HEIGHT = 12;

	private RetroScreen model;

	private int width;
	private int height;

	/**
	 * @param model
	 */
	public RetroScreenGui(RetroScreen model) {

		this.model = model;

		width = model.getWidth();
		height = model.getHeight();

		setWidth(width * PIXEL_WIDTH);
		setHeight(height * PIXEL_HEIGHT);
	}

	/**
	 * 
	 */
	public void draw() {

		GraphicsContext graphicsContext = getGraphicsContext2D();
		graphicsContext.setFill(BACKGROUND);
		graphicsContext.fillRect(0, 0, width * PIXEL_WIDTH, height * PIXEL_HEIGHT);

		boolean pixel;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				pixel = model.getPixel(x, y);
				drawPixel(graphicsContext, x, y, pixel);
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	private void drawPixel(GraphicsContext graphicsContext, int x, int y, boolean pixel) {

		Paint paint = pixel ? PIXEL_ON : PIXEL_OFF;

		graphicsContext.setFill(paint);
		graphicsContext.setStroke(paint);

		graphicsContext.strokeRect(x * PIXEL_WIDTH + 0.5, y * PIXEL_HEIGHT + 0.5, PIXEL_WIDTH - 1, PIXEL_HEIGHT - 1);
		graphicsContext.fillRect(x * PIXEL_WIDTH + 2, y * PIXEL_HEIGHT + 2, PIXEL_WIDTH - 4, PIXEL_HEIGHT - 4);
	}
}