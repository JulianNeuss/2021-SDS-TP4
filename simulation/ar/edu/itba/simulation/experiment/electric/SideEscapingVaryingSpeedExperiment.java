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

public class SideEscapingVaryingSpeedExperiment {
    private static final double MIN_SPEED = Math.pow(10, 3);
    private static final double MAX_SPEED = Math.pow(10, 5);
    private static final int SPEEDS_QTY = 100;
    private static final int LENGTH = 16;
    private static final double DISTANCE_BETWEEN_PARTICLES = Math.pow(10, -8);
    private static final double MIN_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES - DISTANCE_BETWEEN_PARTICLES;
    private static final double MAX_POSITION = (LENGTH-1)/2.0 * DISTANCE_BETWEEN_PARTICLES + DISTANCE_BETWEEN_PARTICLES;
    private static final double TIME_STEP = Math.pow(10, -14);
    private static final double MAX_TIME = 10;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/electric/directionVaryingSpeed.txt";
    private static final int MAX_REALIZATIONS_PER_SPEED = 100;

    public static void main(String[] args) {
        List<List<List<Particle>>> trajectories = new ArrayList<>();
        ElectricSystem system = new ElectricSystem();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            trajectories.add(new LinkedList<>());
            double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
            System.out.println("Testing speed:" + (double)attempt/SPEEDS_QTY * 100 + "%");
            for (int realization = 0; realization < MAX_REALIZATIONS_PER_SPEED; realization++) {
                trajectories.get(attempt).add(system.simulate(new Beeman(), TIME_STEP, MAX_TIME, randomPositionBetween(MIN_POSITION, MAX_POSITION), new Velocity(speed, 0)));
            }
        }

        Map<Double, Map<Direction, Long>> speedToDirectionMap = new HashMap<>();
        for (int attempt = 0; attempt < SPEEDS_QTY; attempt++) {
            double speed = MIN_SPEED + attempt * (MAX_SPEED - MIN_SPEED) / SPEEDS_QTY;
            speedToDirectionMap.put(speed, new HashMap<>());
            for (int realization = 0; realization < MAX_REALIZATIONS_PER_SPEED; realization++) {
                Particle lastState = trajectories.get(attempt).get(realization).get(trajectories.get(attempt).get(realization).size() - 1);
                Direction direction = getDirection(lastState);
                speedToDirectionMap.get(speed).put(direction, speedToDirectionMap.get(speed).getOrDefault(direction, 0L) + 1);
            }
        }

        StringBuilder str = new StringBuilder();
        for (double speed : speedToDirectionMap.keySet().stream().sorted(Double::compare).collect(Collectors.toList())){
            str.append(speed).append('\n');
            for (Direction direction : Direction.values()) {
                str.append(direction).append(':').append(speedToDirectionMap.get(speed).getOrDefault(direction, 0L)).append(' ');
            }
            str.append('\n').append('\n');
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

    private static Direction getDirection(Particle lastState) {
        Position position = lastState.getPosition();
        if (position.getX() > LENGTH * DISTANCE_BETWEEN_PARTICLES){
            return Direction.RIGHT;
        } else if (position.getX() < 0){
            return Direction.LEFT;
        } else if (position.getY() > (LENGTH - 1)* DISTANCE_BETWEEN_PARTICLES){
            return Direction.UP;
        } else if (position.getY() < 0){
            return Direction.DOWN;
        } else {
            return Direction.ABSORBED;
        }
    }

    private static Position randomPositionBetween(double minPosition, double maxPosition) {
        double y = Math.random() * (maxPosition - minPosition) + maxPosition;
        return new Position(0, y);
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ABSORBED
    }
}
