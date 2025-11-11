import java.util.*;

public class PassTwoMacroprocessor16 {

    static class MNTEntry {
        String name;
        int pp, kp, mdtp, kpdtp;
        MNTEntry(String n, int p, int k, int m, int kpdt) {
            name = n;
            pp = p;
            kp = k;
            mdtp = m;
            kpdtp = kpdt;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, MNTEntry> MNT = new HashMap<>();
        List<String> MDT = new ArrayList<>();
        List<String> intermediate = new ArrayList<>();

        System.out.println("===== PASS-II MACROPROCESSOR (USER INPUT MODE) =====");

        System.out.print("\nEnter number of macros in MNT: ");
        int mntCount = Integer.parseInt(sc.nextLine());
        System.out.println("Enter MNT entries in format: <Name> <PP> <KP> <MDTP> <KPDT>:");
        for (int i = 0; i < mntCount; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            MNT.put(parts[0], new MNTEntry(parts[0],
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4])));
        }

        System.out.println("\nEnter MDT lines (type 'MEND' twice to finish):");
        int mendCount = 0;
        while (true) {
            String line = sc.nextLine().trim();
            MDT.add(line);
            if (line.equalsIgnoreCase("MEND")) mendCount++;
            if (mendCount == 2) break;
        }

        System.out.print("\nEnter number of intermediate lines: ");
        int interCount = Integer.parseInt(sc.nextLine());
        System.out.println("Enter Intermediate lines (macro calls):");
        for (int i = 0; i < interCount; i++) {
            intermediate.add(sc.nextLine().trim());
        }

        System.out.println("\n===== PASS-II MACRO EXPANSION =====\n");

        for (String line : intermediate) {
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+|,");
            String macroName = parts[0];

            if (!MNT.containsKey(macroName)) {
                System.out.println(line);
                continue;
            }

            MNTEntry entry = MNT.get(macroName);
            int pp = entry.pp;
            int kp = entry.kp;
            int mdtp = entry.mdtp;

            Map<String, String> AL = new LinkedHashMap<>();

            for (int i = 1; i <= pp; i++) {
                if (i < parts.length)
                    AL.put("#" + i, parts[i]);
            }

            int index = 3; 
            for (int i = pp + 1; i < parts.length; i++) {
                if (parts[i].contains("=")) {
                    String[] kv = parts[i].split("=");
                    if (kv.length == 2) {
                        AL.put("#" + index, kv[1].trim());
                        index++;
                    }
                }
            }

            for (int i = mdtp - 1; i < MDT.size(); i++) {
                String macroLine = MDT.get(i);
                if (macroLine.equalsIgnoreCase("MEND"))
                    break;
                for (Map.Entry<String, String> arg : AL.entrySet()) {
                    macroLine = macroLine.replace(arg.getKey(), arg.getValue());
                }
                System.out.println(macroLine);
            }
        }

        sc.close();
    }
}

