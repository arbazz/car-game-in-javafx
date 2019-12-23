package retrocar;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * @author Jonathan Guéhenneux
 */
public abstract class RetroGame implements EventHandler<KeyEvent> {

	protected RetroScreen screen;

	/**
	 * 
	 */
	public RetroGame() {
		screen = new RetroScreen(11, 30);
	}

	/**
	 * @param duration
	 */
	public abstract void update(Duration duration);

	/**
	 * @return the screen
	 */
	public RetroScreen getScreen() {
		return screen;
	}
}