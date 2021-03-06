package generators;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static generators.Config.PATH;

public class FileGenerator {

	private static final ColorSpace GRAY = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	private static int counter = 1;

	public static void generateFile(double values[][]) {
		try {
			RenderedImage img = getRenderedImage(values);
			ImageIO.write(img, "bmp", new File(Config.PATH + Integer.toString(counter) + ".bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		counter++;
	}

	public static void generateFileInPlace(double values[][]) {
		try {
			RenderedImage img = getRenderedImage(values);
			ImageIO.write(img, "bmp", new File(Config.PATH + "heightmap/2.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static RenderedImage getRenderedImage(double values[][]){
		Color color;
		final BufferedImage res = new BufferedImage(values.length, values[0].length, BufferedImage.TYPE_BYTE_GRAY);

		for (int x = 0; x < values.length; x++) {
			for (int y = 0; y < values[0].length; y++) {
				color = new Color(GRAY, new float[]{(float) values[x][y]}, 1f);
				res.setRGB(x, y, color.getRGB());
			}
		}
		return res;
	}

	public static double[][] loadFromFile(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		int width = img.getWidth();
		int height = img.getHeight();
		double[][] values = new double[width][height];
		Raster raster = img.getData();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				values[i][j] = raster.getSampleDouble(i, j, 0) / 256d;
			}
		}
		return values.clone();
	}

	public static double[][] loadFromFile(String fileName) throws IOException {
		File file = new File(Config.PATH + fileName);
		return loadFromFile(file);
	}
}