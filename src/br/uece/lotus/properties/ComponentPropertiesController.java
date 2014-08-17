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
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ComponentPropertiesController implements Component.Listener, ChangeListener<String> {

    private ChoiceBox mCbxDefault;
    private ChoiceBox mCbxError;
    private TextField mEdtName;

    private Component mComponent;
    private boolean mEmEdicao;
    private final ChangeListener<State> mAoMudarComboBox = new ChangeListener<State>() {
        @Override
        public void changed(ObservableValue observable, State oldValue, State newValue) {
            if (mComponent == null || mEmEdicao) {
                return;
            }
            if (observable == mCbxDefault.getSelectionModel().selectedItemProperty()) {
                mComponent.setInitialState(newValue);
            } else if (observable == mCbxError.getSelectionModel().selectedItemProperty()) {
                mComponent.setErrorState(newValue);
            }
        }
    };
    private Parent mPropertyNameWrapper;
    private VBox mGrandParent;
    private Parent mPropertyDefaulWrapper;
    private Parent mPropertyErrorWrapper;

    public void init(ChoiceBox cbxDefault, ChoiceBox cbxError, TextField edtName) {
        mCbxDefault = cbxDefault;
        mCbxError = cbxError;
        mEdtName = edtName;

        mGrandParent = (VBox) edtName.getParent().getParent();
        mPropertyNameWrapper = edtName.getParent();
        mPropertyDefaulWrapper = cbxDefault.getParent();
        mPropertyErrorWrapper = cbxError.getParent();        
        
        mEdtName.textProperty().addListener(this);
        mCbxDefault.getSelectionModel().selectedItemProperty().addListener(mAoMudarComboBox);
        mCbxError.getSelectionModel().selectedItemProperty().addListener(mAoMudarComboBox);
    }
    
    public void setVisible(boolean v) {
        Node[] aux = {mPropertyNameWrapper, mPropertyDefaulWrapper, mPropertyErrorWrapper};
        final ObservableList<Node> children = mGrandParent.getChildren();
        if (v) {
            if (!children.contains(mPropertyNameWrapper)) {
                children.addAll(aux);
            }
        } else {
            children.removeAll(aux);
        }
    }

    public void changeComponent(Component s) {
        if (mComponent != null) {
            mComponent.removeListener(this);
        }
        mComponent = s;
        if (mComponent != null) {
            mComponent.addListener(this);
        }
    }

    @Override
    public void onChange(Component component) {
        mEmEdicao = true;
        mEdtName.setText(component.getName());
        mCbxDefault.getSelectionModel().select(component.getInitialState());
        mCbxError.getSelectionModel().select(component.getErrorState());
        mEmEdicao = false;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mComponent == null || mEmEdicao) {
            return;
        }
        if (observable == mEdtName.textProperty()) {
            mComponent.setName(newValue);
        }
    }

    @Override
    public void onStateCreated(Component component, State state) {
        mCbxDefault.getItems().add(state);
    }

    @Override
    public void onStateRemoved(Component component, State state) {
        mCbxDefault.getItems().remove(state);
    }

    @Override
    public void onTransitionCreated(Component component, Transition state) {
        //ignora
    }

    @Override
    public void onTransitionRemoved(Component component, Transition state) {
        //ignora
    }

}
