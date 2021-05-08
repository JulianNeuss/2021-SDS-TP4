package ar.edu.itba.particle;

public class Velocity {
    private double velocityX;
    private double velocityY;

    public Velocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getSpeed(){
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    @Override
    public String toString() {
        return "(" + velocityX + ", " + velocityY + ")";
    }
}
