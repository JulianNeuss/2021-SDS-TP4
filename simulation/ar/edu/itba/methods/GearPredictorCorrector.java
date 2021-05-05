package ar.edu.itba.methods;

import ar.edu.itba.systems.Force;
import ar.edu.itba.particle.Particle;
import ar.edu.itba.particle.Position;
import ar.edu.itba.particle.Velocity;
import ar.edu.itba.systems.ForceCalculator;

import java.util.ArrayList;
import java.util.List;

public class GearPredictorCorrector implements TrajectoryAlgorithm{

    private static final double[] ALFA = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};
    private static final int ORDER = 5;

    public Particle nextStep(Particle particle, Particle previousParticle, ForceCalculator forceCalculator, double timeStep){
//        timeStep /= 2;
        List<double[]> coefficients = gearAlgorithmPolynomialCoefficients(particle, forceCalculator, timeStep);
        double[] coefficientsX = coefficients.get(0);
        double[] coefficientsY = coefficients.get(1);
        double x = 0;
        double y = 0;
        double velocityX = 0;
        double velocityY = 0;
        for (int i = 0; i<=ORDER; i++){
            x += coefficientsX[i] * Math.pow(timeStep, i) / factorial(i);
            y += coefficientsY[i] * Math.pow(timeStep, i) / factorial(i);
        }
        for (int i = 0; i<ORDER; i++){
            velocityX += coefficientsX[i + 1] * Math.pow(timeStep, i) / factorial(i);
            velocityY += coefficientsY[i + 1] * Math.pow(timeStep, i) / factorial(i);
        }

        return new Particle(particle.getId(), particle.getMass(), new Position(x, y), new Velocity(velocityX, velocityY));
    }

    // Full algorithm
    private List<double[]> gearAlgorithmPolynomialCoefficients(Particle particle, ForceCalculator forceCalculator, double timeStep){
        List<Position> predictions = predictionStep(forceCalculator.getRDerivatives(ORDER, particle), timeStep);
        Particle predictedParticle = new Particle(particle.getId(), particle.getMass(), predictions.get(0), new Velocity(predictions.get(1).getX(), predictions.get(1).getY()));
        Force deltaR2Force = evaluationStep(predictions, predictedParticle, forceCalculator, timeStep);
        return correctionStep(deltaR2Force, particle.getMass(), predictions, timeStep);
    }

    // Algorithm steps
    // Step 1
    private List<Position> predictionStep(List<Position> positionDerivatives, double timeStep){
        if (positionDerivatives.size() != ORDER + 1)
            throw new IllegalArgumentException("Se deben incluir hasta la derivada " + ORDER + " de la posición");

        List<Position> predictions = new ArrayList<>(ORDER + 1);
        for (int predictionIndex = 0; predictionIndex <= ORDER; predictionIndex++){
            double x = 0;
            double y = 0;
            for (int rIndex = predictionIndex; rIndex <= ORDER; rIndex++) {
                int term = rIndex - predictionIndex;
                x += positionDerivatives.get(rIndex).getX() * Math.pow(timeStep, term) / factorial(term);
                y += positionDerivatives.get(rIndex).getY() * Math.pow(timeStep, term) / factorial(term);
            }
            predictions.add(new Position(x, y));
        }
        return predictions;
    }


    // Step 2
    private Force evaluationStep(List<Position> predictions, Particle predictedParticle, ForceCalculator forceCalculator, double timeStep){
        Force force = forceCalculator.getForce(predictedParticle);
        double accelerationX = force.getX() / predictedParticle.getMass();
        double accelerationY = force.getY() / predictedParticle.getMass();
        double accelerationDeltaX = accelerationX - predictions.get(2).getX();
        double accelerationDeltaY = accelerationY - predictions.get(2).getY();
        double accelerationDeltaXCorrected = accelerationDeltaX * timeStep * timeStep / 2;
        double accelerationDeltaYCorrected = accelerationDeltaY * timeStep * timeStep / 2;
        return new Force(accelerationDeltaXCorrected * predictedParticle.getMass(), accelerationDeltaYCorrected * predictedParticle.getMass());
    }

    // Step 3
    private List<double[]> correctionStep(Force deltaR2Force, double mass, List<Position> prediction, double timeStep){
        double deltaR2X = deltaR2Force.getX() / mass;
        double deltaR2Y = deltaR2Force.getY() / mass;

        List<double[]> corrected = new ArrayList<>(2);
        for (int i = 0; i<2; i++){
            corrected.add(new double[ORDER + 1]);
        }
        for (int i = 0; i<=ORDER; i++){
            corrected.get(0)[i] = prediction.get(i).getX() + ALFA[i] * deltaR2X*factorial(i)/Math.pow(timeStep, i);
            corrected.get(1)[i] = prediction.get(i).getY() + ALFA[i] * deltaR2Y*factorial(i)/Math.pow(timeStep, i);
        }

        return corrected;
    }

    private static long factorial(int n){
        long result = 1;
        for (int i = n; i>0; i--){
            result *= i;
        }
        return result;
    }
}
