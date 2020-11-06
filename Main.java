import java.io.*;
import java.util.*;

public class Main {
  private static String[] nonterminals;
  private static String[] terminals;
  private static String initial;
  private static Hashtable<Integer, String[]> table = new Hashtable<Integer, String[]>();

  public static void main(String[] args) throws FileNotFoundException, IOException {
    Scanner sc = new Scanner(System.in);
    System.out.println("Please enter the name of the file you want to work with (include '.txt')");
    String filename = sc.nextLine();

    ReadFile(filename);

    System.out.println();
    System.out.println("Please enter the string to be tested");
    String input = sc.nextLine();
    System.out.println("Please enter the maximum depth");
    int maxdepth = sc.nextInt();
    System.out.println();

    BuildTree(input, maxdepth);
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
      System.out.println("Llave: " + llaves.nextElement() + " Valor: " + enumeration.nextElement());

    }
  }

  public static void PrintTable2() {
    for (int i = 0; i < table.size(); i++) {
      String temp = i + 1 + " " + table.get(i + 1)[0] + "->" + table.get(i + 1)[1];
      System.out.println(temp);
    }
  }

  public static void calculateDepth(Node temp) {
    int counter = 0;
    while (temp.getParent() != null) {
      temp = temp.getParent();
      counter++;
    }
    System.out.println(counter);
  }

  public static void printTree(Node node, String appender) {
    System.out.println(appender + node.getData());
    node.getChildren().forEach(each -> printTree(each, appender + appender));
  }

  public static void BuildTree(String input, int maxdepth) {
    Node root = new Node(initial);
    Queue<Node> Q = new LinkedList<>();
    Q.add(root);
    String uwv = "";
    int actualdepth = 0;

    //While verde
    while (Q.peek()!=null && !input.equals(uwv) && actualdepth<=maxdepth) {
      Node q = Q.poll();
      int i = 0;
      boolean done = false;
      String varsust = GetLeft(q);

      //While rojo
      while (!done && !input.equals(uwv)) {
        if (!MoreRules(varsust, i)) {
          done = true;
        } else{
          int j = FindProduction(varsust, i);
          String vartemp = table.get(j)[1];
          uwv = q.getData().replaceFirst(varsust, vartemp);
        }
        
      }
    }
  }

  public static String GetLeft(Node q){
    for (int j = 0; j < q.getData().length(); j++) {
      for (int k = 0; k < nonterminals.length; k++) {
        if (q.getData().charAt(j) == (nonterminals[k].charAt(0))) {
          return nonterminals[k];
        } 
      }
    }
    return null;
  }

  public static boolean MoreRules(String varsust, int i){
    for (int j = i+1; j <= table.size(); j++) {
      if (table.get(j)[0].equals(varsust)) {
        return true;
      }
    }
    return false;
  }

  public static int FindProduction(String varsust, int i){
    for (int j = i+1; j <= table.size(); j++) {
      if (table.get(j)[0].equals(varsust)) {
        return j;
      }
    }
    return -1;
  }

}