import java.util.*;

class Process {
    int pid, at, bt, prio, ct, tat, wt;
}

public class CPUSchedulingPriority4 {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("   CPU SCHEDULING SIMULATOR   ");
            System.out.println("==============================");
            System.out.println("1. First Come First Serve (FCFS)");
            System.out.println("2. Priority Scheduling (Non-Preemptive)");
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

                if (choice == 2) {
                    System.out.print("Enter Priority for P" + (i + 1) + ": ");
                    p[i].prio = sc.nextInt();
                }
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- FCFS Scheduling ---");
                    fcfs(p, n);
                    break;

                case 2:
                    System.out.println("\n--- Priority (Non-Preemptive) Scheduling ---");
                    priorityNonPreemptive(p, n);
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
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

  
    static void priorityNonPreemptive(Process[] p, int n) {
        int completed = 0, time = 0;
        boolean[] isCompleted = new boolean[n];
        double totalTAT = 0, totalWT = 0;

        while (completed != n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (p[i].at <= time && !isCompleted[i]) {
                    if (p[i].prio < highestPriority) {
                        highestPriority = p[i].prio;
                        idx = i;
                    } else if (p[i].prio == highestPriority && p[i].at < p[idx].at) {
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                time++;
                continue;
            }

            time += p[idx].bt;
            p[idx].ct = time;
            p[idx].tat = p[idx].ct - p[idx].at;
            p[idx].wt = p[idx].tat - p[idx].bt;
            totalTAT += p[idx].tat;
            totalWT += p[idx].wt;
            isCompleted[idx] = true;
            completed++;
        }

        System.out.println("\nPID\tAT\tBT\tPRIO\tCT\tTAT\tWT");
        for (Process pr : p)
            System.out.println("P" + pr.pid + "\t" + pr.at + "\t" + pr.bt + "\t" + pr.prio + "\t" + pr.ct + "\t" + pr.tat + "\t" + pr.wt);

        System.out.printf("\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);
    }
}

