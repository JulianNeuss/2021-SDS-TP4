package ar.edu.itba.simulation;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.methods.GearPredictorCorrector;
import ar.edu.itba.methods.TrajectoryAlgorithm;
import ar.edu.itba.methods.Verlet;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.systems.ElectricSystem;
import ar.edu.itba.systems.OscillatorySystem;
import ar.edu.itba.systems.System;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class SimulationOscillatoryApp {
    private static final TrajectoryAlgorithm[] ALGORITHMS = {new Verlet(), new Beeman(), new GearPredictorCorrector()};
    private static final double TIME_STEP = Math.pow(10, -15);
    private static final double MAX_TIME = 10;
    private static final int FRAME_RATE = 50;
    private static final String[] FILENAMES = {"./data/oscillatory/verlet.txt", "./data/oscillatory/beeman.txt", "./data/oscillatory/gear.txt"};

    public static void main(String[] args) {
        for (int algorithmNumber = 0; algorithmNumber < ALGORITHMS.length; algorithmNumber++) {
            simulate(ALGORITHMS[algorithmNumber], FILENAMES[algorithmNumber]);
        }
    }

    public static void simulate(TrajectoryAlgorithm algorithm, String outputFilename) {
        System system = new OscillatorySystem();
        List<Particle> trajectory = system.simulate(algorithm, TIME_STEP, MAX_TIME);

        StringBuilder str = new StringBuilder();
        double time = 0;
        double frame = 0;
        for (Particle particleState : trajectory){
            if(frame % FRAME_RATE == 0) {
                str
                        .append(time).append(' ')
                        .append(particleState.getPosition().getX()).append(' ')
                        .append(particleState.getPosition().getY()).append(' ')
                        .append(particleState.getVelocity().getVelocityX()).append(' ')
                        .append(particleState.getVelocity().getVelocityY()).append('\n');
            }
            frame++;
            time += TIME_STEP;
        }

        // check file
        File outputFile = new File(Paths.get(outputFilename).toAbsolutePath().toString());
        if(!outputFile.getParentFile().exists()){
            if(!outputFile.getParentFile().mkdirs()){
                java.lang.System.err.println("Output's folder does not exist and could not be created");
                java.lang.System.exit(1);
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(outputFilename).toAbsolutePath().toString(), false));
            writer.write(str.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File " + outputFilename + " not found");
        }

    }
}
