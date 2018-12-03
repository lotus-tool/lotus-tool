/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.designer.hmscComponent.strategy;

import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.msc.designer.hmscComponent.HmscWindowMSCImpl;
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
    public void onDragDetectedMouse(HmscWindowMSCImpl s, MouseEvent event) {
        if(s.mModoAtual != s.TRANSITION_MODE){
            return;
        }
        if(!(s.componentSobMouse instanceof GenericElementView)){
            return;
        }

        GenericElementView genericElementView = (GenericElementView) s.componentSobMouse;
        //Pegando posicoes iniciais
        s.xInicial = genericElementView.getGenericElement().getLayoutX();
        s.yInicial = genericElementView.getGenericElement().getLayoutY();
        //Guardar objeto pro drag
        s.initialGenericElementView = genericElementView;
        //Inicia o drag
        Dragboard db = s.initialGenericElementView.getNode().startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("gambiarra");
        db.setContent(content);
        
        event.consume();
    }
    
    @Override
    public void onClickedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onMovedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onDragOverMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(HmscWindowMSCImpl s, DragEvent event) {}

    @Override
    public void onDraggedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onPressedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onReleasedMouse(HmscWindowMSCImpl s, MouseEvent event) {}

    @Override
    public void onScrollMouse(HmscWindowMSCImpl s, ScrollEvent event) {}

    @Override
    public void onKeyPressed(HmscWindowMSCImpl s, KeyEvent event) {}

    @Override
    public void onKeyReleased(HmscWindowMSCImpl s, KeyEvent event) {}
    
}
