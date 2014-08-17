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
package br.uece.lotus.designer;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.viewer.BasicComponentViewer;
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import br.uece.lotus.viewer.View;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author emerson
 */
public class DesignerWindow extends AnchorPane {

    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    private final BasicComponentViewer mViewer;
    private final ToolBar mToolbar;
    private final ToggleGroup mToggleGroup;
    private final ToggleButton mBtnArrow;
    private final ToggleButton mBtnState;
    private final ToggleButton mBtnTransition;
    private final ToggleButton mBtnEraser;
    private final ScrollPane mScrollPanel;

    private void applyNormalStateStyle(State s) {
        String oldLabel = (String) s.getValue("oldLabel");
        if (oldLabel != null) {
            s.setLabel(oldLabel);
        }
        s.setColor("aqua");
    }

    private void applyInitialStateStyle(State s) {
        String oldLabel = (String) s.getValue("oldLabel");
        if (oldLabel != null) {
            s.setLabel(oldLabel);
        }
        s.setColor("yellow");
    }

    private void applyFinalStateStyle(State s) {
        String oldLabel = (String) s.getValue("oldLabel");
        if (oldLabel != null) {
            s.setLabel(oldLabel);
        }
        s.setColor("gray");
    }

    private void applyErrorStateStyle(State s) {
        s.setColor("red");
        s.setValue("oldLabel", s.getLabel());
        s.setLabel("-1");
    }

    public interface Listener {

        void onSelectionChange(DesignerWindow v);
    }

    private int mModoAtual;
    private final ContextMenu mComponentContextMenu;
    //seleção e destaque
    private View mComponentSobMouse;
    private View mComponentSelecionado;
    private final List<Listener> mListeners = new ArrayList<>();

    public DesignerWindow(BasicComponentViewer viewer) {

        mToolbar = new ToolBar();
        mToggleGroup = new ToggleGroup();
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent e) -> {
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnState = new ToggleButton();
        mBtnState.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("ic_state.png"))));
        mBtnState.setOnAction((ActionEvent e) -> {
            setModo(MODO_VERTICE);
        });
        mBtnState.setToggleGroup(mToggleGroup);
        mBtnTransition = new ToggleButton();
        mBtnTransition.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("ic_transition.png"))));
        mBtnTransition.setOnAction((ActionEvent e) -> {
            setModo(MODO_TRANSICAO);
        });
        mBtnTransition.setToggleGroup(mToggleGroup);
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent e) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);

        mToolbar.getItems().addAll(mBtnArrow, mBtnState, mBtnTransition, mBtnEraser);
        AnchorPane.setTopAnchor(mToolbar, 0D);
        AnchorPane.setLeftAnchor(mToolbar, 0D);
        AnchorPane.setRightAnchor(mToolbar, 0D);
        getChildren().add(mToolbar);

        mComponentContextMenu = new ContextMenu();

        mViewer = viewer;
        mViewer.setStateContextMenu(mComponentContextMenu);
        mViewer.setOnMouseClicked(aoClicarMouse);
        mViewer.setOnMouseMoved(aoMoverMouse);

        mViewer.setOnDragDetected(aoDetectarDragSobreVertice);
        mViewer.setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        mViewer.setOnDragDropped(aoSoltarMouseSobreVertice);

        mViewer.setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        mViewer.setOnMouseDragged(aoArrastarVerticeComOMouse);
        mViewer.setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);
