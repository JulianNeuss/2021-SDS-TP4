package ar.edu.itba.simulation.experiment.electric;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.systems.ElectricSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrajectoryVaryingSpeedExperiment {
    private static final double MIN_SPEED = Math.pow(10, 4);
    private static final double MAX_SPEED = Math.pow(10, 5);
    private static final int SPEEDS_QTY = 100;

    private static final double TIME_STEP = Math.pow(10, -14);
    private static final double MAX_TIME = 10;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/electric/trajectoryVaryingSpeed.txt";

    public static void main(String[] args) {
        ElectricSystem system = new ElectricSystem();
        List<List<Particle>> trajectories = new ArrayList<>();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
            List<Particle> trajectory = system.simulate(new Beeman(), TIME_STEP, MAX_TIME, new Velocity(speed, 0));
            trajectories.add(trajectory);
        }

        Map<Double, Double> speedToDistanceMap = new HashMap<>();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
            double distance = 0;
            Particle previousState = trajectories.get(attempt).get(0);
            for (Particle state : trajectories.get(attempt)){
                distance += previousState.getPosition().getDistanceTo(state.getPosition());
                previousState = state;
            }
            speedToDistanceMap.put(speed, distance);
        }

        StringBuilder str = new StringBuilder();
        for (Double speed : speedToDistanceMap.keySet().stream().sorted(Double::compare).collect(Collectors.toList())){
            str.append(speed).append(':').append(speedToDistanceMap.get(speed)).append('\n');
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
}