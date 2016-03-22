package br.uece.lotus.uml.designer.blockDiagramModeling.strategy;

import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.designer.blockDiagramModeling.DesingWindowImplBlockDs;
import javafx.scene.input.*;

/**
 * Created by lva on 14/03/16.
 */
public class OnDragDetected implements Strategy {
    @Override
    public void onClickedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockDs s, MouseEvent event) {
        if(s.mModoAtual != s.MODO_TRANSICAO){
            return;
        }
        if(!(s.mComponentSobMouse instanceof BlockDSView)){
            return;
        }

        BlockDSView v = (BlockDSView) s.mComponentSobMouse;
        //Pegando posicoes iniciais
        s.xInicial = v.getNode().getLayoutX();
        s.yInicial = v.getNode().getLayoutY();
        //Guardar objeto pro drag
        s.blockDsInicial = v;
        //Inicia o drag
        Dragboard db = s.blockDsInicial.getNode().startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("gambiarra");
        db.setContent(content);

        event.consume();

    }

    @Override
    public void onDragOverMouse(DesingWindowImplBlockDs s, DragEvent event) {

    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockDs s, DragEvent event) {

    }

    @Override
    public void onDraggedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onPressedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onReleasedMouse(DesingWindowImplBlockDs s, MouseEvent event) {

    }

    @Override
    public void onScrollMouse(DesingWindowImplBlockDs s, ScrollEvent event) {

    }

    @Override
    public void onKeyPressed(DesingWindowImplBlockDs s, KeyEvent event) {

    }

    @Override
    public void onKeyReleased(DesingWindowImplBlockDs s, KeyEvent event) {

    }
}
