package br.uece.gamut.app.editor;

import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_LIGACAO;
import static br.uece.gamut.app.editor.GrafoEditor.MODO_ADICIONAR_VERTICE;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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
                    break;
            }
        }
    };
    
    
    private void adicionarVertice(MouseEvent t) {
        VerticeView v = new VerticeView();
        v.setPosicao(t.getSceneX(), t.getSceneY());
        v.setRotulo("" + contador++);
        view.adicionarVertice(v);
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public void setGrafoView(GrafoView view) {
        this.view = view;
        view.setOnMouseClicked(aoClicarMouse);
        contador = view.getVertices().size() - 1;
    }
}
