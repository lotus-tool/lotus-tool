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

        int count = tam;
        Random rand = new Random();

        Component randomComponent = new Component();

        if(tam == 0) return randomComponent;

        randomComponent.newState(0);
        count--;

        for(int i = 1; count > 0; i++){
            randomComponent.newState(i);
            randomComponent.newTransition(i-1, i);
            count--;
        }

        int randomStateID = 0;

        for(int i = 0; i < tam; i++){
            randomStateID = rand.nextInt(tam);
            while((randomStateID == i+1) || (randomStateID == i)){randomStateID = rand.nextInt(tam);}
            Transition.Builder tb = null;
            tb = randomComponent.buildTransition(i, randomStateID);
            tb.setViewType(1);
            tb.create();
            //randomComponent.newTransition(i, randomStateID);
        }

        return randomComponent;
    }

}
