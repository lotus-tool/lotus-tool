package br.uece.lotus.msc.api.model.msc.hmsc;

import br.uece.lotus.msc.api.model.msc.TransitionMSC;


import java.util.List;

public interface GenericElement {

    void setLayoutX(double layoutX);

    void setLayoutY(double layoutY);

    double getLayoutX();

    double getLayoutY();


    void addOutgoingTransition(TransitionMSC t);

    void addIncomingTransition(TransitionMSC t);

    List<TransitionMSC> getIncomingTransitionList();


    List<TransitionMSC> getOutgoingTransitionList();

    void removeOutgoingTransition(TransitionMSC t);

    void removeIncomingTransition(TransitionMSC t);

    int getID();

    TransitionMSC getTransitionTo(GenericElement genericElement);

    List<TransitionMSC> getTransitionsTo(GenericElement genericElement);

    Object getValue(String key);

    void putValue(String key, Object value);

    String getLabel();
}
