package br.uece.lotus.msc.api.viewer.hMSC.interception_node;

public class InterceptionNodeViewFactory implements InterceptionNodeView.Factory {
    @Override
    public InterceptionNodeView create() {
        return new InterceptionNodeViewImpl();
    }
}
