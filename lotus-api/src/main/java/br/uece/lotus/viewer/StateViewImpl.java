/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
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
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

public class StateViewImpl extends Region implements StateView, State.Listener {

    static final int RAIO_CIRCULO = 15;

    private final Circle mCircle;
    private final Circle mSecondCircle;
    private final Label mText;

    private State mState;
    private static final String DEFAULT_INITIAL_COLOR = "yellow";
    private static final String DEFAULT_FINAL_COLOR = "gray";
    private static final String DEFAULT_ERROR_COLOR = "red";
    private static final String DEFAULT_NORMAL_COLOR = "aqua";

    public StateViewImpl() {
        mCircle = new Circle(RAIO_CIRCULO);
        mSecondCircle = new Circle(RAIO_CIRCULO - 3);
        mSecondCircle.setFill(null);

        //setStyle("-fx-border-color: red;");

        getChildren().addAll(mCircle, mSecondCircle);

        mCircle.setLayoutX(RAIO_CIRCULO);
        mCircle.setLayoutY(RAIO_CIRCULO);
        mSecondCircle.setLayoutX(RAIO_CIRCULO);
        mSecondCircle.setLayoutY(RAIO_CIRCULO);

        mText = new Label();
        mText.layoutXProperty().bind(mCircle.layoutXProperty().subtract(mText.widthProperty().divide(2)));
        mText.layoutYProperty().bind(mCircle.layoutYProperty().subtract(mText.heightProperty().divide(2)));
        getChildren().add(mText);
    }

    public void setState(State state) {
        if (mState != null) {
            mState.removeListener(this);
        }
        mState = state;
        if (state != null) {
            mState.addListener(this);
            updateView();
        }
    }

    @Override
    public boolean isInsideBounds(Point2D point) {
        Point2D aux = mCircle.localToScene(Point2D.ZERO);
        //System.out.printf("(%f %f) (%f %f)\n", aux.getX(), aux.getY(), point.getX(), point.getY());
        return aux.distance(point) <= RAIO_CIRCULO;
    }

    public State getState() {
        return mState;
    }

    private void updateView() {
        String style = "-fx-effect: dropshadow( gaussian , gray , 3 , 0.2 , 1 , 1);";
        style += "-fx-fill: linear-gradient(to bottom right, white, " + computedColor() + ");";
        style += "-fx-stroke: " + (mState.getBorderColor() == null ? "black" : mState.getBorderColor()) + ";";
        style += "-fx-stroke-width: " + (mState.getBorderWidth() == null ? "1" : mState.getBorderWidth()) + ";";
        mCircle.setStyle(style);

        if (mState.isFinal()) {
            mSecondCircle.setStyle(style);
        }

        style = "-fx-text-fill: " + (mState.getTextColor() == null ? "black" : mState.getTextColor()) + ";";
        style += "-fx-font-weight: " + (mState.getTextStyle() == null ? "normal" : mState.getTextStyle()) + ";";
        style += "-fx-font-size: " + (mState.getTextSize() == null ? "12" : mState.getTextSize()) + ";";
        mText.setStyle(style);
        setLayoutX(mState.getLayoutX());
        setLayoutY(mState.getLayoutY());
        mText.setText(computedLabel());
    }

    @Override
    public void onChange(State state) {
        updateView();
    }

    private String computedColor() {
        String cor = mState.getColor();
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
        return cor;
    }

    private String computedLabel() {
        if (mState.isError()) {
            return "-1";
        } else if (mState.isFinal()) {
            return "E";
        }
        return mState.getLabel();
    }

    @Override
    public Node getNode() {
        return this;
    }
}
