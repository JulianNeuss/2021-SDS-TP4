package ar.edu.itba.systems;

import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.methods.TrajectoryAlgorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OscillatorySystem implements System, ForceCalculator{
    private static final double GAMMA = 100;
    private static final double K = 10_000;
    private static final double MASS = 70;
    private static final Position INITIAL_POSITION = new Position(1,0);
    private static final Velocity INITIAL_VELOCITY = new Velocity(-GAMMA/(2*MASS), 0);

    @Override
    public List<Particle> simulate(TrajectoryAlgorithm algorithm, double timeStep, double maxTime) {
        Particle previousParticle = new Particle(MASS, null, null, 0);
        Particle particle = new Particle(MASS, INITIAL_POSITION, INITIAL_VELOCITY, 0);
        List<Particle> particleStates = new LinkedList<>();
        particleStates.add(particle);
        for (double time = 0; time < maxTime; time += timeStep) {
            particle = algorithm.nextStep(particle, previousParticle, this, timeStep);
            previousParticle = particleStates.get(particleStates.size() - 1);
            particleStates.add(new Particle(particle));
        }
        return particleStates;
    }

    @Override
    public Force getForce(Particle particle){
        double forceX = - K * particle.getPosition().getX() - GAMMA * particle.getVelocity().getVelocityX();
        return new Force(forceX, 0);
    }

    @Override
    public List<Position> getRDerivatives(int order, Particle particle){
        List<Position> rDerivatives = new ArrayList<>(order + 1);
        if(order >= 0){
            rDerivatives.add(particle.getPosition());
        }
        if(order >= 1){
            rDerivatives.add(new Position(particle.getVelocity().getVelocityX(), particle.getVelocity().getVelocityY()));
        }
        for (int i = 2; i<=order; i++){
            double xDerivative = (- K * rDerivatives.get(i - 2).getX() - GAMMA * rDerivatives.get(i-1).getX())/ particle.getMass();
            double yDerivative = (- K * rDerivatives.get(i - 2).getY() - GAMMA * rDerivatives.get(i-1).getY())/ particle.getMass();
            rDerivatives.add(new Position(xDerivative, yDerivative));
        }
        return rDerivatives;
    }
}
