package ar.edu.itba.systems;

import ar.edu.itba.methods.TrajectoryAlgorithm;
import ar.edu.itba.particle.Particle;

import java.util.List;

@FunctionalInterface
public interface System {
    List<Particle> simulate(TrajectoryAlgorithm algorithm, double timeStep, double maxTime);
}
