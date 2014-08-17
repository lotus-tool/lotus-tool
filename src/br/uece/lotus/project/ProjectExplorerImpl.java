/*
 * The MIT License
 *
 * Copyright 2014 emerson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uece.lotus.project;

import br.uece.lotus.Component;
import br.uece.lotus.designer.ComponentDesigner;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.seed.app.ExtensibleFXContextMenu;
import br.uece.seed.app.ExtensibleFXMenu;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author emerson
 */
public class ProjectExplorerImpl extends Plugin implements ProjectExplorer {

    private UserInterface mUserInterface;
    private Project mProject;
    private ListView mProjectView;
    private ContextMenu mComponentsContextMenu;
    private ExtensibleFXContextMenu mExtensibleComponentsMenu;
    private ComponentDesigner mComponentEditor;
    private int mComponentId;

    private final EventHandler<MouseEvent> mAoDuploCliqueListaComponentes = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getButton().equals(MouseButton.PRIMARY)) {
                if (t.getClickCount() == 2) {
                    mComponentEditor.show(getSelectedComponent());
                }
            }
        }
    };
    private final Project.Listener mProjectListener = new Project.Listener() {

        @Override
        public void onChange(Project project) {
            //ignora
        }

        @Override
        public void onComponentCreated(Project project, Component component) {
            mProjectView.getItems().add(component.getName());
            component.addListener(mComponentListener);
        }

        @Override
        public void onComponentRemoved(Project project, Component component) {
            mProjectView.getItems().remove(component.getName());
            mComponentEditor.hide(component);
        }
    };
    private final Component.Listener mComponentListener = new Component.Listener() {
        @Override
        public void onChange(Component component) {
            System.out.println("onChange " + component);
            int i = mProject.indexOfComponent(component);
            if (i >= 0) {
                final ObservableList<String> items = mProjectView.getItems();
                items.remove(i);
                items.add(i, component.getName());
            }
        }

        @Override
        public void onStateCreated(Component component, State state) {

        }

        @Override
        public void onStateRemoved(Component component, State state) {

        }

        @Override
        public void onTransitionCreated(Component component, Transition state) {

        }

        @Override
        public void onTransitionRemoved(Component component, Transition state) {

        }
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);        
        mComponentEditor = extensionManager.get(ComponentDesigner.class);

        mProjectView = new ListView();               
        mProjectView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        mProjectView.setOnMouseClicked(mAoDuploCliqueListaComponentes);
        mComponentsContextMenu = new ContextMenu();
        mExtensibleComponentsMenu = new ExtensibleFXContextMenu(mComponentsContextMenu);
        mProjectView.setContextMenu(mComponentsContextMenu);

        mUserInterface.getLeftPanel().newTab("Projects", mProjectView, false);
    }

    @Override
    public Project getSelectedProject() {
        return mProject;
    }

    @Override
    public Component getSelectedComponent() {
        int i = mProjectView.getSelectionModel().getSelectedIndex();
        return i >= 0 ? mProject.getComponent(i) : null;
    }
    
    @Override
    public List<Component> getSelectedComponents() {
        List<Integer> indices = mProjectView.getSelectionModel().getSelectedIndices();
        List<Component> aux = new ArrayList<>();
        for (Integer i: indices) {
            aux.add(mProject.getComponent(i));
        }
        return aux;
    }

    @Override
    public void changeProject(Project p) {
        if (mProject != null) {
            for (Component c : mProject.getComponents()) {
                c.removeListener(mComponentListener);
            }
            mProject.removeListener(mProjectListener);
        }
        mProject = p;
        if (mProject != null) {
            List<String> aux = mProjectView.getItems();
            aux.clear();
            for (Component c : mProject.getComponents()) {
                aux.add(c.getName());
                c.addListener(mComponentListener);
            }
            mProject.addListener(mProjectListener);
//            mTabComponentes.getTabs().clear();
//            if (p.getComponentsCount() > 0) {
//                changeComponent(p.getComponent(0));
//            } else {
//                changeComponent(null);
//            }
        }
    }

    @Override
    public ExtensibleMenu getMenu() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExtensibleMenu getProjectMenu() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ExtensibleMenu getComponentMenu() {
        return mExtensibleComponentsMenu;
    }

}
