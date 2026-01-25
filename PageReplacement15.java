import java.util.*;

/*
 * PageReplacementSimulator
 * -------------------------
 * This program simulates three Page Replacement Algorithms:
 * 1. FIFO (First In First Out)
 * 2. LRU  (Least Recently Used)
 * 3. OPTIMAL
 *
 * It displays:
 * - Page hits
 * - Page faults
 * - Frame contents after each reference
 * - Hit ratio & Fault ratio
 */

public class PageReplacement15 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {

            // ---------- MENU ----------
            System.out.println("\n==================================");
            System.out.println("      PAGE REPLACEMENT SIMULATOR   ");
            System.out.println("==================================");
            System.out.println("1. FIFO");
            System.out.println("2. LRU");
            System.out.println("3. OPTIMAL");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            // Validate menu choice
            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            choice = sc.nextInt();

            if (choice == 4) {
                System.out.println("Exiting... Thank you!");
                break;
            }

            // ---------- INPUT ----------
            System.out.print("Enter number of frames: ");
            int frames = sc.nextInt();

            System.out.print("Enter number of pages: ");
            int n = sc.nextInt();

            // Validate frame/page count
            if (frames <= 0 || n <= 0) {
                System.out.println("Frames and pages must be greater than 0!");
                continue;
            }

            int[] pages = new int[n];
            System.out.println("Enter page reference string:");
            for (int i = 0; i < n; i++) {
                pages[i] = sc.nextInt();
            }

            // ---------- ALGORITHM SELECTION ----------
            switch (choice) {
                case 1 -> fifo(pages, frames);
                case 2 -> lru(pages, frames);
                case 3 -> optimal(pages, frames);
                default -> System.out.println("Invalid choice!");
            }

            System.out.println("\nPress Enter to return to menu...");
            sc.nextLine();
            sc.nextLine();
        }

        sc.close();
    }

    // ======================================================
    // FIFO PAGE REPLACEMENT
    // ======================================================
    static void fifo(int[] pages, int frames) {

        Set<Integer> memory = new LinkedHashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        int hit = 0, fault = 0;

        System.out.println("\n--- FIFO Page Replacement ---");
        printHeader(frames);

        for (int page : pages) {

            if (memory.contains(page)) {
                hit++;
                printRow(page, memory, frames, "Hit");
            } else {
                fault++;

                // Remove oldest page
                if (memory.size() == frames) {
                    int removed = queue.poll();
                    memory.remove(removed);
                }

                memory.add(page);
                queue.add(page);
                printRow(page, memory, frames, "Fault");
            }
        }

        printStats(hit, fault);
    }

    // ======================================================
    // LRU PAGE REPLACEMENT
    // ======================================================
    static void lru(int[] pages, int frames) {

        List<Integer> memory = new ArrayList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- LRU Page Replacement ---");
        printHeader(frames);

        for (int page : pages) {

            if (memory.contains(page)) {
                hit++;

                // Move page to most recently used position
                memory.remove(Integer.valueOf(page));
                memory.add(page);

                printRow(page, memory, frames, "Hit");
            } else {
                fault++;

                // Remove least recently used page
                if (memory.size() == frames) {
                    memory.remove(0);
                }

                memory.add(page);
                printRow(page, memory, frames, "Fault");
            }
        }

        printStats(hit, fault);
    }

    // ======================================================
    // OPTIMAL PAGE REPLACEMENT
    // ======================================================
    static void optimal(int[] pages, int frames) {

        List<Integer> memory = new ArrayList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- OPTIMAL Page Replacement ---");
        printHeader(frames);

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];

            if (memory.contains(page)) {
                hit++;
                printRow(page, memory, frames, "Hit");
            } else {
                fault++;

                if (memory.size() < frames) {
                    memory.add(page);
                } else {
                    int index = findFarthest(memory, pages, i + 1);
                    memory.set(index, page);
                }

                printRow(page, memory, frames, "Fault");
            }
        }

        printStats(hit, fault);
    }

    // ======================================================
    // FIND PAGE WITH FARTHEST FUTURE USE (OPTIMAL)
    // ======================================================
    static int findFarthest(List<Integer> memory, int[] pages, int start) {

        int farthest = -1;
        int indexToReplace = -1;

        for (int i = 0; i < memory.size(); i++) {

            int current = memory.get(i);
            int j;

            // Search future references
            for (j = start; j < pages.length; j++) {
                if (pages[j] == current)
                    break;
            }

            // Page not used again
            if (j == pages.length) {
                return i;
            }

            // Page used farthest in future
            if (j > farthest) {
                farthest = j;
                indexToReplace = i;
            }
        }

        return indexToReplace;
    }

    // ======================================================
    // UTILITY METHODS
    // ======================================================

    // Prints table header
    static void printHeader(int frames) {
        System.out.print("Page\t");
        for (int i = 1; i <= frames; i++) {
            System.out.print("F" + i + "\t");
        }
        System.out.println("Status");
    }

    // Prints one row of table
    static void printRow(int page, Collection<Integer> memory, int frames, String status) {

        System.out.print(page + "\t");

        int count = 0;
        for (int p : memory) {
            System.out.print(p + "\t");
            count++;
        }

        // Fill empty frames
        while (count++ < frames) {
            System.out.print("-\t");
        }

        System.out.println(status);
    }

    // Prints final statistics
    static void printStats(int hit, int fault) {

        int total = hit + fault;

        System.out.println("\nTotal Page References : " + total);
        System.out.println("Total Page Hits       : " + hit);
        System.out.println("Total Page Faults     : " + fault);

        System.out.printf("Hit Ratio   : %.2f%n", (double) hit / total);
        System.out.printf("Fault Ratio : %.2f%n", (double) fault / total);
    }
}
