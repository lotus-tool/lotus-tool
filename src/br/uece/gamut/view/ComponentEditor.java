package br.uece.gamut.view;

import br.uece.gamut.model.ComponentModel;
import br.uece.gamut.model.TransitionModel;
import br.uece.gamut.model.StateModel;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 *
 * @author emerson
 */
public class ComponentEditor extends AnchorPane {

    //modos de edição
    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    //propriedades relacionadas ao editor
    public static final String TAG_POS_X = "_x";
    public static final String TAG_POS_Y = "_y";
    public static final String TAG_LABEL = "_label";
    public static final String TAG_DEFAULT = "_default";
    public static final String TAG_PROBABILIDADE = "_probability";
    private int mModoAtual;
    //model
    private ComponentModel mModel;
    private List<View> mComponents = new ArrayList<>();
//    private Map<VerticeModel, VerticeView> mVertices = new HashMap<>();
//    private Map<TransicaoModel, TransicaoView> mTransicoes = new HashMap<>();
    //Reciclagem de objetos        
    private List<StateView> mReciclagemVertices = new ArrayList<>();
    private List<TransitionView> mReciclagemTransicoes = new ArrayList<>();
    //seleção e destaque
    private View mComponentSobMouse;
    private View mComponentSelecionado;

    public interface OnSelectionChange {

