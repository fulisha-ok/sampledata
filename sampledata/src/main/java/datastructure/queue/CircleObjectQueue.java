package datastructure.queue;

/**
 * @Author: fulisha
 * @Date: 2023-04-09 20:53
 * @desription
 */
public class CircleObjectQueue {
    /**
     * The total space. One space can never be used.
     */
    public static final int TOTAL_SPACE = 10;

    /**
     * The data.
     */
    Object[] data;


    /**
     * The index of the head.
     */
    int head;

    /**
     * The index of the tail.
     */
    int tail;

    public CircleObjectQueue() {
        data = new Object[TOTAL_SPACE];
        head = 0;
        tail = 0;
    }

    /**
     *  Enqueue.
     * @param paraValue  The value of the new node.
     */
    public void enqueue(Object paraValue) {
        if ((tail + 1) % TOTAL_SPACE == head) {
            System.out.println("Queue full.");
            return;
        } // Of if

        data[tail % TOTAL_SPACE] = paraValue;
        tail++;
    }

    /**
     * Dequeue.
     * @return The value at the head.
     */
    public Object dequeue() {
        if (head == tail) {
            return null;
        }

        Object resultValue = data[head % TOTAL_SPACE];

        head++;

        return resultValue;
    }


    /**
     *  Overrides the method claimed in Object, the superclass of any class.
     * @return
     */
    public String toString() {
        String resultString = "";

        if (head == tail) {
            return "empty";
        } // Of if

        for (int i = head; i < tail; i++) {
            resultString += data[i % TOTAL_SPACE] + ", ";
        } // Of for i

        return resultString;
    }


    public static void main(String args[]) {
        CircleObjectQueue tempQueue = new CircleObjectQueue();
    }
}
