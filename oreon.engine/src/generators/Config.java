package generators;

public class Config {
	public static final int MAP_SIZE = 1025;
	public static String PATH = "./oreon.engine/res/";
//	public static String PATH = "./res/";
//	public static String RESOURCE_PATH = "C:\\Users\\kopcion\\Desktop\\TerrainGeneration\\oreon.engine\\res\\heightmap";
	public static final double AMPLITUDE = 1;
	public static final double SMOOTHING_PARAM = 0.6d;
	public static final double RESOLUTION = 75;
	public static final int VORONOI_POINTS = 75;
	public static int VORONOI_NORM;
	public static boolean fileIsChosen = false;
	public static int SMOOTHIG_RANGE = 10;

	public static double EROSION_CONSTANT = 1d;

	public static double JITTER_ENABLED = 1d;

	public static double ACCURACY = 0.1d;
}
