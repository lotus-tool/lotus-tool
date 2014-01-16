package br.uece.gamut.app.editor;

import br.uece.gamut.Grafo;
import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;

/**
 *
 * @author emerson
 */
public class GrafoEditor extends Region implements Grafo {

    public static final int MODO_NENHUM = 0;
    public static final int MODO_VERTICE = 1;
    public static final int MODO_TRANSICAO = 2;
    public static final int MODO_REMOVER = 3;
    public static final String TAG_POS_X = "_x";
    public static final String TAG_POS_Y = "_y";
    public static final String TAG_LABEL = "label";
    public static final String TAG_COR = "_cor";
    public static final String TAG_DEFAULT = "default";
    public static final String TAG_PROBABILIDADE = "probability";

    private int modo;
    private int contadorVertices;
    private List<Vertice> vertices = new ArrayList<>();
    private List<Transicao> transicoes = new ArrayList<>();
    private VerticeView mVerticeSobMouse;
    private TransicaoView mTransicaoSobMouse;
    private VerticeView mVerticeSelecionado;
    private TransicaoView mTransicaoSelecionada;

    private static class DistanciaElipse extends DoubleBinding {

        public DoubleProperty xi;
        public DoubleProperty yi;
        public static final int COMPONENTE_X = 0;
        public static final int COMPONENTE_Y = 1;
        public int componente;

        public DistanciaElipse(DoubleProperty xi, DoubleProperty yi, int componente) {
            super.bind(xi, yi);
            this.xi = xi;
            this.yi = yi;
            this.componente = componente;
        }

        @Override
        protected double computeValue() {
            if (componente == COMPONENTE_X) {
                return xi.getValue() - 25;
            } else {
                return yi.getValue();
            }

        }
    }

    private static class DistanciaSetaElipse extends DoubleBinding {

        public static final int COMPONENTE_X = 0;
        public static final int COMPONENTE_Y = 1;
        public DoubleProperty xi;
        public DoubleProperty yi;
        public int componente;

        public DistanciaSetaElipse(DoubleProperty xi, DoubleProperty yi, int componente) {
            super.bind(xi, yi);
            this.xi = xi;
            this.yi = yi;
            this.componente = componente;
        }

        @Override
        protected double computeValue() {
            if (componente == COMPONENTE_X) {
                return xi.getValue() - 55  ;
            }else{
                return yi.getValue() - 6;
            }
        }
    }

    public interface OnSelectionChange {

        int NOTHING = 0;
        int VERTICE = 1;
        int TRANSICAO = 2;

        void onOnSelectionChange(GrafoEditor v, int objectKind);
    }
    private OnSelectionChange mOnSelectionChangeListener;

    public GrafoEditor() {
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
            if (modo == MODO_VERTICE) {
                if (mVerticeSobMouse == null) {
                    adicionarNovoVerticePelaPosicaoMouse(t);
                }
            } else if (modo == MODO_REMOVER) {
                if (mVerticeSobMouse != null) {
                    removerVertice(mVerticeSobMouse);
                } else if (mTransicaoSobMouse != null) {
                    removerTransicao(mTransicaoSobMouse);
                }
            } else if (modo == MODO_NENHUM) {
                System.out.println(mVerticeSobMouse);
                if (mVerticeSobMouse != null) {
                    setVerticeSelecionado(mVerticeSobMouse);
                    setTransicaoSelecionado(null);
                } else {
                    setVerticeSelecionado(null);
                    setTransicaoSelecionado(mTransicaoSobMouse);
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
            //TODO: Ajustar o cursor de acordo com a ação
            boolean hahVerticeSobMouse = mVerticeSobMouse != null;
            if (hahVerticeSobMouse) {
                mVerticeSobMouse.setDestacado(false);
            }
            mVerticeSobMouse = getVerticePelaPosicaoMouse(t.getX(), t.getY());
            if (mVerticeSobMouse != null) {
                mVerticeSobMouse.setDestacado(true);
            } else {
                boolean hahTransicaoSobMouse = mTransicaoSobMouse != null;
                if (hahTransicaoSobMouse) {
                    mTransicaoSobMouse.setDestacado(false);
                }
                mTransicaoSobMouse = getTransicaoPelaPosicaoMouse(t.getX(), t.getY());
                if (mTransicaoSobMouse != null) {
                    mTransicaoSobMouse.setDestacado(true);
                }
            }
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
            if (modo != MODO_VERTICE && modo != MODO_NENHUM) {
                return;
            }

            if (mVerticeSobMouse == null) {
                return;
            }

            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = mVerticeSobMouse.getLayoutX() - t.getSceneX();
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = mVerticeSobMouse.getLayoutY() - t.getSceneY();
        }
    };
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_VERTICE && modo != MODO_NENHUM) {
                return;
            }

