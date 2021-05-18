import models.Grammar;
import models.ItemFamily;
import models.SLRAnalyserTable;
import runner.SLRAnalyser;


/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */
public class Main {
    public static void main(String[] args) throws Exception{

        Grammar grammar = new Grammar();
        grammar.printAll();

        ItemFamily itFml = new ItemFamily(grammar);
        itFml.print();

        SLRAnalyserTable analyserTable = new SLRAnalyserTable(grammar, itFml);
        analyserTable.printTable();

        String input = "bccd#";
        SLRAnalyser slrAnalyser = new SLRAnalyser(grammar, analyserTable);
        slrAnalyser.analyze(input);
        slrAnalyser.print();

    }
}
