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
package br.uece.lotus.project.v2;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.model.BasicLayouterImpl;
import br.uece.lotus.project.XMLSerializer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BasicPlugin extends Plugin {

    private File mProjectFile;
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    private Runnable mCriarProjetoRandomico = () -> {
        Project p = new Project();
        int n = Integer.parseInt(JOptionPane.showInputDialog("qtd componentes"));
        int m = Integer.parseInt(JOptionPane.showInputDialog("qtd estados"));
        int o = Integer.parseInt(JOptionPane.showInputDialog("qtd transicoes por estado"));
        for (int i = 0; i < n; i++) {
            Component c = new Component();
            c.setName("c" + i);

            for (int j = 0; j < m; j++) {
                State s = c.newState(j);
            }
            for (State s : c.getStates()) {
                int k = 0;
                for (State s2 : c.getStates()) {
                    c.newTransition(s, s2);
                    if (k < o) {
                        continue;
                    }
                    k++;
                    break;
                }
            }
            c.setInitialState(c.getStateByID(0));
            new BasicLayouterImpl().layout(c);
            p.addComponent(c);
        }
        mProjectExplorer.open(p);
    };
    private Runnable mCriarNovoComponente = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "There is no project avaliable!");
            return;
        }
        Component c = new Component();
        c.setName("Component" + (p.getComponentsCount() + 1));
        p.addComponent(c);
    };
    private Runnable mNewProject = () -> {
        Project p = new Project();
        p.setName("Untitled");
        Component c = new Component();
        c.setName("Component" + (p.getComponentsCount() + 1));
        p.addComponent(c);
        mProjectExplorer.open(p);
    };
    private Runnable mOpenProject = () -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open project");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LoTus files (*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("All files", "*")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        try (FileInputStream in = new FileInputStream(file)) {
            Project p = new XMLSerializer().parseStream(in);
            p.setName(file.getName());
            mProjectExplorer.open(p);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
        }
    };
    private final Runnable mSaveProject = () -> {
        Project p = mProjectExplorer.getSelectedProject();
        if (mProjectFile == null) {
            if (p == null) {
                JOptionPane.showMessageDialog(null, "There is no project avaliable!");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save project");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("LoTus files (*.xml)", "*.xml"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
            mProjectFile = fileChooser.showSaveDialog(null);
            if (mProjectFile == null) {
                return;
            }
        }
        try (FileOutputStream out = new FileOutputStream(mProjectFile)) {
            new XMLSerializer().toStream(p, out);
            p.setName(mProjectFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
        }
    };

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(br.uece.lotus.project.v2.ProjectExplorer.class);

        mUserInterface.getMainMenu().addItem(Integer.MIN_VALUE,
                "File/New project", mNewProject);        
        mUserInterface.getMainMenu().addItem(Integer.MIN_VALUE,
                "File/Open project", mOpenProject);
        mUserInterface.getMainMenu().addItem(Integer.MIN_VALUE,
                "File/Save project", mSaveProject);

        mUserInterface.getToolBar().newItem("New Project")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("ic_project_new.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mNewProject)
                .create();
        mUserInterface.getToolBar().newItem("Open Project")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("ic_project_open.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mOpenProject)
                .create();
        mUserInterface.getToolBar().newItem("Save Project")
                .hideText(true)
                .setGraphic(getClass().getResourceAsStream("ic_project_save.png"))
                .setWeight(Integer.MIN_VALUE)
                .setAction(mSaveProject)
                .create();

        mProjectExplorer.getMenu().addItem(Integer.MIN_VALUE, "New project", mNewProject);
        mProjectExplorer.getMenu().addItem(Integer.MIN_VALUE, "Open project...", mOpenProject);
        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "New Component", mCriarNovoComponente);

        mUserInterface.getMainMenu().addItem(Integer.MIN_VALUE,
                "File/Save...", () -> {
                    Project p = mProjectExplorer.getSelectedProject();
                    if (p == null) {
                        JOptionPane.showMessageDialog(null, "There is no project selected!");
                        return;
                    }
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save project as");
                    fileChooser.setInitialDirectory(
                            new File(System.getProperty("user.home"))
                    );
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("LoTus files (*.xml)", "*.xml"),
                            new FileChooser.ExtensionFilter("All files", "*")
                    );
                    mProjectFile = fileChooser.showSaveDialog(null);
                    if (mProjectFile == null) {
                        return;
                    }
                    try (FileOutputStream out = new FileOutputStream(mProjectFile)) {
                        new XMLSerializer().toStream(p, out);
                        p.setName(mProjectFile.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
                    }
                });

        mUserInterface.getMainMenu().addItem(Integer.MIN_VALUE,
                "File/Save all...", () -> {
                });

        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE,
                "File/Exit", () -> {
                    Platform.exit();
                });

        mProjectExplorer.getProjectMenu().addItem(Integer.MIN_VALUE, "Rename...", () -> {
            SwingUtilities.invokeLater(() -> {
                Project p = mProjectExplorer.getSelectedProject();
                String novoNome = JOptionPane.showInputDialog(null, "Enter the new name for " + p.getName() + ":", "Rename component", JOptionPane.QUESTION_MESSAGE);
                if (novoNome != null) {
                    p.setName(novoNome);
                }
            });
        });

        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE, "Rename...", () -> {
            SwingUtilities.invokeLater(() -> {
                Component c = mProjectExplorer.getSelectedComponent();
                String novoNome = JOptionPane.showInputDialog(null, "Enter the new name for " + c.getName() + ":", "Rename component", JOptionPane.QUESTION_MESSAGE);
                if (novoNome != null) {
                    c.setName(novoNome);
                }
            });
        });
        mProjectExplorer.getComponentMenu().addItem(Integer.MIN_VALUE, "Remove...", () -> {
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
        });

    }

}
