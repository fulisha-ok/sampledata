package datastructure.search;

/**
 * @author： fulisha
 * @date： 2023/5/12 13:51
 * @description：
 */
public class Hash {
    //链表的结构
    class HashNode {
        int column;
        HashNode next;
        public HashNode(int paraColumn) {
            column = paraColumn;
            next = null;
        }
    }

    int length;
    HashNode[] data;

    public Hash(int[] paraArray,int paraLenght) {
        length = paraLenght;
        data = new HashNode[paraLenght];
        HashNode tempNode,tempPreviousNode;
        int tempPosition;

        //采用直接地址法构造hash函数 并用拉链法解决冲突
        for (int i = 0; i < paraLenght; i++) {
            data[i] = new HashNode(i);
            tempPreviousNode = data[i];
            for (int j = 0; j < paraArray.length; j++) {
                tempPosition = paraArray[j] % paraLenght;
                //，我对取模值相同的在外层一个for一次搞定
                if (tempPosition == i) {
                    tempNode = new HashNode(paraArray[j]);
                    tempPreviousNode.next = tempNode;
                    tempPreviousNode = tempNode;
                }
            }
        }

        //打印输出链表法的值
        HashNode hashNode;
        for (int i = 0; i < paraLenght; i++) {
            hashNode = data[i];
            String hashString = " ";
            while (hashNode != null) {
                hashString = hashString + " " + hashNode.column ;
                hashNode = hashNode.next;
            }
            System.out.println(hashString);
        }
    }

    public static void main(String args[]) {
        int[] tempUnsortedKeys = { 16, 33, 38, 69, 57, 95, 86 };
        Hash hash = new Hash(tempUnsortedKeys, 8);
    }

}
