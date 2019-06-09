package generators.utils;

import static generators.utils.Utils.det;

public class Triangle {
    public Point a;
    public Point b;
    public Point c;
    public Point circumcentre;
    public Triangle ab, bc, ca;


    public Triangle(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void copyFrom(Triangle toCopy){
        a = toCopy.a;
        b = toCopy.b;
        c = toCopy.c;
        ab = toCopy.ab;
        bc = toCopy.bc;
        ca = toCopy.ca;
    }

    public Triangle(Point a, Point b, Point c, Triangle ab, Triangle bc, Triangle ca) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.ab = ab;
        this.bc = bc;
        this.ca = ca;
    }

    public double area(){
        return Math.abs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y))/2;
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
        return ( (a.equals(triangle.a) || a.equals(triangle.b) || a.equals(triangle.c))
          && (b.equals(triangle.a) || b.equals(triangle.b) || b.equals(triangle.c))
          && (c.equals(triangle.a) || c.equals(triangle.b) || c.equals(triangle.c)));

    }

    public Point getOpositePoint(Point x, Point y){
        if(x.equals(a) && y.equals(b) || x.equals(b) && y.equals(a)){
            if(ab == null){
                return null;
            }

            if(!contains(ab.a)){
                return ab.a;
            }
            if(!contains(ab.b)){
                return ab.b;
            }
            if(!contains(ab.c)){
                return ab.c;
            }
        } else if(x.equals(b) && y.equals(c) || x.equals(c) && y.equals(b)){
            if(bc == null){
                return null;
            }

            if(!contains(bc.a)){
                return bc.a;
            }
            if(!contains(bc.b)){
                return bc.b;
            }
            if(!contains(bc.c)){
                return bc.c;
            }
        } else if(x.equals(c) && y.equals(a) || x.equals(a) && y.equals(c)){
            if(ca == null){
                return null;
            }

            if(!contains(ca.a)){
                return ca.a;
            }
            if(!contains(ca.b)){
                return ca.b;
            }
            if(!contains(ca.c)){
                return ca.c;
            }
        }
        return null;
    }

    public boolean contains(Point point){
        return point.equals(a) || point.equals(b) || point.equals(c);
    }

    @Override
    public int hashCode() {
        return (int)(a.x + a.y + b.x + b.y  + c.x + c.y);//Objects.hash(a, b, c);
    }

    public void updateTriangleNeighbour(Triangle neighbour, Triangle newNeighbour){
        if(this.equals(neighbour.ab)){
            neighbour.ab = newNeighbour;
        } else if(this.equals(neighbour.bc)){
            neighbour.bc = newNeighbour;
        } else if(this.equals(neighbour.ca)){
            neighbour.ca = newNeighbour;
        }
    }

    public static void orientate(Triangle one, Triangle two){
        while(true){
            if(two.equals(one.ab)) return;
            one.rotateTriangle();
        }
//        System.out.println(two + "\n");
//        for(int i=0; i < 3; i++){
//            System.out.println("" + i + " " + one.ab);
//            if(one.ab != null && one.ab.equals(two)){
//                System.out.println("found!!\n");
//                return;
//            }
//            one.rotateTriangle();
//        }
//        System.out.println("\n");
    }

    public void calculateCircumcentre() {
        double x2y2_1 = a.x * a.x + a.y * a.y;
        double x2y2_2 = b.x * b.x + b.y * b.y;
        double x2y2_3 = c.x * c.x + c.y * c.y;

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        double[][] M11 = {
                {a.x,a.y, 1},
                {b.x,b.y, 1},
                {c.x,c.y, 1}};
        double[][] M12 = {
                {x2y2_1,a.y, 1},
                {x2y2_2,b.y, 1},
                {x2y2_3,c.y, 1}};
        double[][] M13 = {
                {x2y2_1,a.x, 1},
                {x2y2_2,b.x, 1},
                {x2y2_3,c.x, 1}};

        System.out.println("\t" + det(M12) + " / " + det(M11));
        System.out.println("\t" + det(M13) + " / " + det(M11));
        circumcentre = new Point(det(M12)/det(M11)/2, -det(M13)/det(M11)/2);

        a.voronoiCell.add(circumcentre);
        b.voronoiCell.add(circumcentre);
        c.voronoiCell.add(circumcentre);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
