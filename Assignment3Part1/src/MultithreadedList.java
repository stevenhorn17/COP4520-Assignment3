import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

public class MultithreadedList {
    private static final int NUM_PRESENTS = 250_000;
    private static final int NUM_THREADS = 4;
    private static final int SEARCH_LIMIT = 50_000;

    public static void main(String[] args) throws InterruptedException {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> bag = new ArrayList<>(NUM_PRESENTS);
        AtomicInteger currPos = new AtomicInteger(1);
        AtomicInteger thankYouNotes = new AtomicInteger(0);
        AtomicBoolean allPresentsAdded = new AtomicBoolean(false);
        AtomicBoolean allNotesWritten = new AtomicBoolean(false);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        AtomicInteger numPresentsInList = new AtomicInteger(0);
        AtomicInteger numSearches = new AtomicInteger(0);
        AtomicInteger searchLimitReached = new AtomicInteger(0);
        AtomicBoolean searchesAllowed = new AtomicBoolean(true);

        // Bag of gifts
        for (int i = 0; i < NUM_PRESENTS; i++) {
            bag.add(i);
        }
        // Randomized order
        Collections.shuffle(bag);
        System.out.println("Beginning Thank You Notes");
        Runnable worker = () -> {

            // Continue on as long as there are thank you notes left to write
            // Or if there are no more presents left
            while (!allNotesWritten.get() || numPresentsInList.get() > 0) {

                // Generate 1 of 3 possible tasks
                int task =ThreadLocalRandom.current().nextInt(1, 4);

                // If all presents already added to list or search limit reached, only remove
                if (allPresentsAdded.get() && !searchesAllowed.get())
                {
                    task = 2;
                }

                // Else search or remove
                else if (searchesAllowed.get() && allPresentsAdded.get())
                {
                    task = ThreadLocalRandom.current().nextInt(2, 4);
                }

                switch (task) {
                    case 1: // Add a present to the list
                        int position = currPos.getAndIncrement();
                        if (position < NUM_PRESENTS) {
                            int present = bag.get(position);
                            list.add(present);
                            numPresentsInList.incrementAndGet();
                            System.out.println("Added present: " + present);
                            System.out.println("Presents in list: " + position);
                        }
                        // Bag is empty, presents now in sorted list
                        else {
                            System.out.println("All presents added to list!");
                            allPresentsAdded.set(true);
                            break;
                        }
                        break;

                    case 2: // Remove a random present if it exists
                        int randomPresent = ThreadLocalRandom.current().nextInt(NUM_PRESENTS);
                        if (list.remove(randomPresent)) {
                            System.out.println("Note written for : " + randomPresent);
                            numPresentsInList.decrementAndGet();
                            int notesWritten = thankYouNotes.getAndIncrement();

                            // All thank you notes written
                            if (notesWritten >= NUM_PRESENTS){
                                System.out.println("No more thank you notes!");
                                allNotesWritten.set(true);
                            }
                        }
                        break;

                    case 3: // Check if a random number is in the list
                        if (searchLimitReached.get() == 0) {
                            int checkPresent = ThreadLocalRandom.current().nextInt(NUM_PRESENTS);
                            numSearches.incrementAndGet();
                            if (list.contains(checkPresent)) {
                                System.out.println("Present " + checkPresent + " is in the list.");
                            } else {
                                System.out.println("Present " + checkPresent + " is not in the list.");
                            }

                            // Limit placed on searches to reduce unneccesary computation
                            if (numSearches.get() >= SEARCH_LIMIT && searchLimitReached.compareAndSet(0, 1)) {
                                System.out.println("Search Limit Reached!");
                            }
                        }
                        break;
                }
            }
        };

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(worker);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
