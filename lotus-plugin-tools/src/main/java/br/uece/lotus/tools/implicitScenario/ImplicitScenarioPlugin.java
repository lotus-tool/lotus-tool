/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.implicitScenario;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.TraceParser;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Aggregator;
import br.uece.lotus.tools.implicitScenario.StructsRefine.OneLoopPath;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Refiner;
import br.uece.lotus.tools.implicitScenario.StructsRefine.Trie;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;

import java.io.*;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Bruno Barbosa && Lucas Vieira
 */
public class ImplicitScenarioPlugin extends Plugin {

    private ProjectExplorer mProjectExplorer;
    private UserInterface mUserInterface;
    private ProjectDialogsHelper mProjectDialogsHelper;

    // Component that contains the behavioral model created from Plugin Model From Trace
    private Component c;
    private File mTraceFile;

    //This list contains the one-loop-path relative the behavioral model ( model gerareter by file trace from Plugin Model From Trace)
    private ArrayList<String> mListOneLoopPathFromBehavioralModel;

    //This list contains the Traces from Real Model and they are create from Plugin Trace Generations
    private ArrayList<String> mListTraceFromRealModel;

    private ArrayList<String> mListCenariosImplicitos;
    private Refiner refiner;
    // this methed start when it is clicked in button Verificar
    private final Runnable implicitScenario = () -> {

        c = mProjectExplorer.getSelectedComponent();

            if (c == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Select a componet!", ButtonType.OK);
                alert.show();
                return;
            }

            OneLoopPath oneLoopPath = new OneLoopPath();

            mListOneLoopPathFromBehavioralModel = oneLoopPath.createOneLoopPath(c);
            mListOneLoopPathFromBehavioralModel = removeElementsRepeated(mListOneLoopPathFromBehavioralModel);
            makePrintFromList(mListOneLoopPathFromBehavioralModel, "One Loop Path From Behavioral Model...", "O-L-P");

            /*mListOneLoopPathFromBehavioralModel.sort(Comparator.<String>naturalOrder());
            mListOneLoopPathFromBehavioralModel = arrumarVirgulaInicial(mListOneLoopPathFromBehavioralModel);*/


        //START ALGORITHM REFINER
                mTraceFile = getTraceFile();
        if (mTraceFile == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Problem in Trace file", ButtonType.OK);
            alert.show();
            return;
        }
        mListTraceFromRealModel = createArrayList(mTraceFile);
        if (mListTraceFromRealModel == null) {
            return;
        }

        //This segment is doing the removal Implicit Scenary
        refiner = new Refiner();
        refiner.refine(mListTraceFromRealModel, mListOneLoopPathFromBehavioralModel);
        mListCenariosImplicitos = refiner.getListCenariosImplicitos();

        String linhaTabelaSelecionada="";
        if(linhaTabelaSelecionada.equals("")){// quer dizer que vou remover tudo
            refiner.removeAllImplicitedScenary();
        }else if (!(linhaTabelaSelecionada.equals(""))) { // quer dizer que vai remover só a linha seleciona (não testei ainda)
            refiner.removeImplicitedScenary(linhaTabelaSelecionada);
        }


        ArrayList<String> mListClearOneLoopPath = refiner.getListCleanOneLoopPath();
        makePrintFromList(mListCenariosImplicitos,"Scenary:","S.I");
        makePrintFromList(mListClearOneLoopPath,"CLEAN ONE LOOP PATH:","Clear-O-L-P");

        //This segment is doing the builder the Component
        Trie trie = new Trie();
        Component modificadComponet = trie.createComponet(mListClearOneLoopPath); // return a component witiout scenarys, but it is all open

        //try join Transitons the same label without generate scenarys
        Aggregator aggregator = new Aggregator(modificadComponet,mListTraceFromRealModel);
        modificadComponet=aggregator.aggregate();

        Project p = new Project();
        TraceParser parser = new TraceParser();
        p.addComponent(modificadComponet);
        p.setName("Project");
        mProjectExplorer.open(p);


        try {
            show(c.clone(), true);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    };

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
        return arrayList;
    }

    private ArrayList<String> removeElementsRepeated(ArrayList<String> arrayList) {
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
                if("ListRefined".equals(key)){
                    return mListCenariosImplicitos;
                }
                if("Refiner".equals(key)){
                    return refiner;
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

    private void makePrintFromList(ArrayList<String> list, String title, String tag) {
        System.out.println("\n\n");
        System.out.println(title);
        for (String s : list) {
            System.out.println(tag + " " + s);
        }


    }
    public File getTraceFile() {
        try {
            return mProjectDialogsHelper.getTraceFile();// find the file
        } catch (NullPointerException e) {
            return null;                    //not find the file
        }
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mProjectDialogsHelper = extensionManager.get(ProjectDialogsHelper.class);
        mUserInterface.getMainMenu().newItem("Verification/Implied Scenario")
                .setWeight(1)
                .setAction(implicitScenario)
                .create();
    }
}
