package ar.edu.itba;

import java.util.Objects;

public class Particle implements Comparable<Particle>{
    private final int id;
    private final double mass;
    private final Position position;
    private final Velocity velocity;

    public Particle(Particle particle){
        id = particle.id;
        mass = particle.mass;
        position = new Position(particle.position.getX(), particle.position.getY());
        velocity = new Velocity(particle.velocity.getVelocityX(), particle.velocity.getVelocityY());
    }
    public Particle(int id, double mass, double radius, Position position, Velocity velocity) {
        this.id = id;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
    }

    public int getId() {
        return id;
    }

    public double getMass() {
        return mass;
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocityX, double velocityY){
        velocity.setVelocityX(velocityX);
        velocity.setVelocityY(velocityY);
    }

    public String toFileString(){
        return String.valueOf(id) + ' ' +
                getPosition().getX() + ' ' + getPosition().getY() +
                ' ' + velocity.getVelocityX() + ' ' + velocity.getVelocityY() +
                ' ' + getMass() + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Particle o) {
        return Integer.compare(id, o.id);
    }
}
