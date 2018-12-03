/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.project;

import br.uece.lotus.Component;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
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
    
    void open(ProjectMSC p);
    void close(ProjectMSC p);
    void rename(ProjectMSC p);
    
    ProjectMSC getSelectedProjectDS();
    HmscComponent getSelectedComponentBuildDS();
    BmscComponent getSelectedBMSC();
    Component getSelectedComponentLTS();
    
    List<ProjectMSC> getSelectedProjectsDS();
    List<BmscComponent> getSelectedBMSCs();
    List<Component> getSelectedComponentsLTS();
    List<ProjectMSC> getAllProjectsDS();
    List<BmscComponent> getAll_BMSC();

    
    void addListener(Listener l);
    void removeListener(Listener l);
    void removeBMSC(BmscComponent bmsc);

    void removeFragmetsLTS();
    void clearSelecao();
    void clear2();
}
