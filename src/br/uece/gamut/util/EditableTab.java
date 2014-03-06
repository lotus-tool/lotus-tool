package br.uece.gamut.util;

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
