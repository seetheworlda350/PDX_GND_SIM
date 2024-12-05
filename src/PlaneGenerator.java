import java.text.SimpleDateFormat;
import java.util.*;

public class PlaneGenerator {
    public static void main(String[] args) {
        System.out.println("Standby while we generate your traffic!\n");

        String[] airlines = {"American", "United", "Delta", "Southwest", "SkyWest", "Alaska", "Spirit", "Allegiant"};
        Map<String, String[]> airlineToPlanes = new HashMap<>();
        airlineToPlanes.put("SkyWest", new String[]{"E175"});
        airlineToPlanes.put("Alaska", new String[]{"B738", "B7M8"});
        airlineToPlanes.put("Southwest", new String[]{"B738", "B7M8"});
        airlineToPlanes.put("United", new String[]{"B738", "B7M8", "A320"});
        airlineToPlanes.put("Delta", new String[]{"B738", "B7M8", "A320"});
        airlineToPlanes.put("American", new String[]{"B738", "B7M8", "A320"});
        airlineToPlanes.put("Spirit", new String[]{"A320"});
        airlineToPlanes.put("Allegiant", new String[]{"A320"});

        Map<String, String> destinationToRoute = new HashMap<>();
        destinationToRoute.put("KSEA", "PTLD2 COUGA KRIEG HAWKZ8");
        destinationToRoute.put("CYVR", "PTLD2 BTG J1 SEA PAE GRIZZ7");
        destinationToRoute.put("KLAX", "CASCD3 CHISM JUDAH JUNEJ Q7 JAGWA BURGL IRNMN2");
        destinationToRoute.put("KSFO", "MINNE5 EASON Q1 ETCHY MLBEC BDEGA4");
        destinationToRoute.put("KSAN", "CASCD3 CHISM PUHTS PITVE Q11 PASKE HUULK COMIX2");
        destinationToRoute.put("KBOI", "WHAMY5 IMB DEVLE KYAAN4");
        destinationToRoute.put("KORD", "LAVAA7 PDT J16 MCW ZZIPR FYTTE7");
        destinationToRoute.put("KDTW", "LAVAA7 PDT J16 HIA BZN BIL J34 BAE PORZL RKCTY2");
        destinationToRoute.put("KBOS", "LTJ V520 PSC PSC V187 GTF J203 HILGR J36 FAR TTAIL Q140 AHPAH Q812 SYR V2 ALB V14 GRAVE");
        destinationToRoute.put("KDEN", "ROAMS V500 IMB J15 BOI J163 OCS J154 ALPOE");
        destinationToRoute.put("KCLT", "LAVAA7 J16 BIL J151 STL V4 PXV V52 LVT V384 VXV V136 SOT V185 MUMMI");
        destinationToRoute.put("KDFW", "ROAMS V500 IMB J15 BOI V253 YUWFE V4 BYI Q154 UKW");
        destinationToRoute.put("KPHL", "LAVAA7 J16 DPR J34 ODI V170 DLL J34 VIO V510 LAN V2 SVM V116 ERI V184 RASHE V276 HERDA");

        String[] commonDestinations = {"KSEA", "CYVR", "KLAX", "KSFO", "KSAN", "KBOI"};
        String[] extendedDestinations = {"KBOS", "KDEN", "KORD", "KDFW", "KCLT", "KPHL", "KDTW"};

        Random random = new Random();
        List<Flight> flights = new ArrayList<>();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")); // Start time in UTC (Zulu time)
        calendar.add(Calendar.MINUTE, 10); // The first flight departs 10 minutes from now

        for (int i = 0; i < 15; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            String[] planes = airlineToPlanes.get(airline);
            String planeType = planes[random.nextInt(planes.length)];

            String[] possibleDestinations = planeType.matches("B738|B7M8|A320") ? concatenateArrays(commonDestinations, extendedDestinations) : commonDestinations;
            String destination = possibleDestinations[random.nextInt(possibleDestinations.length)];

            String route = destinationToRoute.getOrDefault(destination, "N/A");
            int squawkCode = 1000 + random.nextInt(8999);
            String departure = "KPDX";

            // Format the scheduled departure time in Zulu (UTC)
            String departureTime = new SimpleDateFormat("HHmm").format(calendar.getTime());
            flights.add(new Flight(airline, random.nextInt(10000), planeType, departure, destination, route, squawkCode, departureTime));

            // Increment the departure time for the next flight
            calendar.add(Calendar.MINUTE, 5 + random.nextInt(6)); // Each flight is 5–10 minutes apart
        }

        for (Flight flight : flights) {
            System.out.println(flight.toFlightStrip());
            System.out.println();
        }
    }

    private static String[] concatenateArrays(String[] array1, String[] array2) {
        String[] result = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    static class Flight {
        private final String airline;
        private final int flightNumber;
        private final String planeType;
        private final String departure;
        private final String destination;
        private final String route;
        private final int squawkCode;
        private final String departureTime;

        public Flight(String airline, int flightNumber, String planeType, String departure, String destination, String route, int squawkCode, String departureTime) {
            this.airline = airline;
            this.flightNumber = flightNumber;
            this.planeType = planeType;
            this.departure = departure;
            this.destination = destination;
            this.route = route;
            this.squawkCode = squawkCode;
            this.departureTime = departureTime;
        }

        public String toFlightStrip() {
            return String.format(
                    "+--------------------+-----------+-----------------------------------+\n" +
                            "| %-18s | %-4d %-4s | %-33s |\n" +
                            "| %-18s | %-4s %-4s | %-33s |\n" +
                            "+--------------------+-----------+-----------------------------------+",
                    airline + flightNumber, squawkCode, departureTime, route,
                    planeType, departure, destination, ""
            );
        }
    }
}
