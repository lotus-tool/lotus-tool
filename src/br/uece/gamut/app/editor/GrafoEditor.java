package br.uece.gamut.app.editor;

import br.uece.gamut.Vertice;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author emerson
 */
public class GrafoEditor {

    public static final int MODO_NENHUM = 0;//click
    public static final int MODO_MOVER_VERTICE = 1; //drag'n'drop
    public static final int MODO_ADICIONAR_VERTICE = 2;//click
    public static final int MODO_ADICIONAR_LIGACAO = 3;//drag'n'drop
    public static final int MODO_SELECIONAR_VERTICE = 4;//click
    public static final int MODO_SELECIONAR_LIGACAO = 5;//click
    public static final int MODO_REMOVER_VERTICE = 6;//click
    public static final int MODO_REMOVER_LIGACAO = 7;//click
    private int modo;
    private GrafoView view;
    private int contador;
    private EventHandler<? super MouseEvent> aoClicarMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            switch (modo) {
                case MODO_ADICIONAR_VERTICE:
                    adicionarVertice(t);
                    break;
                case MODO_NENHUM:
                    //Default
                    break;
                case MODO_REMOVER_LIGACAO:
                    //TODO
                    break;
                case MODO_REMOVER_VERTICE:
                    Vertice v = (Vertice) procurarVertice(t);
                    removerVertice(v);
                    break;

            }
        }
    };
    /**
     ******************
     * Mover vertice *****************
     */
    private double variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice;
    private double variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice;
    private EventHandler<? super MouseEvent> aoIniciarArrastoVerticeComOMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_MOVER_VERTICE) {
                return;
            }
            VerticeView vertice = (VerticeView) t.getSource();
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
            VerticeView v = (VerticeView) t.getSource();
            v.setLayoutX(t.getSceneX() + variacaoXCliqueMouseComOCantoSuperiorEsquerdoVertice);
            v.setLayoutY(t.getSceneY() + variacaoYCliqueMouseComOCantoSuperiorEsquerdoVertice);
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
    /**
     ***********************
     * Adicionar transição * **********************
     */
    private VerticeView verticeOrigemParaAdicionarTransicao;
    private EventHandler<MouseEvent> aoDetectarDragSobreVertice = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (modo != MODO_ADICIONAR_LIGACAO) {
                return;
            }

            //guarda o objeto no qual iniciamos o drag
            verticeOrigemParaAdicionarTransicao = (VerticeView) t.getSource();

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
        public void handle(DragEvent event) {
            if (modo != MODO_ADICIONAR_LIGACAO) {
                return;
            }

            VerticeView destino = (VerticeView) event.getSource();
            System.out.println(verticeOrigemParaAdicionarTransicao + " -> " + destino);
            view.adicionarTransicao(verticeOrigemParaAdicionarTransicao, destino);
            event.setDropCompleted(true);

            event.consume();
        }
    };

    private void removerVertice(Vertice v) {
        //v.removerLigacoes(); - IMPLEMENTAR
        view.removerVerticeView(v);
        recalcularVerticesECor();
    }

    //Usar apenas no remover vertice e/ou ligação
    private VerticeView procurarVertice(MouseEvent t) {
        VerticeView verticeProcurado = null;
        List<Vertice> lista = view.getVertices();

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

    private void adicionarVertice(MouseEvent t) {
        if (view.getVertices().isEmpty()) {
            contador = 0;
        }
        VerticeView v = new VerticeView(contador);
        //adiciona eventos para tratar a adição de ligações view
        v.setOnDragDetected(aoDetectarDragSobreVertice);
        v.setOnDragOver(aoDetectarPossivelAlvoParaSoltarODrag);
        v.setOnDragDropped(aoSoltarMouseSobreVertice);

        v.setOnMousePressed(aoIniciarArrastoVerticeComOMouse);
        v.setOnMouseDragged(aoArrastarVerticeComOMouse);
        v.setOnMouseReleased(aoLiberarVerticeArrastadoComOMouse);

        v.setPosicao(t.getX(), t.getY());
        v.setRotulo(contador + "");
        view.adicionarVertice(v);
        contador++;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public void setGrafoView(GrafoView view) {
        this.view = view;
        view.setOnMouseClicked(aoClicarMouse);
        //view.setOnMouseDragged(aoArrastarVerticeComOMouse);
        //setei o evento para o vertice view

        //  contador = view.getVertices().size() - 1;
    }

    public GrafoView getGrafo() {
        return view;
    }

    private void recalcularVerticesECor() {
        List<Vertice> lista = view.getVertices();
        contador = 0;
        for (Vertice vert : lista) {
            VerticeView v1 = (VerticeView) vert;
            v1.setRotulo(contador + "");
            Circle c = v1.getCircle();
            if (contador == 0) {
                c.setFill(Color.RED);
            } else {
                c.setFill(Color.CYAN);
            }
            contador++;
        }
    }
}
