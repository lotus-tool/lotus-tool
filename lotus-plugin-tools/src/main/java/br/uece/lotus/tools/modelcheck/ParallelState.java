/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.modelcheck;

import br.uece.lotus.State;
import java.util.Objects;

/**
 *
 * @author Ranniery
 */
    public class ParallelState {

        private State a;
        private State b;
        private State compositeState;

        public ParallelState(State a, State b) {
            this.a = a;
            this.b = b;
        }

        public State getA(){
            return a;
        }

        public State getB(){
            return b;
        }

        public State getCompositeState(){
            return compositeState;
        }

        public void setA(State newA){
            a = newA;
        }

        public void setB(State newB){
            b = newB;
        }

        public void setCompositeState(State newCompositeState){
            compositeState = newCompositeState;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.a);
            hash = 97 * hash + Objects.hashCode(this.b);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ParallelState other = (ParallelState) obj;
            if (!Objects.equals(this.a, other.a)) {
                return false;
            }
            return Objects.equals(this.b, other.b);
        }
    }
