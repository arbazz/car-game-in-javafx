package retrocar;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * @author Jonathan Guéhenneux
 */
public class RetroCarGame extends RetroGame {

	// minimum time between 2 rows of cars in the traffic
	private static final double MINIMUM_TRAFFIC_GAP = 15;

	// maximum time between 2 rows of cars in the traffic
	private static final double MAXIMUM_TRAFFIC_GAP = 22;

	// traffic speed
	private static final double TRAFFIC_SPPED = 14;

	private static final double[] FORCED_SPEEDS = { 20.4, 25.0, 28.8, 32.1, 35.0, 37.6, 39.9, 42.2, 44.2, 46.2, 48.0,
			49.8, 51.5, 53.1, 54.6, 56.1, 57.6, 58.9, 60.3, 61.6, 62.9 };

	private Random random;

	private int laneCount;

	private RetroCar playerCar;
	private RetroBorder border;
	private Queue<RetroCar> traffic;

	private double playerCarSpeed;
	private double nextCarsDistance;

	private boolean accelerate;
	private boolean brake;
	private int level;
	private double time;
	
	private WaveGenerator waveGenerator;

	/**
	 * @throws LineUnavailableException 
	 */
	public RetroCarGame() throws LineUnavailableException {

		random = new Random();

		laneCount = 3;

		time = 0;

		playerCarSpeed = 0;
		accelerate = false;
		brake = false;

		int screenHeight = screen.getHeight();

		playerCar = new RetroCar(laneCount == 2 ? 2 : 1, screenHeight - 4, screen);
		border = new RetroBorder(screen, 2, 6);
		traffic = new LinkedList<>();

		nextCarsDistance = 20;

		waveGenerator = new WaveGenerator();
		waveGenerator.setFrequency(1);
		new Thread(waveGenerator).start();
	}

	@Override
	public void update(Duration duration) {

		time += duration.toSeconds();
		level = (int) Math.floor(time / 20);
		double forcedSpeed = FORCED_SPEEDS[level];

		// drag force
		playerCarSpeed -= 0.0025 * playerCarSpeed * playerCarSpeed * duration.toSeconds();

		if (accelerate || playerCarSpeed < forcedSpeed) {

			// acceleration force
			playerCarSpeed += 20 * duration.toSeconds();

			if (!accelerate && playerCarSpeed > forcedSpeed) {
				playerCarSpeed = forcedSpeed;
			}

		} else {

			if (brake) {

				// brake force
				playerCarSpeed -= 20 * duration.toSeconds();
			}

			if (playerCarSpeed < forcedSpeed) {
				playerCarSpeed = forcedSpeed;
			}
		}

		waveGenerator.setFrequency(6 * (7000 + (playerCarSpeed % 20) / 20 * (8250 - 7000)) / 60);

		double playerCarDistance = playerCarSpeed * duration.toSeconds();
		double trafficDistance = TRAFFIC_SPPED * duration.toSeconds();
		double relativeTrafficDistance = playerCarDistance - trafficDistance;

		border.move(playerCarDistance);
		moveCars(relativeTrafficDistance);

		nextCarsDistance -= relativeTrafficDistance;

		if (nextCarsDistance <= 0) {
			spawnCars();
		}

		removeCars();
	}

	/**
	 * 
	 */
	private void moveCars(double distance) {

		for (RetroCar car : traffic) {
			car.move(0, distance);
		}
	}

	/**
	 * 
	 */
	private void spawnCars() {

		int randomNumber;

		if (laneCount == 2) {

			randomNumber = 1 + random.nextInt(2);

			if ((randomNumber & 0b10) != 0) {
				traffic.offer(new RetroCar(2, -4, screen));
			}

			if ((randomNumber & 0b01) != 0) {
				traffic.offer(new RetroCar(6, -4, screen));
			}

		} else {

			randomNumber = 1 + random.nextInt(6);

			if ((randomNumber & 0b100) != 0) {
				traffic.offer(new RetroCar(1, -4, screen));
			}

			if ((randomNumber & 0b010) != 0) {
				traffic.offer(new RetroCar(4, -4, screen));
			}

			if ((randomNumber & 0b001) != 0) {
				traffic.offer(new RetroCar(7, -4, screen));
			}
		}

		nextCarsDistance = MINIMUM_TRAFFIC_GAP + Math.random() * (MAXIMUM_TRAFFIC_GAP - MINIMUM_TRAFFIC_GAP);
	}

	/**
	 * 
	 */
	private void removeCars() {

		while (traffic.peek() != null && traffic.peek().getY() >= screen.getHeight()) {
			traffic.poll();
		}
	}

	@Override
	public void handle(KeyEvent keyEvent) {

		double playerCarX = playerCar.getX();

		KeyCode keyCode = keyEvent.getCode();
		boolean keyPressed = keyEvent.getEventType() == KeyEvent.KEY_PRESSED;

		switch (keyCode) {

		case LEFT:

			if (keyPressed) {

				if (laneCount == 2 && playerCarX != 2) {
					playerCar.move(-4, 0);
				} else if (laneCount == 3 && playerCarX != 1) {
					playerCar.move(-3, 0);
				}
			}

			break;

		case RIGHT:

			if (keyPressed) {

				if (laneCount == 2 && playerCarX != 6) {
					playerCar.move(+4, 0);
				} else if (laneCount == 3 && playerCarX != 7) {
					playerCar.move(+3, 0);
				}
			}

			break;

		case UP:

			accelerate = keyPressed;
			break;

		case DOWN:

			brake = keyPressed;
			break;

		default:
			break;
		}
	}
}