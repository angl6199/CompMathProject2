import java.io.*;
import java.util.*;

public class Main {
  /* In these variables the data of the file will be store */
  private static String[] nonterminals;
  private static String[] terminals;
  private static String initial;
  private static Hashtable<Integer, String[]> table = new Hashtable<Integer, String[]>();

  public static void main(String[] args) throws FileNotFoundException, IOException {
    Scanner sc = new Scanner(System.in);
    System.out.println("Please enter the name of the file you want to work with (include '.txt')");
    String filename = sc.nextLine();

    /* We call the function "ReadFile" which reads the txt file */
    ReadFile(filename);

    System.out.println();
    System.out.println("Please enter the string to be tested");
    String input = sc.nextLine();
    System.out.println("Please enter the maximum depth");
    int maxdepth = sc.nextInt();
    System.out.println();

    /* We call the function that creates and validates in the tree of the grammar
     * the entered string */
    BuildTree(input, maxdepth);
  }

  /**
   * This method reads the grammar data from the file line by line and saves all
   * the components in its corresponding variables.
   *
   * @param name the name of the file including .txt
   */
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
        // This refers to line 0 which has the non-terminal symbols separated by commas
        nonterminals = SplitWithComma(line);
        for (String string : nonterminals) {
          System.out.println(string);
        }
      }

      if (counter == 1) {
        // This refers to line 1 which has terminal symbols separated by commas
        terminals = SplitWithComma(line);
        for (String string : terminals) {
          System.out.println(string);
        }
      }

      if (counter == 2) {
        // This refers to line 2 which has the start symbol
        initial = line;
        System.out.println(initial);
      }

      if (counter >= 3) {
        // This refers to lines 4 and further which have all the productions of the
        // grammar
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
    // We call the function that prints the retrieved data from the file.
    PrintTable2();
  }

  /**
   * 
   * Returns an array of strings to be saved in the grammar tree. Used for reading
   * the file.
   * 
   * * This method separates the received line from the file into multiple strings
   * whenever it finds a comma and stores them in an array
   *
   * @param line a string containing grammar data from the file to be separated
   *             with commas.
   *
   * @return the grammar data stored in an array.
   * 
   */
  public static String[] SplitWithComma(String line) {
    return line.split(",");
  }

  /**
   * Returns an array of strings containing the productions of the grammar.
   * 
   * Ths method is used when reading the grammar data from the file. It separates
   * the received line from the file into multiple strings whenever it finds an
   * arrow, then it stores them in an array
   *
   * @param line a string containing a transition with the following form: S=>At
   * 
   * @return array of strings containing the grammar productions
   * 
   */
  public static String[] SplitProductions(String line) {
    String[] array = line.split("->");
    return array;
  }

  /**
   * This method is used for prining the information the information in the same
   * format that it was retrieved from the txt file. It helps the user to see data
   * in a friendly way.
   */
  public static void PrintTable2() {
    for (int i = 0; i < table.size(); i++) {
      String temp = i + 1 + " " + table.get(i + 1)[0] + "->" + table.get(i + 1)[1];
      System.out.println(temp);
    }
  }

  /**
   * This method is used to calculate the actual depth of the node that has been
   * stored in the tree.
   * 
   * It uses the properties of the Node class, so it can get the parent of the
   * node recusively until it reaches the root and it counts how many times it has
   * found a parent Node.
   * 
   * @param temp an object Node that has been recently added to the tree
   * 
   * @return an int with the depth of the node
   */
  public static int calculateDepth(Node temp) {
    int counter = 0;
    while (temp.getParent() != null) {
      temp = temp.getParent();
      counter++;
    }
    return counter;
  }

  public static void printTree(Node node, String appender) {
    System.out.println(appender + node.getData());
    node.getChildren().forEach(each -> printTree(each, appender + appender));
  }

  /**
   * This method has the purpose of validate the given input with the grammar
   * productions which are stored at the same time into a tree.
   * 
   * The method uses the Top-down syntatic analysis algorithm, which builds every
   * possible production of the non-terminal symbols to build a son Node and saves
   * it in the tree, repeating the process with every node in the level of the
   * tree until the maximum depth is reached or whenever the string is found.
   * 
   * @param input    string to be tested within the grammar productions, given by
   *                 the user.
   * @param maxdepth int the maximum depth that the tree can have to find the
   *                 input string given by the user.
   */
  public static void BuildTree(String input, int maxdepth) {
    Node root = new Node(initial);
    Queue<Node> Q = new LinkedList<>();
    Q.add(root);
    String uwv = "";
    int actualdepth = 0;

    // Green While
    while (!Q.isEmpty() && !input.equals(uwv) && actualdepth <= maxdepth) {

      Node q = Q.poll();
      int i = 0;
      boolean done = false;
      String varsust = GetLeft(q);

      // Red while
      while (!done && !input.equals(uwv)) {
        if (!MoreRules(varsust, i)) {
          done = true;
        } else {
          int j = FindProduction(varsust, i);
          String vartemp = table.get(j)[1];
          uwv = q.getData().replaceFirst(varsust, vartemp);
          Node temp = new Node(uwv);
          q.addChild(temp);

          if (EnterQueue(uwv, input)) {
            Q.add(temp);
          } else {
            System.out.println(uwv + " was not inserted");
          }

          i = j;
          actualdepth = calculateDepth(temp);
          if (actualdepth > maxdepth) {
            temp.deleteNode();
          }
        }
      }
    }
    printTree(root, " ");
    System.out.println();

    if (input.equals(uwv) && actualdepth <= maxdepth) {
      System.out.println(
          "Input: " + input + " was accepted and found in level: " + (actualdepth + 1) + " with depth: " + actualdepth);
    } else {
      System.out.println("Input: " + input + " was not accepted.");
    }
    System.out.println();
    generateSyntax(root);

  }

  /**
   * This method retrieves the string stored in the node, produced with the
   * grammar of the tree and gets the first non-terminal symbol to use it in the
   * top-down algorithm.
   * 
   * To ensure that it gets a valid symbol, the string is compared with the array
   * that contains all the non-terminal symbols from the txt file.
   * 
   * @param q a node object that has been produced from the tree grammar
   * @return a string containing a non-terminal symbol if possible, else null
   */
  public static String GetLeft(Node q) {
    for (int j = 0; j < q.getData().length(); j++) {
      for (int k = 0; k < nonterminals.length; k++) {
        if (q.getData().charAt(j) == (nonterminals[k].charAt(0))) {
          return nonterminals[k];
        }
      }
    }
    return null;
  }

  /**
   * This method looks for any rules among the grammar that can lead to any
   * production with the received non-terminal symbol.
   * 
   * To avoid repeating rules, the method receives a constantly increased variable
   * from the top-down algorithm and uses it as a key to search the productions in
   * the hashtable.
   * 
   * @param varsust a string containing the non-terminal symbol
   * @param i       an int containing the number of the last rule found in the
   *                table
   * 
   * @return a boolean indicating if there is any other rule available to create a
   *         production.
   */
  public static boolean MoreRules(String varsust, int i) {
    for (int j = i + 1; j <= table.size(); j++) {
      if (table.get(j)[0].equals(varsust)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method assumes that there is an available rule in the grammar to create
   * a production that has not been used. Therefor it searches in the hashtable
   * using an increased int received as key to find the next rule of production.
   * 
   * @param varsust a string that contains the non-terminal symbol
   * @param i       an int containing the last number of rule used in the grammar
   * @return an int that contains the number of the new rule of the grammar in the
   *         hashtable
   */
  public static int FindProduction(String varsust, int i) {
    for (int j = i + 1; j <= table.size(); j++) {
      if (table.get(j)[0].equals(varsust)) {
        return j;
      }
    }
    return -1;
  }

  /**
   * This method compares the last string produced in order to know if it can approach
   * the input string to keep producing it or not.
   * 
   * To achieve the comparisson, first the method defines if the string starts with a 
   * non terminal symbol and automatically keep producing it. If not, the algorithm 
   * looks for any possible non terminal symbol in the string received, if it is found
   * the string is splitted to compare if the rest of it approaches the input string and
   * keep producing it, otherwise it stops.
   * 
   * @param uwv a string containing the last production made in the tree with the grammar
   * @param input a string containing the string to be tested by the user
   * @return  a boolean indicating if the last string produced must be produced again or not
   */
  public static boolean EnterQueue(String uwv, String input) {

    for (int i = 0; i < nonterminals.length; i++) {
      if (uwv.charAt(0) == nonterminals[i].charAt(0)) {
        return true;
      }
    }

    String uwvtrimmed = "";
    int index = 0;
    boolean found = false;

    for (int i = 0; i < uwv.length(); i++) {
      for (int j = 0; j < nonterminals.length; j++) {
        if (uwv.charAt(i) == nonterminals[j].charAt(0) && found == false) {
          index = i;
          found = true;

        }
      }
    }

    uwvtrimmed = uwv.substring(0, index);

    if (uwvtrimmed.length() > input.length()) {
      return false;
    } else {

      for (int i = 0; i < uwvtrimmed.length(); i++) {
        if (uwvtrimmed.charAt(i) != input.charAt(i)) {
          return false;
        }
      }
      return true;

    }

  }

  /**
   * 
   * @param root
   */
  public static void generateSyntax(Node root) {
    String result = "";
    int counter = 0;
    Stack<Node> queue = new Stack<>();
    queue.add(root);

    while (!queue.isEmpty()) {
      Node nodeTemp = queue.pop();
      result += "[" + nodeTemp.getData();

      // Si no tiene hijos su remaining es cero
      if (nodeTemp.getChildren().isEmpty()) {
        nodeTemp.setRemaining(0);

        // Si no tiene papá, poner ]
        if (nodeTemp.getParent() == null) {
          result += "]";
        } else {
          // Si tiene papá
          // si su remaining es cero, poner ]
          if (nodeTemp.getRemaining() == 0) {
            result += "]";

          }
          // ir al papá, restarle uno

          Node papa = nodeTemp.getParent();
          papa.setRemaining(papa.getRemaining() - 1);

          // Si el papá es cero poner v1 counter++ v2 poner ], v3 subir hasta que haya
          // padres, cada cero agrega un ]
          while (nodeTemp.getParent() != null && nodeTemp.getParent().getRemaining() == 0) {
            nodeTemp = nodeTemp.getParent();
            if (nodeTemp.getRemaining() == 0 && !nodeTemp.getDeuda()) {
              result += "]";
              nodeTemp.setDeuda(true);
            }
          }

        }

      } else {
        // si tiene hijos
        if (!nodeTemp.getChildren().isEmpty()) {
          // Si tiene papá
          if (nodeTemp.getParent() != null) {
            // ir al papá y restarle uno, actualizar su remaining =
            // #hijos

            Node papa = nodeTemp.getParent();
            papa.setRemaining(papa.getRemaining() - 1);
            List<Node> hijos = nodeTemp.getChildren();
            nodeTemp.setRemaining(hijos.size());

            // Si el papá se hace cero v1 counter++ v3 no va
            if (papa.getRemaining() == 0) {
              counter++;
            }

            // Meter hijos a la cola
            queue.addAll(hijos);

          } else {
            // Si no tiene papá, meto los hijos a la cola y le pongo su remaining = # hijos
            List<Node> hijos = nodeTemp.getChildren();
            nodeTemp.setRemaining(hijos.size());
            queue.addAll(hijos);
          }

        }

      }
    }
    // Por cada uno del counter agregar ]
    for (int i = 0; i < counter; i++) {
      // result += "]";

    }

    System.out.println(result);

  }

  public static int countOcurrences(String s, char t) {
    int result = 0;
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == t) {
        result++;
      }
    }
    return result;
  }

}