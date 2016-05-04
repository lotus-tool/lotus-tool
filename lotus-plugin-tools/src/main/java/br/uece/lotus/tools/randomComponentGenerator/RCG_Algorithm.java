package br.uece.lotus.tools.randomComponentGenerator;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ranniery on 04/05/2016.
 */
public class RCG_Algorithm {

    public static Component generateRandomComponent (int tam){

        Component randomComponent = new Component();

        if(tam == 0) return randomComponent;

        randomComponent.newState(0);
        tam--;

        for(int i = 1; tam > 0; i++){
            randomComponent.newState(i);
            randomComponent.newTransition(i-1, i);
            tam--;
        }

        return randomComponent;
    }

}
