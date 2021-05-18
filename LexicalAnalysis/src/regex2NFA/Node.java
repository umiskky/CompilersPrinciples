/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */
package regex2NFA;

/**
 * 状态结点类
 */
public class Node {
    private int id;
    private static int ID=0;

    public Node() {
        this.id = ID++;
    }

    public int getId() {
        return id;
    }

    public static void reset(){
        ID=0;
    }

    public static int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return id+"";
    }

}