            if (mVerticeSobMouse == null) {
                return;
            }

            mVerticeSobMouse.setLayoutX(t.getSceneX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice);
            mVerticeSobMouse.setLayoutY(t.getSceneY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice);
        }
    };
    private EventHandler<? super MouseEvent> aoLiberarVerticeArrastadoComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_VERTICE && modo != MODO_NENHUM) {
                return;
            }
            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Adicionar transição
    ////////////////////////////////////////////////////////////////////////////
    private VerticeView mVerticeOrigemParaAdicionarTransicao;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_TRANSICAO) {
                return;
            }

            if (mVerticeSobMouse == null) {
                return;
            }

            //guarda o objeto no qual iniciamos o drag            
            mVerticeOrigemParaAdicionarTransicao = mVerticeSobMouse;

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
            event.consume();
        }
    };
    private EventHandler<DragEvent> aoSoltarMouseSobreVertice = new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            if (modo != MODO_TRANSICAO) {
                return;
            }
            VerticeView destino = getVerticePelaPosicaoMouse(event.getX(), event.getY());
            if (destino.getID() == mVerticeOrigemParaAdicionarTransicao.getID()) {
                Ellipse loop = new Ellipse();
                loop.setRadiusX(25);
                loop.setRadiusY(20);
                loop.setFill(null);
                loop.setStroke(Color.BLACK);
                loop.layoutXProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
                loop.layoutYProperty().bind(new DistanciaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));
                Polygon mSeta = new Polygon(new double[]{
                    5.0, 0.0,
                    10.0, 10.0,
                    0.0, 10.0
                });
                mSeta.layoutXProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 0));
                mSeta.layoutYProperty().bind(new DistanciaSetaElipse(destino.layoutXProperty(), destino.layoutYProperty(), 1));
               
                getChildren().add(mSeta);
                getChildren().add(loop);
                getChildren().remove(destino);
                getChildren().add(destino);

            } else {
                newTransicao(mVerticeOrigemParaAdicionarTransicao.getID(), destino.getID());
                event.setDropCompleted(true);
                event.consume();
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    // Remover vertices
    ////////////////////////////////////////////////////////////////////////////
    private void removerVertice(VerticeView v) {
        ObservableList<Node> aux = getChildren();
        for (Transicao t : v.getTransicoesSaida()) {
            TransicaoView tt = (TransicaoView) t;
            aux.remove(tt);
            transicoes.remove(t);
        }
        for (Transicao t : v.getTransicoesEntrada()) {
            TransicaoView tt = (TransicaoView) t;
            aux.remove(tt);
            transicoes.remove(t);
        }
        aux.remove(v);
        vertices.remove(v);
        recalcularVerticesECor();
    }

    private void removerTransicao(TransicaoView t) {
        t.getOrigem().getTransicoesSaida().remove(t);
        t.getDestino().getTransicoesEntrada().remove(t);
        transicoes.remove(t);
        getChildren().remove(t);
    }

    private void adicionarNovoVerticePelaPosicaoMouse(MouseEvent t) {
        VerticeView v = (VerticeView) newVertice(contadorVertices);
        v.setTag(TAG_POS_X, t.getX());
        v.setTag(TAG_POS_Y, t.getY());
        v.setTag(TAG_LABEL, "" + contadorVertices);
        v.setTag(TAG_DEFAULT, contadorVertices == 0);
        contadorVertices++;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public Grafo getGrafo() {
        return this;
    }

    private void recalcularVerticesECor() {
        contadorVertices = 0;
        for (Vertice v : vertices) {
            VerticeView vertice = (VerticeView) v;
            vertice.setTag(TAG_LABEL, contadorVertices + "");
            vertice.setTag(TAG_DEFAULT, contadorVertices == 0);
            contadorVertices++;
        }
    }

    public void clear() {
        getChildren().clear();
        contadorVertices = 0;
        vertices.clear();
        transicoes.clear();
    }

    @Override
    public List<Vertice> getVertices() {
        return vertices;
    }

    @Override
    public List<Transicao> getTransicoes() {
        return transicoes;
    }

    @Override
    public Vertice getVertice(int id) {
        for (Vertice v : vertices) {
            if (v.getID() == id) {
                return v;
            }
        }
        return null;
    }

    @Override
    public Vertice newVertice(int id) {
        System.out.println("Criando um novo vertice com id " + id);
        VerticeView v = new VerticeView(id);
        vertices.add(v);
        getChildren().add(v);
        return v;
    }

    @Override
    public Transicao newTransicao(int idOrigem, int idDestino) {
        System.out.println("Criando nova transicao de " + idOrigem + " para " + idDestino);
        VerticeView o = (VerticeView) getVertice(idOrigem);
        VerticeView d = (VerticeView) getVertice(idDestino);
        TransicaoView t = new TransicaoView(o, d);
        o.addTransicaoSaida(t);
        d.addTransicaoEntrada(t);
        transicoes.add(t);
        getChildren().add(t);
        t.toBack();
        return t;
    }

    public void layoutGrafo() {
        int i = 1;
        for (Vertice v : vertices) {
            v.setTag(TAG_COR, i == 1 ? Color.RED : Color.CYAN);
            v.setTag(TAG_POS_X, i * 100.0);
            v.setTag(TAG_POS_Y, 300.0);
            i++;
        }
    }

    private TransicaoView getTransicaoPelaPosicaoMouse(double x, double y) {
        for (Transicao tt : transicoes) {
            TransicaoView tv = (TransicaoView) tt;
            if (tv.pontoPertenceAoObjeto(x, y)) {
                //System.out.println("transicao selecionada: " + tv);
                return tv;
            }
        }
        return null;
    }

    private VerticeView getVerticePelaPosicaoMouse(double x, double y) {
        for (int i = vertices.size() - 1; i >= 0; i--) {
            VerticeView vv = (VerticeView) vertices.get(i);
            if (vv.pontoPertenceAoObjeto(x, y)) {
                //System.out.println("vertice selecionado: " + vv);
                return vv;
            }
        }
        return null;
    }

    private void setVerticeSelecionado(VerticeView v) {
        if (mVerticeSelecionado != null) {
            mVerticeSelecionado.setSelecionado(false);
        }
        mVerticeSelecionado = v;
        if (v != null) {
            v.setSelecionado(true);
        }
        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onOnSelectionChange(this, v == null ? OnSelectionChange.NOTHING : OnSelectionChange.VERTICE);
        }
    }

    private void setTransicaoSelecionado(TransicaoView t) {
        if (mTransicaoSelecionada != null) {
            mTransicaoSelecionada.setSelecionado(false);
        }
        mTransicaoSelecionada = t;
        if (t != null) {
            t.setSelecionado(true);
        }
        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onOnSelectionChange(this, t == null ? OnSelectionChange.NOTHING : OnSelectionChange.TRANSICAO);
        }
    }

    public void setOnSelectionChange(OnSelectionChange listener) {
        mOnSelectionChangeListener = listener;
    }

    public Vertice getVerticeSelecionado() {
        return mVerticeSelecionado;
    }

    public Transicao getTransicaoSelecionada() {
        return mTransicaoSelecionada;
    }

}
