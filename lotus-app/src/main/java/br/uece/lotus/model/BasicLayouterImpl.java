package br.uece.lotus.model;

import br.uece.seed.app.UserInterface;
import br.uece.lotus.Component;
import br.uece.lotus.State;

public class BasicLayouterImpl {

    private UserInterface mUI;

//    @Override
//    public void onStart(ExtensionManager extensionManager) throws Exception {
//        System.out.println("Layouter onStart");
//        try {
//            mUI = extensionManager.get(UserInterface.class);            
//
//            mUI.addMainMenuEntry("Modelo/Layout/Simples", () -> {
//                System.out.println("oi");
//                if (mUI.getSelectedComponents().size() < 1) {
//                    throw new RuntimeException("Selecione um componente!");
//                }
//                layout(mUI.getSelectedComponents().get(0));
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public void layout(Component component) {
        int i = 1;
        for (State state : component.getStates()) {
            state.setLayoutX(i * 100);
            state.setLayoutY(100 + (i % 10));
            i++;
        }
    }

}
