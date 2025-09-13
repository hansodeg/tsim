package org.oppgave;
import java.util.Random;
import java.util.Scanner;
import org.oppgave.models.delivery.Payload;
import org.oppgave.models.sub.Sub;
import org.oppgave.models.sub.SubClass;
import org.oppgave.models.surface.AswS;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int course = readInt(scanner, "Enter course (0–359 degrees): ", 0, 359);
            double speed = readDouble(scanner, "Enter speed (>=0 knots): ", 0, 26);

            AswS aswS = new AswS(course, speed, 0, 0);

            // genererer en random Sub fra Subclass enum
            Random rand = new Random();
            SubClass[] all = SubClass.values();
            SubClass picked = all[rand.nextInt(all.length)];
            Sub sub = picked.get();

            double rMin = 2000.0, rMax = 6000.0;
            double u = rand.nextDouble();
            double distance = Math.sqrt(u * (rMax * rMax - rMin * rMin) + rMin * rMin);

            double bearingDeg = rand.nextDouble() * 360.0;
            double bearingRad = Math.toRadians(bearingDeg);

            double subX = distance * Math.cos(bearingRad);
            double subY = distance * Math.sin(bearingRad);

            // Setter "state"
            System.out.println("\nASW ship:");
            System.out.println("  " + aswS);

            System.out.println("\nRandom Sub Selected:");
            System.out.println("  " + sub);

            System.out.println("\nSub placement:");
            System.out.printf("  Bearing %.1f°, range %.0f m%n", bearingDeg, distance);
            System.out.printf("  Pos (%.1f, %.1f) m%n", subX, subY);

            // Forbered anbefaling
            double initialDistanceMeters = Math.hypot(subX, subY);
            Payload payload = new Payload();

            // sub maxfart satt
            double subMaxKnots = sub.MaxSpeed();

            // Tester målløsning for anbefaling
            Payload.Result dryRun = payload.simulateFire(initialDistanceMeters, subMaxKnots);

            System.out.println("\nRecommendation:");
            if (dryRun.willHit()) {
                System.out.printf("  RECOMMEND: FIRE (estimated hit in %s at ~%.0f m from surface)%n",
                        Payload.formatMmSs(dryRun.timeSec), dryRun.interceptRangeMeters());
            } else {
                System.out.printf("  RECOMMEND: QUIT (miss: %s)%n", dryRun.reason);
            }

            // --- Fyrløsning ---
            System.out.print("\nType FIRE to launch torpedo, or QUIT to exit: ");
            String cmd = scanner.next().trim();
            if ("QUIT".equalsIgnoreCase(cmd)) {
                if (!askRunAgain(scanner)) break;
                System.out.println("Goodbye.");
                break;
            }
            if (!"FIRE".equalsIgnoreCase(cmd)) {
                System.out.println("Aborted.");
                if (!askRunAgain(scanner)) break;
                else continue;
            }

            // Simulering
            Payload.Result result = payload.simulateFire(initialDistanceMeters, subMaxKnots);

            System.out.println("\nTorpedo launched:");
            System.out.printf("  Torpedo max speed: %.1f kn%n", payload.getMaxSpeedKnots());
            System.out.printf("  Sub max speed    : %.1f kn (running away)%n", subMaxKnots);
            System.out.printf("  Added launch offset: +%dm%n", payload.getAccelerationOffsetMeters());
            System.out.printf("  Torpedo MAX_RANGE: %dm%n", payload.getMaxRangeMeters());

            System.out.println("\nResult:");
            System.out.println("  " + result);

            if (result.willHit()) {
                System.out.printf("  Intercept range from surface: %.0f m%n", result.interceptRangeMeters());
                System.out.printf("  Time to intercept: %s%n", Payload.formatMmSs(result.timeSec));
            }

            if (!askRunAgain(scanner)) break;
        }

        scanner.close();
    }


    //støtte metode for å spørre om ny simulering
    private static boolean askRunAgain(Scanner sc) {
        System.out.print("\nRun again? (Y/N): ");
        String ans = sc.next().trim();
        return ans.equalsIgnoreCase("Y") || ans.equalsIgnoreCase("YES");
    }


    //støttemetoder for input av kurs og fart
    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                if (v >= min && v <= max) return v;
                System.out.printf("Value must be between %d and %d.%n", min, max);
            } else {
                System.out.println("Please enter an integer.");
                sc.next();
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                if (v >= min && v <= max) return v;
                System.out.printf("Value must be between %.2f and %.2f.%n", min, max);
            } else {
                System.out.println("Please enter a number.");
                sc.next(); //
            }
        }
    }
}
