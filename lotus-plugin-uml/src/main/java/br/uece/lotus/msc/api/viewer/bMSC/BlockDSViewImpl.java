package br.uece.lotus.msc.api.viewer.bMSC;


import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;



/**
 * Created by lva on 11/12/15.
 */
public class BlockDSViewImpl extends Region implements BlockDSView, BmscBlock.Listener{
    static final double ALTURA_RETANGULO = 40;
    static final double LARGURA_RETANGULO = 100;
   // private static final String DEFAULT_COLOR = "green" ;
    private static final String DEFAULT_COLOR = "cornsilk" ;
    private static final String DEFAULT_BORDER_COLOR = "red";
    private final Rectangle mRectangle;
    private BmscBlock mDS;
    private Line mLine;
    private Label mName;





    public BlockDSViewImpl(){
        mRectangle  = new Rectangle(LARGURA_RETANGULO,ALTURA_RETANGULO);
        getChildren().addAll(mRectangle);
        mRectangle.setLayoutX(0);
        mRectangle.setLayoutY(0);

        mName = new Label();
        mName.layoutXProperty().bind(mRectangle.layoutXProperty().add(mRectangle.widthProperty().divide(2)).subtract(mName.widthProperty().divide(2)));
        mName.layoutYProperty().bind(mRectangle.layoutYProperty().add(mRectangle.heightProperty().divide(2)).subtract(mName.heightProperty().divide(2)));
        getChildren().add(mName);

        mLine =new Line(0,0,0,0);
        mLine.startXProperty().bind(mRectangle.layoutXProperty().add(mRectangle.widthProperty().divide(2)));
        mLine.endXProperty().bind(mRectangle.layoutXProperty().add(mRectangle.widthProperty().divide(2)));
        mLine.startYProperty().bind(mRectangle.layoutXProperty().add(mRectangle.heightProperty()));
        mLine.endYProperty().setValue(500);
        getChildren().add(mLine);

    }


    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        Point2D auxRec= mRectangle.localToScene(Point2D.ZERO);
        double deltaX = point.getX()-auxRec.getX();
        double deltaY= point.getY()-auxRec.getY();
        double alturaGeral= ALTURA_RETANGULO+(mLine.endYProperty().getValue()-mLine.startYProperty().getValue());
        if((deltaX>=0 && deltaX<=LARGURA_RETANGULO) && (deltaY>=0 && deltaY<=alturaGeral)){
            //System.out.println("True linha + retangulo");
            return true;
        }

        return false;
    }

    @Override
    public BmscBlock getBlockDS() {
        return mDS;
    }

    @Override
    public void setBlockDS(BmscBlock ds){
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
    public void onChange(BmscBlock ds) {updateView();}

    private void updateView() {
        String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-fill: linear-gradient(to bottom right, white, " + computedColor() + ");";
      //  style += "-fx-stroke: " + (mDS.getBorderColor() == null ? DEFAULT_BORDER_COLOR : mDS.getBorderColor()) + ";";
        style += "-fx-stroke: " + DEFAULT_BORDER_COLOR + ";";
        style += "-fx-stroke-width: " + (mDS.getBorderWidth() == null ? "1" : mDS.getBorderWidth()) + ";";
        mRectangle.setStyle(style);
        mLine.setStyle(style);
        mName.setText(mDS.getLabel());
//
//        style = "-fx-text-fill: " + (mState.getTextColor() == null ? "black" : mState.getTextColor()) + ";";
//        style += "-fx-font-weight: " + (mState.getTextStyle() == null ? "normal" : mState.getTextStyle()) + ";";
//        style += "-fx-font-size: " + (mState.getTextSize() == null ? "12" : mState.getTextSize()) + ";";
//        mText.setStyle(style);
        setLayoutX(mDS.getLayoutX());
        setLayoutY(mDS.getLayoutY());
//        mText.setText(computedLabel());
    }
    private String computedColor() {
        String cor = mDS.getColor();
        if (cor == null) {
          return DEFAULT_COLOR;
        }
        return cor;
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