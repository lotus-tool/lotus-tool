package br.uece.lotus.uml.api.viewer.block;

import br.uece.lotus.uml.api.ds.BlockDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;


/**
 * Created by lva on 11/12/15.
 */
public interface BlockDSView {
    Node getNode();

    interface Factory {
        BlockDSView create();
    }

    boolean isInsideBounds(Point2D point);

    BlockDS getBlockDS();
    void setBlockDS(BlockDS dl);
}