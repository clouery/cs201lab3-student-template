import java.util.*;
public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     } 
     static class Node {
          int val;
          Node left, right;
          Node(int val) { 
               this.val = val;
          }
     }

     public static String verify(String traversal1, String traversal2, String traversal3) {
          int[][] t = new int[3][];
          try {
               t[0] = parse(traversal1);
               t[1] = parse(traversal2);
               t[2] = parse(traversal3);
          } catch (NumberFormatException e) {
               return "Invalid traversals";
          }

          // All three must describe the same number of nodes
          if (t[0].length != t[1].length || t[1].length != t[2].length || t[0].length == 0) {
               return "Invalid traversals";
          }

          final String[] names = { "Inorder", "Preorder", "Postorder" };
          // Each row: which input index plays {inorder, preorder, postorder}
          int[][] perms = { {0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0} };

          for (int[] p : perms) {
               int[] in = t[p[0]], pre = t[p[1]], post = t[p[2]];

               // In a BST, inorder is strictly increasing
               if (!strictlyIncreasing(in)) continue;

               // Preorder of a BST uniquely determines the tree: rebuild by insertion
               Node root = null;
               for (int v : pre) root = insert(root, v);

               int[] idx = new int[1];
               int[] builtPre = new int[pre.length];
               preorder(root, builtPre, idx);
               // check if it is the same
               if (!Arrays.equals(builtPre, pre)) continue;

               idx[0] = 0;
               int[] builtIn = new int[in.length];
               inorder(root, builtIn, idx);
               if (!Arrays.equals(builtIn, in)) continue;

               idx[0] = 0;
               int[] builtPost = new int[post.length];
               postorder(root, builtPost, idx);
               if (!Arrays.equals(builtPost, post)) continue;

               // Valid: label each input traversal
               String[] label = new String[3];
               label[p[0]] = names[0];
               label[p[1]] = names[1];
               label[p[2]] = names[2];
               return "Traversal 1 - " + label[0] + ", Traversal 2 - " + label[1] + ", Traversal 3 - " + label[2];
          }
          return "Invalid traversals";
     }

     private static int[] parse(String s) {
          String[] parts = s.trim().split("-");
          int[] arr = new int[parts.length];
          for (int i = 0; i < parts.length; i++) arr[i] = Integer.parseInt(parts[i].trim());
          return arr;
     }

     // check strictly increasing
     private static boolean strictlyIncreasing(int[] a) {
          for (int i = 1; i < a.length; i++) {
               if (a[i] <= a[i - 1]) return false;
          }
          return true;
     }

     private static Node insert(Node node, int v) {
          if (node == null) return new Node(v);
          if (v < node.val) node.left = insert(node.left, v);
          else node.right = insert(node.right, v);
          return node;
     }

     private static void preorder(Node n, int[] out, int[] i) {
          if (n == null) return;
          out[i[0]++] = n.val;
          preorder(n.left, out, i);
          preorder(n.right, out, i);
     }

     private static void inorder(Node n, int[] out, int[] i) {
          if (n == null) return;
          inorder(n.left, out, i);
          out[i[0]++] = n.val;
          inorder(n.right, out, i);
     }

     private static void postorder(Node n, int[] out, int[] i) {
          if (n == null) return;
          postorder(n.left, out, i);
          postorder(n.right, out, i);
          out[i[0]++] = n.val;
     }
}




