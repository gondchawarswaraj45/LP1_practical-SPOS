import java.util.*;

public class PageReplacement15 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n==============================");
            System.out.println("   PAGE REPLACEMENT SIMULATOR  ");
            System.out.println("==============================");
            System.out.println("1. FIFO");
            System.out.println("2. LRU");
            System.out.println("3. OPTIMAL");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Invalid input! Try again.");
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 4) {
                System.out.println("Exiting... Thank you!");
                break;
            }

            System.out.print("\nEnter number of frames: ");
            int frames = sc.nextInt();

            System.out.print("Enter number of pages: ");
            int n = sc.nextInt();

            int[] pages = new int[n];
            System.out.println("Enter page reference string (" + n + " numbers):");
            for (int i = 0; i < n; i++) {
                pages[i] = sc.nextInt();
            }

            switch (choice) {
                case 1 -> fifo(pages, frames);
                case 2 -> lru(pages, frames);
                case 3 -> optimal(pages, frames);
                default -> System.out.println("Invalid choice! Try again.");
            }

            System.out.print("\nPress Enter to return to menu...");
            sc.nextLine();
            sc.nextLine();
        }

        sc.close();
    }


    static void fifo(int[] pages, int frames) {
        Set<Integer> memory = new LinkedHashSet<>(frames);
        Queue<Integer> queue = new LinkedList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- FIFO Page Replacement ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int page : pages) {
            if (memory.contains(page)) {
                hit++;
                System.out.println(page + "\t" + memory + "\tPage Hit");
            } else {
                fault++;
                if (memory.size() == frames) {
                    int removed = queue.poll();
                    memory.remove(removed);
                }
                memory.add(page);
                queue.add(page);
                System.out.println(page + "\t" + memory + "\tPage Fault");
            }
        }

        double ratio = (double) hit / (hit + fault);
        System.out.println("\nTotal Page Faults: " + fault);
        System.out.println("Total Page Hits: " + hit);
        System.out.printf("Hit Ratio: %.2f%n", ratio);
    }


    static void lru(int[] pages, int frames) {
        List<Integer> memory = new ArrayList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- LRU Page Replacement ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int page : pages) {
            if (memory.contains(page)) {
                hit++;
                memory.remove(Integer.valueOf(page));
                memory.add(page);
                System.out.println(page + "\t" + memory + "\tPage Hit");
            } else {
                fault++;
                if (memory.size() == frames) {
                    memory.remove(0);
                }
                memory.add(page);
                System.out.println(page + "\t" + memory + "\tPage Fault");
            }
        }

        double ratio = (double) hit / (hit + fault);
        System.out.println("\nTotal Page Faults: " + fault);
        System.out.println("Total Page Hits: " + hit);
        System.out.printf("Hit Ratio: %.2f%n", ratio);
    }


    static void optimal(int[] pages, int frames) {
        List<Integer> memory = new ArrayList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- OPTIMAL Page Replacement ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            if (memory.contains(page)) {
                hit++;
                System.out.println(page + "\t" + memory + "\tPage Hit");
            } else {
                fault++;
                if (memory.size() < frames) {
                    memory.add(page);
                } else {
                    int indexToReplace = findFarthest(memory, pages, i + 1);
                    memory.set(indexToReplace, page);
                }
                System.out.println(page + "\t" + memory + "\tPage Fault");
            }
        }

        double ratio = (double) hit / (hit + fault);
        System.out.println("\nTotal Page Faults: " + fault);
        System.out.println("Total Page Hits: " + hit);
        System.out.printf("Hit Ratio: %.2f%n", ratio);
    }


    static int findFarthest(List<Integer> memory, int[] pages, int start) {
        int farthest = -1, indexToReplace = -1;
        for (int i = 0; i < memory.size(); i++) {
            int current = memory.get(i);
            int j;
            for (j = start; j < pages.length; j++) {
                if (pages[j] == current)
                    break;
            }
            if (j == pages.length) 
                return i;
            if (j > farthest) {
                farthest = j;
                indexToReplace = i;
            }
        }
        return indexToReplace;
    }
}

