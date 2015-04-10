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

import br.uece.lotus.Transition;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author emerson
 */
public class SelfTransitionViewImpl extends TransitionViewImpl {

    private TransicaoCirculo mTransicaoEmCirculo;

    @Override
    public boolean isInsideBounds(Point2D point) {
        return mTransicaoEmCirculo.seta.localToScene(Point2D.ZERO).distance(point) < 8;
    }

    @Override
    protected void prepareView() {
        StateViewImpl origem = (StateViewImpl) getTransition().getSource().getValue("view");
        mTransicaoEmCirculo = new TransicaoCirculo();
        getChildren().add(mTransicaoEmCirculo);
        mTransicaoEmCirculo.layoutXProperty().bind(origem.layoutXProperty().add(origem.widthProperty().divide(2)));
        mTransicaoEmCirculo.layoutYProperty().bind(origem.layoutYProperty().subtract(origem.widthProperty().divide(4)));
    }

    @Override
    protected void updateView() {
        Transition t = getTransition();
        mTransicaoEmCirculo.loop.setStyle(StyleBuilder.stroke(t.getColor(), t.getWidth()));
        mTransicaoEmCirculo.seta.setStyle(StyleBuilder.fill(t.getColor()));
        mTransicaoEmCirculo.rotulo.setStyle(StyleBuilder.font(mTransition.getTextColor(), mTransition.getTextStyle(), mTransition.getTextSize()));
        mTransicaoEmCirculo.rotulo.setText(getComputedLabel());
    }

    static class TransicaoCirculo extends Region {

        static final double RAIO_ELIPSE_X = 25;
        static final double RAIO_ELIPSE_Y = 20;
        static final double POSICAO_SETA_X = 2 * RAIO_ELIPSE_X;
        static final double POSICAO_SETA_Y = RAIO_ELIPSE_Y;

        final Label rotulo;
        final Seta seta;
        final Ellipse loop;

        public TransicaoCirculo() {
            rotulo = new Label();
            seta = new Seta();
            seta.setRotate(90);

            loop = new Ellipse();
            loop.setFill(null);
            loop.setRadiusX(25);
            loop.setRadiusY(20);

            loop.setLayoutX(RAIO_ELIPSE_X);
            loop.setLayoutY(RAIO_ELIPSE_Y);
            seta.setLayoutX(POSICAO_SETA_X);
            seta.setLayoutY(POSICAO_SETA_Y);
            rotulo.setLayoutX(2 * RAIO_ELIPSE_X);
            rotulo.setLayoutY(RAIO_ELIPSE_Y - 6);
            getChildren().addAll(rotulo, seta, loop);
        }
    }

}