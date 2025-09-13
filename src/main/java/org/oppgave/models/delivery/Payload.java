package org.oppgave.models.delivery;

public class Payload {
    private static final int MAX_RANGE = 11000;
    private double maxSpeed = 50;
    private int accelerationOffset = 200;

    private static final double KNOT_TO_MPS = 1852.0 / 3600.0;

    public int getMaxRangeMeters() { return MAX_RANGE; }
    public double getMaxSpeedKnots() { return maxSpeed; }
    public double getMaxSpeedMps() { return maxSpeed * KNOT_TO_MPS; }
    public int getAccelerationOffsetMeters() { return accelerationOffset; }


    public Result simulateFire(double initialDistanceMeters, double subMaxSpeedKnots) {
        double D = initialDistanceMeters + accelerationOffset; //offset før full torpedofart - "hardkodet" til 200 meter og ikke akselerasjonstid fra launch til maxfart

        double vt = getMaxSpeedMps();
        double vs = subMaxSpeedKnots * KNOT_TO_MPS;

        if (vt <= vs) {
            double tRange = MAX_RANGE / vt; // tiden til torpedo når max rekkevidde
            return Result.miss("Sub outruns or equals torpedo speed", tRange, MAX_RANGE, D);
        }
// god gammaldags s = v * t og torpedoens tilbakelagt distense
        double tHit = D / (vt - vs);
        double sTorpedo = vt * tHit;

        if (sTorpedo <= MAX_RANGE) {
            return Result.hit(tHit, sTorpedo, D);
        } else {
            double tRange = MAX_RANGE / vt;
            return Result.miss("Sub escaped beyond torpedo range", tRange, MAX_RANGE, D);
        }
    }


    //støttemetode for å konvertere sekunder til mm:ss format - tar først inn antall sekunder og runder av til nærmeste heltall
    // deretter beregner antall minutter og deler antall sekunder på 60 før en modulus som tar resten
    public static String formatMmSs(double seconds) {
        long s = Math.round(seconds);
        long mm = s / 60;
        long ss = s % 60;
        return String.format("%02d:%02d", mm, ss);
    }

    // Result DTO
    public static final class Result {
        public final boolean hit;
        public final String reason;         // i tilfelle ikke treff
        public final double timeSec;        // tidspunkt til treff eller max rekkevidde
        public final double torpedoDist;    // torpedodistande ved tregg
        public final double initialSep;     // offset til torp er på max fart, for context

        private Result(boolean hit, String reason, double timeSec, double torpedoDist, double initialSep) {
            this.hit = hit;
            this.reason = reason;
            this.timeSec = timeSec;
            this.torpedoDist = torpedoDist;
            this.initialSep = initialSep;
        }

        public static Result hit(double timeSec, double torpedoDist, double initialSep) {
            return new Result(true, null, timeSec, torpedoDist, initialSep);
        }

        public static Result miss(String reason, double timeSec, double torpedoDist, double initialSep) {
            return new Result(false, reason, timeSec, torpedoDist, initialSep);
        }

        /** avstand mellom surface og sub ved avskjæring (samme som torpedodistanse ved treff) */
        public double interceptRangeMeters() {
            return torpedoDist;
        }

        public boolean willHit() {
            return hit;
        }

        @Override
        public String toString() {
            if (hit) {
                return String.format("HIT: t=%.1fs (%s), torpedo distance (intercept range)=%.0fm",
                        timeSec, Payload.formatMmSs(timeSec), torpedoDist);
            } else {
                return String.format("MISS (%s): t=%.1fs (%s), torpedo distance at limit=%.0fm, initial sep=%.0fm",
                        reason, timeSec, Payload.formatMmSs(timeSec), torpedoDist, initialSep);
            }
        }
    }
}
