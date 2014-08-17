package br.uece.lotus.tools;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.model.BasicLayouterImpl;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.io.File;
import java.io.PrintStream;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author emerson
 */
public class ToolsPlugin extends Plugin {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);

        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "File/-", null);
        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "File/Export project.../LTSA", () -> {
            Project p = mProjectExplorer.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "There is no project avaliable!");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export project");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("LTSA model (*.lts)", "*.lts"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
            File arq = fileChooser.showSaveDialog(null);
            if (arq == null) {
                return;
            }
            try (PrintStream out = new PrintStream(arq)) {
                new LTSASerializer().toStream(p, out);
                JOptionPane.showMessageDialog(null, "File exported successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
            }
        });

        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "File/Export project.../Prism", () -> {
            Project p = mProjectExplorer.getSelectedProject();
            if (p == null) {
                JOptionPane.showMessageDialog(null, "There is no project avaliable!");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export project");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Prism model (*.prism)", "*.prism"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
            File arq = fileChooser.showSaveDialog(null);
            if (arq == null) {
                return;
            }
            try (PrintStream out = new PrintStream(arq)) {
                new PrismSerializer().toStream(p, out);
                JOptionPane.showMessageDialog(null, "File exported successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
            }
        });        

        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "File/Import project.../LTSA", () -> {
            Project p = new Project();            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import LTSA project");
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("LTSA Files (*.lts)", "*.lts"),
                    new FileChooser.ExtensionFilter("All files", "*")
            );
            File mArquivo = fileChooser.showOpenDialog(null);
            if (mArquivo == null) {
                return;
            }
            try {
                Component c = new LTSAParser().parseFile(mArquivo);
                c.setName(mArquivo.getName());
                new BasicLayouterImpl().layout(c);
                p.addComponent(c);
                p.setName(mArquivo.getName());
                mProjectExplorer.changeProject(p);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getClass() + ": " + e.getMessage());
            }
        });
        mUserInterface.getMainMenu().addItem(Integer.MAX_VALUE - 1, "File/-", null);
    }

}
