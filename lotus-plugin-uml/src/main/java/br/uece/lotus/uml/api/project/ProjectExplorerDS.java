/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.project;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.seed.app.ExtensibleMenu;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public interface ProjectExplorerDS {
    
    
    interface Listener {
        void onChange(ProjectExplorerDS projectExplorerDS);
    }
    
    ExtensibleMenu getMenu();
    ExtensibleMenu getProjectDSMenu();
    ExtensibleMenu getComponentBuildDSMenu();
    ExtensibleMenu getComponentDSMenu();
    ExtensibleMenu getComponentLTSMenu();
    
    void open(ProjectDS p);
    void close(ProjectDS p);
    
    ProjectDS getSelectedProjectDS();
    ComponentBuildDS getSelectedComponentBuildDS();
    ComponentDS getSelectedComponentDS();
    Component getSelectedComponentLTS();
    
    List<ProjectDS> getSelectedProjectsDS();
    List<ComponentDS> getSelectedComponentsDS();
    List<Component> getSelectedComponentsLTS();
    
    void addListener(Listener l);
    void removeListener(Listener l);
            
}
