import java.io.*;
import java.util.*;

public class Main {
  // These are the variables where the data of the file will be stored
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

    Node root = BuildTree(input, maxdepth);
    generateSyntax(root);

    sc.close();
  }

  /**
   * 
   * This method reads the data from the file line by line and saves all the
   * components in its corresponding variables.
   *
   * @param name the name of the file including .txt
   *
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
        // This refers to line 0 which has all the nonterminal symbols
        nonterminals = SplitWithComma(line);
      }

      if (counter == 1) {
        // This refers to line 1 which has all the terminal symbols
        terminals = SplitWithComma(line);
      }

      if (counter == 2) {
        // This refers to line 2 which has the initial symbol
        initial = line;
      }

      if (counter >= 3) {
        // This refers to lines which have the productions
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
  }

  /**
   * 
   * Returns an array of strings to be saved. Used for reading the file.
   * 
   * * This method separates the received line from the file into multiple strings
   * whenever it finds a comma and stores them in an array
   *
   * @param line a string containing data from the file to be separated with
   *             commas.
   *
   * @return the data stored in an array.
   * 
   */
  public static String[] SplitWithComma(String line) {
    return line.split(",");
  }

  /**
   * Returns an array of strings containing the productions.
   * 
   * This method is used when reading the data from the file. It separates the
   * received line from the file into multiple strings whenever it finds an arrow,
   * then it stores them in an array.
   *
   * @param line a string containing a production with the following form: S->a
   * 
   * @return productions array
   * 
   */
  public static String[] SplitProductions(String line) {
    String[] array = line.split("->");
    return array;
  }



  /**
   * Returns an int that is the depth of the given node of the tree.
   * 
   * This method is used after inserting a node in the tree. It finds the depth of
   * the given node.
   *
   * @param temp a node recently inserted in the tree
   * 
   * @return depth of the node
   * 
   */
  public static int calculateDepth(Node temp) {
    int counter = 0;
    while (temp.getParent() != null) {
      temp = temp.getParent();
      counter++;
    }
    return counter;
  }

  /**
   * Returns a node that is the root of the tree.
   * 
   * This method is used after asking the user the string to find and the maximum
   * depth. It makes a top-down parsing and stops when maximum depth is exceeded
   * or the string given by the user is found. It prints if the string was
   * accepted or not.
   *
   * @param input    string that the user wants to process
   * @param maxdepth int that is the maximum depth wanted
   * 
   * @return root of the created tree
   * 
   */
  public static Node BuildTree(String input, int maxdepth) {
    Node root = new Node(initial);
    Queue<Node> Q = new LinkedList<>();
    Q.add(root);
    String uwv = "";
    int actualdepth = 0;

    while (!Q.isEmpty() && !input.equals(uwv) && actualdepth <= maxdepth) {
      Node q = Q.poll();
      int i = 0;
      boolean done = false;
      String varsust = GetLeft(q);

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
          }
          i = j;
          actualdepth = calculateDepth(temp);
          if (actualdepth > maxdepth) {
            temp.deleteNode();
          }
        }
      }
    }

    System.out.println();

    if (input.equals(uwv) && actualdepth <= maxdepth) {
      System.out.println(
          "Input: " + input + " was accepted and found in level: " + (actualdepth + 1) + " with depth: " + actualdepth);
    } else {
      System.out.println("Input: " + input + " was not accepted.");
    }
    System.out.println();

    return root;

  }

  /**
   * Returns a string with the leftmost nonterminal character.
   * 
   * This method is used to find the head of the production of a given string. It
   * compares each character of the string with each of the nonterminal symbols
   * until it finds the first one. Null will be returned if nonterminal symbols
   * are not found.
   *
   * @param q node that contains a string.
   * 
   * 
   * @return leftmost nonterminal character.
   * 
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
   * Returns a boolean stating if there are more rules with head varsust.
   * 
   * This method is used inside the buildTree method to determine whether there
   * are more rules with the given head.
   *
   * @param varsust string corresponds to the head of a production
   * @param i       int that corresponds to the last rule found
   * 
   * @return boolean that states whether another rule was found o not
   * 
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
   * Returns an int that corresponds to the number of the production with head
   * varsust.
   * 
   * This method is used inside the buildTree method to determine the number of
   * production required.
   *
   * @param varsust string corresponds to the head of a production
   * @param i       int that corresponds to the last rule found
   * 
   * @return int that contains the number of the rule with head varsust
   * 
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
   * Returns a boolean that corresponds whether uwv should be added to the queue.
   * 
   * This method is used inside the buildTree method to determine if the given
   * string fulfills the characteristics to be inserted in the queue. If uwv does
   * not belong to Σ* and the terminal prefix matches a prefix in the input.
   * 
   *
   * @param uwv   resulting string after the production rule is applied
   * @param input string given by the user
   * 
   * @return boolean saying whether uwv should be added to the queue or not.
   * 
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
   * 
   * This method is used to generate the tree syntax for jsSyntaxTree. It prints a
   * string that can be copied and pasted to view the tree.
   * 
   *
   * @param root node that is the root of the tree
   * 
   */
  public static void generateSyntax(Node root) {
    String result = "";
    Stack<Node> stack = new Stack<>();
    stack.add(root);

    while (!stack.isEmpty()) {
      Node nodeTemp = stack.pop();
      result += "[" + nodeTemp.getData();

      if (nodeTemp.getChildren().isEmpty()) {
        nodeTemp.setRemaining(0);

        if (nodeTemp.getParent() == null) {
          result += "]";
        } else {
          if (nodeTemp.getRemaining() == 0) {
            result += "]";
          }
          Node papa = nodeTemp.getParent();
          papa.setRemaining(papa.getRemaining() - 1);

          while (nodeTemp.getParent() != null && nodeTemp.getParent().getRemaining() == 0) {
            nodeTemp = nodeTemp.getParent();
            if (nodeTemp.getRemaining() == 0 && !nodeTemp.getDeuda()) {
              result += "]";
              nodeTemp.setDeuda(true);
            }
          }
        }
      } else {
        if (!nodeTemp.getChildren().isEmpty()) {
          if (nodeTemp.getParent() != null) {
            Node papa = nodeTemp.getParent();
            papa.setRemaining(papa.getRemaining() - 1);
            List<Node> hijos = nodeTemp.getChildren();
            nodeTemp.setRemaining(hijos.size());
            stack.addAll(hijos);
          } else {
            List<Node> hijos = nodeTemp.getChildren();
            nodeTemp.setRemaining(hijos.size());
            stack.addAll(hijos);
          }
        }
      }
    }
    System.out.println("To view the tree, enter the following string in http://ironcreek.net/syntaxtree/");
    System.out.println();
    System.out.println(result);
    System.out.println();
  }




}