//        mViewer.setPrefSize(500, 500);        
        mScrollPanel = new ScrollPane(mViewer);
        AnchorPane.setTopAnchor(mScrollPanel, 44D);
        AnchorPane.setLeftAnchor(mScrollPanel, 0D);
        AnchorPane.setRightAnchor(mScrollPanel, 0D);
        AnchorPane.setBottomAnchor(mScrollPanel, 0D);
        getChildren().add(mScrollPanel);
        mViewer.minHeightProperty().bind(mScrollPanel.heightProperty());
        mViewer.minWidthProperty().bind(mScrollPanel.widthProperty());
        MenuItem mSetAsInitialMenuItem = new MenuItem("Set as initial");
        mSetAsInitialMenuItem.setOnAction((ActionEvent event) -> {
            Component c = mViewer.getComponent();
            State initial = c.getInitialState();
            if (initial != null) {
                applyNormalStateStyle(initial);
            }
            State s = ((StateView) mComponentSelecionado).getState();
            c.setInitialState(s);
            applyInitialStateStyle(s);
        });
        MenuItem mSetAsFinalMenuItem = new MenuItem("Set as final");
        mSetAsFinalMenuItem.setOnAction((ActionEvent event) -> {
            Component c = mViewer.getComponent();
            State finalState = c.getFinalState();
            if (finalState != null) {
                applyNormalStateStyle(finalState);
            }
            State s = ((StateView) mComponentSelecionado).getState();
            c.setFinalState(s);
            applyFinalStateStyle(s);
        });
        MenuItem mSetAsErrorMenuItem = new MenuItem("Set as error");
        mSetAsErrorMenuItem.setOnAction((ActionEvent event) -> {
            Component c = mViewer.getComponent();
            State error = c.getErrorState();
            if (error != null) {
                applyNormalStateStyle(error);
            }
            State s = ((StateView) mComponentSelecionado).getState();
            c.setErrorState(s);
            applyErrorStateStyle(s);
        });
        MenuItem mSaveAsPNG = new MenuItem("Save as PNG");
        mSaveAsPNG.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as PNG");
            fileChooser.setInitialFileName(mViewer.getComponent().getName() + ".png");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png")
            );
            File arq = fileChooser.showSaveDialog(null);
            if (arq == null) {
                return;
            }
            mViewer.saveAsPng(arq);
            JOptionPane.showMessageDialog(null, "PNG Image successfuly saved!");
        });
        mComponentContextMenu.getItems().addAll(mSetAsInitialMenuItem, mSetAsFinalMenuItem, mSetAsErrorMenuItem, new SeparatorMenuItem(), mSaveAsPNG);

    }
    ////////////////////////////////////////////////////////////////////////////
    // Ao Clicar o mouse
    ////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                setComponenteSelecionado(mComponentSobMouse);
                mComponentContextMenu.show(mViewer, e.getScreenX(), e.getScreenY());
                return;
            }

            if (mModoAtual == MODO_NENHUM) {
                setComponenteSelecionado(mComponentSobMouse);
            } else {
                if (mModoAtual == MODO_VERTICE) {
                    if (!(mComponentSobMouse instanceof StateView)) {
                        int id = mViewer.getComponent().getStatesCount();
                        State s = new State();
                        s.setID(id);
                        s.setLayoutX(e.getX());
                        s.setLayoutY(e.getY());
                        s.setLabel(String.valueOf(id));
                        if (mViewer.getComponent().getStatesCount() == 0) {
                            mViewer.getComponent().setInitialState(s);
                        }
                        mViewer.getComponent().add(s);
                    }
                } else if (mModoAtual == MODO_REMOVER) {
                    if (mComponentSobMouse instanceof StateView) {
                        State v = ((StateView) mComponentSobMouse).getState();
                        mViewer.getComponent().remove(v);
                    } else if (mComponentSobMouse instanceof TransitionView) {
                        Transition t = ((TransitionView) mComponentSobMouse).getTransition();
                        mViewer.getComponent().remove(t);
                    }
                }
            }
        }
    };
    ////////////////////////////////////////////////////////////////////////////
