package br.uece.gamut.app.editor;

import br.uece.gamut.Grafo;
import br.uece.gamut.Transicao;
import br.uece.gamut.Vertice;
import br.uece.gamut.app.MainSceneController;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_LIGACAO;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_VERTICE;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_MOVER_VERTICE;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_REMOVER_VERTICE;
import static br.uece.gamut.app.editor.GrafoEditor.TAG_COR;
import static br.uece.gamut.app.editor.GrafoEditor.TAG_POS_X;
import static br.uece.gamut.app.editor.GrafoEditor.TAG_POS_Y;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 *
 * @author emerson
 */
public class GrafoEditor extends Region implements Grafo {

    public static final int MODO_NENHUM = 0;//click
    public static final int MODO_MOVER_VERTICE = 1; //drag'n'drop
    public static final int MODO_ADICIONAR_VERTICE = 2;//click
    public static final int MODO_ADICIONAR_LIGACAO = 3;//drag'n'drop
    public static final int MODO_SELECIONAR_VERTICE = 4;//click
    public static final int MODO_SELECIONAR_LIGACAO = 5;//click
    public static final int MODO_REMOVER_VERTICE = 6;//click
    public static final int MODO_REMOVER_LIGACAO = 7;//click
    public static final String TAG_POS_X = "_x";
    public static final String TAG_POS_Y = "_y";
    public static final String TAG_LABEL = "_label";
    public static final String TAG_COR = "_cor";
    public static final String TAG_DEFAULT = "_default";
    //private GrafoView view;
    private int modo;
    private int contadorVertices;
    private List<Vertice> vertices = new ArrayList<>();
    private List<Transicao> transicoes = new ArrayList<>();
    private Object mObjetoAtualmenteSelecionado;
    private Object mObjetoSelecionado;

