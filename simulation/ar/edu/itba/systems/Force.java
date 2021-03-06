package ar.edu.itba.systems;

import java.util.Objects;

public class Force {
    private double x;
    private double y;

    public Force(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistanceTo(Force other){
        double relativeX = x - other.x;
        double relativeY = y - other.y;
        return Math.sqrt(relativeX * relativeX + relativeY * relativeY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Force position = (Force) o;
        return Double.compare(position.x, x) == 0 && Double.compare(position.y, y) == 0;
    }

    public void sum(Force force){
        this.x += force.x;
        this.y += force.y;
    }

    public static Force fromVersorAndModule(Force versor, double module){
        return new Force(module * versor.x, module * versor.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
