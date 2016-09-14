package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;

import java.util.ArrayList;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.List;
import java.util.Objects;

/**
 * Created by lucas on 11/09/16.
 */
public class OneLoopPath {
    ArrayList<Integer> tempLoop = new ArrayList<>();
    ArrayList<List<Integer>> loops = new ArrayList<>();
    List<Transition> transitions;
    int statesCount;
    State initialState;
    ArrayList<Integer> listStatesEnds;
    boolean [] listColors ;
    private Component mComponent;
    ArrayList<String> paths = new ArrayList<>();
    String tempPath = "";

    public  ArrayList<String>createOneLoopPath(Component component){
        mComponent = component;
        transitions = (List<Transition>) component.getTransitions();
        initialState= component.getInitialState();
        listStatesEnds = getStatesEnds();
        listColors = new boolean [stateIDMax()+1];
        changeAllFalse(listColors);

        for(State s : mComponent.getStates()){
            tempLoop.add(s.getID());
            findLoop(s.getID(),s.getID());
            tempLoop.clear();
        }
        
        tempLoop.add(initialState.getID());
        findPaths(initialState.getID());
        tempLoop.clear();

        return paths;
    }

    private void findPaths(int v) {
        if(listStatesEnds.contains(v)){
            String path = tempPath;
            paths.add(path);
            System.out.println("adicionou caminho");
            return;
        }
        List<Transition> saidas = mComponent.getStateByID(v).getOutgoingTransitionsList();
        for(int i=0; i<saidas.size();i++){
            Transition t = saidas.get(i);
            
            int r = t.getDestiny().getID();
            int id = indentificadorTransition(t);
            String tag = t.getLabel();

            tempLoop.add(id);
            tempLoop.add(r);
            tempPath += ">" + tag;
            
            if(menosQue2()){
                findPaths(r);
            }
            
            String[] partes = tempPath.split(">");
            partes[partes.length-1] = "";
            tempPath="";
            for(String p : partes){
                if(!p.equals("")){
                    tempPath += ">"+p;
                }
            }
         
            for(int j=0;j<2;j++){
                int lastIndex = tempLoop.size()-1;
                System.out.println("ultimo index = "+lastIndex);
                tempLoop.remove(lastIndex);
            }
            
            
        }
        listColors[v] = false;

    }

    private boolean menosQue2() {
        for(int i=0;i<loops.size();i++){
            int temp = 0;
            for(int j=0;j<tempLoop.size();j+=2){
                int ok = 0;
                for(int k = 0;k<loops.get(i).size() && j+k < tempLoop.size();k++){
                    if(Objects.equals(loops.get(i).get(k), tempLoop.get(j+k))){
                        ok++;
                    }
                }
                if(ok == loops.get(i).size()){
                    temp ++;
                }
            }
            if(temp > 1){
                return false;
            }
        }
        return true;
    }

    private  boolean findLoop(int v, int in) {
        listColors[v] = true;
        List<Transition> saidas = mComponent.getStateByID(v).getOutgoingTransitionsList();
        for(int i=0; i<saidas.size();i++){
            Transition t = saidas.get(i);
            
            int r = t.getDestiny().getID();
            int id = indentificadorTransition(t);
            String tag = t.getLabel();
            
            if(r==in){
                List<Integer> temp = new ArrayList<>();
                temp.addAll(tempLoop);
                temp.add(id);
                temp.add(r);
                loops.add(temp);
                
            }
            if(!listColors[r]){
                tempLoop.add(id);
                tempLoop.add(r);
                findLoop(r,in);
                int lastIndex = tempLoop.size()-1;
                tempLoop.remove(lastIndex);
                lastIndex = tempLoop.size()-1;
                tempLoop.remove(lastIndex);
            }

        }
        return listColors[v] = false;
    }


    private  void changeAllFalse(boolean[] listColors) {
        for(int i =0 ; i<listColors.length; i++){
            listColors[i]=false;
        }
    }

    private ArrayList<Integer> getStatesEnds() {

        ArrayList<Integer> ends = new ArrayList<>();
        for(State s : mComponent.getStates()){
            if(s.getOutgoingTransitionsList().isEmpty()){
                ends.add(s.getID());
            }
        }
        return ends;
    }

    private int indentificadorTransition(Transition t) {
        int id = -1;
        for(int i=0;i<transitions.size();i++){
            Transition atual = transitions.get(i);
            if(atual == t){
                id = i;
            }
        }
        return id;
    }

    private int stateIDMax() {
        int max = -1;
        for(State s: mComponent.getStates()){
            if(s.getID() > max){
                max = s.getID();
            }
        }
        return max;
    }

    
    //-----------Debug------------------------
    private void printLoops() {
        System.out.println("------------------Loops-------------------");
        for(List<Integer> l : loops){
            for(Integer i : l){
                System.out.print(i +"-");
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

    private void printPaths() {
        System.out.println("-------------Caminhos-----------------------");
        for(String s : paths){
            System.out.println(s);
        }
        System.out.println("\n");
    }
    
}
