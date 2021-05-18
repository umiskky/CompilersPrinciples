import NFAsimulator.Simulator;
import regex2NFA.Regex;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/14
 */
public class Main {
    public static void main(String[] args) throws Exception{
        String regexString1 = "(ab|c)*ab";
        Regex regex1 = new Regex(regexString1);
        System.out.println(regex1.getRegex());
        System.out.println(regex1.transformNFA());

        Simulator simulator = new Simulator(regex1.transformNFA());
        String input = "ababcab";
        Boolean res = simulator.checkWordIsValid(input);
        if(res){
            System.out.println("Input string '" + input + "' Matched!");
        }else{
            System.out.println("Input string '" + input + "' does not Match!");
        }
        regex1.reset();
    }
}
