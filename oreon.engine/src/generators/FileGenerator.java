package generators;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileGenerator {
	
	//static final String PATH = "C:\\Users\\kopcion\\Desktop\\PO_PROJECT\\Lwjgl3-Game-Engine-Programming-Series-starting_code\\oreon.engine\\res\\heightmaps\\";
	static final ColorSpace GRAY = ColorSpace.getInstance(ColorSpace.CS_GRAY);
	static int counter = 1;

	public static void generateFile(float values[][]) {
		Color color;
		final BufferedImage res = new BufferedImage(values.length, values[0].length, BufferedImage.TYPE_BYTE_GRAY);
		
		for(int x=0; x < values.length; x++) {
			for(int y=0; y < values[0].length; y++) {
				color = new Color(GRAY, new float[] {values[x][y]}, 1f);
				res.setRGB(x, y, color.getRGB());
			}
		}
		
		try {
			RenderedImage img = res;
			ImageIO.write(img, "bmp", new File(Config.PATH + Integer.toString(counter) + ".bmp"));
		} catch (IOException e) {
		}
		counter++;
	}
}
