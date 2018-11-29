/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
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
package br.uece.lotus.viewer;

import br.uece.lotus.Transition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class TransitionViewImpl extends Region implements TransitionView, Transition.Listener {

    protected Transition mTransition;
    protected StateView mSourceStateView;
    protected StateView mDestinyStateView;

    public void setTransition(Transition t) {
        if (mTransition != null) {
            mTransition.removeListener(this);
            mSourceStateView = null;
            mDestinyStateView = null;
        }
        mTransition = t;
        if (mTransition != null) {
            mSourceStateView = (StateView) mTransition.getSource().getValue("view");
            mDestinyStateView = (StateView) mTransition.getDestiny().getValue("view");
            mTransition.addListener(this);
            prepareView();
            updateView();
        }
    }

    public Transition getTransition() {
        return mTransition;
    }

    public StateView getSourceStateView() {
        return mSourceStateView;
    }

    public StateView getDestinyDestinoView() {
        return mDestinyStateView;
    }

    protected abstract void prepareView();

    protected abstract void updateView();

    @Override
    public void onChange(Transition transition) {
        Platform.runLater(this::updateView);
    }

    protected String getComputedLabel() {
        String s = "";

        if (mTransition.getGuard() != null) {
            if(mTransition.getGuard().equals("")){
                s += "";
            }else{
                s += "(" + mTransition.getGuard() + ")";
            }
        }

        if(mTransition.getActions() != null){
            if(mTransition.getActions().isEmpty()){
                s+="";
            }else {

                s += "{" + String.join(",",mTransition.getActions()) + "}";
            }
        }

        if (mTransition.getProbability() != null) {
            if(mTransition.getProbability() == null){
                s += "";
            }else{
                s += String.format(" %.2f", mTransition.getProbability());
            }
        }

        if (mTransition.getLabel() != null) {
            s += " " + mTransition.getLabel()+" ";
        }

        if(!mTransition.getParameters().isEmpty()){

            s +="["+String.join("," , mTransition.getParameters())+"]";

        }else {
            s += "";
        }
        return s;
    }



    @Override
    public Node getNode() {
        return this;
    }
}