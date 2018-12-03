package br.uece.lotus.msc.designer.hmscComponent;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class teste2 extends Plugin {
    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;
    public static final String MAX_NUMBER_OF_VISITS_COUNTER_IN_STATE = "MAX_NUMBER";
    public static final String NUMBER_OF_VISITS_COUNTER_IN_STATE = "NUMBER";

    private Runnable mAbout = () -> {
     String path = getPath (mProjectExplorer.getSelectedComponents().get(0));
    };
    private boolean jaEstanapilha;

    private String getPath(Component component) {
        Stack<State> stateStack = new Stack<>();


        //todo eu acho que não está usando o maxvalue
        maximumNumberOfVisitsCounterInStates(component.getStatesList());

        State currentState = component.getInitialState();
        depth(currentState,stateStack);


        return null;
    }

    private void depth(State currentState, Stack<State> stateStack) {
        if(stateStack.contains(currentState) ){
            updateValues(currentState);
            return;
        }

       // for(Transition currentTransition : currentState.getOutgoingTransitionList()){


        Iterator<Transition> transitionIterator = currentState.getOutgoingTransitions().iterator();
        while (transitionIterator.hasNext()) {

            if (chegouAoMaximo(currentState)) {
                stateStack.pop();
                updateValues(stateStack.peek());
            }else if(!stateStack.contains(currentState)){

                stateStack.add(currentState);
                updateValues(currentState);
            }
            Transition currentTransition = transitionIterator.next();
            System.out.println(currentTransition.getSource().getLabel()+" "+currentTransition.getLabel()+" "+currentTransition.getDestiny().getLabel());
            depth(currentTransition.getDestiny(), stateStack);

        }
    //    }

            if(!transitionIterator.hasNext()){
                stateStack.pop();
                updateValues(stateStack.peek());
            }





    }


    private boolean chegouAoMaximo(State currentState) {
        int visitsCount = (int) currentState.getValue(NUMBER_OF_VISITS_COUNTER_IN_STATE);
        int maxVisitsCount = (int) currentState.getValue(MAX_NUMBER_OF_VISITS_COUNTER_IN_STATE);
        return visitsCount==maxVisitsCount;
    }

    private void updateValues(State currentState) {
        int visitsCount = (int) currentState.getValue(NUMBER_OF_VISITS_COUNTER_IN_STATE);
        int maxVisitsCount = (int) currentState.getValue(MAX_NUMBER_OF_VISITS_COUNTER_IN_STATE);

        int newVisitsCount = visitsCount+1;
        currentState.setValue(NUMBER_OF_VISITS_COUNTER_IN_STATE, newVisitsCount);
    }


    private void maximumNumberOfVisitsCounterInStates(List<State> states) {
        for(State currentState: states){
            currentState.setValue(MAX_NUMBER_OF_VISITS_COUNTER_IN_STATE,currentState.getIncomingTransitionsCount()
                    +currentState.getOutgoingTransitionsCount());

            currentState.setValue(NUMBER_OF_VISITS_COUNTER_IN_STATE,0);

        }
    }


    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mProjectExplorer = (ProjectExplorer) extensionManager.get(ProjectExplorer.class);
        mUserInterface = extensionManager.get(UserInterface.class);
        mUserInterface.getMainMenu().newItem("Help/teste")
                .setWeight(Integer.MAX_VALUE)
                .setAction(mAbout).create();

    }
}
