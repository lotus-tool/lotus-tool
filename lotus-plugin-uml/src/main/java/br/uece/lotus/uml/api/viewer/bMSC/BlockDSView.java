package br.uece.lotus.uml.api.viewer.bMSC;

import br.uece.lotus.uml.api.ds.BlockDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;


/**
 * Created by lva on 11/12/15.
 */
public interface BlockDSView {
    Node getNode();

    interface Factory {
        BlockDSView create();
    }

    boolean isInsideBounds(/*Point2D point*/Circle c);

    BlockDS getBlockDS();
    void setBlockDS(BlockDS dl);
}