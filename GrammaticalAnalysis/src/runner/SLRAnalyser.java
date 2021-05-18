package runner;

import models.Grammar;
import models.Production;
import models.SLRAnalyserTable;

import java.util.*;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/18
 */
public class SLRAnalyser {
    private Queue<String> printQueue = new LinkedList<>();
    private Queue<String> inputStringQueue = new LinkedList<>();
    private Stack<Integer> stateStack = new Stack<>();
    private Stack<String> symbolStack = new Stack<>();

    private List<String> nonTerminals;
    private List<String> terminals;
    private List<Production> productions;
    private int startState;
    private Map<Integer, Map<String, String>> actionTable;
    private Map<Integer, Map<String, String>> gotoTable;

    public SLRAnalyser(Grammar grammar, SLRAnalyserTable slrAnalyserTable) {
        nonTerminals = grammar.getNonTerminals();
        terminals = grammar.getTerminals();
        productions = grammar.getProductions();
        actionTable = slrAnalyserTable.getActionTable();
        gotoTable = slrAnalyserTable.getGotoTable();
        startState = slrAnalyserTable.getStartState();
    }

    /**
     * @description The method checkInputIsValid is used to check the input string is valid or not.
     * @param input
     * @return java.lang.Boolean
     * @author umiskky
     * @date 2021/5/18-10:13
     */
    private Boolean checkInputIsValid(String input){
        for(Character character : input.toCharArray()){
            if(terminals.contains(character.toString()) || nonTerminals.contains(character.toString()) || "#".equals(character.toString())){
                inputStringQueue.add(character.toString());
            }else{
                inputStringQueue.clear();
                return false;
            }
        }
        if(!"#".equals(input.substring(input.length()-1))){
            inputStringQueue.add("#");
        }
        return true;
    }

    /**
     * @description The method analyze is used to analyze the input string.
     * @param input
     * @return void
     * @author umiskky
     * @date 2021/5/18-10:12
     */
    public void analyze(String input){
        int step = 1;
        printQueue.offer("分析过程:");
        printQueue.offer(strAppender("步骤", 7) +
                strAppender("状态栈", 20) +
                strAppender("符号栈", 20) +
                strAppender("输入串", 20) +
                strAppender("ACTION", 9) +
                strAppender("GOTO", 7));
        if(checkInputIsValid(input)){
            stateStack.push(startState);
            symbolStack.push("#");
            while(!inputStringQueue.isEmpty()){
                StringBuilder sb = new StringBuilder();
                String character = inputStringQueue.peek();
                String action = actionTable.get(stateStack.peek()).get(character);
                if(action != null){
                    if("S".equals(action.substring(0, 1))){
                        sb.append(strAppenderByLeft("("+step+")", 7))
                                .append(printStack(stateStack))
                                .append(printStack(symbolStack))
                                .append(printQueue(inputStringQueue))
                                .append(strAppender(action, 9))
                                .append(strAppender(" ", 7));
                        printQueue.offer(sb.toString());
                        stateStack.push(Integer.parseInt(action.substring(1)));
                        symbolStack.push(character);
                        inputStringQueue.poll();
                        step++;
                    }else if("r".equals(action.substring(0, 1))){
                        Production production = productions.get(Integer.parseInt(action.substring(1)));
                        String leftPart = production.getLeftPart();
                        List<String> rightParts = production.getRightParts();
                        String gotoState = gotoTable.get(stateStack.elementAt(stateStack.size()-1-rightParts.size())).get(leftPart);
                        sb.append(strAppenderByLeft("("+step+")", 7))
                                .append(printStack(stateStack))
                                .append(printStack(symbolStack))
                                .append(printQueue(inputStringQueue))
                                .append(strAppender(action, 9))
                                .append(strAppender(gotoState, 7));
                        printQueue.add(sb.toString());

                        //规约，退栈
                        for(int i=rightParts.size()-1; i>=0; i--){
                            String topSymbol = symbolStack.pop();
                            stateStack.pop();
                            if(!rightParts.get(i).equals(topSymbol)){
                                System.out.println("Error: 发生未知错误！");
                            }
                        }
                        stateStack.push(Integer.parseInt(gotoState));
                        symbolStack.push(leftPart);
                        step++;
                    }else if("#".equals(character) && "acc".equals(action)){
                        sb.append(strAppenderByLeft("("+step+")", 7))
                                .append(printStack(stateStack))
                                .append(printStack(symbolStack))
                                .append(printQueue(inputStringQueue))
                                .append(strAppender(action, 9))
                                .append(strAppender(" ", 7));
                        printQueue.add(sb.toString());
                        break;
                    }
                }else {
                    System.out.println("Error: 分析错误！");
                    System.exit(0);
                    break;
                }
            }
        }else{
            System.out.println("Error: 输入串含有非法字符");
            System.exit(0);
        }
    }

    /**
     * @description The method print is used to print the analysis results.
     * @param
     * @return void
     * @author umiskky
     * @date 2021/5/18-10:11
     */
    public void print(){
        while(!printQueue.isEmpty()){
            System.out.println();
            System.out.println(printQueue.poll());
        }
        System.out.println();
    }

    /**
     * @description The method printStack is used to print all stack's elements.
     * @param stack
     * @return java.lang.String
     * @author umiskky
     * @date 2021/5/18-13:40
     */
    private String printStack(Stack stack){
        StringBuilder stringBuilder = new StringBuilder();
        if(stack.peek().getClass().equals(Integer.class)){
            for(int i=0; i<stack.size(); i++){
                if((Integer)stack.elementAt(i) >= 10){
                    stringBuilder.append("(").append(stack.elementAt(i)).append(")");
                }else if((Integer)stack.elementAt(i) >= 0 || (Integer)stack.elementAt(i) < 10){
                    stringBuilder.append(stack.elementAt(i));
                }
            }
        }else if(stack.peek().getClass().equals(String.class)){
            for(int i=0; i<stack.size(); i++){
                stringBuilder.append(stack.elementAt(i));
            }
        }
        return strAppenderByLeft(stringBuilder.toString(), 20);
    }

    /**
     * @description The method printQueue is used to print all queue's elements.
     * @param queue
     * @return java.lang.String
     * @author umiskky
     * @date 2021/5/18-13:46
     */
    private String printQueue(Queue<String> queue){
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : queue){
            stringBuilder.append(s);
        }
        return strAppenderByRight(stringBuilder.toString(), 20);
    }

    /**
     * @description The method strAppender is used to format output string.
     * @param string
     * @param length
     * @return java.lang.String
     * @author umiskky
     * @date 2021/5/18-15:12
     */
    private String strAppender(String string, int length){
        int strLength = 0;
        for(Character character : string.toCharArray()){
            if(character>='a'&&character<='z' || character>='A'&&character<='Z' || character>='0'&&character<='9'){
                strLength++;
            }else{
                strLength = strLength + 2;
            }
        }
        if(strLength >= length){
            return string;
        }
        return " ".repeat((length - strLength)/2) + string + " ".repeat(length - (length - strLength)/2 - strLength);
    }

    private String strAppenderByLeft(String string, int length){
        if(string.length() >= length){
            return string;
        }
        return string + " ".repeat(length - string.length());
    }

    private String strAppenderByRight(String string, int length){
        if(string.length() >= length){
            return string;
        }
        return " ".repeat(length - string.length()) + string;
    }
}