// Mover o mouse
////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoMoverMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            View aux = getComponentePelaPosicaoMouse(t.getX(), t.getY());
//            if (mComponentSobMouse != null) {
//                mComponentSobMouse.setHighlighted(false);
//            }
//            if (aux != null) {
//                aux.setHighlighted(true);
//            }
            mComponentSobMouse = aux;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Mover state 
    ////////////////////////////////////////////////////////////////////////////
    private double variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice;
    private double variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice;
    private EventHandler<? super MouseEvent> aoIniciarArrastoVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }

            if (!(mComponentSobMouse instanceof StateView)) {
                return;
            }
            StateView v = (StateView) mComponentSobMouse;

            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getLayoutX() - t.getSceneX();
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = v.getLayoutY() - t.getSceneY();
        }
    };
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }

            if (!(mComponentSobMouse instanceof StateView)) {
                return;
            }
            State s = ((StateView) mComponentSobMouse).getState();
            s.setLayoutX(t.getSceneX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice);
            s.setLayoutY(t.getSceneY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice);
        }
    };
    private EventHandler<? super MouseEvent> aoLiberarVerticeArrastadoComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_VERTICE && mModoAtual != MODO_NENHUM) {
                return;
            }
            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Adicionar transição
    ////////////////////////////////////////////////////////////////////////////
    private StateView mVerticeOrigemParaAdicionarTransicao;
    private StateView mVerticeDestinoParaAdicionarTransicao;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual != MODO_TRANSICAO) {
                return;
            }

            if (!(mComponentSobMouse instanceof StateView)) {
                return;
            }
            StateView v = (StateView) mComponentSobMouse;

            //guarda o objeto no qual iniciamos o drag            
            mVerticeOrigemParaAdicionarTransicao = v;

            //inicia o drag'n'drop
            Dragboard db = mVerticeOrigemParaAdicionarTransicao.startDragAndDrop(TransferMode.ANY);

            //soh funciona com as três linhas a seguir. Porque? Eu não sei.
            ClipboardContent content = new ClipboardContent();
            content.putString("gambiarra");
            db.setContent(content);

            //indica que este evento foi realizado
            t.consume();
        }
    };
    private EventHandler<DragEvent> aoDetectarPossivelAlvoParaSoltarODrag = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            //a informaçao esta sendo solta sobre o alvo
            //aceita soltar o mouse somente se não é o mesmo nodo de origem 
            //e possui uma string            
            if (event.getGestureSource() != event.getSource()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            View v = getComponentePelaPosicaoMouse(event.getX(), event.getY());
            mVerticeDestinoParaAdicionarTransicao = (v instanceof StateView) ? ((StateView) v) : null;
            event.consume();
        }
    };
    private final EventHandler<DragEvent> aoSoltarMouseSobreVertice = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            if (mModoAtual != MODO_TRANSICAO) {
                return;
            }

            if (mVerticeDestinoParaAdicionarTransicao != null) {
                State o = mVerticeOrigemParaAdicionarTransicao.getState();
                State d = mVerticeDestinoParaAdicionarTransicao.getState();
                mViewer.getComponent().newTransition(o, d);
            }

            event.setDropCompleted(true);
            event.consume();
        }
    };

    public Component getComponent() {
        return mViewer.getComponent();
    }

    public void setComponent(Component component) {
        mViewer.setComponent(component);
    }

    public void setModo(int modo) {
        this.mModoAtual = modo;
        mViewer.setCursor(Cursor.DEFAULT);
    }

    private View getComponentePelaPosicaoMouse(double x, double y) {
        for (State s : mViewer.getComponent().getStates()) {
            View v = (View) s.getValue("view");
            if (v.isInsideBounds(x, y)) {
                return v;
            }
        }
        for (Transition t : mViewer.getComponent().getTransitions()) {
            View v = (View) t.getValue("view");
            if (v.isInsideBounds(x, y)) {
                return v;
            }
        }
        return null;
    }

    private void setComponenteSelecionado(View t) {
        if (mComponentSelecionado != null) {
            removeSelectedStyles(mComponentSelecionado);
        }
        mComponentSelecionado = t;
        if (t != null) {
            applySelectedStyles(mComponentSelecionado);
        }
        for (Listener l : mListeners) {
            l.onSelectionChange(this);
        }
    }

    public View getSelectedView() {
        return mComponentSelecionado;
    }

    private void applySelectedStyles(View v) {
        if (v instanceof StateView) {
            State s = ((StateView) v).getState();
            s.setBorderWidth(2);
            s.setBorderColor("blue");
            s.setTextColor("blue");
            s.setTextSyle(State.TEXTSTYLE_BOLD);
        } else if (v instanceof TransitionView) {
            Transition t = ((TransitionView) v).getTransition();
            t.setWidth(1);
            t.setColor("blue");
            t.setTextColor("blue");
            t.setTextSyle(State.TEXTSTYLE_BOLD);
        }
    }

    private void removeSelectedStyles(View v) {
        if (v instanceof StateView) {
            State s = ((StateView) v).getState();
            s.setBorderWidth(1);
            s.setBorderColor("black");
            s.setTextColor("black");
            s.setTextSyle(State.TEXTSTYLE_NORMAL);
        } else if (v instanceof TransitionView) {
            Transition t = ((TransitionView) v).getTransition();
            t.setWidth(1);
            t.setColor("black");
            t.setTextColor("black");
            t.setTextSyle(State.TEXTSTYLE_NORMAL);
        }

    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

}
