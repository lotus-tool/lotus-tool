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

import br.uece.lotus.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class StatePropertiesController implements State.Listener, ChangeListener<String> {

    private TextField mEdtPositionX;
    private TextField mEdtPositionY;

    private State mState;
    private boolean mEmEdicao;
    private Parent mPropertyXWrapper;
    private VBox mGrandParent;
    private Parent mPropertyYWrapper;

    public void init(TextField edtPositionX, TextField edtPositionY) {
        mPropertyXWrapper = edtPositionX.getParent();
        mGrandParent = (VBox) mPropertyXWrapper.getParent();
        mPropertyYWrapper = edtPositionY.getParent();

        mEdtPositionX = edtPositionX;
        mEdtPositionY = edtPositionY;

        mEdtPositionX.textProperty().addListener(this);
        mEdtPositionY.textProperty().addListener(this);
    }

    public void setVisible(boolean v) {
        final ObservableList<Node> children = mGrandParent.getChildren();
        if (v) {
            if (!children.contains(mPropertyXWrapper)) {
                children.addAll(mPropertyXWrapper, mPropertyYWrapper);
            }
        } else {
            children.removeAll(mPropertyXWrapper, mPropertyYWrapper);
        }
    }

    public void changeState(State s) {
        if (mState != null) {
            mState.removeListener(this);
        }
        mState = s;
        if (mState != null) {
            updateEditors();
            mState.addListener(this);
        }
    }

    @Override
    public void onChange(State state) {
        mEmEdicao = true;
        updateEditors();
        mEmEdicao = false;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mState == null || mEmEdicao) {
            return;
        }
        if (observable == mEdtPositionX.textProperty()) {
            mState.setLayoutX(Double.parseDouble(newValue));
        } else if (observable == mEdtPositionY.textProperty()) {
            mState.setLayoutY(Double.parseDouble(newValue));
        }
    }

    private void updateEditors() {
        mEdtPositionX.setText(String.valueOf(mState.getLayoutX()));
        mEdtPositionY.setText(String.valueOf(mState.getLayoutY()));
    }

}
