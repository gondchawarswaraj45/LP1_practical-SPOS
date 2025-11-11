import java.util.*;

public class PageReplacement21 {

    static void printFrames(List<Integer> f, int capacity) {
        for (int i = 0; i < capacity; i++) {
            if (i < f.size()) System.out.print(f.get(i) + " ");
            else System.out.print("- ");
        }
    }

    static void simulateFIFO(int[] refs, int capacity) {
        System.out.println("\n--- FIFO Page Replacement ---");
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> mem = new HashSet<>();
        int faults = 0, hits = 0;

        System.out.println("Page\tFrames\t\tStatus");
        for (int r : refs) {
            boolean hit = mem.contains(r);
            if (hit) hits++;
            else {
                faults++;
                if (q.size() == capacity) {
                    int rem = q.poll();
                    mem.remove(rem);
                }
                q.offer(r);
                mem.add(r);
            }
            System.out.print(r + "\t");
            printFrames(new ArrayList<>(q), capacity);
            System.out.println("\t" + (hit ? "Hit" : "Fault"));
        }
        System.out.println("Total Page Faults: " + faults);
        System.out.println("Total Page Hits: " + hits);
        System.out.printf("Hit Ratio: %.2f\n", (hits * 1.0 / refs.length));
    }

    static void simulateLRU(int[] refs, int capacity) {
        System.out.println("\n--- LRU Page Replacement ---");
        LinkedList<Integer> list = new LinkedList<>();
        int faults = 0, hits = 0;

        System.out.println("Page\tFrames\t\tStatus");
        for (int r : refs) {
            boolean hit = list.contains(r);
            if (hit) {
                hits++;
                list.removeFirstOccurrence(r);
                list.addLast(r);
            } else {
                faults++;
                if (list.size() == capacity) list.removeFirst();
                list.addLast(r);
            }
            System.out.print(r + "\t");
            printFrames(list, capacity);
            System.out.println("\t" + (hit ? "Hit" : "Fault"));
        }
        System.out.println("Total Page Faults: " + faults);
        System.out.println("Total Page Hits: " + hits);
        System.out.printf("Hit Ratio: %.2f\n", (hits * 1.0 / refs.length));
    }

    static void simulateOPTIMAL(int[] refs, int capacity) {
        System.out.println("\n--- OPTIMAL Page Replacement ---");
        List<Integer> mem = new ArrayList<>();
        int faults = 0, hits = 0;

        System.out.println("Page\tFrames\t\tStatus");
        for (int i = 0; i < refs.length; i++) {
            int r = refs[i];
            boolean hit = mem.contains(r);
            if (hit) hits++;
            else {
                faults++;
                if (mem.size() < capacity) mem.add(r);
                else {
                    int idxToReplace = -1;
                    int farthest = -1;
                    for (int j = 0; j < mem.size(); j++) {
                        int page = mem.get(j);
                        int nextUse = -1;
                        for (int k = i + 1; k < refs.length; k++) {
                            if (refs[k] == page) { nextUse = k; break; }
                        }
                        if (nextUse == -1) { idxToReplace = j; break; }
                        if (nextUse > farthest) { farthest = nextUse; idxToReplace = j; }
                    }
                    mem.set(idxToReplace, r);
                }
            }
            System.out.print(r + "\t");
            printFrames(mem, capacity);
            System.out.println("\t" + (hit ? "Hit" : "Fault"));
        }
        System.out.println("Total Page Faults: " + faults);
        System.out.println("Total Page Hits: " + hits);
        System.out.printf("Hit Ratio: %.2f\n", (hits * 1.0 / refs.length));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== PAGE REPLACEMENT SIMULATOR =====");
        System.out.print("Enter number of frames: ");
        int capacity = sc.nextInt();
        System.out.print("Enter number of page references: ");
        int n = sc.nextInt();
        int[] refs = new int[n];
        System.out.println("Enter page reference string:");
        for (int i = 0; i < n; i++) refs[i] = sc.nextInt();

        while (true) {
            System.out.println("\n1. FIFO\n2. LRU\n3. OPTIMAL\n4. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            if (ch == 4) break;
            if (ch == 1) simulateFIFO(refs, capacity);
            else if (ch == 2) simulateLRU(refs, capacity);
            else if (ch == 3) simulateOPTIMAL(refs, capacity);
            else System.out.println("Invalid choice!");
        }
        sc.close();
    }
}

