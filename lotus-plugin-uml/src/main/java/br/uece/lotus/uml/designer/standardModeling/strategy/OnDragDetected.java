/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.designer.standardModeling.strategy;

import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.designer.standardModeling.StandardModelingWindowImpl;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author Bruno Barbosa
 */
public class OnDragDetected implements Strategy{

    @Override
    public void onDragDetectedMouse(StandardModelingWindowImpl s, MouseEvent event) {
        if(s.mModoAtual != s.MODO_TRANSICAO){
            return;
        }
        if(!(s.mComponentSobMouse instanceof HmscView)){
            return;
        }
        
        HmscView v = (HmscView) s.mComponentSobMouse;
        //Pegando posicoes iniciais
        s.xInicial = v.getHMSC().getLayoutX();
        s.yInicial = v.getHMSC().getLayoutY();
        //Guardar objeto pro drag
        s.hMSC_inicial = v;
        //Inicia o drag
        Dragboard db = s.hMSC_inicial.getNode().startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("gambiarra");
        db.setContent(content);
        
        event.consume();
    }
    
    @Override
    public void onClickedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(StandardModelingWindowImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onPressedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(StandardModelingWindowImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(StandardModelingWindowImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(StandardModelingWindowImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(StandardModelingWindowImpl s, KeyEvent event) {}
    
}
