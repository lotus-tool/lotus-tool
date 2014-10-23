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
import br.uece.lotus.viewer.TransitionViewFactory;
import br.uece.lotus.viewer.View;
import br.uece.seed.app.ExtensibleFXToolbar;
import br.uece.seed.app.ExtensibleToolbar;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author emerson
 */
public class DesignerWindowImpl extends AnchorPane implements DesignerWindow {

    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    private final BasicComponentViewer mViewer;
    private final ToolBar mToolbar;
    private final ToggleGroup mToggleGroup;
    private final ToggleButton mBtnArrow;
    private final ToggleButton mBtnState;
    private final ToggleButton mBtnTransitionLine;
    private final ToggleButton mBtnTransitionArc;
    private final ToggleButton mBtnEraser;

    private final ToolBar mStateToolbar;
    private final ToolBar mTransitionToolbar;
    private final ExtensibleToolbar mExtensibleStateToolbar;
    private final ExtensibleToolbar mExtensibleTransitionToolbar;

    private final ScrollPane mScrollPanel;
    private boolean mExibirPropriedadesTransicao;

    private BasicComponentViewer.Listener mViewerListener = new BasicComponentViewer.Listener() {
        @Override
        public void onTransitionViewCreated(BasicComponentViewer v, TransitionView tv) {
            if (mExibirPropriedadesTransicao) {
                setComponenteSelecionado(tv);
            }
            mExibirPropriedadesTransicao = false;
        }
    };
    private String mDefaultTransitionColor;
    private String mDefaultTransitionTextColor;
    private Integer mDefaultTransitionWidth;
    private String mDefaultTransitionLabel;    
    private EventHandler<ActionEvent> mSetStateAsInitial = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setAsInitial();
        }
    };
    private EventHandler<ActionEvent> mSetStateAsNormal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setFinal(false);
            s.setError(false);
        }
    };
    private EventHandler<ActionEvent> mSetStateAsError = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setError(true);
        }
    };
    private EventHandler<ActionEvent> mSetStateAsFinal = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (mComponentSelecionado == null) {
                return;
            }
            State s = ((StateView) mComponentSelecionado).getState();
            s.setFinal(true);
        }
    };
    private int mTransitionViewType;

    @Override
    public ExtensibleToolbar getTransitionContextToolbar() {
        return mExtensibleTransitionToolbar;
    }

    @Override
    public ExtensibleToolbar getStateContextToolbar() {
        return mExtensibleStateToolbar;
    }

    @Override
    public void setDefaultTransitionLabel(String label) {
        mDefaultTransitionLabel = label;
    }

    @Override
    public void setDefaultTransitionWidth(Integer width) {
        mDefaultTransitionWidth = width;
    }

    @Override
    public void setDefaultTransitionTextColor(String color) {
        mDefaultTransitionTextColor = color;
    }

    @Override
    public void setDefaultTransitionColor(String color) {
        mDefaultTransitionColor = color;
    }

    @Override
    public String getTitle() {
        Component c = mViewer.getComponent();
        return c.getName();
    }

    @Override
    public Node getNode() {
        return this;
    }

    public interface Listener {

        void onSelectionChange(DesignerWindowImpl v);
    }

    private int mModoAtual;
    private final ContextMenu mComponentContextMenu;
    //seleção e destaque
    private View mComponentSobMouse;
    private View mComponentSelecionado;
    private final List<Listener> mListeners = new ArrayList<>();

    public DesignerWindowImpl(BasicComponentViewer viewer) {
        mToolbar = new ToolBar();
        mToggleGroup = new ToggleGroup();
        mBtnArrow = new ToggleButton();
        mBtnArrow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_arrow.png"))));
        mBtnArrow.setOnAction((ActionEvent e) -> {
            setModo(MODO_NENHUM);
        });
        mBtnArrow.setToggleGroup(mToggleGroup);
        mBtnState = new ToggleButton();
        mBtnState.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_state.png"))));
        mBtnState.setOnAction((ActionEvent e) -> {
            setModo(MODO_VERTICE);
        });
        mBtnState.setToggleGroup(mToggleGroup);
        mBtnTransitionLine = new ToggleButton();
        mBtnTransitionLine.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_transition_line.png"))));
        mBtnTransitionLine.setOnAction((ActionEvent e) -> {
            mTransitionViewType = TransitionViewFactory.Type.LINEAR;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionLine.setToggleGroup(mToggleGroup);
        
        mBtnTransitionArc = new ToggleButton();
        mBtnTransitionArc.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_transition_semicircle.png"))));
        mBtnTransitionArc.setOnAction((ActionEvent e) -> {
            mTransitionViewType = TransitionViewFactory.Type.SEMI_CIRCLE;
            setModo(MODO_TRANSICAO);
        });
        mBtnTransitionArc.setToggleGroup(mToggleGroup);
        
        mBtnEraser = new ToggleButton();
        mBtnEraser.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ic_eraser.png"))));
        mBtnEraser.setOnAction((ActionEvent e) -> {
            setModo(MODO_REMOVER);
        });
        mBtnEraser.setToggleGroup(mToggleGroup);

        mToolbar.getItems().addAll(mBtnArrow, mBtnState, mBtnTransitionLine, mBtnTransitionArc, mBtnEraser);

        mStateToolbar = new ToolBar();
        mStateToolbar.setVisible(false);
        mTransitionToolbar = new ToolBar();
        mTransitionToolbar.setVisible(false);
//        getChildren().add(mStateToolbar);
//        getChildren().add(mTransitionToolbar);
        mExtensibleStateToolbar = new ExtensibleFXToolbar(mStateToolbar);
        mExtensibleTransitionToolbar = new ExtensibleFXToolbar(mTransitionToolbar);

        HBox aux = new HBox(mToolbar, mStateToolbar, mTransitionToolbar);
        getChildren().add(aux);
        AnchorPane.setTopAnchor(aux, 0D);
        AnchorPane.setLeftAnchor(aux, 0D);
        AnchorPane.setRightAnchor(aux, 0D);

        mComponentContextMenu = new ContextMenu();

        mViewer = viewer;
        mViewer.addListener(mViewerListener);
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
        mSetAsInitialMenuItem.setOnAction(mSetStateAsInitial);
        MenuItem mSetAsNormalMenuItem = new MenuItem("Set as normal");
        mSetAsNormalMenuItem.setOnAction(mSetStateAsNormal);
        MenuItem mSetAsFinalMenuItem = new MenuItem("Set as error");
        mSetAsFinalMenuItem.setOnAction(mSetStateAsError);
        MenuItem mSetAsErrorMenuItem = new MenuItem("Set as final");
        mSetAsErrorMenuItem.setOnAction(mSetStateAsFinal);
        
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
        mComponentContextMenu.getItems().addAll(mSetAsInitialMenuItem, new SeparatorMenuItem(), mSetAsNormalMenuItem, mSetAsFinalMenuItem, mSetAsErrorMenuItem, new SeparatorMenuItem(), mSaveAsPNG);

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
                        State s = mViewer.getComponent().newState(id);
                        s.setLayoutX(e.getX());
                        s.setLayoutY(e.getY());
                        s.setLabel(String.valueOf(id));
                        if (mViewer.getComponent().getStatesCount() == 0) {
                            mViewer.getComponent().setInitialState(s);
                        }
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
                mExibirPropriedadesTransicao = true;
                Transition t = mViewer.getComponent().buildTransition(o, d)
                    .setValue("view.type", mTransitionViewType)
                    .create();
                applyDefaults(t);
            }

            event.setDropCompleted(true);
            event.consume();
        }

        private void applyDefaults(Transition t) {
            if (mDefaultTransitionLabel != null) {
                t.setLabel(mDefaultTransitionLabel);
            }
            if (mDefaultTransitionColor != null) {
                t.setColor(mDefaultTransitionColor);
            }
            if (mDefaultTransitionTextColor != null) {
                t.setTextColor(mDefaultTransitionTextColor);
            }
            if (mDefaultTransitionWidth != null) {
                t.setWidth(mDefaultTransitionWidth);
            }
        }

    };

    @Override
    public Component getComponent() {
        return mViewer.getComponent();
    }

    @Override
    public void setComponent(Component component) {
        mViewer.setComponent(component);
    }

    public void setModo(int modo) {
        this.mModoAtual = modo;
        mViewer.setCursor(Cursor.DEFAULT);
        mStateToolbar.setVisible(mModoAtual == MODO_VERTICE);
        mTransitionToolbar.setVisible(mModoAtual == MODO_TRANSICAO);
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
            if (t == null) {
                return;
            }
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