    public GrafoEditor() {
        setOnMouseClicked(aoClicarMouse);
        setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                mObjetoSelecionado = getVerticeOuTransicaoPelaPosicaoMouse(t.getX(), t.getY());
                mObjetoAtualmenteSelecionado = null;
                // setObjetoNasPropriedades(MainSceneController.tbPropriedades);
            }
        });
        setOnDragDetected(aoDetectarDragSobreVertice);
        setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        setOnDragDropped(aoSoltarMouseSobreVertice);

        //eventos para tratar da movimentação
        setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        setOnMouseDragged(aoArrastarVerticeComOMouse);
        setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);
    }
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo == MODO_ADICIONAR_VERTICE) {
                adicionarNovoVerticePeloCliqueMouse(t);
            } else if (modo == MODO_REMOVER_VERTICE) {
                VerticeView v = (VerticeView) procurarVertice(t);
                removerVertice(v);
            } else if (modo != MODO_ADICIONAR_LIGACAO) {
                for (Transicao tt : transicoes) {
                    TransicaoView tv = (TransicaoView) tt;
                    if (tv.pontoPertenceAoObjeto(t.getX(), t.getY())) {
                        mObjetoSelecionado = tv;
                        System.out.println("transicao selecionada: " + tv);
                        setObjetoNasPropriedades(MainSceneController.tbPropriedades);
                        return;
                    }
                    mObjetoSelecionado = null;

                    setObjetoNasPropriedades(MainSceneController.tbPropriedades);
                    //Default
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
            if (modo != MODO_MOVER_VERTICE) {
                return;
            }
            VerticeView vertice = getVerticePelaPosicaoMouse(t.getX(), t.getY());
            if (vertice == null) return;
            
            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = vertice.getLayoutX() - t.getSceneX();
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = vertice.getLayoutY() - t.getSceneY();
        }
    };
    private EventHandler<? super MouseEvent> aoArrastarVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_MOVER_VERTICE) {
                return;
            }
            VerticeView vertice = getVerticePelaPosicaoMouse(t.getX(), t.getY());
            if (vertice == null) return;
            
            vertice.setLayoutX(t.getSceneX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice);
            vertice.setLayoutY(t.getSceneY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice);
        }
    };
    private EventHandler<? super MouseEvent> aoLiberarVerticeArrastadoComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_MOVER_VERTICE) {
                return;
            }            
            variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
            variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice = 0;
        }
    };
    ////////////////////////////////////////////////////////////////////////////
    // Adicionar transição
    ////////////////////////////////////////////////////////////////////////////
    private VerticeView verticeOrigemParaAdicionarTransicao;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_ADICIONAR_LIGACAO) {
                return;
            }

            //guarda o objeto no qual iniciamos o drag
            //verticeOrigemParaAdicionarTransicao = (VerticeView) t.getSource();
            verticeOrigemParaAdicionarTransicao = getVerticePelaPosicaoMouse(t.getX(), t.getY());

            //inicia o drag'n'drop
            Dragboard db = verticeOrigemParaAdicionarTransicao.startDragAndDrop(TransferMode.ANY);

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
            if (modo != MODO_ADICIONAR_LIGACAO) {
                return;
            }

            VerticeView destino = getVerticePelaPosicaoMouse(event.getX(), event.getY());//(VerticeView) event.getSource();

            newTransicao(verticeOrigemParaAdicionarTransicao.getID(), destino.getID());
            event.setDropCompleted(true);

            event.consume();
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    // Remover vertices
    ////////////////////////////////////////////////////////////////////////////
    private void removerVertice(VerticeView v) {
        removerVerticeView(v);
        recalcularVerticesECor();
    }

    //Usar apenas no remover vertice e/ou ligação
    private VerticeView procurarVertice(MouseEvent t) {
        VerticeView verticeProcurado = null;
        List<Vertice> lista = vertices;

        for (Vertice v : lista) {
            VerticeView vv = (VerticeView) v;

            if (t.getX() >= vv.getLayoutX() && t.getX() <= vv.getLayoutX() + 20) {
                if (t.getY() >= vv.getLayoutY() && t.getY() <= vv.getLayoutY() + 20) {
                    verticeProcurado = vv;
                } else {
                    if (t.getY() <= vv.getLayoutY() && t.getY() >= vv.getLayoutY() - 20) {
                        verticeProcurado = vv;
                    }
                }
            } else {
                if (t.getX() <= vv.getLayoutX() && t.getX() >= vv.getLayoutX() - 20) {
                    if (t.getY() >= vv.getLayoutY() && t.getY() <= vv.getLayoutY() + 20) {
                        verticeProcurado = vv;
                    } else {
                        if (t.getY() <= vv.getLayoutY() && t.getY() >= vv.getLayoutY() - 20) {
                            verticeProcurado = vv;
                        }
                    }
                }
            }
        }
        return verticeProcurado;
    }

    public void adicionarNovoVerticePeloCliqueMouse(MouseEvent t) {
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

    private void removerVerticeView(VerticeView v) {
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

        //adiciona eventos para tratar a adição de ligações view
//        v.setOnDragDetected(aoDetectarDragSobreVertice);
//        v.setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
//        v.setOnDragDropped(aoSoltarMouseSobreVertice);

        //eventos para tratar da movimentação
//        v.setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
//        v.setOnMouseDragged(aoArrastarVerticeComOMouse);
//        v.setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);

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

    // SETAR AS PROPRIEDADES DA TRANSIÇÃO
    public void setObjetoNasPropriedades(ToolBar tbPropriedades) {
        if (mObjetoSelecionado == null) {
            tbPropriedades.setVisible(false);

        } else {
            tbPropriedades.setVisible(true);
            final TransicaoView tv = (TransicaoView) mObjetoSelecionado;

            MainSceneController.setPropNome(tv.getTxtRotulo().getText());

            MainSceneController.tfNomeTransicao.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    tv.setTxtRotulo(MainSceneController.tfNomeTransicao.getText());

                }
            });
            /*MainSceneController.tfPropProb.setOnAction(new EventHandler<ActionEvent>() {

             @Override
             public void handle(ActionEvent t) {
                
             }
             });*/

        }
    }

    private Object getVerticeOuTransicaoPelaPosicaoMouse(double x, double y) {
        Object obj = getVerticePelaPosicaoMouse(x, y);
        if (obj != null) {
            return obj;
        }
        return getTransicaoPelaPosicaoMouse(x, y);
    }

    private TransicaoView getTransicaoPelaPosicaoMouse(double x, double y) {
        for (Transicao tt : transicoes) {
            TransicaoView tv = (TransicaoView) tt;
            if (tv.pontoPertenceAoObjeto(x, y)) {
                mObjetoAtualmenteSelecionado = tv;
                System.out.println("transicao selecionada: " + tv);
                return tv;
            }
        }
        return null;
    }

    private VerticeView getVerticePelaPosicaoMouse(double x, double y) {
        for (int i = vertices.size() - 1; i >= 0; i--) {
            VerticeView vv = (VerticeView) vertices.get(i);
            if (vv.pontoPertenceAoObjeto(x, y)) {
                System.out.println("vertice selecionado: " + vv);
                return vv;
            }
        }
        return null;
    }
}
