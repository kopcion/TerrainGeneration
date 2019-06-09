package Tests;

import generators.utils.Point;
import generators.utils.Triangle;
import org.junit.Test;

import static org.junit.Assert.*;

public class TriangleTest {

    Point a = new Point(0d,0d);
    Point b = new Point(2d,0d);
    Point c = new Point(0d,1d);

    Point a1 = new Point(0d,-1d);
    Triangle ab = new Triangle(a,b,a1);

    Point a2 = new Point(2d,1d);
    Triangle bc = new Triangle(b,a2,c);

    Point a3 = new Point(-1d,0d);
    Triangle ca = new Triangle(c,a,a3);

    Triangle triangle = new Triangle(a,b,c,ab,bc,ca);


    @Test
    public void area() {
        System.out.println(triangle.area());
        assertTrue(triangle.area() == 1d);
    }

    @Test
    public void rotateTriangle() {
        assertTrue(triangle.a == a);
        assertTrue(triangle.ab == ab);
        triangle.rotateTriangle();
        assertTrue(triangle.a == b);
        assertTrue(triangle.ab == bc);
        triangle.rotateTriangle();
        assertTrue(triangle.a == c);
        assertTrue(triangle.ab == ca);
        triangle.rotateTriangle();
        assertTrue(triangle.a == a);
        assertTrue(triangle.ab == ab);
    }

    @Test
    public void reverse() {
        assertTrue(triangle.b == b);
        assertTrue(triangle.c == c);
        assertTrue(triangle.ab == ab);
        assertTrue(triangle.ca == ca);
        triangle.reverse();
        assertTrue(triangle.b == c);
        assertTrue(triangle.c == b);
        assertTrue(triangle.ab == ca);
        assertTrue(triangle.ca == ab);
        triangle.reverse();
        assertTrue(triangle.b == b);
        assertTrue(triangle.c == c);
        assertTrue(triangle.ab == ab);
        assertTrue(triangle.ca == ca);
    }

    @Test
    public void getOpositePoint() {
        assertTrue(triangle.getOpositePoint(a, b).equals(a1));
        assertTrue(triangle.getOpositePoint(b, a).equals(a1));

        assertTrue(triangle.getOpositePoint(b, c).equals(a2));
        assertTrue(triangle.getOpositePoint(c, b).equals(a2));

        assertTrue(triangle.getOpositePoint(a, c).equals(a3));
        assertTrue(triangle.getOpositePoint(c, a).equals(a3));
    }

    @Test
    public void contains() {
        assertTrue(triangle.contains(a));
        assertTrue(triangle.contains(b));
        assertTrue(triangle.contains(c));

        assertFalse(triangle.contains(a1));
        assertFalse(triangle.contains(a2));
        assertFalse(triangle.contains(a3));
    }

    @Test
    public void orientate() {
        assertTrue(triangle.ab.equals(ab));

        assertFalse(triangle.ab.equals(ca));
        Triangle.orientate(triangle, ca);
        assertTrue(triangle.ab.equals(ca));

        assertFalse(triangle.ab.equals(bc));
        Triangle.orientate(triangle, bc);
        assertTrue(triangle.ab.equals(bc));
    }

    @Test
    public void calculateCircumcentre(){
        triangle.calculateCircumcentre();
        assertTrue(triangle.circumcentre.equals(new Point(1, 0.5)));
    }
}