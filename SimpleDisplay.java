package retrocar;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Jonathan Guéhenneux
 */
public class SimpleDisplay extends Application {

	/**
	 * @param arguments
	 *            none
	 */
	public static void main(String... arguments) {
		launch(arguments);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		RetroGame game = new RetroCarGame();
		RetroScreen screen = game.getScreen();
		RetroScreenGui gui = new RetroScreenGui(screen);
		Group root = new Group(gui);
		Scene scene = new Scene(root);

		scene.setOnKeyPressed(game);
		scene.setOnKeyReleased(game);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Simple retroscreen display");
		primaryStage.show();

		Duration frameDuration = Duration.seconds(1.0 / 60.0);
		KeyFrame keyFrame = new KeyFrame(frameDuration, (onFinished) -> {
			game.update(frameDuration);
			gui.draw();
		});
		Animation animation = new Timeline(keyFrame);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}

	@Override
	public void stop() throws Exception {

		Platform.exit();
		System.exit(0);
	}
}