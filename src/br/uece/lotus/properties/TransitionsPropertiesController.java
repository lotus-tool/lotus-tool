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

import br.uece.lotus.Transition;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TransitionsPropertiesController implements Transition.Listener, ChangeListener<String> {

    private TextField mEdtLabel;
    private TextField mEdtGuard;
    private TextField mEdtProbability;

    private Transition mTransition;
    private boolean mEmEdicao;
    private Parent mPropertyLabelWrapper;
    private Parent mPropertyGuardWrapper;
    private Parent mPropertyProbabilityWrapper;
    private VBox mGrandParent;

    void init(TextField edtLabel, TextField edtGuard, TextField edtProbability) {        
        mPropertyLabelWrapper = edtLabel.getParent();
        mPropertyGuardWrapper = edtGuard.getParent();
        mPropertyProbabilityWrapper = edtProbability.getParent();
        mGrandParent = (VBox) mPropertyLabelWrapper.getParent();
                
        mEdtLabel = edtLabel;
        mEdtGuard = edtGuard;
        mEdtProbability = edtProbability;
        mEdtProbability.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                aplicarProbabilidade();
            }
        });
        mEdtProbability.setOnAction((ActionEvent event) -> {
            aplicarProbabilidade();
            mEdtProbability.selectAll();
        });
        mEdtLabel.textProperty()
                .addListener(this);
        mEdtGuard.textProperty()
                .addListener(this);
        mEdtProbability.textProperty()
                .addListener(this);
    }

    public void setVisible(boolean v) {
        final ObservableList<Node> children = mGrandParent.getChildren();
        final Node[] aux = {mPropertyLabelWrapper, mPropertyGuardWrapper, mPropertyProbabilityWrapper};
        if (v) {
            if (!children.contains(mPropertyLabelWrapper)) {
                children.addAll(aux);
            }
        } else {            
            children.removeAll(aux);
        }
    }

    public void changeTransition(Transition t) {
        if (mTransition != null) {
            mTransition.removeListener(this);
        }
        mTransition = t;
        if (mTransition != null) {
            updateEditors();
            mTransition.addListener(this);
        }
    }

    @Override
    public void onChange(Transition transition) {
        mEmEdicao = true;
        updateEditors();
        mEmEdicao = false;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mTransition == null || mEmEdicao) {
            return;
        }
        if (observable == mEdtLabel.textProperty()) {
            mTransition.setLabel(newValue);
        } else if (observable == mEdtGuard.textProperty()) {
            mTransition.setGuard(newValue);
        }
    }

    private void updateEditors() {
        mEdtLabel.setText(mTransition.getLabel());
        mEdtGuard.setText(mTransition.getGuard());
        Double d = mTransition.getProbability();
        mEdtProbability.setText(d == null ? "" : String.valueOf(d));
    }

    private void aplicarProbabilidade() {
        try {
            mTransition.setProbability(Double.parseDouble(mEdtProbability.getText()));
        } catch (NumberFormatException e) {
            //ignora
        }
    }

}
