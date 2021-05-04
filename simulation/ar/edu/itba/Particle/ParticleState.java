package ar.edu.itba.Particle;

public class ParticleState {
    private final Position position;
    private final Velocity velocity;

    public ParticleState(Position position, Velocity velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Position getPosition() {
        return position;
    }
    public Velocity getVelocity() {
        return velocity;
    }
}
