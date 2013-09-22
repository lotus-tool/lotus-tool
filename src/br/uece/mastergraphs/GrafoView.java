/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.mastergraphs;

import br.uece.mastergraphs.model.Grafo2;
import br.uece.mastergraphs.model.Ligacao;
import br.uece.mastergraphs.model.Ligacao2;
import br.uece.mastergraphs.model.Vertice;
import br.uece.mastergraphs.model.Vertice2;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javax.swing.JOptionPane;

/**
 *
 * @author Emerson
 */
public class GrafoView extends Region implements Grafo2.Listener {

    private Grafo2 grafo;

    private String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "W", "U", "V", "W", "X", "Y", "Z"};
    private static int contadorVertices;
    
    public GrafoView() {
        this.setOnMouseClicked(adicionarVerticeAoClicar);
    }

    public void setGrafo(Grafo2 grafo) {
        if (this.grafo != null) {
            this.grafo.removeListener(this);
        }
        this.grafo = grafo;
        grafo.addListener(this);
    }

    public Grafo2 getGrafo() {
        return grafo;
    }

    @Override
    public void aoAdicionar(Vertice2 vertice) {
        VerticeView view = new VerticeView(vertice);
        vertice.putValor("view", view);
        view.setOnMouseEntered(onMovimentarMouseEntered);
        view.setOnMousePressed(onMovimentarMousePressed);
        view.setOnMouseReleased(onMovimentarMouseReleased);
        view.setOnMouseDragged(onMovimentarMosueDragged);
        view.setOnDragDetected(onEditarDragDetected);
        view.setOnDragOver(onEditarDragOver);
        view.setOnDragDropped(onEditarDragDropped);
        getChildren().add(view);
    }

    @Override
    public void aoAdicionar(Ligacao2 ligacao) {
        VerticeView origemView = (VerticeView) ligacao.getOrigem().getValor("view");
        VerticeView destinoView = (VerticeView) ligacao.getDestino().getValor("view");
        LigacaoView view = new LigacaoView(ligacao, origemView, destinoView);
        view.setOnMouseClicked(aoClicarLigacao);
        view.toBack();
        ligacao.putValor("view", view);
        getChildren().add(view);
        view.toBack();
    }

    @Override
    public void aoMudar(Vertice2 vertice) {
    }

    @Override
    public void aoMudar(Ligacao2 vertice) {
    }

    @Override
    public void aoRemover(Vertice2 v) {
        getChildren().remove(v.getValor("view"));
    }

    @Override
    public void aoRemover(Ligacao2 l) {
        getChildren().remove(l.getValor("view"));
    }

    /**
     * *******************
     * EDITOR
     * *****************
     */
    public enum Modo {

        SELECAO, VERTICE, LIGACAO, MOVER, APAGAR
    }
    private Modo modo = Modo.SELECAO;

    public void setModo(Modo modo) {
        this.modo = modo;
    }
    /**
     * *******************
     * ADICIONAR VERTICE * *******************
     */
    private EventHandler<MouseEvent> adicionarVerticeAoClicar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent arg0) {
            if (modo == Modo.VERTICE) {
                Vertice2 v = grafo.novoVertice();
                v.putValor("nome", letras[contadorVertices++]);
                VerticeView view = (VerticeView) v.getValor("view");
                view.setLayoutX(arg0.getX());
                view.setLayoutY(arg0.getY());
                v.putValor("view.x", "" + view.getLayoutX());
                v.putValor("view.y", "" + view.getLayoutY());

            }
        }
    };
    /**
     * *******************
     * MOVIMENTAR VERTICES *******************
     */
    final Delta dragDelta = new Delta();

    class Delta {

        double x, y;
    }
    private EventHandler<MouseEvent> onMovimentarMouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (modo != Modo.MOVER) {
                return;
            }
            ((Region) mouseEvent.getSource()).setCursor(Cursor.HAND);
        }
    };
    EventHandler<MouseEvent> onMovimentarMousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (modo != Modo.MOVER) {
                return;
            }
            // record a delta distance for the drag and drop operation.
            Region vertice = (Region) mouseEvent.getSource();
            dragDelta.x = vertice.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = vertice.getLayoutY() - mouseEvent.getSceneY();
            vertice.setCursor(Cursor.MOVE);
        }
    };
    EventHandler<MouseEvent> onMovimentarMouseReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (modo != Modo.MOVER) {
                return;
            }
            ((Region) mouseEvent.getSource()).setCursor(Cursor.HAND);
        }
    };
    EventHandler<MouseEvent> onMovimentarMosueDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (modo != Modo.MOVER) {
                return;
            }
            Region vertice = (Region) mouseEvent.getSource();
            vertice.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            vertice.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            Vertice2 v = ((VerticeView) vertice).getVertice();
            v.putValor("view.x", "" + vertice.getLayoutX());
            v.putValor("view.y", "" + vertice.getLayoutY());
        }
    };
    /**
     * ********************
     * ADICIONAR LIGACOES * *******************
     */
    Region origem;
    EventHandler<MouseEvent> onEditarDragDetected = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (modo != Modo.LIGACAO) {
                return;
            }
            /* drag was detected, start drag-and-drop gesture */
            System.out.println("onDragDetected");

            /* allow any transfer mode */
            Dragboard db = startDragAndDrop(TransferMode.ANY);

            /* put a string on dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("foi");
            origem = (Region) event.getSource();
            db.setContent(content);

            event.consume();
        }
    };
    EventHandler<DragEvent> onEditarDragOver = new EventHandler<DragEvent>() {
        public void handle(DragEvent event) {
            if (modo != Modo.LIGACAO) {
                return;
            }
            /* data is dragged over the target */
            System.out.println("onDragOver");

            /*
             * accept it only if it is not dragged from the same node and if
             * it has a string data
             */
            if (event.getGestureSource() != event.getSource()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        }
    };
    EventHandler<DragEvent> onEditarDragDropped = new EventHandler<DragEvent>() {
        public void handle(DragEvent event) {
            if (modo != Modo.LIGACAO) {
                return;
            }
            /* data dropped */
            System.out.println("onDragDropped");
            /* if there is a string data on dragboard, read it and use it */
            Region destino = (Region) event.getSource();
            System.out.append("origem " + origem + " destino " + destino);
            Vertice2 vo = ((VerticeView) origem).getVertice();
            Vertice2 vf = ((VerticeView) destino).getVertice();
            System.out.append("peso ");
            Ligacao2 l = vo.ligarA(vf);
            System.out.append("peso ");
            l.putValor("peso", (int) ((Math.random() * 100) % 89) + 10);
            
            /*
             * let the source know whether the string was successfully
             * transferred and used
             */
            event.setDropCompleted(true);

            event.consume();
        }
    };
    EventHandler<? super MouseEvent> aoClicarLigacao = new EventHandler<MouseEvent>() {
        @Override
        public void handle(final MouseEvent event) {
            if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 2) {
                return;
            }
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Ligacao2 ligacao = ((LigacaoView) event.getSource()).getLigacao();
                    String valor = JOptionPane.showInputDialog(null, "Digite o valor da ligação " + ligacao.getOrigem().getValor("nome") + " -> " + ligacao.getDestino().getValor("nome"));
                    if (valor == null) {
                        return;
                    }
                    try {
                        int v = Integer.parseInt(valor);
                        ligacao.putValor("peso", v);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "O valor digitado não é um número!");
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    };
}
