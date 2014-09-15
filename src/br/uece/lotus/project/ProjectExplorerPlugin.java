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
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.designer.ComponentDesignerManager;
import br.uece.seed.app.ExtensibleFXContextMenu;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author emerson
 */
public final class ProjectExplorerPlugin extends Plugin implements ProjectExplorer {

    private UserInterface mUserInterface;

    private final ContextMenu mMnuWorkspace;
    private final ContextMenu mMnuProject;
    private final ContextMenu mMnuComponent;
    private final ExtensibleMenu mExtMnuWorkspace;
    private final ExtensibleMenu mExtMnuProject;
    private final ExtensibleMenu mExtMnuComponent;
    private final TreeView<Wrapper> mProjectView;

    private final Project.Listener mProjectListener = new Project.Listener() {
        @Override
        public void onChange(Project project) {
            TreeItem<Wrapper> itm = findItem(mProjectView.getRoot(), project);
            Wrapper tmp = itm.getValue();
            itm.setValue(null);
            itm.setValue(tmp);
        }

        @Override
        public void onComponentCreated(Project project, Component component) {
            TreeItem<Wrapper> r = findItem(mProjectView.getRoot(), project);
            r.getChildren().add(new TreeItem<>(new Wrapper(component), new ImageView(
                    new Image(getClass().getResourceAsStream("res/ic_component.png"))
            )));
        }

        @Override
        public void onComponentRemoved(Project project, Component component) {
            TreeItem<Wrapper> itmP = findItem(mProjectView.getRoot(), project);
            TreeItem<Wrapper> itmC = findItem(itmP, component);
            itmP.getChildren().remove(itmC);
        }
    };
    private final Component.Listener mComponentListener = new Component.Listener() {
        @Override
        public void onChange(Component component) {
            for (TreeItem<Wrapper> itm : mProjectView.getRoot().getChildren()) {
                for (TreeItem<Wrapper> sub : itm.getChildren()) {
                    if (sub.getValue().getObject() == component) {
                        Wrapper tmp = sub.getValue();
                        sub.setValue(null);
                        sub.setValue(tmp);
                        return;
                    }
                }
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
    private ComponentDesignerManager mComponentDesigner;

    public ProjectExplorerPlugin() {
        mMnuComponent = new ContextMenu();
        mMnuProject = new ContextMenu();
        mMnuWorkspace = new ContextMenu();

        mExtMnuComponent = new ExtensibleFXContextMenu(mMnuComponent);
        mExtMnuProject = new ExtensibleFXContextMenu(mMnuProject);
        mExtMnuWorkspace = new ExtensibleFXContextMenu(mMnuWorkspace);

        mProjectView = new TreeView<>();
        mProjectView.setRoot(new TreeItem<>());
        mProjectView.setShowRoot(false);
        mProjectView.setOnMouseClicked((MouseEvent e) -> {
            if (MouseButton.SECONDARY.equals(e.getButton())) {
                if (getSelectedComponent() != null) {
                    mMnuComponent.show(mProjectView, e.getScreenX(), e.getScreenY());
                } else if (getSelectedProject() != null) {
                    mMnuProject.show(mProjectView, e.getScreenX(), e.getScreenY());
                } else {
                    mMnuWorkspace.show(mProjectView, e.getScreenX(), e.getScreenY());
                }
            } else if (e.getClickCount() == 2) {
                Component c = getSelectedComponent();
                if (c != null) {
                    mComponentDesigner.show(c);
                }
            } else {
                mMnuComponent.hide();
                mMnuProject.hide();
                mMnuWorkspace.hide();
            }
        });
//        mProjectView.setCellFactory((TreeView<Wrapper> param) -> new ItemTreeCell());
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mComponentDesigner = extensionManager.get(ComponentDesignerManager.class);
        AnchorPane.setTopAnchor(mProjectView, 0D);
        AnchorPane.setRightAnchor(mProjectView, 0D);
        AnchorPane.setBottomAnchor(mProjectView, 0D);
        AnchorPane.setLeftAnchor(mProjectView, 0D);
        mUserInterface.getLeftPanel().newTab("Projects", mProjectView, false);
        //todo: mProjectView.getScene() is null!
//        System.out.println("mProjectView.getScene(): " + mProjectView.getScene());
//        mProjectView.getScene().setOnMouseReleased((MouseEvent mouseEvent) -> {
//            mMnuComponent.hide();
//            mMnuProject.hide();
//            mMnuWorkspace.hide();
//        });
    }

    @Override
    public ExtensibleMenu getMenu() {
        return mExtMnuWorkspace;
    }

    @Override
    public ExtensibleMenu getProjectMenu() {
        return mExtMnuProject;
    }

    @Override
    public ExtensibleMenu getComponentMenu() {
        return mExtMnuComponent;
    }

    @Override
    public void open(Project p) {
        TreeItem<Wrapper> itm = findItem(mProjectView.getRoot(), p);
        if (itm == null) {
            p.addListener(mProjectListener);
            itm = new TreeItem<>(new Wrapper(p), new ImageView(
                    new Image(getClass().getResourceAsStream("res/ic_project.png"))
            ));
            List<TreeItem<Wrapper>> aux = itm.getChildren();
            for (Component c : p.getComponents()) {
                c.addListener(mComponentListener);
                TreeItem<Wrapper> itm2 = new TreeItem<>(new Wrapper(c), new ImageView(
                        new Image(getClass().getResourceAsStream("res/ic_component.png"))
                ));
                aux.add(itm2);
            }
            mProjectView.getRoot().getChildren().add(itm);
        }
    }

    private TreeItem<Wrapper> findItem(TreeItem<Wrapper> root, Object obj) {
        for (TreeItem<Wrapper> itm : root.getChildren()) {
            if (itm.getValue().getObject() == obj) {
                return itm;
            }
        }
        return null;
    }

    @Override
    public void close(Project p) {
        TreeItem<Wrapper> itm = findItem(mProjectView.getRoot(), p);
        if (itm != null) {
            List<TreeItem<Wrapper>> aux = itm.getChildren();
            for (TreeItem<Wrapper> itm2 : aux) {
                ((Component) itm2.getValue().getObject()).removeListener(mComponentListener);
            }
            aux.clear();
            mProjectView.getRoot().getChildren().remove(itm);
        }
    }

    @Override
    public Project getSelectedProject() {
        TreeItem<Wrapper> itm = mProjectView.getSelectionModel().getSelectedItem();
        if (itm == null) {
            return null;
        }
        if (itm.getValue().getObject() instanceof Component) {
            itm = itm.getParent();
        }
        Object obj = itm.getValue().getObject();
        return (Project) obj;
    }

    @Override
    public Component getSelectedComponent() {
        TreeItem<Wrapper> itm = mProjectView.getSelectionModel().getSelectedItem();
        if (itm == null) {
            return null;
        }
        Object obj = itm.getValue().getObject();
        return obj instanceof Component ? (Component) obj : null;
    }

    @Override
    public List<Component> getSelectedComponents() {
        List<Component> aux = new ArrayList<>();
        for (TreeItem<Wrapper> itm : mProjectView.getSelectionModel().getSelectedItems()) {
            Object obj = itm.getValue().getObject();
            if (obj instanceof Component) {
                aux.add((Component) obj);
            }
        }
        return aux;
    }

    @Override
    public List<Project> getSelectedProjects() {
        List<Project> aux = new ArrayList<>();
        for (TreeItem<Wrapper> itm : mProjectView.getSelectionModel().getSelectedItems()) {
            Object obj = itm.getValue().getObject();
            if (obj instanceof Project) {
                aux.add((Project) obj);
            }
        }
        return aux;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> aux = new ArrayList<>();
        for (TreeItem<Wrapper> itm : mProjectView.getRoot().getChildren()) {
            Object obj = itm.getValue().getObject();
            if (obj instanceof Project) {
                aux.add((Project) obj);
            }
        }
        return aux;
    }

}

class Wrapper {

    private final Object mObj;

    public Wrapper(Object obj) {
        mObj = obj;
    }

    Object getObject() {
        return mObj;
    }

    void set(String s) {
        if (mObj instanceof Project) {
            ((Project) mObj).setName(s);
        } else {
            ((Component) mObj).setName(s);
        }
    }

    @Override
    public String toString() {
        return (mObj instanceof Project) ? ((Project) mObj).getName() : ((Component) mObj).getName();
    }

}

//class ItemTreeCell extends TreeCell<Wrapper> {
//
//    @Override
//    protected void updateItem(Wrapper obj, boolean empty) {
//        super.updateItem(obj, empty);
//        if (!empty && obj != null) {
//            setText(obj.toString());
//        } else {
//            setText(null);
//        }
//    }
//}
