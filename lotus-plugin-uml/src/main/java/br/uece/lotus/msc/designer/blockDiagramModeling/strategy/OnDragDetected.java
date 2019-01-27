package br.uece.lotus.msc.designer.blockDiagramModeling.strategy;

import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.msc.designer.blockDiagramModeling.DesingWindowImplBlockMSC;
import javafx.scene.input.*;

/**
 * Created by Lucas Vieira Alves on 14/03/16.
 */
public class OnDragDetected implements Strategy {
    @Override
    public void onClickedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onMovedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onDragDetectedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {
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
    public void onDragOverMouse(DesingWindowImplBlockMSC s, DragEvent event) {

    }

    @Override
    public void onDragDroppedMouse(DesingWindowImplBlockMSC s, DragEvent event) {

    }

    @Override
    public void onDraggedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onPressedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onReleasedMouse(DesingWindowImplBlockMSC s, MouseEvent event) {

    }

    @Override
    public void onScrollMouse(DesingWindowImplBlockMSC s, ScrollEvent event) {

    }

    @Override
    public void onKeyPressed(DesingWindowImplBlockMSC s, KeyEvent event) {

    }

    @Override
    public void onKeyReleased(DesingWindowImplBlockMSC s, KeyEvent event) {

    }
}
