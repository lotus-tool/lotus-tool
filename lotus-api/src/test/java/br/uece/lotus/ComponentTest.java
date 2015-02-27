/*
 * The MIT License
 *
 * Copyright 2014 emerson.
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

package br.uece.lotus;

import br.uece.lotus.Component;
import static junit.framework.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author emerson
 */
public class ComponentTest {
    
    private Component COMPONENT;
    
    @Before
    public void setUp() {
    	COMPONENT = new Component();
    	COMPONENT.newState(0);
    	COMPONENT.newState(1);
    	COMPONENT.newState(2);
    	COMPONENT.newState(3);
    	COMPONENT.newState(4);
    	COMPONENT.newState(5);
    	COMPONENT.newState(6);
    	COMPONENT.newTransition(0, 3);
    	COMPONENT.newTransition(1, 3);
    	COMPONENT.newTransition(2, 3);
    	COMPONENT.newTransition(3, 4);
    	COMPONENT.newTransition(3, 5);
    	COMPONENT.newTransition(3, 6);
    }
    
    private Component createRandomComponent(int stateFactor, double transitionFactor) {
        Component c = new Component();
        c.newState(0);
        int transitionCount = 1;
        if (transitionFactor > 0) {
            transitionCount = (int) Math.ceil(stateFactor * transitionFactor);
        }
        for (int i = 1; i < stateFactor; i++) {
            c.newState(i);
            for (int j = 0; j < transitionCount; j++) {
                c.buildTransition(i - 1, i)
                        .setGuard("" + Math.random())
                        .setProbability(Math.random())
                        .setLabel("" + Math.random())
                        .create();
            }
        }        
        for (int j = 0; j < transitionCount; j++) {
            c.buildTransition(stateFactor - 1, 0)
                    .setGuard("" + Math.random())
                    .setProbability(Math.random())
                    .setLabel("" + Math.random())
                    .create();
        }
        return c;
    }
        
    @Test
    public void cloneAndEqualsTest() {
        Component c = createRandomComponent(1000, 0.02);
        try {
            Component c2 = c.clone();            
            assertTrue("states count different", c2.getStatesCount() == c.getStatesCount());
            assertTrue("transitions count different", c2.getTransitionsCount() == c.getTransitionsCount());
            assertTrue("transitions count different", c2.getInitialState().getID() == c.getInitialState().getID());            
            assertTrue("equals components not equals", c2.equals(c));
        } catch (CloneNotSupportedException ex) {
            Assert.assertTrue(ex.getMessage(), false);
        }
        
    }
    
    @Test
    public void removeStateTest() {    	    	  
    	assertTrue(COMPONENT.getStateByID(0).getOutgoingTransitionsCount() == 1);
    	assertTrue(COMPONENT.getStateByID(1).getOutgoingTransitionsCount() == 1);
    	assertTrue(COMPONENT.getStateByID(2).getOutgoingTransitionsCount() == 1);
    	assertTrue(COMPONENT.getStateByID(3).getOutgoingTransitionsCount() == 3);
    	assertTrue(COMPONENT.getStateByID(4).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(5).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(6).getOutgoingTransitionsCount() == 0);
    	
    	assertTrue(COMPONENT.getStateByID(0).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(1).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(2).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(3).getIncomingTransitionsCount() == 3);
    	assertTrue(COMPONENT.getStateByID(4).getIncomingTransitionsCount() == 1);
    	assertTrue(COMPONENT.getStateByID(5).getIncomingTransitionsCount() == 1);
    	assertTrue(COMPONENT.getStateByID(6).getIncomingTransitionsCount() == 1);
    	
    	COMPONENT.remove(COMPONENT.getStateByID(3));
    	
    	assertTrue(COMPONENT.getStateByID(0).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(1).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(2).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(3) == null);
    	assertTrue(COMPONENT.getStateByID(4).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(5).getOutgoingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(6).getOutgoingTransitionsCount() == 0);
    	
    	assertTrue(COMPONENT.getStateByID(0).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(1).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(2).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(3) == null);
    	assertTrue(COMPONENT.getStateByID(4).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(5).getIncomingTransitionsCount() == 0);
    	assertTrue(COMPONENT.getStateByID(6).getIncomingTransitionsCount() == 0);    	
    }
    
}


