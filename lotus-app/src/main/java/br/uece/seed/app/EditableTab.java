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

package br.uece.seed.app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class EditableTab extends Tab {

    private Label mLabel = new Label();
    private TextField mTxtRenomearAba = new TextField();
    private EventHandler<ActionEvent> mOnChange;
    private final EventHandler<? super MouseEvent> aoClicar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() == 2) {
                mTxtRenomearAba.setText(mLabel.getText());
                setGraphic(mTxtRenomearAba);
                mTxtRenomearAba.selectAll();
                mTxtRenomearAba.requestFocus();
            }
        }
    };
    private final EventHandler<ActionEvent> aoConfirmar = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            mLabel.setText(mTxtRenomearAba.getText());
            setGraphic(mLabel);
            if (mOnChange != null) {
                mOnChange.handle(new ActionEvent(EditableTab.this, null));
            }
        }
    };
    private final ChangeListener aoPerderFoco = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                mLabel.setText(mTxtRenomearAba.getText());
                setGraphic(mLabel);
                if (mOnChange != null) {
                    mOnChange.handle(new ActionEvent(EditableTab.this, null));
                }
            }
        }
    };
    //Menu de contexto
    private final MenuItem mMnuFechar = new MenuItem("Fechar");
    private final MenuItem mMnuFecharTudo = new MenuItem("Fechar tudo");
    private final MenuItem mMnuFecharOutro = new MenuItem("Fechar outro");
    private final ContextMenu mMnuAbas;

    public EditableTab(String text) {
        mLabel.setText(text);
        setGraphic(mLabel);

        mLabel.setOnMouseClicked(aoClicar);
        mTxtRenomearAba.setOnAction(aoConfirmar);
        mTxtRenomearAba.focusedProperty().addListener(aoPerderFoco);
                       
        mMnuFechar.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                getTabPane().getTabs().remove(EditableTab.this);
            }
        });
        mMnuFecharTudo.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                getTabPane().getTabs().clear();
            }
        });
        mMnuFecharOutro.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                getTabPane().getTabs().retainAll(EditableTab.this);
            }
        });
        mMnuAbas = new ContextMenu(mMnuFechar, mMnuFecharTudo, mMnuFecharOutro);
        setContextMenu(mMnuAbas);
        
    }

    public void setOnChange(EventHandler<ActionEvent> onChange) {
        this.mOnChange = onChange;
    }

    public String getLabel() {
        return mLabel.getText();
    }
}
