/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer;

import br.uece.lotus.uml.api.ds.BlockBuildDS;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Bruno Barbosa
 */
public class BlockBuildDSViewImpl extends  Region implements BlockBuildDSView, BlockBuildDS.Listener{

    static final int LARGURA = 50,ALTURA = 50;
    static final int RAIO = 20;
    
    private Rectangle mRetangulo;
    private Circle mCircle;
    private Label mTitulo;
    
    private BlockBuildDS mBlock;
    
    public BlockBuildDSViewImpl() {
        mRetangulo = new Rectangle(LARGURA, ALTURA);
        mCircle = new Circle(RAIO);
        
        getChildren().add(mRetangulo);
        mRetangulo.setLayoutX(LARGURA);
        mRetangulo.setLayoutY(ALTURA);
        
        mCircle.layoutXProperty().bind(mRetangulo.layoutXProperty().subtract(RAIO));
        mCircle.layoutYProperty().bind(mRetangulo.layoutYProperty().subtract(RAIO));
        getChildren().add(mCircle);
        
        mTitulo.layoutXProperty().bind(mRetangulo.layoutXProperty().subtract(mTitulo.widthProperty().divide(2)));
        mTitulo.layoutYProperty().bind(mRetangulo.layoutYProperty().subtract(mTitulo.heightProperty().divide(2)));
        getChildren().add(mTitulo);
    }
    
    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        Point2D aux = mRetangulo.localToScreen(point);
        return aux.distance(point) <= mRetangulo.computeAreaInScreen();
    }

    @Override
    public BlockBuildDS getBlockBuildDS() {
        return mBlock;
    }

    @Override
    public void setBlockBuildDS(BlockBuildDS bbds) {
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
    public void onChange(BlockBuildDS blockBuildDS) {
        updateView();
    }

    private void updateView() {
         String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
         style += "-fx-fill: linear-gradient(to bottom right, white, " + mBlock.getColor() + ");";
         style += "-fx-border-color: blue ;" +
                    "-fx-border-width: 5 ;" +
                    "-fx-border-style: segments(10, 15, 15, 15)  line-cap round ;";
         mRetangulo.setStyle(style);
         
         style = "";
         style += "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
         style += "-fx-border-color: black ;";
         style += "-fx-border-width: 2;";
         style += "-fx-fill:"+mBlock.getColorStatus()+";";
         mCircle.setStyle(style);
         
        style = "-fx-text-fill: " + (mBlock.getTextColor() == null ? "black" : mBlock.getTextColor()) + ";";
        style += "-fx-font-weight: " + (mBlock.getTextStyle() == null ? "normal" : mBlock.getTextStyle()) + ";";
        style += "-fx-font-size: " + (mBlock.getTextSize() == null ? "12" : mBlock.getTextSize()) + ";";
        style += "-fx-text-alignment: center;";
        mTitulo.setStyle(style);
        
        setLayoutX(mBlock.getLayoutX());
        setLayoutY(mBlock.getLayoutY());
    }

    
    
}
