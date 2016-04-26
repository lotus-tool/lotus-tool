/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Bruno Barbosa
 */
public class ImplicitScenarioPlugin extends Plugin {

    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;
    private Component c;

    private final Runnable implicitScenario = () -> {

        c = mProjectExplorer.getSelectedComponent();
        try {
            if (c == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a componet!", ButtonType.OK);
                alert.show();
                return;
            }
            show(c.clone(), true);

            ArrayList<String> paths = createrPaths();
            paths = removeElementsrepeated(paths);

            for (String s : paths) {
                System.out.println("StructPath Value: " + s);
            }

        } catch (CloneNotSupportedException e) {
        }
    };

    private State currentState;
    private String currentTrace;
    private String label;
    private State dst;

    private ArrayList createrPaths() {
        ArrayList<String> out = new ArrayList<>();
        StructPath structPath = new StructPath();
        Path path = new Path();
        structPath.setLastStateOnPath(c.getInitialState());

        //initializing StructPath
        for (Transition t : structPath.getLastState().getOutgoingTransitionsList()) {
            State dst = t.getDestiny();
            path.addWay(dst, t.getLabel() + ", ");
        }
        //run StructPath
        for (int i = 0; i < path.getStates().size(); i++) {
            currentState = path.getStates().get(i);
            currentTrace = path.getTraces().get(i);

            //element on StructPath doesn't have TransitionsOut
            if (currentState.getOutgoingTransitionsCount() == 0) {
                out.add(currentTrace);
                continue;

             //case haves
            } else {
                //run TransitionsOut of state of StructPath
                for (Transition t : currentState.getOutgoingTransitionsList()) {
                    label = t.getLabel();
                    dst = t.getDestiny();

                    //if this Transition already visited
                    if (contains(currentTrace, label)) {
                        continue;
                    }
                    //else add this Transition on StructPath
                    path.addWay(dst, currentTrace + label + ", ");
                }
            }
        }
        //add all StructPath for add on arrow table
        out.add(currentTrace);
        return out;

    }


    private boolean contains(String currentTrace, String label) {
        if (currentTrace.contains(label)) {
            return true;
        } else {
            return false;
        }
    }

    private ArrayList<String> removeElementsrepeated(ArrayList<String> arrayList) {
        Set<String> hs = new HashSet<>();
        hs.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(hs);

        return arrayList;
    }

    public void show(Component c, boolean editable) {

        URL location;
        location = getClass().getResource("/fxml/ImplicitScenarioWindowFXML.fxml");
        FXMLLoader loader = new FXMLLoader();

        ResourceBundle bundle = new ResourceBundle() {

            @Override
            protected Object handleGetObject(String key) {
                Component mComponent = c;
                if ("component".equals(key)) {
                    return mComponent;
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
            int id = mUserInterface.getCenterPanel().newTab(c.getName() + " - [ImplicitScenario]", root, true);
            mUserInterface.getCenterPanel().showTab(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mUserInterface.getMainMenu().newItem("Verification/Implicit Scenario")
                .setWeight(1)
                .setAction(implicitScenario)
                .create();
    }
}
