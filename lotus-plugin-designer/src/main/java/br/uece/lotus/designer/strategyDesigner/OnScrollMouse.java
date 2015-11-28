/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.designer.strategyDesigner;

import br.uece.lotus.designer.DesignerWindowImpl;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 *
 * @author Bruno Barbosa
 */
public class OnScrollMouse implements Strategy{

    
    @Override
    public void onScrollMouse(DesignerWindowImpl dwi, ScrollEvent event) {
        if (event.isControlDown()) {
            dwi.zoomReset.setSelected(false);
            final double SCALE_DELTA = 1.1;
            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
            zoom(dwi.mViewer.getNode(), event.getX(), event.getY(), scaleFactor);
        }
    }
    
    private void zoom(Node node, double centerX, double centerY, double factor) {
        final Point2D center = node.localToParent(centerX, centerY);
        final Bounds bounds = node.getBoundsInParent();
        final double w = bounds.getWidth();
        final double h = bounds.getHeight();

        final double dw = w * (factor - 1);
        final double xr = 2 * (w / 2 - (center.getX() - bounds.getMinX())) / w;

        final double dh = h * (factor - 1);
        final double yr = 2 * (h / 2 - (center.getY() - bounds.getMinY())) / h;

        node.setScaleX(node.getScaleX() * factor);
        node.setScaleY(node.getScaleY() * factor);
        node.setTranslateX(node.getTranslateX() + xr * dw / 2);
        node.setTranslateY(node.getTranslateY() + yr * dh / 2);
    }
    
    @Override
    public void onClickedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onMovedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragDetectedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onDragOverMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDragDroppedMouse(DesignerWindowImpl dwi, DragEvent event) {}

    @Override
    public void onDraggedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onPressedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onReleasedMouse(DesignerWindowImpl dwi, MouseEvent event) {}

    @Override
    public void onKeyPressed(DesignerWindowImpl dwi, KeyEvent event) {}

    @Override
    public void onKeyReleased(DesignerWindowImpl dwi, KeyEvent event) {}

    
}
