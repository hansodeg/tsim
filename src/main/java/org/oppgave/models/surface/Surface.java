package org.oppgave.models.surface;

public class Surface {
    public int course;
    protected double speed;


    public Surface(int course, double speed) {
        this.course = course;
        setSpeed(speed);
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed > 25) {
            throw new IllegalArgumentException("Max speed is  25 kts ");
        }
        this.speed = speed;
    }
}




