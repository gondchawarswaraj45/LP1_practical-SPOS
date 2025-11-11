import java.util.*;
import java.io.*;

class MOTEntry {
    String mnemonic, classType;
    int opcode, length;

    MOTEntry(String m, String c, int o, int l) {
        mnemonic = m;
        classType = c;
        opcode = o;
        length = l;
    }
}

public class PassOneAssembler1 {
    static int LC = 0;
    static HashMap<String, MOTEntry> MOT = new HashMap<>();
    static LinkedHashMap<String, Integer> symbolTable = new LinkedHashMap<>();
    static ArrayList<String> literalTable = new ArrayList<>();
    static ArrayList<Integer> literalAddress = new ArrayList<>();
    static ArrayList<Integer> poolTable = new ArrayList<>();
    static ArrayList<String> intermediateCode = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        initializeMOT();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter assembly program (END to stop):");

        poolTable.add(0);
        String line;
        while (!(line = br.readLine()).equals("END")) {
            processLine(line.trim());
        }
        assignLiteralAddresses();
        intermediateCode.add("+ (AD,02)");


        System.out.println("\n----- SYMBOL TABLE -----");
        System.out.println("Symbol\tAddress");
        for (String sym : symbolTable.keySet())
            System.out.println(sym + "\t" + symbolTable.get(sym));

        System.out.println("\n----- LITERAL TABLE -----");
        System.out.println("Literal\tAddress");
        for (int i = 0; i < literalTable.size(); i++)
            System.out.println(literalTable.get(i) + "\t" + literalAddress.get(i));

        System.out.println("\n----- POOL TABLE -----");
        for (int i : poolTable)
            System.out.println(i);

        System.out.println("\n----- INTERMEDIATE CODE -----");
        for (String ic : intermediateCode)
            System.out.println(ic);
    }

    static void initializeMOT() {
        MOT.put("STOP", new MOTEntry("STOP", "IS", 0, 1));
        MOT.put("ADD", new MOTEntry("ADD", "IS", 1, 1));
        MOT.put("SUB", new MOTEntry("SUB", "IS", 2, 1));
        MOT.put("MULT", new MOTEntry("MULT", "IS", 3, 1));
        MOT.put("MOVER", new MOTEntry("MOVER", "IS", 4, 1));
        MOT.put("MOVEM", new MOTEntry("MOVEM", "IS", 5, 1));
        MOT.put("COMP", new MOTEntry("COMP", "IS", 6, 1));
        MOT.put("BC", new MOTEntry("BC", "IS", 7, 1));
        MOT.put("DIV", new MOTEntry("DIV", "IS", 8, 1));
        MOT.put("READ", new MOTEntry("READ", "IS", 9, 1));
        MOT.put("PRINT", new MOTEntry("PRINT", "IS", 10, 1));

        MOT.put("START", new MOTEntry("START", "AD", 1, 0));
        MOT.put("END", new MOTEntry("END", "AD", 2, 0));
        MOT.put("ORIGIN", new MOTEntry("ORIGIN", "AD", 3, 0));
        MOT.put("EQU", new MOTEntry("EQU", "AD", 4, 0));
        MOT.put("LTORG", new MOTEntry("LTORG", "AD", 5, 0));
        MOT.put("DS", new MOTEntry("DS", "DL", 1, 0));
        MOT.put("DC", new MOTEntry("DC", "DL", 2, 0));
    }

    static void processLine(String line) {
        if (line.isEmpty()) return;
        String[] parts = line.split("\\s+");
        int index = 0;


        if (!MOT.containsKey(parts[0])) {
            symbolTable.put(parts[0], LC);
            index++;
        }

        String mnemonic = parts[index];
        MOTEntry entry = MOT.get(mnemonic);
        if (entry == null) return;

        switch (entry.classType) {
            case "AD":
                handleAssemblerDirective(entry, parts, index + 1);
                break;
            case "DL":
                handleDeclarative(entry, parts, index + 1);
                break;
            case "IS":
                handleImperative(entry, parts, index);
                break;
        }
    }

    static void handleAssemblerDirective(MOTEntry entry, String[] parts, int i) {
        switch (entry.mnemonic) {
            case "START":
                LC = Integer.parseInt(parts[i]);
                intermediateCode.add("+ (AD,01)(C," + LC + ")");
                break;
            case "ORIGIN":
                LC = evaluateExpression(parts[i]);
                intermediateCode.add("+ (AD,03)(C," + LC + ")");
                break;
            case "EQU":
                symbolTable.put(parts[0], evaluateExpression(parts[i]));
                intermediateCode.add("+ (AD,04)(" + parts[0] + "," + symbolTable.get(parts[0]) + ")");
                break;
            case "LTORG":
                assignLiteralAddresses();
                poolTable.add(literalTable.size());
                intermediateCode.add("+ (AD,05)");
                break;
        }
    }

    static void handleDeclarative(MOTEntry entry, String[] parts, int i) {
        if (entry.mnemonic.equals("DS")) {
            int size = Integer.parseInt(parts[i]);
            intermediateCode.add("+ (DL,01)(C," + size + ")");
            LC += size;
        } else if (entry.mnemonic.equals("DC")) {
            intermediateCode.add("+ (DL,02)(C," + parts[i] + ")");
            LC++;
        }
    }

    static void handleImperative(MOTEntry entry, String[] parts, int i) {
        StringBuilder ic = new StringBuilder("+ (IS,");
        ic.append(String.format("%02d", entry.opcode)).append(")");

        LC++;

        for (int j = i + 1; j < parts.length; j++) {
            String operand = parts[j].replace(",", "");

            if (isRegister(operand)) {
                ic.append("(R,").append(regNum(operand)).append(")");
            } else if (operand.startsWith("=")) {
                if (!literalTable.contains(operand))
                    literalTable.add(operand);
                ic.append("(L,").append(literalTable.indexOf(operand)).append(")");
            } else {
                if (!symbolTable.containsKey(operand))
                    symbolTable.put(operand, -1);
                ic.append("(S,").append(symbolIndex(operand)).append(")");
            }
        }

        intermediateCode.add(ic.toString());
    }

    static boolean isRegister(String s) {
        return s.equals("AREG") || s.equals("BREG") || s.equals("CREG") || s.equals("DREG");
    }

    static int regNum(String reg) {
        switch (reg) {
            case "AREG": return 1;
            case "BREG": return 2;
            case "CREG": return 3;
            case "DREG": return 4;
        }
        return 0;
    }

    static int symbolIndex(String sym) {
        List<String> list = new ArrayList<>(symbolTable.keySet());
        return list.indexOf(sym);
    }

    static int evaluateExpression(String expr) {
        if (expr.contains("+")) {
            String[] p = expr.split("\\+");
            return symbolTable.get(p[0]) + Integer.parseInt(p[1]);
        } else if (expr.contains("-")) {
            String[] p = expr.split("\\-");
            return symbolTable.get(p[0]) - Integer.parseInt(p[1]);
        } else {
            try {
                return Integer.parseInt(expr);
            } catch (NumberFormatException e) {
                return symbolTable.getOrDefault(expr, 0);
            }
        }
    }

    static void assignLiteralAddresses() {
        for (int i = 0; i < literalTable.size(); i++) {
            if (literalAddress.size() <= i)
                literalAddress.add(LC++);
        }
    }
}

