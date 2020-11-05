import java.io.*;
import java.util.*;

public class Main {
  private static String[] nonterminals;
  private static String[] terminals;
  private static String initial;
  private static Hashtable<Integer, String[]> table = new Hashtable<Integer, String[]>();

  public static void main(String[] args) throws FileNotFoundException, IOException {
    ReadFile("test1.txt");
  }

  public static void ReadFile(String name) throws FileNotFoundException, IOException {
    // These are the objects that will allow us to read the data from the txt file
    File file = new File(name);
    FileReader fr = new FileReader(file);
    BufferedReader br = new BufferedReader(fr);

    // These variables will allow us to read and separate data in lines
    String line;
    int counter = 0;
    while ((line = br.readLine()) != null) {

      if (counter == 0) {
        // This refers to line 0 which has all the states of the DFA
        nonterminals = SplitWithComma(line);
        for (String string : nonterminals) {
          System.out.println(string);
        }
      }

      if (counter == 1) {
        // This refers to line 1 which has all the characters of the alphabet of the DFA
        terminals = SplitWithComma(line);
        for (String string : terminals) {
          System.out.println(string);
        }
      }

      if (counter == 2) {
        // This refers to line 2 which has the initial state of the DFA
        initial = line;
        System.out.println(initial);
      }

      if (counter >= 3) {
        // This refers to lines 4 and further which have all the transitions of the DFA
        String[] array = SplitProductions(line);
        if (table.isEmpty()) {
          table.put(1, array);
        } else {
          int index = table.size();
          table.put(index + 1, array);
        }
      }

      counter++;
    }
    br.close();
    PrintTable2();
  }

  public static String[] SplitWithComma(String line) {
    return line.split(",");
  }

  public static String[] SplitProductions(String line) {
    String[] array = line.split("->");
    return array;
  }

  public static void PrintTable() {
    Enumeration enumeration = table.elements();
    Enumeration llaves = table.keys();
    while (enumeration.hasMoreElements()) {
      System.out.println("Llave: " + llaves.nextElement() + " Valor: " +  enumeration.nextElement());

    }
  }

  public static void PrintTable2() {
    for (int i = 0; i < table.size(); i++) {
      String temp = i+1 + " " + table.get(i+1)[0] + "->" + table.get(i+1)[1];
      System.out.println(temp);
    }
  }


}