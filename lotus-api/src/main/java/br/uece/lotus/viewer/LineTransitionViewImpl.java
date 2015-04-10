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

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 *
 * @author emerson
 */
public class LineTransitionViewImpl extends TransitionViewImpl {
    
    private final Label mRotulo;
    private final Seta mSeta;
    private final Line mLinha;
    
    public LineTransitionViewImpl() {
        mRotulo = new Label();
        getChildren().add(mRotulo);
        
        mSeta = new Seta();
        getChildren().add(mSeta);
        
        mLinha = new Line();
        getChildren().add(mLinha);
    }
    
    @Override
    public boolean isInsideBounds(Point2D point) {
        return mSeta.localToScene(Point2D.ZERO).distance(point) < 8;
    }
    
    @Override
    protected void prepareView() {
        Region origem = (Region) mSourceStateView.getNode();
        Region destino = (Region) mDestinyStateView.getNode();

        mSeta.rotateProperty().bind(Geom.angle(origem, destino));
        mSeta.rotateProperty().bind(new Geom.CartesianCase(origem, destino)
                .first(Geom.angle(origem, destino))
                .second(Geom.angle(origem, destino).add(180))
                .thirth(Geom.angle(origem, destino).add(180))
                .fourth(Geom.angle(origem, destino))
                .firstAndSecond(Geom.angle(origem, destino).add(180))
                .secondAndThirth(Geom.angle(origem, destino).add(180))
                .thirthAndFourth(Geom.angle(origem, destino))
        );

        DoubleBinding origemX = origem.layoutXProperty().add(origem.widthProperty().divide(2));
        DoubleBinding origemY = origem.layoutYProperty().add(origem.heightProperty().divide(2));
        DoubleBinding destinoX = destino.layoutXProperty().add(destino.widthProperty().divide(2));
        DoubleBinding destinoY = destino.layoutYProperty().add(destino.heightProperty().divide(2));

        mLinha.startXProperty().bind(origemX);
        mLinha.startYProperty().bind(origemY);
        mLinha.endXProperty().bind(destinoX);
        mLinha.endYProperty().bind(destinoY);

        NumberBinding meioCaminhoX = mLinha.endXProperty().subtract(mLinha.startXProperty()).divide(2);
        NumberBinding meioCaminhoY = mLinha.endYProperty().subtract(mLinha.startYProperty()).divide(2);

        mSeta.layoutXProperty().bind(origemX.add(meioCaminhoX));
        mSeta.layoutYProperty().bind(origemY.add(meioCaminhoY));

        mRotulo.layoutXProperty().bind(mSeta.layoutXProperty().subtract(mRotulo.widthProperty().divide(2)));
        mRotulo.layoutYProperty().bind(mSeta.layoutYProperty().subtract(mRotulo.heightProperty()));
        
        mRotulo.translateXProperty().bind(origem.widthProperty().divide(2));
        mRotulo.translateYProperty().bind(origem.heightProperty().divide(2));
    }
    
    @Override
    protected void updateView() {
        mLinha.setStyle(StyleBuilder.stroke(mTransition.getColor(), mTransition.getWidth()));
        mSeta.setStyle(StyleBuilder.fill(mTransition.getColor()));
        mRotulo.setStyle(StyleBuilder.font(mTransition.getTextColor(), mTransition.getTextStyle(), mTransition.getTextSize()));
        mRotulo.setText(getComputedLabel());
    }

}
