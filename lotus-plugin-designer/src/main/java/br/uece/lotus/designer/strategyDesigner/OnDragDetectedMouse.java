/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.BigState;
import br.uece.lotus.designer.DesignerWindowImpl;
import static br.uece.lotus.designer.DesignerWindowImpl.MODO_TRANSICAO;
import br.uece.lotus.viewer.StateView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javax.swing.JOptionPane;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDragDetectedMouse implements Strategy{

    
    @Override
    public void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent t) {
        
        if (dwi.mModoAtual != MODO_TRANSICAO) {
            return;
        }

        if (!(dwi.mComponentSobMouse instanceof StateView)) {
            return;
        }
        StateView v = (StateView) dwi.mComponentSobMouse;

        ///pegando posicioes inicias (x,y)
        dwi.xInicial=v.getState().getLayoutX();
        dwi.yInicial=v.getState().getLayoutY();

        //guarda o objeto no qual iniciamos o drag            
        dwi.mVerticeOrigemParaAdicionarTransicao = v;

        if (BigState.verifyIsBigState(dwi.mVerticeOrigemParaAdicionarTransicao.getState())) {
            JOptionPane.showMessageDialog(null, "Impossible to create transitions in a Big State!", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(dwi.mVerticeOrigemParaAdicionarTransicao.getState().isFinal()){
            JOptionPane.showMessageDialog(null, "Impossible to create transitions in a Final State!", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(dwi.mVerticeOrigemParaAdicionarTransicao.getState().isError()){
            JOptionPane.showMessageDialog(null, "Impossible to create transitions in a Error State!", "Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //inicia o drag'n'drop
        Dragboard db = dwi.mVerticeOrigemParaAdicionarTransicao.getNode().startDragAndDrop(TransferMode.ANY);
        
        //soh funciona com as trÃªs linhas a seguir. Porque? Eu nÃ£o sei.
        ClipboardContent content = new ClipboardContent();
        content.putString("gambiarra");
        db.setContent(content);

        //indica que este evento foi realizado
        t.consume();
    }
    
    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}
    
}
