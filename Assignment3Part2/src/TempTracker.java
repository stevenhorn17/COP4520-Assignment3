import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TempTracker {

    private static final int NUM_SENSORS = 8;
    private static final int REPORT_INTERVAL = 60;
    static AtomicInteger reportCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Beginning Temperature Readings");

        // Creating and Managing Sensors (threads)
        List<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < NUM_SENSORS; i++) {
            sensors.add(new Sensor());
        }
        ExecutorService executor = Executors.newFixedThreadPool(NUM_SENSORS);
        for (Sensor sensor : sensors) {
            executor.submit(sensor);
        }

        // Until terminated, generate temp reports
        try{
            while (true)
            {
                Thread.sleep(REPORT_INTERVAL * 1000);
                System.out.println("Report " + reportCounter.incrementAndGet() + " Ready!");
                generateReport(sensors);

            }
        }
        catch (InterruptedException error){
            System.err.println(error);
            executor.shutdownNow();
        }
        executor.shutdownNow();
    }

    private static void generateReport(List<Sensor> sensors) {
        int maxDifference = 0;
        int maxDiffStart = 0;

        // Add all temps to a list and sort to find highest and lowest
        List<Integer> allTemps = new ArrayList<>();
        for (Sensor sensor : sensors) {
            allTemps.addAll(sensor.getTemperatures());
        }
        Collections.sort(allTemps);
        System.out.println("Top 5 lowest temperatures: " + allTemps.subList(0, 5));
        System.out.println("Top 5 highest temperatures: " + allTemps.subList(allTemps.size() - 5, allTemps.size()));

        // Look for 10 temps with the largest difference
        for (int i = 0; i < allTemps.size() - 11; i++) {
            int change = allTemps.get(i + 10) - allTemps.get(i);
            if (change > maxDifference) {
                maxDifference = change;
                maxDiffStart = i;
            }
        }

        System.out.println("10 min interval of largest temp difference " + maxDifference + ",  from minute: " + maxDiffStart + " to " + (maxDiffStart + 10));
    }

    private static class Sensor implements Runnable {
        private final List<Integer> temperatures = new CopyOnWriteArrayList<>();
        private final Random random = new Random();

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                // Generate and add temps every minute (simulated as every second)
                // Temp range: [-170, 70]
                int temp = random.nextInt(171) - 100;
                temperatures.add(temp);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        public List<Integer> getTemperatures() {
            return temperatures;
        }
    }
}
