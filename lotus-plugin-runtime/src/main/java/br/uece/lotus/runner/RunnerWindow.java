package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import br.uece.seed.app.UserInterface;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by emerson on 29/03/15.
 */
public class RunnerWindow implements Window, Initializable {

    private Parent mNode;
    private Component mComponent;
    private ComponentView mViewer;
    private RunnerContext mContext;

    @FXML
    private ScrollPane mScrollPanel;
    @FXML
    private ListView<String> mLstTrace;
    @FXML
    private TextArea mTxtOutput;
    @FXML
    private VBox mTransitionContainer;
    @FXML
    private TableView<Symbol> mSymbolTable;
    @FXML
    private TableColumn<String, Symbol> mSymbolNameCol;
    @FXML
    private TableColumn<String, Symbol> mSymbolValueCol;
    @FXML
    private Button mBtnAddSaveSymbol;
    @FXML
    private TextField mEdtSymbolName;
    @FXML
    private TextField mEdtSymbolValue;


    private EventHandler<ActionEvent> mBtnTransitionClicked = (event) -> {
        descolorirTransicoes();
        mContext.step((Transition) ((Button) event.getTarget()).getUserData());
        updateView();
    };

    private void descolorirTransicoes() {
        for (Transition t: mContext.getEnabledActions()) {
            applyNormalStyle(t);
        }
        for (Transition t: mContext.getDisabledActions()) {
            applyNormalStyle(t);
        }
    }

    private EventHandler<ActionEvent> mBtnAddSaveSymbolClick = (event) -> {
        mContext.putSymbol(mEdtSymbolName.getText(), mEdtSymbolValue.getText());
        updateSymbolTable();
    };

    private void updateSymbolTable() {
        mSymbolTable.getItems().clear();
        for (Map.Entry<String, Object> e: mContext.getSymbols().entrySet()) {
            Symbol s = new Symbol();
            s.nameProperty().set(e.getKey());
            s.valueProperty().set(e.getValue());
            mSymbolTable.getItems().add(s);
        }
    }

    @Override
    public Component getComponent() {
        return mComponent;
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
        mComponent = component;
        mContext = new RunnerContext(component);
        updateView();
    }

    @Override
    public String getTitle() {
        return mComponent.getName() + " - [Running]";
    }

    public Node getNode() {
        return mNode;
    }

    public void setNode(Parent root) {
        mNode = root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mViewer = new ComponentViewImpl();
        mScrollPanel.setContent((Node) mViewer);
        mViewer.getNode().minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.getNode().minWidthProperty().bind(mScrollPanel.widthProperty());
        mBtnAddSaveSymbol.setOnAction(mBtnAddSaveSymbolClick);
        mSymbolNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mSymbolValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    private void updateView() {
        mTxtOutput.setText(mContext.getOutput());
        mTransitionContainer.getChildren().clear();
        for (Transition t: mContext.getEnabledActions()) {
            Button btn = new Button();
            btn.setText(t.getLabel());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setUserData(t);
            btn.setOnAction(mBtnTransitionClicked);
            mTransitionContainer.getChildren().add(btn);
        }
        mLstTrace.getItems().clear();
        mLstTrace.getItems().addAll(mContext.getTrace());
        updateSymbolTable();
        colorirTransicoes();
    }

    private void colorirTransicoes() {
        for (Transition t: mContext.getEnabledActions()) {
            applyEnabledStyle(t);
        }
        for (Transition t: mContext.getDisabledActions()) {
            applyDisabledStyle(t);
        }
    }

    public void applyDisabledStyle(Transition t) {
        t.setColor("red");
        t.setTextColor("red");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setWidth(2);
    }

    public static void applyEnabledStyle(Transition t) {
        t.setColor("blue");
        t.setTextColor("blue");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setWidth(2);
    }

    public static void applyNormalStyle(Transition t) {
        t.setColor("black");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor(null);
        t.setWidth(1);
    }

}