        void onSelectionChange(ComponentEditor v);
    }
    private OnSelectionChange mOnSelectionChangeListener;
    private ListChangeListener<StateModel> aoMudarVertices = new ListChangeListener<StateModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends StateModel> c) {
            while (c.next()) {
                for (StateModel model : c.getRemoved()) {
                    removerVertice(model);
                }
                for (StateModel model : c.getAddedSubList()) {
                    adicionarVertice(model);
                }
            }
        }
    };
    private ListChangeListener<TransitionModel> aoMudarTransicoes = new ListChangeListener<TransitionModel>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends TransitionModel> c) {
            while (c.next()) {
                for (TransitionModel model : c.getRemoved()) {
                    removerTransicao(model);
                }
                for (TransitionModel model : c.getAddedSubList()) {
                    adicionarTransicao(model);
                }
            }
        }
    };

    public ComponentEditor() {
        setOnMouseClicked(aoClicarMouse);

        setOnMouseMoved(aoMoverMouse);

        setOnDragDetected(aoDetectarDragSobreVertice);
        setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        setOnDragDropped(aoSoltarMouseSobreVertice);

        setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        setOnMouseDragged(aoArrastarVerticeComOMouse);
        setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);
    }
    ////////////////////////////////////////////////////////////////////////////
    // Ao Clicar o mouse
    ////////////////////////////////////////////////////////////////////////////
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (mModoAtual == MODO_VERTICE) {
                System.out.println(mComponentSobMouse instanceof StateView);
                if (!(mComponentSobMouse instanceof StateView)) {
                    int id = mModel.getVertices().size();
                    StateModel model = new StateModel();
                    model.setID(mModel.getVertices().size());
                    model.setValue(TAG_POS_X, String.valueOf(t.getX()));
                    model.setValue(TAG_POS_Y, String.valueOf(t.getY()));
                    model.setValue(TAG_LABEL, String.valueOf(id));
                    model.setValue(TAG_DEFAULT, String.valueOf(id == 1));
                    mModel.add(model);
                }
            } else if (mModoAtual == MODO_REMOVER) {
                if (mComponentSobMouse instanceof StateView) {
                    StateModel model = ((StateView) mComponentSobMouse).getModel();
                    mModel.remove(model);
                } else if (mComponentSobMouse instanceof TransitionView) {
                    TransitionModel model = ((TransitionView) mComponentSobMouse).getModel();
                    mModel.remove(model);
                }
            } else if (mModoAtual == MODO_NENHUM) {
                System.out.println(mComponentSobMouse);
                setComponenteSelecionado(mComponentSobMouse);
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
//            System.out.println(aux);
//            if (aux == null) {
//                return;
//            }

            if (mComponentSobMouse != null) {
                mComponentSobMouse.setDestacado(false);
            }
            if (aux != null) {
                aux.setDestacado(true);
            }
            mComponentSobMouse = aux;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Mover vertice 
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
            StateView v = (StateView) mComponentSobMouse;

            v.setLayoutX(t.getSceneX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice);
            v.setLayoutY(t.getSceneY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice);
            
            StateModel m = v.getModel();
            m.setValue(ComponentEditor.TAG_POS_X, String.valueOf(v.getLayoutX()));
            m.setValue(ComponentEditor.TAG_POS_Y, String.valueOf(v.getLayoutY()));
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
    private EventHandler<DragEvent> aoSoltarMouseSobreVertice = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            if (mModoAtual != MODO_TRANSICAO) {
                return;
            }

            if (mVerticeDestinoParaAdicionarTransicao != null) {
                StateModel o = mVerticeOrigemParaAdicionarTransicao.getModel();
                StateModel d = mVerticeDestinoParaAdicionarTransicao.getModel();
                mModel.newTransicao(o, d);
            }

            event.setDropCompleted(true);
            event.consume();
        }
    };

    public ComponentModel getModel() {
        return mModel;
    }

    public void setModel(ComponentModel m) {
        if (mModel != null) {
            mModel.getVertices().removeListener(aoMudarVertices);
            mModel.getTransicoes().removeListener(aoMudarTransicoes);
        }
        clear();
        if (m != null) {
            m.getVertices().addListener(aoMudarVertices);
            m.getTransicoes().addListener(aoMudarTransicoes);
        }
        mModel = m;
        if (mModel != null) {
            for (StateModel vm : mModel.getVertices()) {
                adicionarVertice(vm);
            }
            for (TransitionModel tm : mModel.getTransicoes()) {
                adicionarTransicao(tm);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Remover
    ////////////////////////////////////////////////////////////////////////////
    private void removerVertice(StateModel model) {
        ObservableList<Node> aux = getChildren();
        StateView view = (StateView) model.getTag();

        List<TransitionModel> transicoes = view.getModel().getTransicoesEntrada();
        for (TransitionModel tm : transicoes) {
            TransitionView tv = (TransitionView) tm.getTag();
            mComponents.remove(tv);
            tv.setModel(null);
            aux.remove(tv);
            mReciclagemTransicoes.add(tv);
        }

        transicoes = view.getModel().getTransicoesSaida();
        for (TransitionModel tm : transicoes) {
            TransitionView tv = (TransitionView) tm.getTag();
            mComponents.remove(tv);
            tv.setModel(null);
            aux.remove(tv);
            mReciclagemTransicoes.add(tv);
        }

        aux.remove(view);
        mComponents.remove(view);
        view.setModel(null);
        recalcularVerticesECor();
        mReciclagemVertices.add(view);
    }

    private void removerTransicao(TransitionModel model) {
        TransitionView view = (TransitionView) model.getTag();
        mComponents.remove(view);
        getChildren().remove(view);
        mReciclagemTransicoes.add(view);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Adicionar
    ////////////////////////////////////////////////////////////////////////////    
    public void adicionarVertice(StateModel model) {
        StateView view = mReciclagemVertices.isEmpty() ? new StateView() : mReciclagemVertices.remove(0);
        view.setModel(model);
        model.setTag(view);
        mComponents.add(view);
        getChildren().add(view);
    }

    public void adicionarTransicao(TransitionModel model) {
        TransitionView view = mReciclagemTransicoes.isEmpty() ? new TransitionView() : mReciclagemTransicoes.remove(0);
        view.setModel(model);
        model.setTag(view);
        mComponents.add(view);
        getChildren().add(view);
        view.toBack();
    }

    public void setModo(int modo) {
        this.mModoAtual = modo;
        setCursor(Cursor.DEFAULT);
    }

    private void recalcularVerticesECor() {
        int i = 0;
        for (StateModel v : mModel.getVertices()) {
            v.setValue(TAG_LABEL, String.valueOf(i));
            v.setValue(TAG_DEFAULT, String.valueOf(i == 1));
            i++;
        }
    }

    public void clear() {
        if (mModel == null) {
            return;
        }
        ObservableList<Node> aux = getChildren();
        for (StateModel m : mModel.getVertices()) {
            StateView v = (StateView) m.getTag();
            m.setTag(null);
            aux.remove(v);
            mReciclagemVertices.add(v);
        }

        for (TransitionModel m : mModel.getTransicoes()) {
            TransitionView v = (TransitionView) m.getTag();
            m.setTag(null);
            aux.remove(v);
            mReciclagemTransicoes.add(v);
        }

        mModel = null;
    }

    public void layoutGrafo() {
        int i = 1;
        for (StateModel v : mModel.getVertices()) {
            v.setValue(TAG_DEFAULT, String.valueOf(i == 1 ? Color.RED : Color.CYAN));
            v.setValue(TAG_POS_X, String.valueOf(i * 100.0));
            v.setValue(TAG_POS_Y, String.valueOf(300.0));
            i++;
        }
    }

    private View getComponentePelaPosicaoMouse(double x, double y) {
        for (View c : mComponents) {
            if (c.pontoPertenceAoObjeto(x, y)) {
                return c;
            }
        }
        return null;
    }

    private void setComponenteSelecionado(View t) {
        if (mComponentSelecionado != null) {
            mComponentSelecionado.setSelecionado(false);
        }
        mComponentSelecionado = t;
        if (t != null) {
            t.setSelecionado(true);
        }
        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onSelectionChange(this);
        }
    }

    public void setOnSelectionChange(OnSelectionChange listener) {
        mOnSelectionChangeListener = listener;
    }

    public View getSelectedView() {
        return mComponentSelecionado;
    }
}
