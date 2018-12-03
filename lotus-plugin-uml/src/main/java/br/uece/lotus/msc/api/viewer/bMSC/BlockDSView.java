package br.uece.lotus.msc.api.viewer.bMSC;

import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
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

    BmscBlock getBlockDS();
    void setBlockDS(BmscBlock dl);
}