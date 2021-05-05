package ar.edu.itba.methods;

import ar.edu.itba.particle.Particle;
import ar.edu.itba.systems.ForceCalculator;

@FunctionalInterface
public interface TrajectoryAlgorithm {
    Particle nextStep(Particle particle, Particle previousParticle, ForceCalculator forceCalculator, double timeStep);
}
