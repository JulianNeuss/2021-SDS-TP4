package ar.edu.itba.methods;

public class GearPredictorCorrector {

    private double deltaTime;
    private double alfa0 = 3/16;
    private double alfa1 = 251/360;
    private double alfa2 = 1;
    private double alfa3 = 11/18;
    private double alfa4 = 1/6;
    private double alfa5 = 1/60;


//  Builder
    public GearPredictorCorrector(double deltaTime) {
        this.deltaTime = deltaTime;
    }

// Algorithm steps
// Step 1
    private double[] predicitonStep(double r, double r1, double r2, double r3, double r4, double r5){
        double[] prediction = new double[6];
        prediction[0] = r + r1*deltaTime + r2*Math.pow(deltaTime,2)/2 + r3*Math.pow(deltaTime,3)/(3*2) + r4*Math.pow(deltaTime,4)/(4*3*2) + r5*Math.pow(deltaTime,5)/(5*4*3*2);
        prediction[1] = r1 + r2*deltaTime + r3*Math.pow(deltaTime,2)/2 + r4*Math.pow(deltaTime,3)/(3*2) + r5*Math.pow(deltaTime,4)/(4*3*2);
        prediction[2] = r2 + r3*deltaTime + r4*Math.pow(deltaTime,2)/2 + r5*Math.pow(deltaTime,3)/(3*2);
        prediction[3] = r3 + r4*deltaTime + r5*Math.pow(deltaTime,2)/2;
        prediction[4] = r4 + r5*deltaTime;
        prediction[5] = r5;
        return prediction;
    }
// Step 2
    private double evaluationStep(double[] prediction){
        double acceleration;
        double accelerationDelta;
        double r2Delta;
        acceleration = (-100000)*prediction[0] - 100*prediction[1];
        accelerationDelta = acceleration - prediction[2];
        r2Delta = (accelerationDelta*deltaTime*deltaTime)/2;
        return r2Delta;
    }
// Step 3
    private double[] correctionStep(double r2Delta, double[] prediction){
        double[] corrected = new double[6];
        corrected[0] = prediction[0] + alfa0*r2Delta;
        corrected[1] = prediction[1] + alfa1*r2Delta/deltaTime;
        corrected[2] = prediction[2] + alfa2*r2Delta*(2/deltaTime);
        corrected[3] = prediction[3] + alfa3*r2Delta*(3*2/deltaTime);
        corrected[4] = prediction[4] + alfa4*r2Delta*(4*3*2/deltaTime);
        corrected[5] = prediction[5] + alfa5*r2Delta*(5*4*3*2/deltaTime);
        return corrected;
    }
// Full algorithm
    public double[] gearAlgorithm(double r, double r1, double r2, double r3, double r4, double r5){
        double[] prediction = predicitonStep(r, r1, r2, r3, r4, r5);
        double r2Delta = evaluationStep(prediction);
        return correctionStep(r2Delta, prediction);
    }
}
