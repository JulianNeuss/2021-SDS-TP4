package ar.edu.itba.methods;

import ar.edu.itba.systems.Force;
import ar.edu.itba.particle.*;
import ar.edu.itba.systems.ForceCalculator;

public class Beeman implements TrajectoryAlgorithm{
    public Particle nextStep(Particle particle, Particle previousParticle, ForceCalculator forceCalculator, double timeStep){
        Force currentForce = forceCalculator.getForce(particle);
        double currentAccelerationX = currentForce.getX()/particle.getMass();
        double currentAccelerationY = currentForce.getY()/particle.getMass();

        if(previousParticle.getPosition() == null) {
            previousParticle = Euler.getPreviousToInitialParticle(particle, forceCalculator, timeStep);
        }
        Force previousForce = forceCalculator.getForce(previousParticle);
        double previousAccelerationX = previousForce.getX()/particle.getMass();
        double previousAccelerationY = previousForce.getY()/particle.getMass();

        double x = particle.getPosition().getX() + particle.getVelocity().getVelocityX() * timeStep
                + (2*currentAccelerationX/3 - previousAccelerationX/6) * timeStep * timeStep;
        double y = particle.getPosition().getY() + particle.getVelocity().getVelocityY() * timeStep
                + (2*currentAccelerationY/3 - previousAccelerationY/6) * timeStep * timeStep;

        double predictedVelocityX = particle.getVelocity().getVelocityX() + (3 * currentAccelerationX
                - previousAccelerationX) * timeStep / 2;

        double predictedVelocityY = particle.getVelocity().getVelocityY() + (3 * currentAccelerationY
                - previousAccelerationY) * timeStep / 2;

        Particle nextPredictedParticle = new Particle(particle.getMass(), new Position(x, y),
                new Velocity(predictedVelocityX, predictedVelocityY), particle.getElectricCharge());

        Force nextForce = forceCalculator.getForce(nextPredictedParticle);
        double nextAccelerationX = nextForce.getX() / particle.getMass();
        double nextAccelerationY = nextForce.getY() / particle.getMass();

        double correctVelocityX = particle.getVelocity().getVelocityX() +
                (nextAccelerationX/3 + 5*currentAccelerationX/6 - previousAccelerationX/6) * timeStep;
        double correctVelocityY = particle.getVelocity().getVelocityY() +
                (nextAccelerationY/3 + 5*currentAccelerationY/6 - previousAccelerationY/6) * timeStep;

        return new Particle(particle.getMass(), new Position(x, y), new Velocity(correctVelocityX, correctVelocityY), particle.getElectricCharge());
    }

    @Override
    public String toString() {
        return "Beeman";
    }
}
