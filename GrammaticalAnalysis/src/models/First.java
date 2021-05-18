package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/16
 */
public class First {
    private List<String> first = new ArrayList<>();

    public List<String> getFirst() {
        return first;
    }

    public Boolean setFirst(First nonTerminal) {
        boolean judge = false;
        for (int i = 0; i < nonTerminal.getFirst().size(); i++) {
            if (!first.contains(nonTerminal.getFirst().get(i))) {
                // 如果first集中不包含对象非终结符中的第i个元素，那么就添加。
                first.add(nonTerminal.getFirst().get(i));
                judge = true;
            }
        }
        return judge;
    }

    public Boolean setFirstWithoutEmpty(First nonTerminal) {
        boolean judge = false;
        for (int i = 0; i < nonTerminal.getFirst().size(); i++) {
            if (!"~".equals(nonTerminal.getFirst().get(i)) && !first.contains(nonTerminal.getFirst().get(i))) {
                // 如果first集中不包含对象非终结符中的第i个元素，那么就添加。
                first.add(nonTerminal.getFirst().get(i));
                judge = true;
            }
        }
        return judge;
    }

    public Boolean setFirst(String string){
        if(!first.contains(string)){
            first.add(string);
            return true;
        }
        return false;
    }
}
