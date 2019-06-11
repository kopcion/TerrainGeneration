package generators.utils;

import java.util.Objects;

public class LakePass {
    public Lake first;
    public Lake second;
    public Point a;
    public Point b;

    public LakePass(Lake first, Lake second, Point a, Point b) {
        this.first = first;
        this.second = second;
        this.a = a;
        this.b = b;
    }

    public LakePass(Lake first, Lake second) {
        this.first = first;
        this.second = second;
    }

    public double getPassHeight(){
        return Math.max(a.height, b.height);
    }

    public void updateLakes(){
        if(first == null || second == null) return;
        first.neighbours.add(this);
        second.neighbours.add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LakePass lakePass = (LakePass) o;
        return Objects.equals(first, lakePass.first) && Objects.equals(second, lakePass.second)
               || Objects.equals(second, lakePass.first) && Objects.equals(first, lakePass.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
