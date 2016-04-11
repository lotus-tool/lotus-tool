/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.layout.Circle;

import br.uece.lotus.Component;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.layout.TreeLayouter;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

/**
 *
 * @author vieir_000
 */
public class MainCircle extends Plugin {
    private UserInterface mUserInterface;
    private ProjectExplorer mPRojectExplorer;
    
     private final Runnable mCircleLayout = new Runnable() {
        @Override
        public void run() {
            new CircleLayout().aplicar(mPRojectExplorer.getSelectedComponent());
        }
    };
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mPRojectExplorer = extensionManager.get(ProjectExplorer.class);
        
        mUserInterface.getMainMenu().newItem("Model/Layout/Circle")
                .setWeight(Integer.MAX_VALUE)
                .setAction(() -> {
                   
                    Component c = mPRojectExplorer.getSelectedComponent();
                    new CircleLayout().aplicar(c);
                }).create();
    
    
}
}