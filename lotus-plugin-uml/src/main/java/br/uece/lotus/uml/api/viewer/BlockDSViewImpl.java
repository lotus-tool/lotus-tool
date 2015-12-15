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
    private BlockDS dl;



    public BlockDSViewImpl(){
        mRectangle  = new Rectangle(LARGURA_RETANGULO,ALTURA_RETANGULO);
        getChildren().addAll(mRectangle);
        mRectangle.setLayoutX(ALTURA_RETANGULO);
        mRectangle.setLayoutY(LARGURA_RETANGULO);
    }


    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        return false;
    }

    @Override
    public BlockDS getDiagramaDeSequencia() {
        return null;
    }

    @Override
    public void setDiagramaDeSequencia(BlockDS dl) {

    }

    @Override
    public void onChange(BlockDS diagrama) {

    }
}