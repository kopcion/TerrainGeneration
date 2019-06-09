package Tests;

import generators.utils.Point;
import generators.utils.Triangle;
import org.junit.Test;

import static generators.utils.Utils.isInsideTriangle;
import static org.junit.Assert.*;

public class UtilsTest {

    Point a = new Point(0,0);
    Point b = new Point(10,0);
    Point c = new Point(5,10);
    Triangle triangle = new Triangle(a,b,c);
    Point inside = new Point(2,1);
    Point outside = new Point(0,10);
    @Test
    public void isInsideTriangleTest() {
        assertTrue(isInsideTriangle(inside, triangle));
        assertFalse(isInsideTriangle(outside, triangle));
    }
}