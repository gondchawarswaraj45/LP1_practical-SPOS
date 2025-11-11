import java.util.*;

public class PageReplacementFIFOOptimal7 {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println(" PAGE REPLACEMENT SIMULATOR ");
            System.out.println("==============================");
            System.out.println("1. FIFO");
            System.out.println("2. Optimal");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 3) {
                System.out.println("Exiting... Thank you!");
                break;
            }

            System.out.print("\nEnter number of frames: ");
            int frames = sc.nextInt();

            System.out.print("Enter number of pages: ");
            int n = sc.nextInt();

            int[] pages = new int[n];
            System.out.println("Enter page reference string:");
            for (int i = 0; i < n; i++) {
                pages[i] = sc.nextInt();
            }

            switch (choice) {
                case 1:
                    fifo(pages, frames);
                    break;
                case 2:
                    optimal(pages, frames);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

   
    static void fifo(int[] pages, int frames) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> memory = new HashSet<>();
        int pageFaults = 0, pageHits = 0;

        System.out.println("\n--- FIFO Page Replacement ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int page : pages) {
            if (!memory.contains(page)) {
                if (memory.size() == frames) {
                    int removed = queue.poll();
                    memory.remove(removed);
                }
                memory.add(page);
                queue.add(page);
                pageFaults++;
                System.out.printf("%d\t%s\tPage Fault\n", page, memory);
            } else {
                pageHits++;
                System.out.printf("%d\t%s\tPage Hit\n", page, memory);
            }
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + pageHits);
        System.out.printf("Hit Ratio: %.2f\n", (float) pageHits / pages.length);
    }

  
    static void optimal(int[] pages, int frames) {
        List<Integer> memory = new ArrayList<>();
        int pageFaults = 0, pageHits = 0;

        System.out.println("\n--- Optimal Page Replacement ---");
        System.out.println("Page\tFrames\t\tStatus");

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];

            if (memory.contains(page)) {
                pageHits++;
                System.out.printf("%d\t%s\tPage Hit\n", page, memory);
                continue;
            }

          
            if (memory.size() < frames) {
                memory.add(page);
            } else {
               
                int indexToReplace = findFutureReplacement(memory, pages, i + 1);
                memory.set(indexToReplace, page);
            }

            pageFaults++;
            System.out.printf("%d\t%s\tPage Fault\n", page, memory);
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
        System.out.println("Total Page Hits: " + pageHits);
        System.out.printf("Hit Ratio: %.2f\n", (float) pageHits / pages.length);
    }


    static int findFutureReplacement(List<Integer> memory, int[] pages, int currentIndex) {
        int farthest = -1;
        int replaceIndex = -1;

        for (int i = 0; i < memory.size(); i++) {
            int page = memory.get(i);
            int nextUse = Integer.MAX_VALUE;


            for (int j = currentIndex; j < pages.length; j++) {
                if (page == pages[j]) {
                    nextUse = j;
                    break;
                }
            }


            if (nextUse == Integer.MAX_VALUE)
                return i;


            if (nextUse > farthest) {
                farthest = nextUse;
                replaceIndex = i;
            }
        }
        return replaceIndex;
    }
}

