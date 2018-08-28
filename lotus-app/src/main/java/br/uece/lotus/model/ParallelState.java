/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.model;

import br.uece.lotus.State;
import java.util.Objects;

/**
 *
 * @author Ranniery
 */
    public class ParallelState {

        private State firstState;
        private State secondState;
        private State compositeState;

        public ParallelState(State firstState, State secondState) {
            this.firstState = firstState;
            this.secondState = secondState;
        }

        public State getFirstState(){
            return firstState;
        }

        public State getSecondState(){
            return secondState;
        }

        public State getCompositeState(){
            return compositeState;
        }

        public void setFirstState(State newA){
            firstState = newA;
        }

        public void setSecondState(State newB){
            secondState = newB;
        }

        public void setCompositeState(State newCompositeState){
            compositeState = newCompositeState;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.firstState);
            hash = 97 * hash + Objects.hashCode(this.secondState);
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
            if (!Objects.equals(this.firstState, other.firstState)) {
                return false;
            }
            return Objects.equals(this.secondState, other.secondState);
        }
    }
