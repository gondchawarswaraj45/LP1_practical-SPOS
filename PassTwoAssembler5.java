import java.io.*;
import java.util.*;

public class PassTwoAssembler5 {

    static class Symbol {
        String name;
        int address;
        Symbol(String n, int a) { name = n; address = a; }
    }

    static class Literal {
        String literal;
        int address;
        Literal(String l, int a) { literal = l; address = a; }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String outFile = System.getProperty("user.home") + "/Desktop/Output.txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

            ArrayList<Symbol> symtab = new ArrayList<>();
            ArrayList<Literal> littab = new ArrayList<>();
            ArrayList<String> intermediate = new ArrayList<>();

            System.out.println("===== PASS-II ASSEMBLER (USER INPUT MODE) =====");

            System.out.print("\nEnter number of symbols: ");
            int ns = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < ns; i++) {
                System.out.print("Enter symbol name and address : ");
                String[] s = sc.nextLine().trim().split("\\s+");
                symtab.add(new Symbol(s[0], Integer.parseInt(s[1])));
            }

            System.out.print("\nEnter number of literals: ");
            int nl = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < nl; i++) {
                System.out.print("Enter literal and address : ");
                String[] l = sc.nextLine().trim().split("\\s+");
                littab.add(new Literal(l[0], Integer.parseInt(l[1])));
            }

            System.out.print("\nEnter number of intermediate code lines: ");
            int ni = Integer.parseInt(sc.nextLine());
            System.out.println("Enter intermediate lines one by one :");

            for (int i = 0; i < ni; i++) {
                System.out.print("Line " + (i + 1) + ": ");
                intermediate.add(sc.nextLine().trim());
            }

            System.out.println("\n===== PASS-II FINAL MACHINE CODE =====\n");
            System.out.printf("%-10s%-15s%n", "LC", "Machine Code");
            System.out.println("------------------------------------");
            bw.write("LC\tMachine Code\n");

            int LC = 0;

            for (String line : intermediate) {


                if (line.contains("AD")) {
                    if (line.contains("(C,")) {
                        int start = line.indexOf("(C,") + 3;
                        int end = line.indexOf(")", start);
                        LC = Integer.parseInt(line.substring(start, end));
                    }
                    continue;
                }


                if (line.contains("DL,01")) { 
                    if (line.contains("(C,")) {
                        int start = line.indexOf("(C,") + 3;
                        int end = line.indexOf(")", start);
                        int val = Integer.parseInt(line.substring(start, end));
                        printAndWrite(bw, LC, "00", "00", pad(String.valueOf(val), 3));
                        LC++;
                    }
                    continue;
                }

                if (line.contains("DL,02")) { 
                    printAndWrite(bw, LC, "00", "00", "000");
                    LC++;
                    continue;
                }


                if (line.contains("IS")) {
                    int startOp = line.indexOf("IS,") + 3;
                    int endOp = line.indexOf(")", startOp);
                    String op = pad(line.substring(startOp, endOp), 2);
                    String reg = "00";
                    String mem = "000";

                    if (line.contains(")(")) {
                        int pos = line.indexOf(")(") + 2;
                        if (pos < line.length()) reg = line.substring(pos, pos + 1);
                        reg = pad(reg, 2);
                    }

                    if (line.contains("(S,")) { 
                        int s = line.indexOf("(S,") + 3;
                        int e = line.indexOf(")", s);
                        int index = Integer.parseInt(line.substring(s, e));
                        mem = String.valueOf(symtab.get(index - 1).address);
                    } else if (line.contains("(L,")) { 
                        int s = line.indexOf("(L,") + 3;
                        int e = line.indexOf(")", s);
                        int index = Integer.parseInt(line.substring(s, e));
                        mem = String.valueOf(littab.get(index - 1).address);
                    } else if (line.contains("(C,")) { 
                        int s = line.indexOf("(C,") + 3;
                        int e = line.indexOf(")", s);
                        mem = line.substring(s, e);
                    }

                    mem = pad(mem, 3);
                    printAndWrite(bw, LC, op, reg, mem);
                    LC++;
                }
            }

            bw.close();
            System.out.println("\n✅ Output saved to: " + outFile);

        } catch (Exception e) {
            System.out.println("\n❌ Error: " + e.getMessage());
        }
    }


    public static void printAndWrite(BufferedWriter bw, int lc, String op, String reg, String mem) throws IOException {
        String formatted = String.format("%-10d+%s %s %s", lc, op, reg, mem);
        System.out.println(formatted);
        bw.write(formatted + "\n");
    }


    public static String pad(String s, int len) {
        while (s.length() < len) s = "0" + s;
        return s;
    }
}

