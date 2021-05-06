package ar.edu.itba.methods;

import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.systems.Force;
import ar.edu.itba.systems.ForceCalculator;

public class Euler {
    public static Particle getPreviousToInitialParticle(Particle particle, ForceCalculator forceCalculator, double timeStep){
        Force force = forceCalculator.getForce(particle);
        double velocityX = particle.getVelocity().getVelocityX() - timeStep * force.getX() / particle.getMass();
        double velocityY = particle.getVelocity().getVelocityY() - timeStep * force.getY() / particle.getMass();

        double x = particle.getPosition().getX() - timeStep * velocityX - timeStep * timeStep * force.getX()/ (2 * particle.getMass());
        double y = particle.getPosition().getY() - timeStep * velocityY - timeStep * timeStep * force.getY()/ (2 * particle.getMass());

        return new Particle(particle.getId(), particle.getMass(), new Position(x, y), new Velocity(x, y));
    }
}
