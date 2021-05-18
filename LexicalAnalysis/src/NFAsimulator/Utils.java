package NFAsimulator;

import regex2NFA.Edge;
import regex2NFA.Graph;
import regex2NFA.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */
public class Utils {

    /**
     * @description The method getStateNum is used to get DFA state number.
     * @param
     * @return int
     * @author umiskky
     * @date 2021/5/15-15:45
     */
    public static int getStateNum() throws Exception{
        return Node.getID();
    }

    /**
     * @description The method getAllLabels is used to get all DFA labels.
     * @param graph
     * @return java.util.HashSet<java.lang.String>
     * @author umiskky
     * @date 2021/5/15-15:48
     */
    public static HashSet<String> getAllLabels(Graph graph){
        HashSet<String> labels = new HashSet<>();
        for(Edge edge : graph.getEdges()){
            labels.add(edge.getLabel());
        }
        return labels;
    }

    /**
     * @description The method generateMoveArray is used to generate move(state, label) array.
     * @param graph
     * @return java.util.ArrayList<java.util.HashMap < java.lang.String, java.util.HashSet < java.lang.Integer>>>
     * @author umiskky
     * @date 2021/5/15-16:11
     */
    public static ArrayList<HashMap<String, HashSet<Integer>>> generateMoveArray(Graph graph) throws Exception{
        ArrayList<HashMap<String, HashSet<Integer>>> moveArray = new ArrayList<>();
        for(int i=0; i<getStateNum(); i++){
            HashMap<String, HashSet<Integer>> hashMap = new HashMap<>(getAllLabels(graph).size());
            moveArray.add(hashMap);
        }

        for(Edge edge : graph.getEdges()){
            int beginState = edge.getBegin().getId();
            HashMap<String, HashSet<Integer>> hashMap = moveArray.get(beginState);
            HashSet<Integer> hashSet = hashMap.get(edge.getLabel());

            if(hashSet == null){
                hashSet = new HashSet<>();
                hashSet.add(edge.getEnd().getId());
                hashMap.put(edge.getLabel(), hashSet);
            }else{
                hashSet.add(edge.getEnd().getId());
            }
        }

        return moveArray;
    }

}
