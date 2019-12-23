package retrocar;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * @author Jonathan Guéhenneux
 */
public class WaveGenerator implements Runnable {

	private static final int BUFFER_SIZE = 1024;
	private static final int SAMPLING_RATE = 44100;
	private static final int SAMPLE_SIZE = 1;
	private static final int CHANNEL_COUNT = 1;
	private static final boolean SIGNED = true;
	private static final boolean BIG_ENDIAN = true;
	private static final double AMPLIFICATION = 0.02;

	private static final AudioFormat FORMAT = new AudioFormat(SAMPLING_RATE, SAMPLE_SIZE * 8, CHANNEL_COUNT, SIGNED,
			BIG_ENDIAN);

	private static final double[] SINE;
	private static final double[] SQUARE;
	private static final double[] SAWTOOTH;
	private static final double[] RECTANGLE;

	static {

		SINE = new double[SAMPLING_RATE];
		SQUARE = new double[SAMPLING_RATE];
		SAWTOOTH = new double[SAMPLING_RATE];
		RECTANGLE = new double[SAMPLING_RATE];

		for (int sampleIndex = 0; sampleIndex < SAMPLING_RATE; sampleIndex++) {

			SINE[sampleIndex] = AMPLIFICATION * (Math.sin(2 * Math.PI * sampleIndex / SAMPLING_RATE));
			SQUARE[sampleIndex] = AMPLIFICATION * (sampleIndex < SAMPLING_RATE / 2 ? 1 : -1);
			SAWTOOTH[sampleIndex] = AMPLIFICATION * ((double) sampleIndex / SAMPLING_RATE - 0.5);
			RECTANGLE[sampleIndex] = AMPLIFICATION * (sampleIndex < SAMPLING_RATE / 10 ? 1 : -1);
		}
	}

	/**
	 * @param sample
	 * @param sampleSize
	 * @param signed
	 * @param bigEndian
	 * @param buffer
	 * @param index
	 */
	private static void writeSample(double sample, int sampleSize, boolean signed, boolean bigEndian, byte[] buffer,
			int offset) {

		switch (sampleSize) {

		case 1:

			byte sample8 = (byte) Math.round(sample * Byte.MAX_VALUE);
			buffer[offset] = sample8;
			break;

		case 2:

			short sample16 = (short) Math.round(sample * Short.MAX_VALUE);
			byte highByte = (byte) (sample16 >> 8);
			byte lowByte = (byte) (sample16 & 0xFF);

			if (bigEndian) {

				buffer[offset] = highByte;
				buffer[offset + 1] = lowByte;

			} else {

				buffer[offset] = lowByte;
				buffer[offset + 1] = highByte;
			}

			break;

		default:
			break;
		}
	}

	private SourceDataLine line;
	private double frequency;

	/**
	 * @throws LineUnavailableException
	 */
	public WaveGenerator() throws LineUnavailableException {

		DataLine.Info sourceDataLineInformations = new DataLine.Info(SourceDataLine.class, FORMAT);
		line = (SourceDataLine) AudioSystem.getLine(sourceDataLineInformations);
		line.open(FORMAT);
		line.start();
	}

	@Override
	public void run() {

		byte[] buffer = new byte[BUFFER_SIZE * SAMPLE_SIZE];
		double sample;

		double h0, h1, h2, h3, h4;
		double a0, a1, a2, a3, a4;

		h0 = 1.00;
		h1 = 0.75;
		h2 = 0.50;
		h3 = 0.25;
		h4 = 0.125;

		a0 = 1;
		a1 = 2;
		a2 = 3;
		a3 = 4;
		a4 = 5;

		double i0, i1, i2, i3, i4;
		double f0, f1, f2, f3, f4;
		double s0, s1, s2, s3, s4;

		i0 = i1 = i2 = i3 = i4 = 0;

		while (true) {

			for (int bufferIndex = 0; bufferIndex < BUFFER_SIZE; bufferIndex++) {

				f0 = frequency * h0;
				f1 = frequency * h1;
				f2 = frequency * h2;
				f3 = frequency * h3;
				f4 = frequency * h4;

				i0 += f0;
				i1 += f1;
				i2 += f2;
				i3 += f3;
				i4 += f4;

				i0 %= SAMPLING_RATE;
				i1 %= SAMPLING_RATE;
				i2 %= SAMPLING_RATE;
				i3 %= SAMPLING_RATE;
				i4 %= SAMPLING_RATE;

				s0 = a0 * SINE[(int) i0];
				s1 = a1 * SINE[(int) i1];
				s2 = a2 * SINE[(int) i2];
				s3 = a3 * SINE[(int) i3];
				s4 = a4 * SINE[(int) i4];

				sample = (s0 + s1 + s2 + s3 + s4) / (a0 + a1 + a2 + a3 + a4);

				writeSample(sample, SAMPLE_SIZE, SIGNED, BIG_ENDIAN, buffer, bufferIndex * SAMPLE_SIZE);
			}

			line.write(buffer, 0, BUFFER_SIZE * SAMPLE_SIZE);
		}
	}

	/**
	 * @param frequency
	 */
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
}