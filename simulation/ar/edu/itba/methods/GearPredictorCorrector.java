package ar.edu.itba.methods;

public class GearPredictorCorrector {

    private double deltaTime;

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
    private double[] correctionStep(){

    }
// Full algorithm
    public double[] gearAlgorithm(){

    }
}
