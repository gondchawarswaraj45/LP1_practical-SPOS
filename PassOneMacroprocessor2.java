import java.util.*;
import java.io.*;

class MNTEntry {
    String name;
    int mdtIndex;
    MNTEntry(String name, int mdtIndex) {
        this.name = name;
        this.mdtIndex = mdtIndex;
    }
}

public class PassOneMacroprocessor2 {
    static ArrayList<MNTEntry> MNT = new ArrayList<>();
    static ArrayList<String> MDT = new ArrayList<>();
    static ArrayList<LinkedHashMap<String, String>> ALAList = new ArrayList<>();
    static int MDTIndex = 1;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter macro program (END to stop):");
        boolean inMacro = false;
        String macroName = null;
        LinkedHashMap<String, String> currentALA = null;
        String line;
        while (!(line = br.readLine()).equalsIgnoreCase("END")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("MACRO")) {
                inMacro = true;
                currentALA = new LinkedHashMap<>();
                continue;
            }
            if (inMacro) {
                if (macroName == null) {
                    String[] header = line.split("[ ,\\t]+");
                    macroName = header[0];
                    MNT.add(new MNTEntry(macroName, MDTIndex));
                    for (int i = 1; i < header.length; i++) {
                        if (header[i].startsWith("&")) {
                            currentALA.put(header[i], "#" + currentALA.size());
                        }
                    }
                    MDT.add(String.format("%02d) %s", MDTIndex++, String.join(" ", header)));
                    ALAList.add(currentALA);
                } else if (line.equalsIgnoreCase("MEND")) {
                    MDT.add(String.format("%02d) MEND", MDTIndex++));
                    inMacro = false;
                    macroName = null;
                } else {
                    for (String arg : currentALA.keySet()) {
                        if (line.contains(arg))
                            line = line.replace(arg, currentALA.get(arg));
                    }
                    MDT.add(String.format("%02d) %s", MDTIndex++, line));
                }
            }
        }
        printTables();
    }

    static void printTables() {
        System.out.println("\n----- MNT (Macro Name Table) -----");
        System.out.println("Index\tMacro Name\tMDT Index");
        int i = 1;
        for (MNTEntry e : MNT)
            System.out.println(String.format("%02d\t%s\t\t%02d", i++, e.name, e.mdtIndex));
        System.out.println("\n----- ALA (Argument List Array) -----");
        for (int j = 0; j < ALAList.size(); j++) {
            System.out.println("ALA for Macro " + (j + 1) + ":");
            int k = 1;
            for (String arg : ALAList.get(j).keySet())
                System.out.println(String.format("%02d\t%s -> %s", k++, arg, ALAList.get(j).get(arg)));
            System.out.println();
        }
        System.out.println("----- MDT (Macro Definition Table) -----");
        for (String m : MDT)
            System.out.println(m);
    }
}

