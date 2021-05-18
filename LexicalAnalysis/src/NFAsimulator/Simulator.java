package NFAsimulator;

import regex2NFA.Graph;

import java.util.*;

/**
 * @author umiskky
 * @version 0.0.1
 * @date 2021/05/15
 */
public class Simulator {

    private ArrayList<HashMap<String, HashSet<Integer>>> moveArray;
    private Graph graph;

    private Stack oldStates;
    private Stack newStates;
    private Boolean[] alreadyOn;

    public Simulator(Graph graph) {
        this.graph = graph;
        try {
            moveArray = Utils.generateMoveArray(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description The method checkWordIsValid is used for lexical Analysis.
     * @param string
     * @return java.lang.Boolean
     * @author umiskky
     * @date 2021/5/15-17:53
     */
    public Boolean checkWordIsValid(String string) throws Exception{

        Queue<Character> charQueue = new LinkedList<Character>();;
        for(char c : string.toCharArray()){
            charQueue.offer(c);
        }
        oldStates = new Stack();
        newStates = new Stack();
        alreadyOn = new Boolean[Utils.getStateNum()];
        for(int i=0; i<alreadyOn.length; i++){
            alreadyOn[i] = false;
        }

        HashSet<Integer> start = new HashSet<>();
        start.add(graph.getStart().getId());

        oldStates.addAll(getEpsilonClosure(start));
        while(!charQueue.isEmpty()){
            Character character = (Character) charQueue.poll();
            while(!oldStates.isEmpty()){
                int state = (Integer) oldStates.pop();
                HashSet<Integer> states = moveArray.get(state).get(String.valueOf(character));
                if(states != null){
                    for(int tmpState : states){
                        if(!alreadyOn[tmpState]){
                            addState(tmpState);
                        }
                    }
                }
            }
            while(!newStates.isEmpty()){
                int state = (Integer) newStates.pop();
                oldStates.push(state);
                alreadyOn[state] = false;
            }
        }

        if(oldStates.contains(graph.getEnd().getId())){
            return true;
        }
        return false;
    }

    private void addState(Integer state){
        newStates.push(state);
        alreadyOn[state] = true;
        HashSet<Integer> hashSet = moveArray.get(state).get("epsilon");
        if(hashSet!=null){
            for(int tmpState : hashSet){
                if(!alreadyOn[tmpState]){
                    addState(tmpState);
                }
            }
        }
    }

    /**
     * @description The method getEpsilonClosure is used to calculate the NFA state's epsilon closure.
     * @param states
     * @return java.util.HashSet<java.lang.Integer>
     * @author umiskky
     * @date 2021/5/15-16:33
     */
    public HashSet<Integer> getEpsilonClosure(HashSet<Integer> states){
        HashSet<Integer> epsilonClosure = new HashSet<>();
        epsilonClosure.addAll(states);
        Stack stateStack = new Stack();
        stateStack.addAll(states);

        while(!stateStack.isEmpty()){
            int state = (Integer) stateStack.pop();
            HashSet<Integer> hashSet = moveArray.get(state).get("epsilon");
            if(hashSet != null && !hashSet.isEmpty()){
                for(int tmpState : hashSet){
                    if(!epsilonClosure.contains(tmpState)){
                        epsilonClosure.add(tmpState);
                        stateStack.push(tmpState);
                    }
                }
            }
        }
        return epsilonClosure;
    }

}
