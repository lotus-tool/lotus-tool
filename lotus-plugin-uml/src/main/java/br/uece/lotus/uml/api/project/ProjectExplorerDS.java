/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.api.project;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.StandardModeling;
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
    ExtensibleMenu getProjectMSCMenu();
    ExtensibleMenu getStandarModelingMenu();
    ExtensibleMenu getComponentBMSCMenu();
    ExtensibleMenu getComponentLTSMenu();
    
    void open(ProjectDS p);
    void close(ProjectDS p);
    void rename(ProjectDS p);
    
    ProjectDS getSelectedProjectDS();
    StandardModeling getSelectedComponentBuildDS();
    ComponentDS getSelectedBMSC();
    Component getSelectedComponentLTS();
    
    List<ProjectDS> getSelectedProjectsDS();
    List<ComponentDS> getSelectedBMSCs();
    List<Component> getSelectedComponentsLTS();
    List<ProjectDS> getAllProjectsDS();
    List<ComponentDS> getAll_BMSC();
    
    void addListener(Listener l);
    void removeListener(Listener l);
    void removeBMSC(ComponentDS bmsc);
    void clearSelecao();
    void clear2();
}
