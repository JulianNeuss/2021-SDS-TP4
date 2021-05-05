package ar.edu.itba.methods;

import ar.edu.itba.Particle.*;

import java.util.ArrayList;
import java.util.List;

public class Beeman {
    public static List<ParticleState> getXandV(
            double t0, double tf, double dt,
            Particle p, double k, double gamma,
            ForceCalculator f){

        List<ParticleState> res = new ArrayList<>();
        res.add(p.getState());
        if(t0+dt >= tf){
            return res;
        }
        //current particle
        Particle cp = p;

        //get a
        double a1 = f.getA(cp);

        //TODO: check
        //predict a-1 with euler
        double a0 = f.getA(cp.updateParticle(new ParticleState(
                    new Position(
                        cp.getPosition().getX() - cp.getVelocity().getVelocityX() * dt,
                            cp.getPosition().getY()
                    ),
                    new Velocity(
                        cp.getVelocity().getVelocityX() - a1*dt,
                            cp.getVelocity().getVelocityY()
                    )
                )));


        for(int i = 1; i*dt + t0 <= tf; i++){
            // calculate next x
            double x = Beeman.nextX(
                cp.getPosition().getX(),cp.getVelocity().getVelocityX(),
                dt,a1,a0
            );

            // calculate a+1 with prediction
            double a2 = f.getA(cp.updateParticle(new ParticleState(
                new Position(x,cp.getPosition().getY()),
                new Velocity(
                        Beeman.predictV(cp.getVelocity().getVelocityX(),dt,a1,a0),
                        cp.getVelocity().getVelocityY()
                )
            )));
            // calculate v with a+1
            double v = Beeman.nextV(cp.getVelocity().getVelocityX(),dt,a2,a1,a0);

            // calculate a+1 with correct v
            a2 = f.getA(cp.updateParticle(new ParticleState(
                    new Position(x,cp.getPosition().getY()),
                    new Velocity(v,p.getVelocity().getVelocityY())
            )));

            // create new state
            ParticleState newState = new ParticleState(
                    new Position(x,cp.getPosition().getY()),
                    new Velocity(v,cp.getVelocity().getVelocityY())
            );

            //update particle
            cp = cp.updateParticle(newState);
            //save state
            res.add(newState);
            //update accelerations
            a0 = a1;
            a1 = a2;
        }

        return res;
    }

    public static double nextX(double px,double pv,double dt,double pa,double ppa){
        return px + pv * dt +( (2*pa)/3 - ppa/6 ) * Math.pow(dt,2);
    }
    public static double nextV(double pv,double dt,double a,double pa,double ppa){
        return pv + (a/3 + (5*pa)/6 - ppa/6) * dt;
    }
    public static double predictV(double pv,double dt,double pa,double ppa){
        return pv + (3 * pa * dt) / 2 - (ppa * dt) / 2;
    }
}
