/*
 * The MIT License
 *
 * Copyright 2014 emerson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.viewer;

import br.uece.lotus.State;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;

/**
 *
 * @author emerson
 */
public class ElipseTransitionViewImpl extends TransitionViewImpl {

    private TransicaoEmArco mCurva;

    @Override
    public boolean isInsideBounds(Point2D point) {
        return mCurva.seta.localToScene(Point2D.ZERO).distance(point) < 8;
    }

    @Override
    protected void prepareView() {
        State origem = mTransition.getSource();
        State destino = mTransition.getDestiny();
        StateViewImpl origemView = (StateViewImpl) origem.getValue("view");
        StateViewImpl destinoView = (StateViewImpl) destino.getValue("view");

        mCurva = new TransicaoEmArco(origemView, destinoView);
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

    @Override
    protected void updateView() {
        mCurva.arco.setStyle(StyleBuilder.stroke(mTransition.getColor(), mTransition.getWidth()));
        mCurva.seta.setStyle(StyleBuilder.fill(mTransition.getColor()));
        mCurva.rotulo.setStyle(StyleBuilder.font(mTransition.getColor(), mTransition.getTextStyle(), mTransition.getTextSize()));
        mCurva.rotulo.setText(getComputedLabel());
    }

    static class TransicaoEmArco extends Region {

        final Label rotulo = new Label();
        final Seta seta = new Seta();
        final Arc arco = new Arc();

        public TransicaoEmArco(Node origem, Node destino) {
            getChildren().addAll(rotulo, seta, arco);
            arco.radiusXProperty().bind(Geom.distance(origem, destino).divide(2));
            arco.radiusYProperty().bind(Geom.distance(origem, destino).divide(4));
            arco.centerXProperty().bind(arco.radiusXProperty());
            arco.centerYProperty().bind(arco.radiusYProperty().add(17));
            arco.setLength(180);
            arco.setType(ArcType.OPEN);
            arco.setFill(null);
            arco.setStroke(Color.BLACK);

            seta.layoutXProperty().bind(arco.radiusXProperty());
            seta.setLayoutY(17);
            rotulo.setLayoutY(5);
            rotulo.layoutXProperty().bind(arco.radiusXProperty().subtract(rotulo.widthProperty().divide(2)));
        }
    }
}
