package ar.edu.itba.systems;

import ar.edu.itba.methods.GearPredictorCorrector;
import ar.edu.itba.methods.TrajectoryAlgorithm;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElectricSystem implements System, ForceCalculator{
    private static final int LENGTH = 16;
    private static final double K = Math.pow(10, 10);
    private static final double CHARGE = Math.pow(10, -19);
    private static final double MASS = Math.pow(10, -27);
    private static final double DISTANCE_BETWEEN_PARTICLES = Math.pow(10, -8);
    private static final double MIN_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES - DISTANCE_BETWEEN_PARTICLES;
    private static final double MAX_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES + DISTANCE_BETWEEN_PARTICLES;
    private static final Position INITIAL_POSITION = randomPositionBetween(MIN_POSITION, MAX_POSITION);
    private static final double MIN_VELOCITY = Math.pow(10, 4);
    private static final double MAX_VELOCITY = Math.pow(10,5);
    private static final Velocity INITIAL_VELOCITY = randomVelocityBetween(MIN_VELOCITY, MAX_VELOCITY);
    private static final List<List<Particle>> particles = systemConfiguration();

    @Override
    public List<Particle> simulate(TrajectoryAlgorithm algorithm, double timeStep, double maxTime) {
        if(algorithm.getClass() == GearPredictorCorrector.class){
            throw new IllegalArgumentException("The electric system does not support Gear method");
        }
        Particle previousParticle = new Particle(MASS, null, null, CHARGE);
        Particle particle = new Particle(MASS, INITIAL_POSITION, INITIAL_VELOCITY, CHARGE);
        List<Particle> particleStates = new LinkedList<>();
        particleStates.add(particle);
        for (double time = 0; !endCondition(particle) && time < maxTime; time += timeStep) {
            particle = algorithm.nextStep(particle, previousParticle, this, timeStep);
            previousParticle = particleStates.get(particleStates.size() - 1);
            particleStates.add(new Particle(particle));
        }
        return particleStates;
    }

    private boolean endCondition(Particle particle) {
        boolean outX = particle.getPosition().getX() < 0 || particle.getPosition().getX() > LENGTH * DISTANCE_BETWEEN_PARTICLES;
        boolean outY = particle.getPosition().getY() < 0 || particle.getPosition().getY() > (LENGTH - 1) * DISTANCE_BETWEEN_PARTICLES;
        if(outX || outY){
            return true;
        }
        for (List<Particle> row : particles){
            for (Particle particle2 : row){
                if(particle.getPosition().getDistanceTo(particle2.getPosition()) < 0.01 * DISTANCE_BETWEEN_PARTICLES){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Force getForce(Particle particle1) {
        Force totalForce = new Force(0, 0);
        for (List<Particle> row : particles){
            for (Particle particle2 : row){
                totalForce.sum(getForceInParticle1(particle1, particle2));
            }
        }
        java.lang.System.out.println("Force: (" +totalForce.getX() + ", " + totalForce.getY() + ")" );
        return totalForce;
    }

    public double getPotentialEnergy(Particle particle1) {
        double energy = 0;
        for (List<Particle> row : particles){
            for (Particle particle2 : row){
                energy += K * particle1.getElectricCharge() * particle2.getElectricCharge() / particle1.getPosition().getDistanceTo(particle2.getPosition());
            }
        }
        return energy;
    }

    @Override
    public List<Position> getRDerivatives(int order, Particle particle) {
        return null; // fiaca derivar, muy dificil
    }

    private static List<List<Particle>> systemConfiguration(){
        List<List<Particle>> system = new ArrayList<>(LENGTH);
        for (int row = 0; row<LENGTH; row++){
            system.add(new ArrayList<>(LENGTH));
            for (int column = 0; column<LENGTH; column++){
                system.get(row).add(new Particle(MASS, new Position((column+1)*DISTANCE_BETWEEN_PARTICLES, row*DISTANCE_BETWEEN_PARTICLES),
                        new Velocity(0,0), ((row+column)%2 == 0)?CHARGE:-CHARGE));
            }
        }
        return system;
    }

    private static Force getForceInParticle1(Particle particle1, Particle particle2){
        double distance = particle1.getPosition().getDistanceTo(particle2.getPosition());
        double module = -K * particle1.getElectricCharge() * particle2.getElectricCharge() / (distance * distance);
        Force versor = particle1.getVersor(particle2);
        return Force.fromVersorAndModule(versor, module);
    }

    private static Position randomPositionBetween(double minPosition, double maxPosition) {
        double y = Math.random() * (maxPosition - minPosition) + maxPosition;
        return new Position(0, y);
    }

    private static Velocity randomVelocityBetween(double minVelocity, double maxVelocity) {
        double x = Math.random() * (maxVelocity - minVelocity) + minVelocity;
        return new Velocity(x, 0);
    }
}
