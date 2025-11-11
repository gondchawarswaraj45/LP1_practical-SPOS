import java.util.*;

public class PageReplacement13 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n==============================");
            System.out.println("   PAGE REPLACEMENT SIMULATOR  ");
            System.out.println("==============================");
            System.out.println("1. FIFO");
            System.out.println("2. Optimal");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Invalid input! Try again.");
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 3) {
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
                case 2 -> optimal(pages, frames);
                default -> System.out.println("Invalid choice! Try again.");
            }

            System.out.print("\nPress Enter to return to menu...");
            sc.nextLine();
            sc.nextLine();
        }

        sc.close();
    }

    // FIFO Algorithm
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

    // Optimal Algorithm
    static void optimal(int[] pages, int frames) {
        List<Integer> memory = new ArrayList<>();
        int hit = 0, fault = 0;

        System.out.println("\n--- Optimal Page Replacement ---");
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
                    // find the page to replace
                    int farthest = -1;
                    int indexToReplace = -1;

                    for (int j = 0; j < memory.size(); j++) {
                        int current = memory.get(j);
                        int nextUse = -1;

                        for (int k = i + 1; k < pages.length; k++) {
                            if (pages[k] == current) {
                                nextUse = k;
                                break;
                            }
                        }

                        if (nextUse == -1) { // not used again
                            indexToReplace = j;
                            break;
                        } else if (nextUse > farthest) {
                            farthest = nextUse;
                            indexToReplace = j;
                        }
                    }

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
}

