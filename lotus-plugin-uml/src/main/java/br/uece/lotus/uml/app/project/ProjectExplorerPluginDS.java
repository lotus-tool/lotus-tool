/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.Component;
import br.uece.lotus.uml.api.ds.BlockBuildDS;
import br.uece.lotus.uml.api.ds.ComponentBuildDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.ds.TransitionBuildDS;
import br.uece.lotus.uml.api.project.ProjectExplorerDS;
import br.uece.seed.app.ExtensibleFXContextMenu;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Bruno Barbosa
 */
public final class ProjectExplorerPluginDS extends Plugin implements ProjectExplorerDS{

    private UserInterface mUserInterface;
    
    private final ContextMenu mMnuWorkspace;
    private final ContextMenu mMnuProjectDS;
    private final ContextMenu mMnuComponentBuildDS;
    private final ContextMenu mMnuComponentDS;
    private final ContextMenu mMnuComponentLTS;
    
    private final ExtensibleMenu mExtMnuWorkspace;
    private final ExtensibleMenu mExtMnuProjectDS;
    private final ExtensibleMenu mExtMnuComponentBuildDS;
    private final ExtensibleMenu mExtMnuComponentDS;
    private final ExtensibleMenu mExtMnuComponentLTS;
    
    private final TreeView<WrapperDS> mProjectDSView;
    
    private final List<Listener> mListeners;
    
    private final ProjectDS.Listener mProjectDSListener = new ProjectDS.Listener() {

        @Override
        public void onChange(ProjectDS project) {
            TreeItem<WrapperDS> itm = findItem(mProjectDSView.getRoot(), project);
            WrapperDS w = itm.getValue();
            itm.setValue(null);
            itm.setValue(w);
        }

        @Override
        public void onComponentDSCreated(ProjectDS project, ComponentDS componentDs) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> listaDeDiagramas = findItem(raiz, "Sequence Diagrams");
            if(listaDeDiagramas != null){
                TreeItem<WrapperDS> cds = new TreeItem<>(new WrapperDS(componentDs)/*, */);
                listaDeDiagramas.getChildren().add(cds);
            }else{
                TreeItem<WrapperDS> pastaDS = new TreeItem<>(new WrapperDS("Sequence Diagrams")/*,graph */);
                TreeItem<WrapperDS> cds = new TreeItem<>(new WrapperDS(componentDs)/*, */);
                pastaDS.getChildren().add(cds);
                raiz.getChildren().add(pastaDS);
                findItem(raiz, "Sequence Diagrams").setExpanded(true);
            }
        }

