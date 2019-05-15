package generators.utils;

public class Line{
    int x1;
    int x2;
    int y1;
    int y2;

    public Line(int a, int b, int c, int d) {
        x1 = a;
        y1 = b;
        x2 = c;
        y2 = d;
    }

    public Line(Point one, Point two){
        x1 = (int) one.x;
        y1 = (int) one.y;
        x2 = (int) two.x;
        y2 = (int) two.y;
    }

    public boolean checkPoint(int x, int y) {
        return (y1 - y2) * x + y1 * (x1 - x2) - x1 * (y1 - y2) == y * (x1 - x2);
    }
}