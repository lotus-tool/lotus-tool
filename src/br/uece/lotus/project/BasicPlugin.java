/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
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
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.JarModule;
import br.uece.seed.ext.Plugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BasicPlugin extends Plugin {

    private static final Logger logger = Logger.getLogger(JarModule.class.getName());
    private File mProjectFile;
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private FileChooser mFileChooser;
    private Map<Project, File> mProjectsFiles = new HashMap<>();

    private Runnable mNewComponent = () -> {
        SwingUtilities.invokeLater(() -> {
            Project p = mProjectExplorer.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "Please select a project!");
                return;
            }
            String name = JOptionPane.showInputDialog(null, "Enter the new component's name", "Component" + (p.getComponentsCount() + 1));
            if (name == null) {
                return;
            }
            Component c = new Component();
            c.setName(name);
            p.addComponent(c);
        });
    };
    private Runnable mNewProject = () -> {
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Enter the new project's name", "Untitled");
            if (name == null) {
                return;
            }
            Project p = new Project();
            p.setName(name);
            Component c = new Component();
            c.setName("Component" + (p.getComponentsCount() + 1));
            p.addComponent(c);
            mProjectExplorer.open(p);
        });
    };
    private Runnable mRenameProject = () -> {
        SwingUtilities.invokeLater(() -> {
            Project p = mProjectExplorer.getSelectedProject();
            String novoNome = JOptionPane.showInputDialog(null, "Enter the new name for \"" + p.getName() + "\"", "Rename project", JOptionPane.QUESTION_MESSAGE);
            if (novoNome != null) {
                p.setName(novoNome);
            }
        });
    };
    private Runnable mRenameComponent = () -> {
        SwingUtilities.invokeLater(() -> {
            Component c = mProjectExplorer.getSelectedComponent();
            String novoNome = JOptionPane.showInputDialog(null, "Enter the new name for \"" + c.getName() + "\"", "Rename component", JOptionPane.QUESTION_MESSAGE);
            if (novoNome != null) {
                c.setName(novoNome);
            }
        });
    };
    private Runnable mRemoveComponent = () -> {
        SwingUtilities.invokeLater(() -> {
            Project p = mProjectExplorer.getSelectedProject();
            Component c = mProjectExplorer.getSelectedComponent();
            if (c == null) {
                JOptionPane.showMessageDialog(null, "Select a component!");
                return;
            }
            int r = JOptionPane.showConfirmDialog(null, "Do you really want to remove the component " + c.getName() + "?", "Remove component", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                p.removeComponent(c);
            }
        });
    };
    private Runnable mCloseProject = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Please select a project!");
            return;
        }
        mProjectExplorer.close(p);
    };
    private Runnable mCloseOthersProjects = () -> {
        Project projetoSelecionado = mProjectExplorer.getSelectedProject();
        if (projetoSelecionado == null) {
            JOptionPane.showMessageDialog(null, "Please select a project!");
            return;
        }
        Project[] todosProjetos =  mProjectExplorer.getAllProjects().toArray(new Project[0]);
        for (Project projeto: todosProjetos) {
            if (projeto != projetoSelecionado) {
                mProjectExplorer.close(projeto);
            }
        }
    };
    private Runnable mCloseAllProjects = () -> {
        for (Project p: mProjectExplorer.getAllProjects()) {
            mProjectExplorer.close(p);
        }
    };

    private FileChooser getFileChooser(String title) {
        if (mFileChooser == null) {
            mFileChooser = new FileChooser();
            mFileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            mFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("LoTus files (*.xml)", "*.xml"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
        }
        mFileChooser.setTitle(title);
        return mFileChooser;
    }
    private Runnable mOpenProject = () -> {
        File file = getFileChooser("Open project").showOpenDialog(null);
        if (file == null) {
            return;
        }
        try (FileInputStream in = new FileInputStream(file)) {
            Project p = new ProjectXMLSerializer().parseStream(in);
            p.setName(file.getName());
            mProjectExplorer.open(p);
            mProjectsFiles.put(p, file);
        } catch (Exception e) {
            showException(e);
        }
    };
    private final Runnable mSaveProject = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Please select a project!");
            return;
        }
        File f = mProjectsFiles.get(p);
        if (f == null) {
            f = getFileChooser("Save project").showSaveDialog(null);
            if (f == null) {
                return;
            }
        }
        try (FileOutputStream out = new FileOutputStream(f)) {
            new ProjectXMLSerializer().toStream(p, out);
            p.setName(f.getName());
            mProjectsFiles.put(p, f);
        } catch (Exception e) {
            showException(e);
        }
    };
    private final Runnable mSaveAsProject = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Please select a project!");
            return;
        }
        File f = getFileChooser("Save project as").showSaveDialog(null);
        if (f == null) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(f)) {
            new ProjectXMLSerializer().toStream(p, out);
            p.setName(f.getName());
            mProjectsFiles.put(p, f);
        } catch (Exception e) {
            showException(e);
        }
    };

    private void showException(Exception e) {
        logger.log(Level.WARNING, "Exception", e);
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
        });
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(br.uece.lotus.project.ProjectExplorer.class);

        ExtensibleMenu mMainMenu = mUserInterface.getMainMenu();

        mMainMenu.newItem("File/New Project...")
                .setWeight(Integer.MIN_VALUE)
                .setAccelerator(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
                .setAction(mNewProject)
                .create();
        mMainMenu.newItem("File/New Component...")
                .setWeight(Integer.MIN_VALUE)
                .setAccelerator(KeyCode.N, KeyCombination.CONTROL_DOWN)
                .setAction(mNewComponent)
                .create();
        mMainMenu.newItem("File/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("File/Open...")
                .setWeight(Integer.MIN_VALUE)
                .setAccelerator(KeyCode.O, KeyCombination.CONTROL_DOWN)
                .setAction(mOpenProject)
                .create();
        mMainMenu.newItem("File/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("File/Close Project...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mCloseProject)
                .create();
        mMainMenu.newItem("File/Close Others Projects...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mCloseOthersProjects)
                .create();
        mMainMenu.newItem("File/Close All Projects...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mCloseAllProjects)
                .create();
//        mMainMenu.newItem("File/Open Recent")
//                .setWeight(Integer.MIN_VALUE)
//                .setAction(mOpenProject)
//                .create();
        mMainMenu.newItem("File/-")
                .setWeight(Integer.MIN_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("File/Save")
                .setWeight(Integer.MIN_VALUE)
                .setAccelerator(KeyCode.S, KeyCombination.CONTROL_DOWN)
                .setAction(mSaveProject)
                .create();
        mMainMenu.newItem("File/Save as...")
                .setWeight(Integer.MIN_VALUE)
                .setAction(mSaveAsProject)
                .create();
        mMainMenu.newItem("File/Save all")
                .setWeight(Integer.MIN_VALUE)
                .setAccelerator(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)
                .setAction(mSaveProject)
                .create();
        mMainMenu.newItem("File/-")
                .setWeight(Integer.MAX_VALUE)
                .showSeparator(true)
                .create();
        mMainMenu.newItem("File/Quit")
                .setWeight(Integer.MAX_VALUE)
                .create();

        mUserInterface.getToolBar().newItem("New Component")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("res/ic_component_new.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mNewComponent)
                .create();

        mUserInterface.getToolBar().newItem("New Project")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("res/ic_project_new.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mNewProject)
                .create();
        mUserInterface.getToolBar().newItem("Open Project")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("res/ic_project_open.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mOpenProject)
                .create();
        mUserInterface.getToolBar().newItem("Save All")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("res/ic_project_save.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mSaveProject)
                .create();

        mProjectExplorer.getMenu().addItem(Integer.MIN_VALUE, "New project", mNewProject);
        mProjectExplorer.getMenu().addItem(Integer.MIN_VALUE, "Open project...", mOpenProject);        
        
        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "New Component", mNewComponent);
        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "Rename...", mRenameProject);
        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "-", null);
        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "Close project...", mCloseProject);        

        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE, "Rename...", mRenameComponent);
        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE, "Remove...", mRemoveComponent);

    }

}
