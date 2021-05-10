package ar.edu.itba.simulation.experiment.electric;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.systems.ElectricSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class EnergyExperiment {
    private static final double MIN_EXP = -16;
    private static final double MAX_EXP = -12;
    private static final double MIN_TIME_STEP = Math.pow(10, MIN_EXP);
    private static final double MAX_TIME_STEP = Math.pow(10, MAX_EXP);
    private static final double TIME_STEP_QTY = 4;
    private static final double MAX_TIME = 10;
    private static final double FRAMES = 10000000;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/electric/energyVaryingTimeStep.txt";

    public static void main(String[] args) {
        ElectricSystem system = new ElectricSystem();
        List<List<Particle>> trajectories = new ArrayList<>();
        for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
            double timeStep = Math.pow(10, MIN_EXP + attempt * (MAX_EXP - MIN_EXP)/TIME_STEP_QTY);
            List<Particle> trajectory = system.simulate(new Beeman(), timeStep, MAX_TIME);
            trajectories.add(trajectory);
        }

        List<Double> initialEnergies = new ArrayList<>();
        for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
            Particle startingState = trajectories.get(attempt).get(0);
            double speed = startingState.getVelocity().getSpeed();
            initialEnergies.add(system.getPotentialEnergy(startingState) + startingState.getMass() * speed * speed / 2);
        }

        Map<Double, List<Double>> timeStepToErrorsMap = new HashMap<>();
        for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
            double timeStep = Math.pow(10, MIN_EXP + attempt * (MAX_EXP - MIN_EXP)/TIME_STEP_QTY);
            timeStepToErrorsMap.put(timeStep, new LinkedList<>());
            for(int stepNumber = 0; stepNumber < trajectories.get(attempt).size(); stepNumber++){
                if(stepNumber % Math.ceil(trajectories.get(attempt).size()/FRAMES) == 0) {
                    Particle currentState = trajectories.get(attempt).get(stepNumber);
                    double speed = currentState.getVelocity().getSpeed();
                    double currentEnergy = system.getPotentialEnergy(currentState) + currentState.getMass() * speed * speed / 2;
                    timeStepToErrorsMap.get(timeStep).add(Math.abs(initialEnergies.get(attempt) - currentEnergy));
                }
            }
        }

        StringBuilder str = new StringBuilder();
        for (Double timeStep : timeStepToErrorsMap.keySet().stream().sorted(Double::compare).collect(Collectors.toList())){
            str.append(timeStep).append('\n');
            for (Double error : timeStepToErrorsMap.get(timeStep)) {
                    str.append(error).append(' ');
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File " + DEFAULT_OUTPUT_FILENAME + " not found");
        }

    }
}
