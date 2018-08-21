/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/*
 * To change this license header, choose License Headers in ProjectMSCCustom Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.runtime.monitor;

import br.uece.lotus.uml.app.runtime.model.custom.HMSCCustom;
import br.uece.lotus.uml.app.runtime.model.custom.StantardModelingCustom;
import br.uece.lotus.uml.app.runtime.model.custom.TransitionHMSCCustom;


import java.util.LinkedList;

/**
 *
 * @author emerson
 * Adapted by Lucas Vieira
 */
public class ProbabilisticAnnotator {
	
   private StantardModelingCustom hMSC;
    private static final String VISIT_COUNT = "visit.count";
    private HMSCCustom currentHMSCCustom, nextHMSCCustom;

    public void annotate(StantardModelingCustom hMSC, LinkedList<String> HMSCCustomtrace) {
	    this.hMSC = hMSC;

	    for(int currentIndex = 0; currentIndex < HMSCCustomtrace.size(); currentIndex ++){

           if(existNextLabel(currentIndex,HMSCCustomtrace.size())){

               String currentLabelHMSCCustom = HMSCCustomtrace.get(currentIndex);

               String nextLabelHMSCCustom = HMSCCustomtrace.get(currentIndex+1);

               currentHMSCCustom = hMSC.getHMSCCustomFromLabel(currentLabelHMSCCustom.trim());
               nextHMSCCustom = hMSC.getHMSCCustomFromLabel(nextLabelHMSCCustom.trim());
               step(currentHMSCCustom, nextHMSCCustom);

           }

        }

	}

    private void step(HMSCCustom currentHMSCCustom, HMSCCustom nextHMSCCustom) {
        TransitionHMSCCustom transition = hMSC.getTransitionHMSCCustom(currentHMSCCustom.getId(), nextHMSCCustom.getId());
        //currentHMSCCustom.get
        if (transition == null) {
            System.out.println("invalid transition from :"+ currentHMSCCustom.getLabel() + "to "+ nextHMSCCustom.getLabel() + "not exist!");
            return;
        }

        int updatedHMSCCustomVisitCount = getHMSCCustomVisitCount(currentHMSCCustom) + 1;
        setHMSCCustomVisitCount(currentHMSCCustom, updatedHMSCCustomVisitCount);
        for (TransitionHMSCCustom t: currentHMSCCustom.getOutGoingTransitions()) {
            int transitionCount = getTransitionCount(t);
            if (t == transition) {
                transitionCount += 1;
                setTransitionCount(t, transitionCount);
            }
            t.setProbability(((double) transitionCount) / updatedHMSCCustomVisitCount);
        }
       // currentHMSCCustom = transition.getDestinyHMSCCustom();
    }

    private boolean existNextLabel(int currentIndex, int size) {
        if(currentIndex+1 < size){
            return true;
        }
        return false;
    }

//    private void step(String labelHMSCCustom) {
//        TransitionHMSCCustom transition = currentHMSCCustom.getTransitionByLabel(labelHMSCCustom);
//        //currentHMSCCustom.get
//        if (transition == null) {
//            System.out.println("invalid event " + labelHMSCCustom + " at HMSCCustom " + currentHMSCCustom.getLabel());
//            return;
//        }
//
//        int updatedHMSCCustomVisitCount = getHMSCCustomVisitCount(currentHMSCCustom) + 1;
//        setHMSCCustomVisitCount(currentHMSCCustom, updatedHMSCCustomVisitCount);
//        for (TransitionHMSCCustom t: currentHMSCCustom.getOutGoingTransitions()) {
//            int transitionCount = getTransitionCount(t);
//            if (t == transition) {
//                transitionCount += 1;
//                setTransitionCount(t, transitionCount);
//            }
//            t.setProbability(((double) transitionCount) / updatedHMSCCustomVisitCount);
//        }
//        currentHMSCCustom = transition.getDestinyHMSCCustom();
//
//    }

    private int getHMSCCustomVisitCount(HMSCCustom HMSCCustom) {
        Object obj = HMSCCustom.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setHMSCCustomVisitCount(HMSCCustom s, int value) {
        s.putValue(VISIT_COUNT, value);
    }

    private int getTransitionCount(TransitionHMSCCustom t) {
        Object obj = t.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setTransitionCount(TransitionHMSCCustom t, int v) {
        t.putValue(VISIT_COUNT, v);
    }

}
