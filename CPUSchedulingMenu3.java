import java.util.*;

class Process {
    int pid, at, bt, ct, tat, wt, remaining;
}

public class CPUSchedulingMenu3 {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("   CPU SCHEDULING SIMULATOR   ");
            System.out.println("==============================");
            System.out.println("1. First Come First Serve (FCFS)");
            System.out.println("2. Shortest Job First (Preemptive)");
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
                System.out.print("Enter Arrival Time and Burst Time for P" + (i + 1) + ": ");
                p[i].pid = i + 1;
                p[i].at = sc.nextInt();
                p[i].bt = sc.nextInt();
                p[i].remaining = p[i].bt;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- FCFS Scheduling ---");
                    fcfs(p, n);
                    break;

                case 2:
                    System.out.println("\n--- SJF (Preemptive) Scheduling ---");
                    sjfPreemptive(p, n);
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }


    static void fcfs(Process[] p, int n) {
        Arrays.sort(p, Comparator.comparingInt(a -> a.at));

        int time = 0;
        double totalTAT = 0, totalWT = 0;

        for (Process pr : p) {
            if (time < pr.at)
                time = pr.at;
            time += pr.bt;
            pr.ct = time;
            pr.tat = pr.ct - pr.at;
            pr.wt = pr.tat - pr.bt;
            totalTAT += pr.tat;
            totalWT += pr.wt;
        }

        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);
    }


    static void sjfPreemptive(Process[] orig, int n) {
        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            p[i].pid = orig[i].pid;
            p[i].at = orig[i].at;
            p[i].bt = orig[i].bt;
            p[i].remaining = orig[i].bt;
        }

        int completed = 0, time = 0, minIndex = -1, minRemaining;
        double totalTAT = 0, totalWT = 0;
        boolean[] isCompleted = new boolean[n];

        System.out.print("\nGantt Chart: ");
        while (completed != n) {
            minRemaining = Integer.MAX_VALUE;
            minIndex = -1;

            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && !isCompleted[i] && p[i].remaining < minRemaining && p[i].remaining > 0) {
                    minRemaining = p[i].remaining;
                    minIndex = i;
                }
            }

            if (minIndex == -1) {
                time++;
                continue;
            }

            System.out.print(" P" + p[minIndex].pid);
            p[minIndex].remaining--;
            time++;

            if (p[minIndex].remaining == 0) {
                isCompleted[minIndex] = true;
                completed++;
                p[minIndex].ct = time;
                p[minIndex].tat = p[minIndex].ct - p[minIndex].at;
                p[minIndex].wt = p[minIndex].tat - p[minIndex].bt;
                totalTAT += p[minIndex].tat;
                totalWT += p[minIndex].wt;
            }
        }

        System.out.println("\n\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);
    }
}

