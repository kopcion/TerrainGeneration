package generators.utils;

import static generators.Config.ACCURACY;

public class Utils {
    public static boolean within(double a, double b){
        return (b + ACCURACY > a && b - ACCURACY < a);
    }

    public static double pow(double x, int n) {
        if(n == 0) {
            return 1;
        }
        return x * pow(x, n-1);
    }

    public static double nthRoot(int n, double number) {
        return (double)Math.pow(Math.E, Math.log(number)/n);
    }

    public static void print(Object[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                System.out.print(values[i][j] + " ");
            }
            System.out.print('\n');
        }
    }

    public static boolean isInsideTriangle(Point point, Triangle triangle){
        return within(triangle.area(), new Triangle(point, triangle.a, triangle.b).area()
                + new Triangle(point, triangle.b, triangle.c).area()
                + new Triangle(point, triangle.c, triangle.a).area()
        );
    }

    public static double dist(Point a, Point b){
        return Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
    }

    public static void findLakePass(Lake first, Lake second){

    }
}
