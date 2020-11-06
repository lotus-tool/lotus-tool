/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Bruno Barbosa && Lucas Vieira
 */
public class ImplicitScenarioPlugin extends Plugin {

    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;
    private ProjectDialogsHelper mProjectDialogsHelper;

    private Component c;
    private File mTraceFileModel;
    private OneLoopPath oneLoopPath;
    private List<String> pathsFromOLP;
    private List<String> pathsFromTraceSystem;
    private List<String> pathsScenarioImplied;

    private final Runnable implicitScenario = () -> {

        oneLoopPath = new OneLoopPath();
        pathsFromOLP = new CopyOnWriteArrayList<>();
        pathsFromTraceSystem = new ArrayList<>();
        pathsScenarioImplied = new ArrayList<>();

        c = mProjectExplorer.getSelectedComponent();
        if (c == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a componet!", ButtonType.OK);
            alert.show();
            return;
        }

        mTraceFileModel = getTraceFile();
        if (mTraceFileModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Problem in Trace file", ButtonType.OK);
            alert.show();
            return;
        }

        pathsFromOLP = oneLoopPath.createOneLoopPath(c);
        pathsFromTraceSystem = createArrayList(mTraceFileModel);
        getScenarioImpliedies();
        makePrintFromList(pathsFromOLP, "Caminhos do OneLoopPath", "OLP: ");
        makePrintFromList(pathsFromTraceSystem, "Caminhos do Sistema", "Trace: ");
        makePrintFromList(pathsScenarioImplied, "Caminhos do ImpliedScenario", "IS: ");
        System.out.println("OLP : "+pathsFromOLP.size());
        
        try {
            show(c.clone(), true);
        } catch (CloneNotSupportedException e) {}

    };

    public void show(Component c, boolean editable) {
        URL location;
        location = getClass().getResource("/fxml/ImplicitScenarioWindowFXML.fxml");
        FXMLLoader loader = new FXMLLoader();
        ResourceBundle bundle = new ResourceBundle() {

            @Override
            protected Object handleGetObject(String key) {
                if ("component".equals(key)) {
                    return c;
                }
                if ("CenariosImplicitos".equals(key)) {
                    return pathsScenarioImplied;
                }
                if ("mProjectExplorer".equals(key)) {
                    return mProjectExplorer;
                }
                if ("TraceModelo".equals(key)) {
                    return pathsFromTraceSystem;
                }
                if("OLP".equals(key)){
                    return pathsFromOLP;
                }
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        try {
            loader.setClassLoader(getClass().getClassLoader());
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setResources(bundle);
            Parent root = (Parent) loader.load(location.openStream());
            int id = mUserInterface.getCenterPanel().newTab(c.getName() + " - [ImpliedScenario]", root,4, true);
            mUserInterface.getCenterPanel().showTab(id);
        } catch (IOException e) {}
    }

    public File getTraceFile() {
        try {
            return mProjectDialogsHelper.getTraceFile();// find the file
        } catch (NullPointerException e) {
            return null;                    //not find the file
        }
    }

    private ArrayList<String> createArrayList(File file) {
        BufferedReader bufferedReader = null;
        String line;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("Problem in read File");
        }
        if (bufferedReader == null) {
            return null;
        } else {
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    if (!arrayList.contains(line) && (!line.equals(""))) {
                        arrayList.add(line);
                    }

                }
            } catch (IOException e) {
                System.out.println("Problem in read bufferedReader");
            }
        }
        finally {
            bufferedReader.close()
        }
        //Tratar elementos Repetidos (podem ser gerados pelo trace generator)
        Set<String> notRepeat = new HashSet<>();
        notRepeat.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(notRepeat);
        return arrayList;
    }

    private void getScenarioImpliedies() {
        for (String olp : pathsFromOLP) {
            if (!pathsFromTraceSystem.contains(olp)) {
                pathsScenarioImplied.add(olp);
            }
        }
    }

    private void makePrintFromList(List<String> list, String title, String tag) {
        System.out.println("\n");
        System.out.println(title);
        for (String s : list) {
            System.out.println(tag + " " + s);
        }
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsHelper.class);
        mUserInterface.getMainMenu().newItem("Verification/Implied Scenarios Detection")
                .setWeight(1)
                .setAction(implicitScenario)
                .create();
    }
}
