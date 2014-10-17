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

package br.uece.lotus.benchmark;

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
    
    public ComponentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
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
    
}
