/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.viewer.Seta;
import br.uece.lotus.viewer.StyleBuilder;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Bruno Barbosa
 */
public class LineTransitionMSCViewImpl extends TransitionMSCViewImpl{

    private final Label mRotulo;
    private final Seta mSeta;
    private final Line mLine;

    public LineTransitionMSCViewImpl() {
        mRotulo = new Label();
        getChildren().add(mRotulo);
        mSeta = new Seta();
        getChildren().add(mSeta);
        mLine = new Line();
        getChildren().add(mLine);
    }
    
    @Override
    protected void prepareView() {
        switch (mValueType) {
            case "hMSC":{
                Region origem = (Region) hMscSource.getNode();
                Region destiny = (Region) hMscDestiny.getNode();
                
                DoubleBinding origemX = origem.layoutXProperty().add(origem.widthProperty().divide(2));
                DoubleBinding origemY = origem.layoutYProperty().add(origem.heightProperty().divide(2));
                DoubleBinding destinoX = destiny.layoutXProperty().add(destiny.widthProperty().divide(2));
                DoubleBinding destinoY = destiny.layoutYProperty().add(destiny.heightProperty().divide(2));
                
                
                break;  
            }
                
            case "bMSC":{
                
                break;
            }
                
        }
    }

    @Override
    protected void updateView() {
        mLine.setStyle(StyleBuilder.stroke(mTransition.getColor(), mTransition.getWidth()));
        mSeta.setStyle(StyleBuilder.fill(mTransition.getColor()));
        mRotulo.setStyle(StyleBuilder.font(mTransition.getTextColor(), mTransition.getTextStyle(), mTransition.getTextSize()));
        mRotulo.setText(getComputedLabel());
    }

    @Override
    public boolean isInsideBounds(Circle circle) {
        if(circle.getBoundsInParent().intersects(mRotulo.getBoundsInParent())){
            return true;
        }
        else if(circle.getBoundsInParent().intersects(mSeta.getBoundsInParent())){
            return true;
        }
        else if(circle.getBoundsInParent().intersects(mLine.getBoundsInParent())){
            return true;
        }
        return false;
    }
    
}
