package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/16
 */
public class Grammar {
    private List<Production> productions = new ArrayList<>();
    private Map<String, List<Production>> productionsMap = new HashMap<>();
    private List<First> firsts = new ArrayList<>();
    private List<Follow> follows = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private List<String> nonTerminals = new ArrayList<>();
    private Set<String> nonTerminalsWithEmpty = new HashSet<>();
    private Set<String> nonTerminalsWithoutEmpty = new HashSet<>();

    public Grammar(String path) throws Exception{
        FileReader fr = new FileReader(path);
        BufferedReader bf = new BufferedReader(fr);
        createProduction(bf);
        createNonTerminals();
        createTerminals();
        divideNonTerminals();
        createFirstSets();
        createFollowSets();
    }

    public Grammar() throws Exception{
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(Grammar.class.getResourceAsStream("/Grammar.txt")));
        BufferedReader bf = new BufferedReader(isr);
        createProduction(bf);
        createNonTerminals();
        createTerminals();
        divideNonTerminals();
        createFirstSets();
        createFollowSets();
    }

    /**
     * @description The method createProduction is used to import Production from the file.
     * @param bf
     * @return void
     * @author umiskky
     * @date 2021/5/16-14:44
     */
    private void createProduction(BufferedReader bf) throws Exception{
        String s = bf.readLine();
        while (s != null) {
            Production production = new Production(s);
            productions.add(production);
            if(productionsMap.get(production.leftPart) == null){
                List<Production> productions = new ArrayList<>();
                productions.add(production);
                productionsMap.put(production.leftPart, productions);
            }else{
                productionsMap.get(production.leftPart).add(production);
            }
            s = bf.readLine();
        }
        bf.close();
    }

    /**
     * @description The method createNonTerminals is used to get all non-terminals.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/16-14:53
     */
    private void createNonTerminals(){
        for (Production production : productions) {
            String leftPart = production.leftPart;
            if (!nonTerminals.contains(leftPart)) {
                nonTerminals.add(leftPart);
            }
        }
    }

    /**
     * @description The method createTerminals is used to get all terminals.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/16-14:53
     */
    private void createTerminals(){
        for (Production production : productions) {
            for (int j = 0; j < production.rightParts.size(); j++) {
                String rightPart = production.rightParts.get(j);
                if (!nonTerminals.contains(rightPart) && !terminals.contains(rightPart)) {
                    terminals.add(rightPart);
                }
            }
        }
    }

    /**
     * @description The method divideNonTerminals is used to divide non-terminals to two sets.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/17-8:32
     */
    private void divideNonTerminals(){
        //?????????????????????A????????????????????????????????????????????????????????????????????????????????????????????emptyOK??????
        for(String nonTerminal : nonTerminals){
            List<Production> productions = productionsMap.get(nonTerminal);
            if(productions != null){
                for(Production production : productions){
                    if(production.rightParts.size() == 1 && "~".equals(production.rightParts.get(0))){
                        nonTerminalsWithEmpty.add(production.leftPart);
                    }
                }
            }
        }
        //?????????????????????A??????????????????????????????????????????????????????????????????????????????a??????????????????emptyNO??????
        for(String nonTerminal : nonTerminals){
            List<Production> productions = productionsMap.get(nonTerminal);
            if(productions != null){
                boolean flag = true;
                for(Production production : productions){
                    for(String string : production.rightParts){
                        if(terminals.contains(string) && !"~".equals(string)){
                            flag = true;
                            break;
                        }
                        flag = false;
                    }
                    if(flag){
                        continue;
                    }
                    break;
                }
                if(flag){
                    nonTerminalsWithoutEmpty.add(nonTerminal);
                }
            }
        }

        boolean ALTERED_ = true;
        while(ALTERED_){
            ALTERED_ = false;
            //?????????????????????????????????????????????????????????(ABC)??????????????????emptyOK????????????????????????????????????emptyOK??????
            for(Production production : productions){
                boolean flag = true;
                for(String string : production.rightParts){
                    if(nonTerminals.contains(string) && nonTerminalsWithEmpty.contains(string)){
                        continue;
                    }
                    flag = false;
                    break;
                }
                if(flag && !nonTerminalsWithEmpty.contains(production.leftPart)){
                    nonTerminalsWithEmpty.add(production.leftPart);
                    ALTERED_ = true;
                }
            }
            //??????????????????????????????????????????????????????A??????????????????????????????????????????????????????????????????????????????b ??? ?????????emptyNO??????????????????B?????????A??????emptyNO??????
            for(String nonTerminal : nonTerminals){
                List<Production> productions = productionsMap.get(nonTerminal);
                if(productions != null){
                    boolean flag = true;
                    for(Production production : productions){
                        for(String string : production.rightParts){
                            if((terminals.contains(string) && !"~".equals(string)) || nonTerminalsWithoutEmpty.contains(string)){
                                flag = true;
                                break;
                            }
                            flag = false;
                        }
                        if(flag){
                            continue;
                        }
                        break;
                    }
                    if(flag && !nonTerminalsWithoutEmpty.contains(nonTerminal)){
                        nonTerminalsWithoutEmpty.add(nonTerminal);
                        ALTERED_ = true;
                    }
                }
            }
        }
    }

    /**
     * @description The method createFirstSets is used to calculate the first set of grammar.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/16-14:53
     */
    private void createFirstSets(){
        boolean ALTERED_ = true;
        for (int i = 0; i < nonTerminals.size(); i++) {
            firsts.add(new First());
        }
        //???????????????
        for(String nonTerminal : nonTerminals){
            List<Production> productions = productionsMap.get(nonTerminal);
            if(productions != null){
                for(Production production : productions){
                    if(production.rightParts.size() == 1 && "~".equals(production.rightParts.get(0))){
                        ALTERED_ = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirst("~");
                    }
                    if(terminals.contains(production.rightParts.get(0)) && !"~".equals(production.rightParts.get(0))){
                        ALTERED_ = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirst(production.rightParts.get(0));
                    }
                }
            }
        }
        //????????????
        while(ALTERED_){
            boolean tmpFlag;
            ALTERED_ = false;
            for(String nonTerminal : nonTerminals){
                List<Production> productions = productionsMap.get(nonTerminal);
                if(productions != null){
                    for(Production production : productions){
                        List<String> rightParts = production.rightParts;
                        for(int index=0; index<rightParts.size(); index++){
                            if(nonTerminalsWithEmpty.contains(rightParts.get(index))){
                                tmpFlag = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirstWithoutEmpty(firsts.get(nonTerminals.indexOf(rightParts.get(index))));
                                ALTERED_ = ALTERED_ || tmpFlag;
                                if(index == rightParts.size()-1){
                                    tmpFlag = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirst("~");
                                    ALTERED_ = ALTERED_ || tmpFlag;
                                }
                            }else if(nonTerminalsWithoutEmpty.contains(rightParts.get(index))){
                                tmpFlag = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirstWithoutEmpty(firsts.get(nonTerminals.indexOf(rightParts.get(index))));
                                ALTERED_ = ALTERED_ || tmpFlag;
                                break;
                            }else if(terminals.contains(rightParts.get(index))){
                                tmpFlag = firsts.get(nonTerminals.indexOf(nonTerminal)).setFirst(rightParts.get(index));
                                ALTERED_ = ALTERED_ || tmpFlag;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @description The method createFollowSets is used to calculate the follow set of grammar.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/16-14:54
     */
    private void createFollowSets(){
        boolean ALTERED_ = true;
        for (int i = 0; i < nonTerminals.size(); i++) {
            follows.add(new Follow());
        }
        //???????????????
        follows.get(0).setFollow("#");
        for(Production production : productions){
            List<String> rightParts = production.rightParts;
            for(int i=0; i<rightParts.size(); i++){
                if(nonTerminals.contains(rightParts.get(i))){
                    if(i+1 < rightParts.size()){
                        if(terminals.contains(rightParts.get(i+1))){
                            follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow(rightParts.get(i+1));
                        }
                    }else{
                        follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow("#");
                    }
                }
            }
        }
        //????????????
        while(ALTERED_){
            boolean tmpFlag;
            ALTERED_ = false;
            for(Production production : productions){
                List<String> rightParts = production.rightParts;
                for(int i=0; i<rightParts.size(); i++){
                    if(nonTerminals.contains(rightParts.get(i))){
                        for(int j=i+1; j<rightParts.size(); j++){
                            if(terminals.contains(rightParts.get(j))){
                                tmpFlag = follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow(rightParts.get(j));
                                ALTERED_ = ALTERED_ || tmpFlag;
                                break;
                            }else if(nonTerminals.contains(rightParts.get(j))){
                                tmpFlag = follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow(firsts.get(nonTerminals.indexOf(rightParts.get(j))));
                                ALTERED_ = ALTERED_ || tmpFlag;
                                if(nonTerminalsWithoutEmpty.contains(rightParts.get(j))){
                                    break;
                                }else if(nonTerminalsWithEmpty.contains(rightParts.get(j))){
                                    if(j == rightParts.size() - 1){
                                        tmpFlag = follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow(follows.get(nonTerminals.indexOf(production.leftPart)));
                                        ALTERED_ = ALTERED_ || tmpFlag;
                                    }
                                }
                            }
                        }
                        if(i == rightParts.size()-1){
                            tmpFlag = follows.get(nonTerminals.indexOf(rightParts.get(i))).setFollow(follows.get(nonTerminals.indexOf(production.leftPart)));
                            ALTERED_ = ALTERED_ || tmpFlag;
                        }
                    }
                }
            }
        }

    }

    public void printAll() {
        printTerminalAndNonTerminal();
        printGrammar();
        System.out.println();
        printFirst();
        System.out.println();
        printFollow();
    }

    public void printFirst() {
        System.out.println("?????????First?????????");
        for (int i = 0; i < firsts.size(); i++) {
            System.out.print("First(" + nonTerminals.get(i) + ") = { ");
            for (int j = 0; j < firsts.get(i).getFirst().size(); j++) {
                System.out.print(firsts.get(i).getFirst().get(j) + " ");
            }
            System.out.println("}");
        }
    }

    public void printFollow() {
        System.out.println("?????????Follow?????????");
        for (int i = 0; i < follows.size(); i++) {
            System.out.print("Follow(" + nonTerminals.get(i) + ") = { ");
            for (int j = 0; j < follows.get(i).getFollow().size(); j++) {
                System.out.print(follows.get(i).getFollow().get(j) + " ");
            }
            System.out.println("}");
        }
    }

    public void printGrammar() {
        System.out.println("?????????????????????");
        for (Production production : productions) {
            System.out.print("(" + production.getId() + ")");
            System.out.print(production.leftPart + "->");
            for (int j = 0; j < production.rightParts.size(); j++) {
                System.out.print(production.rightParts.get(j));
            }
            System.out.println();
        }
    }

    public void printTerminalAndNonTerminal() {
        System.out.println("????????????  " + terminals);
        System.out.println("???????????????" + nonTerminals);
    }

    public Production getProduction(int index) {
        return productions.get(index + 1);
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public Map<String, List<Production>> getProductionsMap() {
        return productionsMap;
    }

    public List<Follow> getFollows() {
        return follows;
    }
}
