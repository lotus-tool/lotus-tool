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
package br.uece.lotus.msc.app.runtime.monitor;

import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;


import java.util.LinkedList;

/**
 *
 * @author emerson
 * Adapted by Lucas Vieira
 */
public class ProbabilisticAnnotator {
	
   private HmscComponent standardModeling;
    public static final String VISIT_COUNT = "visit.count";
    private HmscBlock currentHMSCHmscBlock, nextHMSCHmscBlock;

    public void annotate(HmscComponent standardModeling, LinkedList<String> HMSCtrace) {
	    this.standardModeling = standardModeling;

	    for(int currentIndex = 0; currentIndex < HMSCtrace.size(); currentIndex ++){

           if(existNextLabel(currentIndex,HMSCtrace.size())){

               String currentLabelHMSC = HMSCtrace.get(currentIndex);

               String nextLabelHMSC = HMSCtrace.get(currentIndex+1);

               currentHMSCHmscBlock = standardModeling.getHmscByLabel(currentLabelHMSC.trim());
               nextHMSCHmscBlock = standardModeling.getHmscByLabel(nextLabelHMSC.trim());
               step(currentHMSCHmscBlock, nextHMSCHmscBlock);

           }

        }

	}

    private void step(HmscBlock currentHMSCHmscBlock, HmscBlock nextHMSCHmscBlock) {
        TransitionMSC transition = standardModeling.getTransitionMSC(currentHMSCHmscBlock.getID(), nextHMSCHmscBlock.getID());
        //currentHMSCHmscBlock.get
        if (transition == null) {
            System.out.println("invalid transition from :"+ currentHMSCHmscBlock.getLabel() + "to "+ nextHMSCHmscBlock.getLabel() + "not exist!");
            return;
        }

        int updatedHMSCVisitCount = getHMSCVisitCount(currentHMSCHmscBlock) + 1;
        setHMSCVisitCount(currentHMSCHmscBlock, updatedHMSCVisitCount);
        for (TransitionMSC t: currentHMSCHmscBlock.getOutgoingTransitionList()) {
            int transitionCount = getTransitionCount(t);
            if (t == transition) {
                transitionCount += 1;
                setTransitionCount(t, transitionCount);
            }
            t.setProbability(((double) transitionCount) / updatedHMSCVisitCount);
        }
       // currentHMSCHmscBlock = transition.getDestinyHMSCCustom();
    }

    private boolean existNextLabel(int currentIndex, int size) {
        if(currentIndex+1 < size){
            return true;
        }
        return false;
    }

//    private void step(String labelHMSCCustom) {
//        TransitionHMSCCustom transition = currentHMSCHmscBlock.getTransitionByLabel(labelHMSCCustom);
//        //currentHMSCHmscBlock.get
//        if (transition == null) {
//            System.out.println("invalid event " + labelHMSCCustom + " at HMSCCustom " + currentHMSCHmscBlock.getLabel());
//            return;
//        }
//
//        int updatedHMSCCustomVisitCount = getHMSCVisitCount(currentHMSCHmscBlock) + 1;
//        setHMSCVisitCount(currentHMSCHmscBlock, updatedHMSCCustomVisitCount);
//        for (TransitionHMSCCustom t: currentHMSCHmscBlock.getOutGoingTransitions()) {
//            int transitionCount = getTransitionCount(t);
//            if (t == transition) {
//                transitionCount += 1;
//                setTransitionCount(t, transitionCount);
//            }
//            t.setProbability(((double) transitionCount) / updatedHMSCCustomVisitCount);
//        }
//        currentHMSCHmscBlock = transition.getDestinyHMSCCustom();
//
//    }

    private int getHMSCVisitCount(HmscBlock HMSCHmscBlock) {
        Object obj = HMSCHmscBlock.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setHMSCVisitCount(HmscBlock s, int value) {
        s.putValue(VISIT_COUNT, value);
    }

    private int getTransitionCount(TransitionMSC t) {
        Object obj = t.getValue(VISIT_COUNT);
        if (obj == null) {
            obj = 0;
        }
        return (int) obj;
    }

    private void setTransitionCount(TransitionMSC t, int v) {
        t.putValue(VISIT_COUNT, v);
    }

}
