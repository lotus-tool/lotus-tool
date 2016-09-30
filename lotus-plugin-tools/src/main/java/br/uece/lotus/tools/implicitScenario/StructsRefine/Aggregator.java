package br.uece.lotus.tools.implicitScenario.StructsRefine;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.TransitionView;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lucas on 15/07/16.
 */
public class Aggregator {


    private Component mComponentChanged;
    private Component mComponentBackUp;
    private ArrayList<String> mlistTraces;
    ArrayList<Component> listComponents = new ArrayList<>();


    public Aggregator(Component mInitialComponent, ArrayList<String> listTraces) {

        try {
            this.mComponentBackUp = mInitialComponent.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.mlistTraces = listTraces;
    }

    public /*ArrayList<Component>*/Component aggregate() {
       /* ArrayList<Component> listComponents = new ArrayList<>();*/
        Transition transition1, transition2;
        boolean createScenary;
        boolean cont = true;
        while (cont) {
            Iterator<Transition> iterator1;
            Iterator<Transition> iterator2;
            cont = false;
            outerloop:
            for (iterator1 = mComponentBackUp.getTransitions().iterator(); iterator1.hasNext(); ) {
                transition1 = iterator1.next();
                for (iterator2 = mComponentBackUp.getTransitions().iterator(); iterator2.hasNext(); ) {
                    transition2 = iterator2.next();
                    if (transition1 != transition2) {
                        if (transition1.getLabel().equals(transition2.getLabel())) {
                            createScenary = union(transition1, transition2);
                            if (!createScenary) {
                                System.out.println(" NOT Create scenary");
                                cont = true;
                                try {
                                    mComponentBackUp = mComponentChanged.clone();
                                    /*listComponents.add(mComponentBackUp.clone());*/
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                                break outerloop;

                            } else {
                                System.out.println("Create scenary");
                            }
                        }
                    }
                }
            }
        }
        deleteStatesWithinTransition(mComponentBackUp);
        return mComponentBackUp;

        /*deleteStatesWithinTransition(listComponents.get(listComponents.size()-1));
        return listComponents;*/
    }

    private void deleteStatesWithinTransition(Component component) {
        ArrayList<State> states = new ArrayList<>();

         states.addAll((ArrayList<State>) component.getStates());

        for (State s : states ) {
            if (s.getIncomingTransitionsCount() == 0 && s.getOutgoingTransitionsCount() == 0) {
                int id = s.getID();
                s=component.getStateByID(id);
                component.remove(s);
            }
        }

    }

    private boolean union(Transition t1, Transition t2) {
        try {
            mComponentChanged = mComponentBackUp.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Transition transition1 = searchTransition(t1, mComponentChanged);
        Transition transition2 = searchTransition(t2, mComponentChanged);

        if(sameStateOrigemAndDestiny(transition1,transition2)){
            mComponentChanged.remove(transition2);

        }
        else {
        System.out.println("UNIÃO DE:");
        System.out.println(transition1.getSource().getID() + " " + transition1.getLabel() + " " + transition1.getDestiny().getID());
        System.out.println(transition2.getSource().getID() + " " + transition2.getLabel() + " " + transition2.getDestiny().getID());

        State stateDst1 = transition1.getDestiny();
        State stateSrc1 = transition1.getSource();
        State stateDst2 = transition2.getDestiny();
        State stateSrc2 = transition2.getSource();

        addTransitionsIN(transition1, transition2, getTransitionsIn(stateSrc2));
        addTransitionsOUT(transition1,  transition2, getTranstionOut(stateSrc2));
        addTransitionsIN(transition1,  transition2, getTransitionsIn(stateDst2));
        addTransitionsOUT(transition1, transition2, getTranstionOut(stateDst2));
        mComponentChanged.remove(transition2);
        }

        Boolean bl = checkGerationScenarie();
        return bl;




        /*Iterable<Transition> listTransitionInDst2 = stateDst2.getIncomingTransitionsList();
        Iterable<Transition> listTransitionOutDst2 = stateDst2.getOutgoingTransitionsList();

        Iterable<Transition> listTransitionInSrc2 = stateSrc2.getIncomingTransitionsList();
        Iterable<Transition> listTransitionOutSrc2 = stateDst2.getOutgoingTransitionsList();

        ArrayList<String> tBackUp1 = new ArrayList<>();
        boolean sai1 = true;
        while (listTransitionInSrc2.iterator().hasNext()) {
            Transition t = listTransitionInSrc2.iterator().next();
            for (String s : tBackUp1) {
                if (s.equals(t.getLabel())) {
                    sai1 = false;
                    break;
                }
            }
            if (sai1) {
                tBackUp1.add(t.getLabel());

            } else {
                break;
            }

            mComponentChanged.buildTransition(t.getSource().getID(), stateSrc1.getID())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();


            mComponentChanged.remove(t);
        }

        ArrayList<String> tBackUp2 = new ArrayList<>();
        boolean sai2 = true;
        while (listTransitionOutSrc2.iterator().hasNext()) {
            Transition t = listTransitionOutSrc2.iterator().next();
            for (String s : tBackUp2) {
                if (s.equals(t.getLabel())) {
                    sai2 = false;
                    break;
                }
            }
            if (sai2) {
                tBackUp2.add(t.getLabel());

            } else {
                break;
            }

            mComponentChanged.buildTransition(stateSrc1.getID(),t.getDestiny().getID())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();


            mComponentChanged.remove(t);
        }





        ArrayList<String> tBackUp3 = new ArrayList<>();
        boolean sai3= true;
        while (listTransitionOutDst2.iterator().hasNext()) {
            Transition t = listTransitionOutDst2.iterator().next();
            for (String s : tBackUp3) {
                if (s.equals(t.getLabel())) {
                    sai3 = false;
                    break;
                }
            }
            if (sai3) {
                tBackUp3.add(t.getLabel());
            } else {
                break;
            }
            mComponentChanged.buildTransition(stateDst1, t.getDestiny())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
            mComponentChanged.remove(t);

        }

        ArrayList<String> tBackUp4 = new ArrayList<>();
        boolean sai4= true;
        while (listTransitionInDst2.iterator().hasNext()) {
            Transition t = listTransitionInDst2.iterator().next();
            for (String s : tBackUp4) {
                if (s.equals(t.getLabel())) {
                    sai4 = false;
                    break;
                }
            }
            if (sai4) {
                tBackUp4.add(t.getLabel());
            } else {
                break;
            }
            mComponentChanged.buildTransition(t.getSource().getID(), stateDst1.getID())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
            mComponentChanged.remove(t);

        }


        mComponentChanged.remove(transition2);
*/


    }

    private void addTransitionsIN(Transition transition1, Transition transition2, List<Transition>transIn) {
        ArrayList<Transition> transitionsIn = new ArrayList<>();
        transitionsIn.addAll(transIn);
        Transition t = null;
        Iterator iterator;
        for (iterator = transitionsIn.iterator(); iterator.hasNext(); ){
             t= (Transition) iterator.next();
            if(t==transition1|| t==transition2){
                continue;
            }
            mComponentChanged.buildTransition(t.getSource().getID(), transition1.getSource().getID())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
            mComponentChanged.remove(t);
            /*iterator.remove();*/
        }
    }



    private void addTransitionsOUT(Transition transition1, Transition transition2, List<Transition> transOut) {
        ArrayList<Transition> transitionsOut = new ArrayList<>();
        transitionsOut.addAll(transOut);
        Transition t = null;
        Iterator iterator;
        for (iterator = transitionsOut.iterator(); iterator.hasNext(); ){
            t= (Transition) iterator.next();
            if(t==transition1|| t==transition2){
                continue;
            }
            mComponentChanged.buildTransition(transition1.getDestiny().getID(), t.getDestiny().getID())
                    .setLabel(t.getLabel())
                    .setViewType(TransitionView.Geometry.CURVE)
                    .create();
            mComponentChanged.remove(t);
           /* iterator.remove();*/
        }
    }

    private List<Transition> getTranstionOut(State state) {
        return state.getOutgoingTransitionsList();
    }

    private List<Transition> getTransitionsIn(State state) {
        return state.getIncomingTransitionsList();
    }

   /* private void addTransitions(ArrayList<Transition> transitions, Transition transition1, Transition transition2) {
        for(Transition t : transitions){
            if()
        }
    }*/

  /*  private ArrayList<Transition> getAllTransitonsFrom(State state) {

    }*/
Transition lastTransitionSearch=null;
    private Transition searchTransition(Transition transition, Component component) {
        Iterator<Transition> iterator;
        Transition tempTransition = null;
        for (iterator = component.getTransitions().iterator(); iterator.hasNext(); ) {
            Transition transitionFromSeach = iterator.next();
            if (sameStateOrigemAndDestiny(transition, transitionFromSeach)) {
                if(transitionFromSeach!=lastTransitionSearch){
                tempTransition = transitionFromSeach;
                lastTransitionSearch= transitionFromSeach;
                    break;
                }
            }
        }
        return tempTransition;

    }

    private boolean sameStateOrigemAndDestiny(Transition transition, Transition transitionFromSeach) {
        if (transition.getSource().getID() == transitionFromSeach.getSource().getID()) {
            if (transition.getDestiny().getID() == transitionFromSeach.getDestiny().getID()) {
                return true;
            }
        }
        return false;
    }

    private Boolean checkGerationScenarie() {
        Boolean retorno = false;
        OneLoopPath oneLoopPath = new OneLoopPath();
        ArrayList<String> listOneLoopPath = removeElementsrepeated(oneLoopPath.createOneLoopPath(mComponentChanged));


        ArrayList<String> tempArrayList = method(listOneLoopPath);
        listOneLoopPath.clear();
        listOneLoopPath.addAll(tempArrayList);

        for (int i = 0; i < listOneLoopPath.size(); i++) {
            String transition = listOneLoopPath.get(i);
            if (!mlistTraces.contains(transition)) {

                return true;
                // gerou cenário
            } else {
                retorno = false; //  não gerou cenário
            }

        }
        return retorno;
    }

    public Component getmComponentChanged() {
        return mComponentChanged;
    }


    public ArrayList method(ArrayList<String> list) {
        ArrayList<String> retorno = new ArrayList<>();
        for (String str : list) {
            String a = String.valueOf(str.charAt(str.length() - 2));
            if (str != null && str.length() > 0 && str.charAt(str.length() - 2) == ',') {
                str = str.substring(0, str.length() - 2);
                retorno.add(str);
            }
        }
        return retorno;
    }

    ArrayList<String> removeElementsrepeated(ArrayList<String> arrayList) {
        Set<String> hs = new HashSet<>();
        hs.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(hs);

        return arrayList;
    }


    public void gsetmComponentChanged(Component mComponentChanged) {
        this.mComponentChanged = mComponentChanged;
    }
}
