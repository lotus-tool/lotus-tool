/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.viewer.transition;

import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.hMSC.HmscViewImpl;
import br.uece.lotus.viewer.ElipseTransitionViewImpl;
import br.uece.lotus.viewer.Geom;
import br.uece.lotus.viewer.Seta;
import br.uece.lotus.viewer.StyleBuilder;
import java.util.List;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Bruno Barbosa
 */
public class ElipseTransitionMSCViewImpl extends TransitionMSCViewImpl{

    private TransicaoEmArcoMSC mCurva;
    
    @Override
    protected void prepareView() {
        HmscViewImpl origemView =  null;
        HmscViewImpl destinoView = null;

        switch (mValueType) {
            case "hMSC":{
                origemView = (HmscViewImpl) hMscSource.getHMSC().getValue("view");
                destinoView = (HmscViewImpl) hMscDestiny.getHMSC().getValue("view");
                
                mCurva = new TransicaoEmArcoMSC(origemView, destinoView, mTransition);
                mCurva.setStyle(StyleBuilder.stroke("#f00", 1));
                mCurva.layoutXProperty().bind(origemView.layoutXProperty().add(origemView.heightProperty().divide(2)));
                mCurva.layoutYProperty().bind(origemView.layoutYProperty().subtract(mCurva.heightProperty()).add(origemView.heightProperty().divide(2)));

                Rotate r = new Rotate();
                DoubleBinding angle = Geom.angle(origemView, destinoView);
                r.angleProperty().bind(new Geom.CartesianCase(origemView, destinoView)
                        .firstAndSecond(-90)
                        .thirthAndFourth(90)
                        .secondAndThirth(angle.add(180))
                        .first(angle)
                        .second(angle.add(180))
                        .thirth(angle.add(180))
                        .fourth(angle)                
                );
                r.setAxis(Rotate.Z_AXIS);
                r.pivotYProperty().bind(mCurva.heightProperty());
                mCurva.getTransforms().add(r);
                mCurva.rotulo.rotateProperty().bind(new Geom.CartesianCase(origemView, destinoView)
                                .second(180)
                                .thirth(180)
                );
                getChildren().add(mCurva);
            }
        }
        
    }

    @Override
    protected void updateView() {
        mCurva.arco.setStyle(StyleBuilder.stroke(mTransition.getColor(), mTransition.getWidth()));
        mCurva.seta.setStyle(StyleBuilder.fill(mTransition.getColor()));
        mCurva.rotulo.setStyle(StyleBuilder.font(mTransition.getColor(), mTransition.getTextStyle(), mTransition.getTextSize()));
        mCurva.rotulo.setText(getComputedLabel());
    }

    @Override
    public boolean isInsideBounds_hMSC(Circle circle) {
        return circle.getBoundsInParent().intersects(mCurva.seta.getLayoutBounds());
    }

    @Override
    public boolean isInsideBounds_bMSC(Circle circle) {
        return false;
    }

    @Override
    public Line getLineTransition() {
        return null;
    }

    static class TransicaoEmArcoMSC extends Region {
        TransitionMSC mTransition;
        final Label rotulo = new Label();
        final Seta seta = new Seta();
        final Arc arco = new Arc();

        public TransicaoEmArcoMSC (Node origem, Node destino, TransitionMSC transition) {
            mTransition = transition;
            getChildren().addAll(rotulo, seta, arco);
            arco.radiusXProperty().bind(Geom.distance(origem, destino).divide(2));
            arco.radiusYProperty().bind(Geom.distance(origem, destino).divide(4).add(fatorY(mTransition)));
            arco.centerXProperty().bind(arco.radiusXProperty());
            arco.centerYProperty().bind(arco.radiusYProperty().add(17));
            arco.setLength(180);
            arco.setType(ArcType.OPEN);
            arco.setFill(null);
            arco.setStroke(Color.BLACK);

            seta.layoutXProperty().bind(arco.radiusXProperty());
            seta.setLayoutY(17);
            rotulo.setLayoutY(-5);
            rotulo.layoutXProperty().bind(arco.radiusXProperty().subtract(rotulo.widthProperty().divide(2)));
        }
        
        private int fatorY(TransitionMSC mTransition) {
            return quantidadeFilhosHmsc(mTransition) * 25;
        }

        private int quantidadeFilhosHmsc(TransitionMSC mTransition) {
            int index = 0;
            boolean temLine = false;
            List<TransitionMSC> listaT = ((HmscView)mTransition.getSource()).getHMSC().getTransitionsTo(((HmscView)mTransition.getDestiny()).getHMSC());
            for(int i=0;i<listaT.size();i++){
                if(listaT.get(i) == mTransition){
                    index = i;
                }
            }
            temLine = verificarSeExisteTransitionLine(listaT);
            if(temLine){
                index = index-1;
            }
            return index;
        }
        
        private boolean verificarSeExisteTransitionLine(List<TransitionMSC> transitions){
            boolean line = false;
            for(TransitionMSC t : transitions){
                if((int)t.getValue("view.type") == 0){
                    line = true;
                }
            }
            return line;
        } 
    }
}
