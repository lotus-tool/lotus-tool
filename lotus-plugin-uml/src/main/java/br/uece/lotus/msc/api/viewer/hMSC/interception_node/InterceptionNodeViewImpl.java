package br.uece.lotus.msc.api.viewer.hMSC.interception_node;

import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.viewer.StyleBuilder;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

public class InterceptionNodeViewImpl extends Region implements InterceptionNodeView,InterceptionNode.Listener  {
    public static final int RADIUS_CIRCLE = 15;
    private final Circle circle;
    private InterceptionNode interceptionNode;


    public InterceptionNodeViewImpl() {

        circle = new Circle(RADIUS_CIRCLE);

        getChildren().add(circle);
    }

    @Override
    public GenericElement getGenericElement() {
        return interceptionNode;
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public boolean isInsideBounds(Point2D point) {

        Point2D point2D = circle.localToScene(Point2D.ZERO);

        return point2D.distance(point) <= RADIUS_CIRCLE;
    }

    @Override
    public InterceptionNode getInterceptionNode() {
        return interceptionNode;
    }

    @Override
    public void setInterceptionNode(InterceptionNode interceptionNode) {

        if(this.interceptionNode != null){
            this.interceptionNode.removeListener(this);
        }

        this.interceptionNode = interceptionNode;

        if(this.interceptionNode != null){
            interceptionNode.addListener(this);

            updateView();
        }
    }

    @Override
    public void onChange(InterceptionNode interceptionNode) {
        updateView();
    }

    private void updateView() {
        setOpacity(0.1);
       setLayoutX(interceptionNode.getLayoutX());
       setLayoutY(interceptionNode.getLayoutY());
       setStyle(StyleBuilder.stroke(interceptionNode.getColor(), interceptionNode.getBorderWidth()));
    }
}
