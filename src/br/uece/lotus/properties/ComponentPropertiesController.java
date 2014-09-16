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

package br.uece.lotus.properties;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ComponentPropertiesController implements Component.Listener, ChangeListener<String> {

    private TextField mEdtName;

    private Component mComponent;
    private boolean mEmEdicao;
    private VBox mGrandParent;
    private Parent mPropertyWrapper;

    public void init(TextField edtName) {
        mGrandParent = (VBox) edtName.getParent().getParent();
        mPropertyWrapper = edtName.getParent();
        mEdtName = edtName;
        mEdtName.textProperty().addListener(this);
    }

    public void setVisible(boolean v) {
        final ObservableList<Node> children = mGrandParent.getChildren();
        if (v) {
            if (!children.contains(mPropertyWrapper)) {
                children.add(mPropertyWrapper);
            }
        } else {
            children.remove(mPropertyWrapper);
        }
    }

    public void changeComponent(Component c) {
        if (mComponent != null) {
            mComponent.removeListener(this);
        }
        mComponent = c;        
        if (mComponent != null) {
            mEdtName.setText(c.getName());
            mComponent.addListener(this);
        }
    }

    @Override
    public void onChange(Component component) {
        mEmEdicao = true;
        mEdtName.setText(component.getName());
        mEmEdicao = false;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mComponent == null || mEmEdicao || newValue.trim().isEmpty()) {
            return;
        }
        if (observable == mEdtName.textProperty()) {
            mComponent.setName(newValue);
        }
    }    

    @Override
    public void onStateCreated(Component component, State state) {
        
    }

    @Override
    public void onStateRemoved(Component component, State state) {
        
    }

    @Override
    public void onTransitionCreated(Component component, Transition state) {
        
    }

    @Override
    public void onTransitionRemoved(Component component, Transition state) {
        
    }

}