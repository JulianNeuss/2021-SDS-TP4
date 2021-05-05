package ar.edu.itba.methods;

import ar.edu.itba.systems.Force;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.systems.ForceCalculator;

public class Verlet implements TrajectoryAlgorithm {
    @Override
    public Particle nextStep(Particle particle, Particle previousParticle, ForceCalculator forceCalculator, double timeStep){
        Force force = forceCalculator.getForce(particle);
        Position previousPosition = previousParticle.getPosition();
        if (previousPosition == null){
            previousPosition = getPreviousToInitialParticle(particle, forceCalculator, timeStep).getPosition();
        }

        double x = 2*particle.getPosition().getX() - previousPosition.getX() + (timeStep * timeStep / particle.getMass()) * force.getX();
        double y = 2*particle.getPosition().getY() - previousPosition.getY() + (timeStep * timeStep / particle.getMass()) * force.getY();
        Position nextPosition = new Position(x, y);

        // TODO: Chequear, en realidad creo que estoy recalculando la velocidad actual
        double velocityX = (nextPosition.getX() - previousPosition.getX())/(2*timeStep);
        double velocityY = (nextPosition.getY() - previousPosition.getY())/(2*timeStep);
        Velocity nextVelocity = new Velocity(velocityX, velocityY);
        return new Particle(particle.getId(), particle.getMass(), nextPosition, nextVelocity);
    }

    private static Particle getPreviousToInitialParticle(Particle particle, ForceCalculator forceCalculator, double timeStep){
        Force force = forceCalculator.getForce(particle);
        double velocityX = particle.getVelocity().getVelocityX() - timeStep * force.getX() / particle.getMass();
        double velocityY = particle.getVelocity().getVelocityY() - timeStep * force.getY() / particle.getMass();

        double x = particle.getPosition().getX() - timeStep * velocityX - timeStep * timeStep * force.getX()/ (2 * particle.getMass());
        double y = particle.getPosition().getY() - timeStep * velocityY - timeStep * timeStep * force.getY()/ (2 * particle.getMass());

        return new Particle(particle.getId(), particle.getMass(), new Position(x, y), new Velocity(x, y));
    }
}
