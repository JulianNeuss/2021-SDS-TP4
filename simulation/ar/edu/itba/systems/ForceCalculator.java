package ar.edu.itba.systems;

import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;

import java.util.List;

public interface ForceCalculator {
    Force getForce(Particle particle);
    List<Position> getRDerivatives(int order, Particle particle);
}
