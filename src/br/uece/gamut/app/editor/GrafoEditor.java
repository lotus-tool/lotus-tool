/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.app.editor;

/**
 *
 * @author emerson
 */
public class GrafoEditor {
    
    public static final int MODO_NENHUM = 0;
    public static final int MODO_MOVER_VERTICE = 1;
    public static final int MODO_ADICIONAR_VERTICE = 2;
    public static final int MODO_ADICIONAR_LIGACAO = 3;
    public static final int MODO_SELECIONAR_VERTICE = 4;
    public static final int MODO_SELECIONAR_LIGACAO = 5;
    public static final int MODO_REMOVER_VERTICE = 6;
    public static final int MODO_REMOVER_LIGACAO = 7;
    
    private int modo;
    private GrafoView view;
    
    public void setModo(int modo) {
        this.modo = modo;
    }

    public void setGrafoView(GrafoView view) {
        this.view = view;
    }
    
}
