package br.uece.gamut.app.editor;

import br.uece.gamut.Vertice;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_VERTICE;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    private void removerVertice(Vertice v) {
        //v.removerLigacoes(); - IMPLEMENTAR
        view.removerVerticeView(v);
        recalcularVerticesECor();
    }

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
