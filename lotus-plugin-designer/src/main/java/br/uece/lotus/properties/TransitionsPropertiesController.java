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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.security.InvalidParameterException;

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
                applyProbability();
            }
        });
        mEdtProbability.setOnAction((ActionEvent event) -> {
            applyProbability();
            mEdtProbability.selectAll();
        });
        mEdtLabel.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                mTransition.setLabel(mEdtLabel.getText());
            }
        });
        mEdtLabel.setOnAction((ActionEvent event) -> {
            mTransition.setLabel(mEdtLabel.getText());
            mEdtLabel.selectAll();
        });
        mEdtGuard.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue || mTransition == null) {
                mTransition.setGuard(mEdtGuard.getText());
            }
        });
        mEdtGuard.setOnAction((ActionEvent event) -> {
            mTransition.setGuard(mEdtGuard.getText());
            mEdtGuard.selectAll();
        });
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
            mEdtLabel.requestFocus();
        }
    }

    @Override
    public void onChange(Transition transition) {
        updateEditors();
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mTransition == null) {
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

    private void applyProbability() {
        try {
            String s = mEdtProbability.getText().trim();

            if (s.isEmpty()) {
                mTransition.setProbability(null);
            }

            Double probability = Double.parseDouble(s);

            if (!(probability >= 0 && probability <= 1)) {
                throw new InvalidParameterException();
            }

            mTransition.setProbability(probability);
        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Invalid probability value!", "Invalid Value", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidParameterException e) {
            JOptionPane.showMessageDialog(null, "The probability must be a value between 0 and 1.0!", "Invalid Value", JOptionPane.ERROR_MESSAGE);
            mTransition.setProbability(null);
        }
    }
    
    // Mascara para Probabilidade
    public static void campoProbability(final TextField textField) {
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = textField.getText().charAt(oldValue.intValue());
                    if (!(ch >= '0' && ch <= '9' || ch == '.' || ch == ',' || ch == '%')) {
                        textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                    }
                }
            }
        });
    }

}
