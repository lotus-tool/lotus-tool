package br.uece.lotus.msc.api.viewer.hMSC;

import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import javafx.scene.Node;

public interface GenericElementView  {

    Node getNode();

    GenericElement getGenericElement();
}
