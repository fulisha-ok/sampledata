package datastructure.tree;

/**
 * @Author: fulisha
 * @Date: 2023-04-12 19:25
 * @desription
 */
public class Hanoi {
    /**
     * Move a number of plates.
     * @param paraSource The source pole.
     * @param paraIntermediary The intermediary pole.
     * @param paraDestination The destination pole.
     * @param paraNumber The number of plates
     */
    public static void hanoi(char paraSource, char paraIntermediary, char paraDestination, int paraNumber) {
        if (paraNumber == 1) {
            System.out.println(paraSource + "->" + paraDestination + " ");
            return;
        }
        hanoi(paraSource, paraDestination, paraIntermediary, paraNumber - 1);
        System.out.println(paraSource + "->" + paraDestination + " ");
        hanoi(paraIntermediary, paraSource, paraDestination, paraNumber - 1);
    }

    public static void main(String args[]) {
        hanoi('a', 'b', 'c', 4);
    }

}
