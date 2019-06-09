package Tests;

import generators.UpliftErosionGenerator;
import generators.utils.Point;
import generators.utils.Triangle;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpliftErosionGeneratorTest {

    Point a = new Point(0d,0d);
    Point b = new Point(2d,0d);
    Point c = new Point(0d,1d);
    Triangle triangle = new Triangle(a,b,c);
    Point a1 = new Point(1d,-1d);
    Point a2 = new Point(1d,1d);
    Triangle second = new Triangle(a,b,a1);


    UpliftErosionGenerator generator = new UpliftErosionGenerator();
    @Test
    public void checkIfPointLiesInsideTrianglesCircle() {
        triangle.ab = second;
        second.ab = triangle;
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, a2));
        assertFalse(generator.checkIfPointLiesInsideTrianglesCircle(triangle, triangle.getOpositePoint(a, b)));
        System.out.println(second.getOpositePoint(a, b).x + " " + second.getOpositePoint(a, b).y);
        generator.flipTriangles(triangle, second);
        System.out.println(triangle.a.x + " " + triangle.a.y);
        System.out.println(triangle.b.x + " " + triangle.b.y);
        System.out.println(triangle.c.x + " " + triangle.c.y);
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, b));
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, triangle.getOpositePoint( triangle.b, triangle.c)));
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(second, second.getOpositePoint( second.b, second.c)));

        b = new Point(4,0);
        c = new Point(0,3);
        triangle = new Triangle(a,b,c);
        Point inside = new Point(2, -0.1);
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, inside));
        inside = new Point(2,3);
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, inside));
        inside = new Point(-0.1,1);
        assertTrue(generator.checkIfPointLiesInsideTrianglesCircle(triangle, inside));
    }
}