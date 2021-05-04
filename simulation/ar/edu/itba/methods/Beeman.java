package methods;

import java.util.ArrayList;
import java.util.List;

public class Beeman {
    public static void main(String[] args) {

    }

    public static List<Double>[] getXandV(double t0,double tf,double dt,Particle p){
        List<Double>[] xandv = new ArrayList[2];
        xandv[0] = new ArrayList<>();
        xandv[1] = new ArrayList<>();

        double t = t0;

        while(t<tf){

            t += dt;
        }

        return xandv;
    }

    public static double nextX(double px,double pv,double dt,double pa,double ppa){
        return px + pv * dt +( (2*pa)/3 - ppa/6 ) * Math.pow(dt,2);
    }
    public static double nextV(double pv,double dt,double a,double pa,double ppa){
        return pv + (a/3 + (5*pa)/6 - ppa/6) * dt;
    }
}
