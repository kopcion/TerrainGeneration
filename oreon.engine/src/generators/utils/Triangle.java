package generators.utils;

import java.util.Objects;

public class Triangle {
    public Point a = null;
    public Point b = null;
    public Point c = null;
    public Triangle ab, bc, ca;

    public Triangle() {}

    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double area(){
        return (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y));
    }

    public Triangle getTriangle(Point one, Point two) {
        if(a==one && b==two || b==one && a==two)
            return ab;
        if(b==one && c==two || c==one && b==two)
            return bc;
        if(c==one && a==two || a==one && c==two)
            return ca;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(a, triangle.a) &&
                Objects.equals(b, triangle.b) &&
                Objects.equals(c, triangle.c) &&
                Objects.equals(ab, triangle.ab) &&
                Objects.equals(bc, triangle.bc) &&
                Objects.equals(ca, triangle.ca);
    }

    @Override
    public int hashCode() {

        return Objects.hash(a, b, c, ab, bc, ca);
    }
}
