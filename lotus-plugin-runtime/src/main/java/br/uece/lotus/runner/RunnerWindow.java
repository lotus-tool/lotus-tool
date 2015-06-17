package br.uece.lotus.runner;

import br.uece.lotus.Component;
import br.uece.lotus.Transition;
import br.uece.lotus.helpers.window.Window;
import br.uece.lotus.viewer.ComponentView;
import br.uece.lotus.viewer.ComponentViewImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by emerson on 29/03/15.
 */
public class RunnerWindow implements Window, Initializable, RunnerContext.Listener {

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
    private TextField mTxtInput;
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

    private List<Transition> mTransicoesColoridas = new ArrayList<>();

    private EventHandler<ActionEvent> mBtnTransitionClicked = (event) -> {
        descolorirTransicoes();
        mContext.step((Transition) ((Button) event.getTarget()).getUserData());
        updateView();
    };

    private void descolorirTransicoes() {
        for (Transition t: mTransicoesColoridas) {
            applyNormalStyle(t);
        }
        mTransicoesColoridas.clear();
    }

    private EventHandler<ActionEvent> mBtnAddSaveSymbolClick = (event) -> {
        if (!mEdtSymbolName.getText().trim().isEmpty()) {
            Object v = literalValue(mEdtSymbolValue.getText());
            mContext.putSymbol(mEdtSymbolName.getText(), v);
            updateSymbolTable();
        }
    };

    private Object literalValue(String literal) {
        try {
            return Double.parseDouble(literal);
        } catch (Exception e) {
            //ignora
        }
        try {
            return Boolean.parseBoolean(literal);
        } catch (Exception e) {
            //ignora
        }
        return literal;
    }

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
        mContext = new RunnerContext(component, new ScriptStandardLibrary(mTxtOutput, mTxtInput));
        mContext.addListener(this);
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
        descolorirTransicoes();
        colorirTransicoes();
    }

    private void colorirTransicoes() {
        for (Transition t: mContext.getEnabledActions()) {
            mTransicoesColoridas.add(t);
            applyEnabledStyle(t);
        }
        for (Transition t: mContext.getDisabledActions()) {
            mTransicoesColoridas.add(t);
            applyDisabledStyle(t);
        }
    }

    private void applyDisabledStyle(Transition t) {
        t.setColor("red");
        t.setTextColor("red");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setWidth(2);
    }

    private static void applyEnabledStyle(Transition t) {
        t.setColor("blue");
        t.setTextColor("blue");
        t.setTextSyle(Transition.TEXTSTYLE_BOLD);
        t.setWidth(2);
    }

    private static void applyNormalStyle(Transition t) {
        t.setColor("black");
        t.setTextSyle(Transition.TEXTSTYLE_NORMAL);
        t.setTextColor("black");
        t.setWidth(1);
    }

    @Override
    public void onChanged(RunnerContext runnerContext) {
        Platform.runLater(() -> {
            updateView();
        });
    }
}
