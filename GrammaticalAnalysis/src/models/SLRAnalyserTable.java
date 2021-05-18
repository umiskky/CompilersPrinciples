package models;

import java.util.*;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/17
 */
public class SLRAnalyserTable {
    private Map<Integer, Map<String, String>> actionTable = new HashMap<>();
    private Map<Integer, Map<String, String>> gotoTable = new HashMap<>();
    private int startState;
    private int stateNum;
    private List<String> nonTerminals;
    private List<String> terminals;


    public SLRAnalyserTable(Grammar grammar, ItemFamily itemFamily) {
        createSLRAnalyserTable(grammar, itemFamily);
        stateNum = itemFamily.getItemSets().size();
    }

    /**
     * @description The method createSLRAnalyserTable is used to construct SLR analysis table.
     * @param grammar
     * @param itemFamily
     * @return void
     * @author umiskky
     * @date 2021/5/18-10:11
     */
    private void createSLRAnalyserTable(Grammar grammar, ItemFamily itemFamily){
        //准备数据
        nonTerminals = grammar.getNonTerminals();
        terminals = grammar.getTerminals();
        List<Follow> follows = grammar.getFollows();
        Map<Integer, Map<String, Integer>> gotoMap = itemFamily.getGotoMap();
        List<ItemSet> itemSets = itemFamily.getItemSets();

        //初始化分析表
        for(int i=0; i<itemSets.size(); i++){
            actionTable.put(i, new HashMap<>(terminals.size()));
            gotoTable.put(i, new HashMap<>(nonTerminals.size()));
        }

        //扫描构建分析表
        for(ItemSet itemSet : itemSets){
            for(Item item : itemSet.items){
                if("Ω".equals(item.leftPart)){
                    if(item.point == 0){
                        startState = itemSet.id;
                    }else if(item.point == item.rightParts.size()){
                        actionTable.get(itemSet.id).put("#", "acc");
                    }
                }else if(item.point == item.rightParts.size()){
                    List<String> follow = follows.get(nonTerminals.indexOf(item.leftPart)).getFollow();
                    if(follow != null && !follow.isEmpty()){
                        for(String string : follow){
                            actionTable.get(itemSet.id).put(string, "r"+item.id);
                        }
                    }
                }
            }
        }
        for(int i=0; i<gotoMap.size(); i++){
            for(String string : gotoMap.get(i).keySet()){
                if(nonTerminals.contains(string)){
                    if(gotoTable.get(i).get(string) != null){
                        System.out.println("Error: " + string + "-" + gotoTable.get(i).get(string));
                    }
                    gotoTable.get(i).put(string, String.valueOf(gotoMap.get(i).get(string)));
                }else if(terminals.contains(string)){
                    if(gotoTable.get(i).get(string) != null){
                        System.out.println("Error: " + string + "-" + gotoTable.get(i).get(string));
                    }
                    actionTable.get(i).put(string, "S"+gotoMap.get(i).get(string));
                }
            }
        }
    }

    /**
     * @description The method printTable is used to print analysis table.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/18-10:11
     */
    public void printTable(){
        List<String> actionColumnNames = new ArrayList<>();
        List<String> gotoColumnNames = new ArrayList<>();
        System.out.println("\n预测分析表：");
        for (int i = 0; i < terminals.size(); i++) {
            if(!"~".equals(terminals.get(i))){
                System.out.print("\t" + terminals.get(i));
                actionColumnNames.add(terminals.get(i));
            }
        }
        System.out.print("\t" + "#");
        actionColumnNames.add("#");
        for (int i = 0; i < nonTerminals.size(); i++) {
            System.out.print("\t" + nonTerminals.get(i));
            gotoColumnNames.add(nonTerminals.get(i));
        }
        System.out.println();
        for(int state=0; state<stateNum; state++){
            System.out.print(state);
            for(String terminal : actionColumnNames){
                String content = actionTable.get(state).get(terminal);
                System.out.print("\t" + Objects.requireNonNullElse(content, "--"));
            }
            for(String nonTerminal : gotoColumnNames){
                String content = gotoTable.get(state).get(nonTerminal);
                System.out.print("\t" + Objects.requireNonNullElse(content, "-"));
            }
            System.out.println();
        }
    }

    public Map<Integer, Map<String, String>> getActionTable() {
        return actionTable;
    }

    public Map<Integer, Map<String, String>> getGotoTable() {
        return gotoTable;
    }

    public int getStartState() {
        return startState;
    }
}
