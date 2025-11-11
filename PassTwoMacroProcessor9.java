import java.io.*;
import java.util.*;

class PassTwoMacroProcessor9 {
    static class MNTEntry {
        String name;
        int pp, kp, mdtIndex;
        MNTEntry(String n, int p1, int p2, int idx) {
            name = n; pp = p1; kp = p2; mdtIndex = idx;
        }
    }

    static class MDTEntry {
        String line;
        MDTEntry(String l) { line = l; }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader brMNT = new BufferedReader(new FileReader("Mnt.txt"));
        BufferedReader brMDT = new BufferedReader(new FileReader("Mdt.txt"));
        BufferedReader brINT = new BufferedReader(new FileReader("Intermediate.txt"));

        ArrayList<MNTEntry> MNT = new ArrayList<>();
        ArrayList<MDTEntry> MDT = new ArrayList<>();

        String line;
        while ((line = brMNT.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            MNT.add(new MNTEntry(parts[0],
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]),
                    Integer.parseInt(parts[3])));
        }

        while ((line = brMDT.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            MDT.add(new MDTEntry(line));
        }

        System.out.println("===== PASS-II OUTPUT =====\n");

        while ((line = brINT.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String macroName = parts[0];
            String argString = parts.length > 1 ? parts[1] : "";

            MNTEntry mntEntry = null;
            for (MNTEntry e : MNT)
                if (e.name.equals(macroName)) mntEntry = e;

            if (mntEntry == null) {
                System.out.println(line);
                continue;
            }

            HashMap<Integer, String> ALA = new HashMap<>();
            String[] args = argString.split(",");

            for (int i = 0; i < mntEntry.pp && i < args.length; i++) {
                ALA.put(i + 1, args[i]);
            }

            for (int i = mntEntry.pp; i < args.length; i++) {
                String[] kv = args[i].split("=");
                if (kv.length == 2) {
                    ALA.put(i + 1, kv[1]);
                }
            }

            int index = mntEntry.mdtIndex - 1;
            for (int i = index; i < MDT.size(); i++) {
                String mline = MDT.get(i).line;
                if (mline.equalsIgnoreCase("MEND"))
                    break;

                for (Map.Entry<Integer, String> e : ALA.entrySet()) {
                    mline = mline.replace("#" + e.getKey(), e.getValue());
                }

                System.out.println(mline);
            }
        }

        brMNT.close();
        brMDT.close();
        brINT.close();
        System.out.println("\n Macro expansion completed successfully!");
    }
}

