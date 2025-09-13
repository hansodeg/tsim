package org.oppgave.models.sub;

public record Sub(
        String Id,
        double length,
        double Width,
        double cruiseSpeed,
        double MaxSpeed


) {
    @Override
    public String toString() {
        return String.format("%s — %.1fm x %.1fm, cruise %.1f kn, max %.1f kn",
                Id, length, Width, cruiseSpeed, MaxSpeed);
    }
}




