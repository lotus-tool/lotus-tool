/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.hMSC;

import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author Bruno Barbosa
 */
public class HmscViewImpl extends Region implements HmscView, Hmsc.Listener{

    public static final int LARGURA = 150,ALTURA = 70;
    static final int RAIO = 5;
    
    private Rectangle mRetangulo;
    private Circle mCircle;
    private Label mTitulo;
    private Polygon mInitial;
    
    private Hmsc mBlock;
    private static final String DEFAULT_COLOR = "yellow";
    
    public HmscViewImpl() {
        mRetangulo = new Rectangle(LARGURA, ALTURA);
        getChildren().add(mRetangulo);
        mRetangulo.setLayoutX(0);
        mRetangulo.setLayoutY(0);
        
        mCircle = new Circle(RAIO);
        mCircle.layoutXProperty().bind(mRetangulo.layoutXProperty().add(mRetangulo.widthProperty().subtract(8)));
        mCircle.layoutYProperty().bind(mRetangulo.layoutYProperty().add(mRetangulo.heightProperty().subtract(8)));
        getChildren().add(mCircle);

        mInitial = new Polygon();
        mInitial.getPoints().addAll(new Double[]{
                0.0, 0.0,
                10.0, 0.0,
                5.0, 10.0
        });
        mInitial.layoutXProperty().bind(mCircle.layoutXProperty().subtract(5));
        mInitial.layoutYProperty().bind(mCircle.layoutYProperty().subtract(mCircle.layoutYProperty().divide(3)));
        getChildren().add(mInitial);
        
        mTitulo = new Label();
        mTitulo.layoutXProperty().bind(mRetangulo.layoutXProperty().add(mRetangulo.widthProperty().divide(2)).subtract(mTitulo.widthProperty().divide(2)));
        mTitulo.layoutYProperty().bind(mRetangulo.layoutYProperty().add(mRetangulo.heightProperty().divide(2)).subtract(mTitulo.heightProperty().divide(2)));
        getChildren().add(mTitulo);


    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        Point2D aux = mRetangulo.localToScene(Point2D.ZERO);
        //System.out.println("auxX:"+aux.getX()+" auxY:"+aux.getY());
        //System.out.println("pointX:"+point.getX()+" pointY:"+point.getY());
        //System.out.println("distanciaX:"+(point.getX()-aux.getX())+" distanciaY:"+(point.getY()-aux.getY()));
        double distanciaX = point.getX()-aux.getX();
        double distanciaY = point.getY()-aux.getY();
        return((distanciaX >= 0 && distanciaX <= LARGURA) && (distanciaY >= 0 && distanciaY <= ALTURA));
            
        
    }

    @Override
    public Hmsc getHMSC() {
        return mBlock;
    }

    @Override
    public void setHMSC(Hmsc bbds) {
        if(mBlock != null){
            mBlock.removeListener(this);
        }
        mBlock = bbds;
        if(bbds != null){
            mBlock.addListener(this);
            updateView();
        }
    }

    @Override
    public void onChange(Hmsc blockBuildDS) {
        updateView();
    }

    private void updateView() {
        String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-fill: linear-gradient(to bottom right, white, " + computedColor() + ");";
        style += "-fx-stroke: " + (mBlock.getBorderColor() == null ? "black" : mBlock.getBorderColor()) + ";";
        style += "-fx-stroke-width: " + (mBlock.getBorderWidth() == null ? "1" : mBlock.getBorderWidth()) + ";";
        mRetangulo.setStyle(style);

        style = "";
        style += "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-stroke: black ;";
        style += "-fx-stroke-width: 1px;";
        style += "-fx-fill:"+mBlock.getColorStatus()+";";
        mCircle.setStyle(style);
         
        style = "-fx-text-fill: " + (mBlock.getTextColor() == null ? "black" : mBlock.getTextColor()) + ";";
        style += "-fx-font-weight: " + (mBlock.getTextStyle() == null ? "normal" : mBlock.getTextStyle()) + ";";
        style += "-fx-font-size: " + (mBlock.getTextSize() == null ? "12" : mBlock.getTextSize()) + ";";
        style += "-fx-text-alignment: center;";
        mTitulo.setStyle(style);
        mTitulo.setText(mBlock.getLabel());

        if(mBlock.get_Initial()){
                mInitial.setVisible(true);
        }else{
                mInitial.setVisible(false);
        }
        setLayoutX(mBlock.getLayoutX());
        setLayoutY(mBlock.getLayoutY());
    }

    private String computedColor() {
        String cor = mBlock.getColor();
        if(cor == null){
            return DEFAULT_COLOR;
        }
        return cor;
    }
    
}
