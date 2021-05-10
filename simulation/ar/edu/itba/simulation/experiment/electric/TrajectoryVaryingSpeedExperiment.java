package ar.edu.itba.simulation.experiment.electric;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.systems.ElectricSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class TrajectoryVaryingSpeedExperiment {
    private static final double MIN_SPEED = Math.pow(10, 4);
    private static final double MAX_SPEED = Math.pow(10, 5);
    private static final int SPEEDS_QTY = 50;

    private static final double TIME_STEP = Math.pow(10, -14);
    private static final double MAX_TIME = 10;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/electric/trajectoryVaryingSpeed.txt";
    private static final int LENGTH = 16;
    private static final double DISTANCE_BETWEEN_PARTICLES = Math.pow(10, -8);
    private static final double MIN_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES - DISTANCE_BETWEEN_PARTICLES;
    private static final double MAX_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES + DISTANCE_BETWEEN_PARTICLES;
    private static final int REALIZATIONS = 100;

    public static void main(String[] args) {
        ElectricSystem system = new ElectricSystem();
        List<List<List<Particle>>> trajectories = new ArrayList<>();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            trajectories.add(new LinkedList<>());
            for(int realization=0; realization < REALIZATIONS; realization++) {
                double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
                List<Particle> trajectory = system.simulate(new Beeman(), TIME_STEP, MAX_TIME, randomPositionBetween(MIN_POSITION, MAX_POSITION), new Velocity(speed, 0));
                trajectories.get(attempt).add(trajectory);
            }
        }

        Map<Double, Double[]> speedToDistanceMap = new HashMap<>();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
            List<Double> distances = new LinkedList<>();
            for(int realization=0; realization < REALIZATIONS; realization++) {
                double distance = 0;
                Particle previousState = trajectories.get(attempt).get(realization).get(0);
                for (Particle state : trajectories.get(attempt).get(realization)) {
                    distance += previousState.getPosition().getDistanceTo(state.getPosition());
                    previousState = state;
                }
                distances.add(distance);
            }
            speedToDistanceMap.put(speed, new Double[]{average(distances), standardDeviation(distances)});
        }

        StringBuilder str = new StringBuilder();
        for (Double speed : speedToDistanceMap.keySet().stream().sorted(Double::compare).collect(Collectors.toList())){
            str.append(speed).append(':').append(speedToDistanceMap.get(speed)[0]).append(':').append(speedToDistanceMap.get(speed)[1]).append('\n');
        }

        // check file
        File outputFile = new File(Paths.get(DEFAULT_OUTPUT_FILENAME).toAbsolutePath().toString());
        if(!outputFile.getParentFile().exists()){
            if(!outputFile.getParentFile().mkdirs()){
                java.lang.System.err.println("Output's folder does not exist and could not be created");
                java.lang.System.exit(1);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(DEFAULT_OUTPUT_FILENAME).toAbsolutePath().toString(), false));
            writer.write(str.toString());
            writer.close();
        } catch (
                IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File " + DEFAULT_OUTPUT_FILENAME + " not found");
        }
    }

    private static Position randomPositionBetween(double minPosition, double maxPosition) {
        double y = Math.random() * (maxPosition - minPosition) + maxPosition;
        return new Position(0, y);
    }

    public static double average(List<Double> results){
        double aliveSum = 0;
        for (Double result : results){
            aliveSum += result;
        }

        return aliveSum/results.size();
    }

    public static double standardDeviation(List<Double> results){
        double aliveSum = 0;
        double aliveStandardDeviationAccum = 0;
        for (Double result : results){
            aliveSum += result;
        }

        double aliveMean = aliveSum/results.size();
        for (Double result : results){
            aliveStandardDeviationAccum += Math.pow(result - aliveMean, 2);
        }

        return Math.sqrt(aliveStandardDeviationAccum/results.size());
    }
}