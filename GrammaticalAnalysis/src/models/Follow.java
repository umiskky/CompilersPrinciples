package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/16
 */
public class Follow {
    private List<String> follow = new ArrayList<>();

    public List<String> getFollow() {
        return follow;
    }

    public boolean setFollow(Follow nonTerminal) {
        boolean judge = false;
        for (int i = 0; i < nonTerminal.getFollow().size(); i++) {
            if (!follow.contains(nonTerminal.getFollow().get(i)) && !nonTerminal.getFollow().get(i).equals("~")) {
                // 如果follow集中不包含对象非终结符中的第i个元素，那么就添加。
                follow.add(nonTerminal.getFollow().get(i));
                judge = true;
            }
        }
        return judge;
    }

    public boolean setFollow(First nonTerminal) {
        boolean judge = false;
        for (int i = 0; i < nonTerminal.getFirst().size(); i++) {
            if (!follow.contains(nonTerminal.getFirst().get(i)) && !nonTerminal.getFirst().get(i).equals("~")) {
                // 如果Follow集中不包含对象非终结符中的第i个元素，那么就添加。
                follow.add(nonTerminal.getFirst().get(i));
                judge = true;
            }
        }
        return judge;
    }

    public boolean setFollow(String terminal) {
        boolean judge = false;
        if (!follow.contains(terminal) && !terminal.equals("~")) {
            // 如果Follow集中不包含该终结符，则将这个终结符添加到Follow集中
            follow.add(terminal);
            judge = true;
        }
        return judge;
    }
}