        @Override
        public void onComponentDSRemoved(ProjectDS project, ComponentDS componentDs) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> listaDeDiagramas = findItem(raiz, "Sequence Diagrams");
            TreeItem<WrapperDS> cDS = findItem(listaDeDiagramas, componentDs);
            listaDeDiagramas.getChildren().remove(cDS);
        }

        @Override
        public void onComponentLTSCreated(ProjectDS project, ComponentDS cds, Component component) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> listaDeDiagramas = findItem(raiz, "Sequence Diagrams");
            TreeItem<WrapperDS> compDS = findItem(listaDeDiagramas, cds);
            TreeItem<WrapperDS> listaDeFragmentos = findItem(compDS, "Fragments LTS");
            if(listaDeDiagramas != null){
                listaDeFragmentos.getChildren().add(new TreeItem<>(new WrapperDS(component)));
            }else{
                TreeItem<WrapperDS> fragment = new TreeItem<>(new WrapperDS("Fragments LTS"));
                TreeItem<WrapperDS> c = new TreeItem<>(new WrapperDS(component));
                fragment.getChildren().add(c);
                compDS.getChildren().add(fragment);
                findItem(compDS, "Fragments LTS").setExpanded(true);
            }
        }

        @Override
        public void onComponentLTSRemoved(ProjectDS project, ComponentDS cds, Component component) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> listaDeDiagramas = findItem(raiz, "Sequence Diagrams");
            TreeItem<WrapperDS> compDS = findItem(listaDeDiagramas, cds);
            TreeItem<WrapperDS> listaDeFragmentos = findItem(compDS, "Fragments LTS");
            TreeItem<WrapperDS> c = findItem(listaDeFragmentos, component);
            listaDeFragmentos.getChildren().remove(c);
        }

        @Override
        public void onComponentLTSGeralCreated(ProjectDS projectDS, ComponentBuildDS buildDS, Component component) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), projectDS);
            TreeItem<WrapperDS> build = findItem(raiz, buildDS);
            build.getChildren().add(new TreeItem<>(new WrapperDS(component)/*, */));
        }

        @Override
        public void onComponentLTSGeralRemove(ProjectDS project, ComponentBuildDS buildDS, Component component) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> build = findItem(raiz, buildDS);
            TreeItem<WrapperDS> compGeralLTS = findItem(build, component);
            build.getChildren().remove(compGeralLTS);
        }

        @Override
        public void onComponentLTSFragmentOfBuildDSCreate(ProjectDS project, ComponentBuildDS buildDS, Component componentGeralLTS, Component frag) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> build = findItem(raiz, buildDS);
            TreeItem<WrapperDS> compGeralLTS = findItem(build, componentGeralLTS);
            compGeralLTS.getChildren().add(new TreeItem<>(new WrapperDS(frag)/*, */));
        }

        @Override
        public void onComponentLTSFragmentOfBuildDSRemove(ProjectDS project, ComponentBuildDS buildDS, Component componentGeralLTS, Component frag) {
            TreeItem<WrapperDS> raiz = findItem(mProjectDSView.getRoot(), project);
            TreeItem<WrapperDS> build = findItem(raiz, buildDS);
            TreeItem<WrapperDS> compGeralLTS = findItem(build, componentGeralLTS);
            TreeItem<WrapperDS> fragment = findItem(compGeralLTS, frag);
            compGeralLTS.getChildren().remove(fragment);
        }

    };
    
    private  final ComponentBuildDS.Listener mComponentBuildListener = new ComponentBuildDS.Listener() {

        @Override
        public void onChange(ComponentBuildDS buildDS) {
            for(TreeItem<WrapperDS> p : mProjectDSView.getRoot().getChildren()){
                for(TreeItem<WrapperDS> itm : p.getChildren()){
                    if(itm.getValue().getObject() == buildDS){
                        WrapperDS w = itm.getValue();
                        itm.setValue(null);
                        itm.setValue(w);
                        return;
                    }
                }
            }
        }
        @Override
        public void onBlockCreate(ComponentBuildDS buildDS, BlockBuildDS bbds) {}
        @Override
        public void onBlockRemove(ComponentBuildDS buildDS, BlockBuildDS bbds) {}
        @Override
        public void onTransitionCreate(ComponentBuildDS buildDS, TransitionBuildDS t) {}
        @Override
        public void onTransitionRemove(ComponentBuildDS buildDS, TransitionBuildDS t) {}
    };

    //Falta os listeners do componentDS e ComponentLTS <- (o normal)
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception{
        mUserInterface = extensionManager.get(UserInterface.class);
        
        AnchorPane.setTopAnchor(mProjectDSView, 0D);
        AnchorPane.setRightAnchor(mProjectDSView, 0D);
        AnchorPane.setBottomAnchor(mProjectDSView, 0D);
        AnchorPane.setLeftAnchor(mProjectDSView, 0D);
        mUserInterface.getLeftPanel().newTab("Projects UML", mProjectDSView, false);
    }
    
    public ProjectExplorerPluginDS(){
        mListeners = new ArrayList<>();
        mMnuWorkspace = new ContextMenu();
        mMnuProjectDS = new ContextMenu();
        mMnuComponentBuildDS = new ContextMenu();
        mMnuComponentDS = new ContextMenu();
        mMnuComponentLTS = new ContextMenu();
        
        mExtMnuWorkspace = new ExtensibleFXContextMenu(mMnuWorkspace);
        mExtMnuProjectDS = new ExtensibleFXContextMenu(mMnuProjectDS);
        mExtMnuComponentBuildDS = new ExtensibleFXContextMenu(mMnuComponentBuildDS);
        mExtMnuComponentDS = new ExtensibleFXContextMenu(mMnuComponentDS);
        mExtMnuComponentLTS = new ExtensibleFXContextMenu(mMnuComponentLTS);
        
        mProjectDSView = new TreeView<>();
        mProjectDSView.setRoot(new TreeItem<>());
        mProjectDSView.setShowRoot(false);
        mProjectDSView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mProjectDSView.setOnMouseClicked((MouseEvent e) -> {
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                if(getSelectedComponentBuildDS() != null){
                    mMnuComponentBuildDS.show(mProjectDSView, e.getSceneX(),e.getSceneY());
                    mMnuComponentDS.hide();
                    mMnuComponentLTS.hide();
                    mMnuProjectDS.hide();
                    mMnuWorkspace.hide();
                }
                else if(getSelectedComponentDS() != null){
                    mMnuComponentBuildDS.hide();
                    mMnuComponentDS.show(mProjectDSView, e.getSceneX(),e.getSceneY());
                    mMnuComponentLTS.hide();
                    mMnuProjectDS.hide();
                    mMnuWorkspace.hide();
                }
                else if(getSelectedComponentLTS() != null){
                    mMnuComponentBuildDS.hide();
                    mMnuComponentDS.hide();
                    mMnuComponentLTS.show(mProjectDSView, e.getSceneX(),e.getSceneY());
                    mMnuProjectDS.hide();
                    mMnuWorkspace.hide();
                }
                else if(getSelectedProjectDS() != null){
                    mMnuComponentBuildDS.hide();
                    mMnuComponentDS.hide();
                    mMnuComponentLTS.hide();
                    mMnuProjectDS.show(mProjectDSView, e.getSceneX(),e.getSceneY());
                    mMnuWorkspace.hide();
                }else{
                    mMnuComponentBuildDS.hide();
                    mMnuComponentDS.hide();
                    mMnuComponentLTS.hide();
                    mMnuProjectDS.hide();
                    mMnuWorkspace.show(mProjectDSView, e.getSceneX(),e.getSceneY());
                }
            }else if(e.getClickCount() == 2){
                ComponentBuildDS cbds = getSelectedComponentBuildDS();
                System.out.println("Clicou 2x no ComponentBuildDS: "+cbds);
                ComponentDS cds = getSelectedComponentDS();
                Component c = getSelectedComponentLTS();
                if(cbds != null){
                    mExtMnuComponentBuildDS.triggerDefaultAction();
                    System.out.println("Chamou o triggerDefaultAction do click 2x");
                }else if(cds != null){
                    mExtMnuComponentDS.triggerDefaultAction();
                }else if(c != null){
                    mExtMnuComponentLTS.triggerDefaultAction();
                }
            }else{
                mMnuComponentBuildDS.hide();
                mMnuComponentDS.hide();
                mMnuComponentLTS.hide();
                mMnuProjectDS.hide();
                mMnuWorkspace.hide();
            }
        });
    }
    
    @Override
    public ExtensibleMenu getMenu() {
        return mExtMnuWorkspace;
    }

    @Override
    public ExtensibleMenu getProjectDSMenu() {
        return mExtMnuProjectDS;
    }

    @Override
    public ExtensibleMenu getComponentBuildDSMenu() {
        return mExtMnuComponentBuildDS;
    }

    @Override
    public ExtensibleMenu getComponentDSMenu() {
        return mExtMnuComponentDS;
    }

    @Override
    public ExtensibleMenu getComponentLTSMenu() {
        return mExtMnuComponentLTS;
    }

    @Override
    public void open(ProjectDS p) {
        if(p == null){
            throw new IllegalArgumentException("project can not be null!");
        }
        TreeItem<WrapperDS> project = findItem(mProjectDSView.getRoot(), p);
        if(project == null){
            p.addListener(mProjectDSListener);
            project = new TreeItem<>(new WrapperDS(p)/*, new ImageView(
                    new Image(getClass().getResourceAsStream("/images/ic_project.png")))*/);
            List<TreeItem<WrapperDS>> filhos = project.getChildren();
            ComponentBuildDS buildDS = p.getComponentBuildDS();
            if (buildDS != null){
                buildDS.addListener(mComponentBuildListener);
                TreeItem<WrapperDS> compBuildDs = new TreeItem<>(new WrapperDS(buildDS)/*,new ImageView(
                                    new Image(getClass().getResourceAsStream("/images/ic_component.png"))) */);
                filhos.add(compBuildDs);
            }
            // continuar para toda a hierarquia...
            mProjectDSView.getRoot().getChildren().add(project);
        }
    }

    @Override
    public void close(ProjectDS p) {
        if(p == null){
            throw new IllegalArgumentException("project can not be null!");
        }
        //Continua para fechar toda a hierarquia, que ainda nao foi implementada
    }

    @Override
    public ProjectDS getSelectedProjectDS() {
        TreeItem<WrapperDS> projeto = mProjectDSView.getSelectionModel().getSelectedItem();
        if(projeto == null){
            return null;
        }
        Object obj = projeto.getValue().getObject();
        if(obj instanceof ComponentBuildDS){
            projeto = projeto.getParent();
        }
        else if(obj instanceof ComponentDS){
            projeto = projeto.getParent().getParent();
        }
        else if(obj instanceof Component){
            projeto = projeto.getParent().getParent().getParent();
        }
        return (ProjectDS) projeto.getValue().getObject();
    }

    @Override
    public ComponentBuildDS getSelectedComponentBuildDS() {
        TreeItem<WrapperDS> buildDs = mProjectDSView.getSelectionModel().getSelectedItem();
        if(buildDs == null){
            return null;
        }
        Object obj = buildDs.getValue().getObject();
        return obj instanceof ComponentBuildDS ? (ComponentBuildDS)obj : null;
    }

    @Override
    public ComponentDS getSelectedComponentDS() {
        TreeItem<WrapperDS> ds = mProjectDSView.getSelectionModel().getSelectedItem();
        if(ds == null){
            return null;
        }
        Object obj = ds.getValue().getObject();
        return obj instanceof ComponentDS ? (ComponentDS)obj : null;
    }

    @Override
    public Component getSelectedComponentLTS() {
        TreeItem<WrapperDS> lts = mProjectDSView.getSelectionModel().getSelectedItem();
        if(lts == null){
            return null;
        }
        Object obj = lts.getValue().getObject();
        return obj instanceof Component ? (Component)obj : null;
    }

    @Override
    public List<ProjectDS> getSelectedProjectsDS() {
        List<ProjectDS> aux = new ArrayList<>();
        for(TreeItem<WrapperDS> p : mProjectDSView.getSelectionModel().getSelectedItems()){
            Object obj = p.getValue().getObject();
            if(obj instanceof ProjectDS){
                aux.add((ProjectDS)obj);
            }
        }
        return aux;
    }

    @Override
    public List<ComponentDS> getSelectedComponentsDS() {
        List<ComponentDS> aux = new ArrayList<>();
        for(TreeItem<WrapperDS> cds : mProjectDSView.getSelectionModel().getSelectedItems()){
            Object obj = cds.getValue().getObject();
            if(obj instanceof ComponentDS){
                aux.add((ComponentDS)obj);
            }
        }
        return aux;
    }

    @Override
    public List<Component> getSelectedComponentsLTS() {
        List<Component> aux = new ArrayList<>();
        for(TreeItem<WrapperDS> c : mProjectDSView.getSelectionModel().getSelectedItems()){
            Object obj = c.getValue().getObject();
            if(obj instanceof Component){
                aux.add((Component)obj);
            }
        }
        return aux;
    }
    
    @Override
    public List<ProjectDS> getAllProjectsDS() {
        List<ProjectDS> aux = new ArrayList<>();
        for (TreeItem<WrapperDS> itm : mProjectDSView.getRoot().getChildren()) {
            Object obj = itm.getValue().getObject();
            if (obj instanceof ProjectDS) {
                aux.add((ProjectDS) obj);
            }
        }
        return aux;
    }

    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    @Override
    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
    private TreeItem<WrapperDS> findItem(TreeItem<WrapperDS> root, Object obj) {
        for (TreeItem<WrapperDS> itm : root.getChildren()) {
            if (itm.getValue().getObject() == obj) {
                return itm;
            }
        }
        return null;
    }
    
}

class WrapperDS {
    
    private final Object mObj;
    
    public WrapperDS(Object obj) {
        this.mObj = obj;
    }
    
    Object getObject() {
        return mObj;
    }
    
    void set(String s) {
        if (mObj instanceof ProjectDS) {
            ((ProjectDS) mObj).setName(s);
        } 
        else if(mObj instanceof ComponentBuildDS){
            ((ComponentBuildDS) mObj).setName(s);
        }
        else if(mObj instanceof ComponentDS){
            ((ComponentDS) mObj).setName(s);
        }
        else if(mObj instanceof Component){
            ((Component) mObj).setName(s);
        }
    }
    
    @Override
    public String toString(){
        String s = "";
        if(mObj instanceof ProjectDS){
            s = ((ProjectDS) mObj).getName();
        }
        else if(mObj instanceof ComponentBuildDS){
            s = ((ComponentBuildDS) mObj).getName();
        }
        else if(mObj instanceof ComponentDS){
            s = ((ComponentDS) mObj).getName();
        }
        else if(mObj instanceof Component){
            s = ((Component) mObj).getName();
        }
        else{
            s = ((String) mObj);
        }
        
        return s;
    }
}
