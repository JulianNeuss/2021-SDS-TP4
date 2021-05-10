package ar.edu.itba.simulation.experiment.oscillatory;

import ar.edu.itba.methods.Beeman;
import ar.edu.itba.methods.GearPredictorCorrector;
import ar.edu.itba.methods.TrajectoryAlgorithm;
import ar.edu.itba.methods.Verlet;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.systems.OscillatorySystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class VaryingTimeStepExperiment {
    private static final TrajectoryAlgorithm[] ALGORITHMS = {new Verlet(), new Beeman(), new GearPredictorCorrector()};
    private static final double MIN_TIME_STEP = Math.pow(10, -5);
    private static final double MAX_TIME_STEP = Math.pow(10, -2);
    private static final double TIME_STEP_QTY = 50;
    private static final double MAX_TIME = 10;
    private static final double FRAMES = 1000;
    private static final String DEFAULT_OUTPUT_FILENAME = "./data/oscillatory/varyingTimeStep.txt";

    public static void main(String[] args) {
        OscillatorySystem system = new OscillatorySystem();
        List<List<List<Particle>>> trajectories = new ArrayList<>(ALGORITHMS.length);
        for (int algorithmNumber = 0; algorithmNumber < ALGORITHMS.length; algorithmNumber++) {
            trajectories.add(new LinkedList<>());
            for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
                double timeStep = MIN_TIME_STEP + attempt * (MAX_TIME_STEP - MIN_TIME_STEP)/TIME_STEP_QTY;
                List<Particle> trajectory = system.simulate(ALGORITHMS[algorithmNumber], timeStep, MAX_TIME);
                trajectories.get(algorithmNumber).add(trajectory);
            }
            System.out.println(ALGORITHMS[algorithmNumber] + " done");
        }

        List<List<Particle>> analyticalSolutions = new ArrayList<>();
        for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
            double timeStep = MIN_TIME_STEP + attempt * (MAX_TIME_STEP - MIN_TIME_STEP)/TIME_STEP_QTY;
            List<Particle> analyticalSolution = OscillatorySystem.analyticalSolution(MAX_TIME, timeStep);
            analyticalSolutions.add(analyticalSolution);
        }
        System.out.println("Analytical solution generated");


        List<Map<Double, Double>> timeStepToErrorMaps = new LinkedList<>();
        for (int algorithmNumber = 0; algorithmNumber < ALGORITHMS.length; algorithmNumber++) {
            timeStepToErrorMaps.add(new HashMap<>());
            for (int attempt = 0; attempt < TIME_STEP_QTY; attempt++) {
                double timeStep = MIN_TIME_STEP + attempt * (MAX_TIME_STEP - MIN_TIME_STEP)/TIME_STEP_QTY;
                double errorAccumulator = 0;
                int errorsCalculated = 0;
                for(int stepNumber = 0; stepNumber < trajectories.get(algorithmNumber).get(attempt).size(); stepNumber++){
                    if(stepNumber % Math.ceil(trajectories.get(algorithmNumber).get(attempt).size()/FRAMES) == 0) {
                        Particle realState = analyticalSolutions.get(attempt).get(stepNumber);
                        Particle approximatedState = trajectories.get(algorithmNumber).get(attempt).get(stepNumber);
                        errorAccumulator += quarticError(realState, approximatedState);
                        errorsCalculated++;
                    }
                }
                timeStepToErrorMaps.get(algorithmNumber).put(timeStep, errorAccumulator/trajectories.get(algorithmNumber).get(attempt).size());
                System.out.println(timeStep + " : " + errorAccumulator/errorsCalculated);
            }
        }
        System.out.println("Errors calculated");

        StringBuilder str = new StringBuilder();
        for (int algorithmNumber = 0; algorithmNumber < ALGORITHMS.length; algorithmNumber++) {
            str.append(ALGORITHMS[algorithmNumber]).append('\n');
            for (Double timeStep : timeStepToErrorMaps.get(algorithmNumber).keySet().stream().sorted(Double::compare).collect(Collectors.toList())){
                str.append(timeStep).append(':').append(timeStepToErrorMaps.get(algorithmNumber).get(timeStep)).append('\n');
            }
            str.append('\n');
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

    private static double quarticError(Particle realState, Particle approximatedState) {
        double error = realState.getPosition().getX() - approximatedState.getPosition().getX();
        return error * error;
    }
}
