import java.util.*;

class Process {
    int pid, at, bt, ct, tat, wt, remaining;
}

public class CPUScheduling_FCFS_RR8 {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("   CPU SCHEDULING SIMULATOR   ");
            System.out.println("==============================");
            System.out.println("1. First Come First Serve (FCFS)");
            System.out.println("2. Round Robin (RR)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 3) {
                System.out.println("Exiting... Thank you!");
                break;
            }

            System.out.print("\nEnter number of processes: ");
            int n = sc.nextInt();
            Process[] p = new Process[n];

            for (int i = 0; i < n; i++) {
                p[i] = new Process();
                p[i].pid = i + 1;
                System.out.print("Enter Arrival Time and Burst Time for P" + (i + 1) + ": ");
                p[i].at = sc.nextInt();
                p[i].bt = sc.nextInt();
                p[i].remaining = p[i].bt;
            }

            switch (choice) {
                case 1:
                    fcfs(p, n);
                    break;
                case 2:
                    roundRobin(p, n);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }


    static void fcfs(Process[] p, int n) {
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));
        int time = 0;
        double totalTAT = 0, totalWT = 0;

        for (Process pr : p) {
            if (time < pr.at) time = pr.at;
            time += pr.bt;
            pr.ct = time;
            pr.tat = pr.ct - pr.at;
            pr.wt = pr.tat - pr.bt;
            totalTAT += pr.tat;
            totalWT += pr.wt;
        }

        System.out.println("\n--- FCFS Scheduling ---");
        System.out.println("PID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);
    }


    static void roundRobin(Process[] p, int n) {
        System.out.print("Enter Time Quantum: ");
        int tq = sc.nextInt();

        int time = 0, completed = 0;
        double totalTAT = 0, totalWT = 0;
        Queue<Process> q = new LinkedList<>();

        Arrays.sort(p, Comparator.comparingInt(a -> a.at));
        int i = 0;
        q.add(p[i]);
        i++;

        System.out.println("\n--- Round Robin Scheduling ---");
        System.out.println("Gantt Chart:");

        while (!q.isEmpty()) {
            Process cur = q.poll();

            if (cur.remaining > tq) {
                System.out.print(" P" + cur.pid);
                time += tq;
                cur.remaining -= tq;
            } else {
                System.out.print(" P" + cur.pid);
                time += cur.remaining;
                cur.remaining = 0;
                cur.ct = time;
                cur.tat = cur.ct - cur.at;
                cur.wt = cur.tat - cur.bt;
                totalTAT += cur.tat;
                totalWT += cur.wt;
                completed++;
            }


            while (i < n && p[i].at <= time) {
                q.add(p[i]);
                i++;
            }


            if (cur.remaining > 0) q.add(cur);


            if (q.isEmpty() && i < n) {
                time = p[i].at;
                q.add(p[i]);
                i++;
            }
        }

        System.out.println("\n\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);
    }
}

