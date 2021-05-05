package ar.edu.itba.Particle;

@FunctionalInterface
public interface ForceCalculator {
    double getF(Particle p);

    /*
    * returns the acceleration
    * */
    default double getA(Particle p){
        return this.getF(p)/p.getMass();
    }
}
