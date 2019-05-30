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

    public void rotateTriangle(){
        Point tmp = a;
        a = b;
        b = c;
        c = tmp;
        Triangle tmp2 = ab;
        ab = bc;
        bc = ca;
        ca = tmp2;
    }

    public void reverse(){
        Point tmp = b;
        b = c;
        c = tmp;
        Triangle tmp2 = ab;
        ab = ca;
        ca = tmp2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return ( (a == triangle.a || a == triangle.b || a == triangle.c)
          &&(b == triangle.a || b == triangle.b || b == triangle.c)
          &&(c == triangle.a || c == triangle.b || c == triangle.c));

    }

    public Point getOpositePoint(Point x, Point y){
        if(x.equals(a) && y.equals(b) || x.equals(b) && y.equals(a)){
            if(!contains(ab.a)){
                return a;
            }
            if(!contains(ab.b)){
                return b;
            }
            if(!contains(ab.c)){
                return c;
            }
        } else if(x.equals(b) && y.equals(c) || x.equals(c) && y.equals(c)){
            if(!contains(bc.a)){
                return a;
            }
            if(!contains(bc.b)){
                return b;
            }
            if(!contains(bc.c)){
                return c;
            }
        } else if(x.equals(c) && y.equals(a) || x.equals(a) && y.equals(c)){
            if(!contains(ca.a)){
                return a;
            }
            if(!contains(ca.b)){
                return b;
            }
            if(!contains(ca.c)){
                return c;
            }
        }
        return null;
    }

    public boolean contains(Point point){
        return point.equals(a) || point.equals(b) || point.equals(c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    public static void orientate(Triangle one, Triangle two){
        for(int i=0; i < 3; i++){
            if(one.ab == two){
                break;
            }
            one.rotateTriangle();
        }
    }
}
