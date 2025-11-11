import java.util.*;

public class CPUScheduler17 {

    static class Proc {
        String name;
        int arrival;
        int burst;
        int priority;
        int start = -1;
        int completion = -1;
        int turnaround = -1;
        int waiting = -1;
        Proc(String n, int a, int b, int p) { name = n; arrival = a; burst = b; priority = p; }
        Proc copy() { return new Proc(name, arrival, burst, priority); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("CPU Scheduling Simulator (FCFS, Priority - non-preemptive)");
        System.out.print("Enter number of processes: ");
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Proc> list = new ArrayList<>();
        System.out.println("Enter processes (one per line) in format: name arrival burst priority");
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            while (line.isEmpty()) line = sc.nextLine().trim();
            String[] p = line.split("\\s+");
            if (p.length < 4) { i--; System.out.println("Invalid — enter: name arrival burst priority"); continue; }
            list.add(new Proc(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3])));
        }

        while (true) {
            System.out.println("\nChoose algorithm:");
            System.out.println("1. FCFS");
            System.out.println("2. Priority (non-preemptive) (lower number = higher priority)");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int ch = Integer.parseInt(sc.nextLine().trim());
            if (ch == 3) break;
            List<Proc> copy = new ArrayList<>();
            for (Proc pr : list) copy.add(pr.copy());
            if (ch == 1) runFCFS(copy);
            else if (ch == 2) runPriorityNonPreemptive(copy);
            else System.out.println("Invalid choice.");
        }
        sc.close();
    }

    static void runFCFS(List<Proc> procs) {
        procs.sort(Comparator.comparingInt((Proc p)->p.arrival));
        int time = 0;
        List<String> gantt = new ArrayList<>();
        for (Proc p : procs) {
            if (time < p.arrival) {
                gantt.add("idle(" + (p.arrival - time) + ")");
                time = p.arrival;
            }
            p.start = time;
            time += p.burst;
            p.completion = time;
            p.turnaround = p.completion - p.arrival;
            p.waiting = p.start - p.arrival;
            gantt.add(p.name + "(" + p.burst + ")");
        }
        printResult("FCFS", procs, gantt);
    }

    static void runPriorityNonPreemptive(List<Proc> procs) {
        procs.sort(Comparator.comparingInt((Proc p)->p.arrival));
        int time = 0;
        List<String> gantt = new ArrayList<>();
        List<Proc> ready = new ArrayList<>();
        int completed = 0;
        boolean[] done = new boolean[procs.size()];
        while (completed < procs.size()) {
            for (int i = 0; i < procs.size(); i++) {
                if (!done[i] && procs.get(i).arrival <= time) ready.add(procs.get(i));
            }
            if (ready.isEmpty()) {

                int nextArr = Integer.MAX_VALUE;
                for (int i = 0; i < procs.size(); i++) if (!done[i]) nextArr = Math.min(nextArr, procs.get(i).arrival);
                if (nextArr == Integer.MAX_VALUE) break;
                gantt.add("idle(" + (nextArr - time) + ")");
                time = nextArr;
                continue;
            }
            
            ready.sort((a,b)-> {
                if (a.priority != b.priority) return Integer.compare(a.priority, b.priority);
                if (a.arrival != b.arrival) return Integer.compare(a.arrival, b.arrival);
                return a.name.compareTo(b.name);
            });
            Proc p = ready.remove(0);

            for (int i = 0; i < procs.size(); i++) {
                if (!done[i] && procs.get(i) == p) { done[i] = true; break; }
            }
            p.start = Math.max(time, p.arrival);
            time = p.start + p.burst;
            p.completion = time;
            p.turnaround = p.completion - p.arrival;
            p.waiting = p.start - p.arrival;
            completed++;

            ready.clear();
            gantt.add(p.name + "(" + p.burst + ")");
        }
        printResult("Priority (Non-preemptive)", procs, gantt);
    }

    static void printResult(String algo, List<Proc> procs, List<String> gantt) {
        System.out.println("\n=== " + algo + " ===");
        System.out.printf("%-6s %-8s %-6s %-8s %-10s %-10s%n", "Name","Arrival","Burst","Priority","Completion","Turnaround");
        double totalTurn = 0, totalWait = 0;
        procs.sort(Comparator.comparingInt((Proc p)->p.completion));
        for (Proc p : procs) {
            System.out.printf("%-6s %-8d %-6d %-8d %-10d %-10d%n", p.name, p.arrival, p.burst, p.priority, p.completion, p.turnaround);
            totalTurn += p.turnaround;
            totalWait += p.waiting;
        }
        int n = procs.size();
        System.out.printf("Average Turnaround = %.2f, Average Waiting = %.2f%n", totalTurn/n, totalWait/n);
        System.out.println("\nGantt-like timeline:");
        System.out.println(String.join(" | ", gantt));
    }
}

