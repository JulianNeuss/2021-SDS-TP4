package ar.edu.itba.simulation;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.methods.TrajectoryAlgorithm;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.systems.ElectricSystem;
import ar.edu.itba.systems.System;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class SimulationElectricApp {
    private static final TrajectoryAlgorithm ALGORITHM = new Beeman();
    private static final double TIME_STEP = Math.pow(10, -15);
    private static final double MAX_TIME = 10;
    private static final int FRAME_RATE = 100;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/electric/beeman.txt";

    public static void main(String[] args) {
        System system = new ElectricSystem();
        List<Particle> trajectory = system.simulate(ALGORITHM, TIME_STEP, MAX_TIME);

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
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File " + DEFAULT_OUTPUT_FILENAME + " not found");
        }

    }
}
