package br.uece.lotus.uml.api.viewer;


import br.uece.lotus.uml.api.ds.BlockDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;



/**
 * Created by lva on 11/12/15.
 */
public class BlockDSViewImpl extends Region implements BlockDSView, BlockDS.Listener{
    static final double ALTURA_RETANGULO = 30;
    static final double LARGURA_RETANGULO = 30;
    private final Rectangle mRectangle;
    private BlockDS mDs;



    public BlockDSViewImpl(){
        mRectangle  = new Rectangle(LARGURA_RETANGULO,ALTURA_RETANGULO);
        getChildren().addAll(mRectangle);
        mRectangle.setLayoutX(ALTURA_RETANGULO);
        mRectangle.setLayoutY(LARGURA_RETANGULO);
    }


    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        return false;
    }

    @Override
    public BlockDS getBlockDS() {
        return mDs;
    }

    @Override
    public void setBlockDS(BlockDS dl) {
        /*  if (mDs != null) {
            mDs.removeListener(this);
        }
        mDs = ds;
        if (ds != null) {
            mDs.addListener(this);
            updateView();
        }*/

    }

    @Override
    public void onChange(BlockDS ds) {
        /*updateView();*/
    }
}