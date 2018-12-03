package br.uece.lotus.msc.api.viewer.hMSC.interception_node;

import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public interface InterceptionNodeView extends GenericElementView {

    interface Factory{
        InterceptionNodeView create();
    }

    @Override
    GenericElement getGenericElement();

    Node getNode();
    boolean isInsideBounds(Point2D point);
    InterceptionNode getInterceptionNode();
    void setInterceptionNode(InterceptionNode interceptionNode);
}
