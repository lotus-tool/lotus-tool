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
    static final double LARGURA_RETANGULO = 25;
    private final Rectangle mRectangle;
    private BlockDS mDS;
    private static final String DEFAULT_NORMAL_COLOR = "aqua";




    public BlockDSViewImpl(){
        mRectangle  = new Rectangle(LARGURA_RETANGULO,ALTURA_RETANGULO);
        getChildren().addAll(mRectangle);
        mRectangle.setLayoutX(ALTURA_RETANGULO);
        mRectangle.setLayoutY(LARGURA_RETANGULO);

        /*mText = new Label();
        mText.layoutXProperty().bind(mCircle.layoutXProperty().subtract(mText.widthProperty().divide(2)));
        mText.layoutYProperty().bind(mCircle.layoutYProperty().subtract(mText.heightProperty().divide(2)));
        getChildren().add(mText);*/
    }


    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        /*Point2D aux = mCircle.localToScene(Point2D.ZERO);
        //System.out.printf("(%f %f) (%f %f)\n", aux.getX(), aux.getY(), point.getX(), point.getY());
        return aux.distance(point) <= RAIO_CIRCULO;*/
        return false;
    }

    @Override
    public BlockDS getBlockDS() {
        return mDS;
    }

    @Override
    public void setBlockDS(BlockDS ds){
        if (mDS != null) {
            mDS.removeListener(this);
        }
        mDS = ds;
        if (ds != null) {
            mDS.addListener(this);
            updateView();
        }
    }

    @Override
    public void onChange(BlockDS ds) {updateView();}

    private void updateView() {
        /*String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-fill: linear-gradient(to bottom right, white, " + computedColor() + ");";
        style += "-fx-stroke: " + (mState.getBorderColor() == null ? "black" : mState.getBorderColor()) + ";";
        style += "-fx-stroke-width: " + (mState.getBorderWidth() == null ? "1" : mState.getBorderWidth()) + ";";
        mCircle.setStyle(style);

        if (mState.isFinal()) {
            mSecondCircle.setStyle(style);
        }
        else{
            mSecondCircle.setStyle(null);
        }

        if(mState.isBig()){
            mCircle.setRadius(RAIO_CIRCULO+3);
            mSecondCircle.setRadius(RAIO_CIRCULO+1);
            mSecondCircle.setStyle(style);
        }

        style = "-fx-text-fill: " + (mState.getTextColor() == null ? "black" : mState.getTextColor()) + ";";
        style += "-fx-font-weight: " + (mState.getTextStyle() == null ? "normal" : mState.getTextStyle()) + ";";
        style += "-fx-font-size: " + (mState.getTextSize() == null ? "12" : mState.getTextSize()) + ";";
        mText.setStyle(style);
        setLayoutX(mState.getLayoutX());
        setLayoutY(mState.getLayoutY());
        mText.setText(computedLabel());*/
    }
    private String computedColor() {
       /* String cor = mState.getColor();
        if (cor == null) {
            if (mState.isInitial()) {
                return DEFAULT_INITIAL_COLOR;
            } else if (mState.isFinal()) {
                return DEFAULT_FINAL_COLOR;
            } else if (mState.isError()) {
                return DEFAULT_ERROR_COLOR;
            } else {
                return DEFAULT_NORMAL_COLOR;
            }
        }
        return cor;*/
        return null;
    }
    private String computedLabel() {
        /*if (mState.isError()) {
            return "-1";
        } else if (mState.isFinal()) {
            return "E";
        }
        return mState.getLabel();*/
        return null;
    }
}