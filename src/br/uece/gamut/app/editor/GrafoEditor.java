package br.uece.gamut.app.editor;

import br.uece.gamut.Vertice;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_LIGACAO;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_VERTICE;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
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
                case MODO_ADICIONAR_LIGACAO:
                    //TODO
                    break;
                case MODO_MOVER_VERTICE:
                   //TODO
                    break;
                case MODO_NENHUM:
                    //TODO
                    break;
                case MODO_REMOVER_LIGACAO:
                    //TODO
                    break;
                case MODO_REMOVER_VERTICE:
                    //TODO
                    break;
                case MODO_SELECIONAR_LIGACAO:
                //TODO
                case MODO_SELECIONAR_VERTICE:
                    //TODO
                    break;

            }
        }
    };

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
}
