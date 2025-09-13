package org.oppgave.models.surface;

public class AswS {
    protected int course;
    protected double speed;
    protected double x, y;

    public AswS(int course, double speed, double x, double y) {
        if (course < 0 || course > 359) throw new IllegalArgumentException("course 0–359");
        if (speed < 0) throw new IllegalArgumentException("speed >= 0");
        this.course = course;
        this.speed = speed;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("ASW ship at (%.1f, %.1f) m — course %d°, speed %.1f kn",
                x, y, course, speed);
    }
}




