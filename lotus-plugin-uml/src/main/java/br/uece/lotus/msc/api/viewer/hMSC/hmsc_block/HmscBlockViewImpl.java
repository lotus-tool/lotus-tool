/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.viewer.hMSC.hmsc_block;

import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Bruno Barbosa
 * edited by Lucas Vieira Alves 11/29/2018
 */
public class HmscBlockViewImpl extends Region implements HmscBlockView, HmscBlock.Listener{

    public static final int LARGURA = 150,ALTURA = 70;
    public static final Integer DEFAULT_BORDER_WIDTH = 2;
    public static final Integer EXCEPTIONAL_BORDER_WIDTH = 0;
    public static final String EXCEPTIONAL_COLOR = "#999966";
    public static final String DEFAULT_COLOR = "yellow";
    static final int RAIO = 5;
    
    private Rectangle mRetangulo;
    private Circle mCircle;
    private Label mTitulo;
    private Polygon mInitial;
    
    private HmscBlock hmscBlock;

    
    public HmscBlockViewImpl() {
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
    public GenericElement getGenericElement() {
        return hmscBlock;
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
    public HmscBlock getHMSC() {
        return hmscBlock;
    }

    @Override
    public void setHMSC(HmscBlock bbds) {
        if(hmscBlock != null){
            hmscBlock.removeListener(this);
        }
        hmscBlock = bbds;
        if(bbds != null){
            hmscBlock.addListener(this);
            updateView();
        }
    }

    @Override
    public void onChange(HmscBlock hmscHmscBlock) {
        updateView();
    }

    private void updateView() {
        String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-fill: linear-gradient(to bottom right, white, " + computedColor() + ");";
        style += "-fx-stroke: " + (hmscBlock.getBorderColor() == null ? "black" : hmscBlock.getBorderColor()) + ";";
        style += "-fx-stroke-width: " + (hmscBlock.getBorderWidth() == null ? "1" : hmscBlock.getBorderWidth()) + ";";
        mRetangulo.setStyle(style);

        style = "";
        style += "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-stroke: black ;";
        style += "-fx-stroke-width: 1px;";
        style += "-fx-fill:"+ hmscBlock.getColorStatus()+";";
        mCircle.setStyle(style);
         
        style = "-fx-text-fill: " + (hmscBlock.getTextColor() == null ? "black" : hmscBlock.getTextColor()) + ";";
        style += "-fx-font-weight: " + (hmscBlock.getTextStyle() == null ? "normal" : hmscBlock.getTextStyle()) + ";";
        style += "-fx-font-size: " + (hmscBlock.getTextSize() == null ? "12" : hmscBlock.getTextSize()) + ";";
        style += "-fx-text-alignment: center;";
        mTitulo.setStyle(style);
        mTitulo.setText(hmscBlock.getLabel());

        if(hmscBlock.isInitial()){
                mInitial.setVisible(true);
        }else{
                mInitial.setVisible(false);
        }
        setLayoutX(hmscBlock.getLayoutX());
        setLayoutY(hmscBlock.getLayoutY());
    }

    private String computedColor() {
        String cor = hmscBlock.getColor();
        if(cor == null){
            return DEFAULT_COLOR;
        }
        return cor;
    }
    
}
