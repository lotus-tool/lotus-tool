package br.uece.lotus.tools.implicitScenario.StructsRefine;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by lucas on 02/08/16.
 */
public class Refiner {
    private BufferedReader bufferTrace;
    private BufferedReader bufferOneLoopPath;
    private ArrayList<String> listCenariosImplicitos = new ArrayList<>();
    private ArrayList<String> listOneLoopPath = new ArrayList<>();
    private ArrayList<String> listCleanOneLoopPath = new ArrayList<>();
    private ArrayList<String> listTrace, fileOneLoopPath;

    public Refiner (){
    }

    public void refine (ArrayList<String> listTrace, ArrayList<String> listOneLoopPath){
        this.listTrace = listTrace;
        this.listOneLoopPath= listOneLoopPath;
        buildCenariosImplicitos();
       /* removeAllImplicitedScenary();*/
    }

    public void buildCenariosImplicitos() {
        listCenariosImplicitos= comparation();
    }

    private ArrayList<String> comparation() {
        ArrayList<String> listResulted = new ArrayList<>();
        if (!listTrace.equals(listOneLoopPath)) {
            String linha;

            for (int i = 0; i < listOneLoopPath.size(); i++) {
                linha = listOneLoopPath.get(i);
                boolean contain = listTraceContain(linha);

                if (!contain) {
                    listResulted.add(linha);
                }
            }

        }
        return listResulted;

    }

    private boolean listTraceContain(String linhaOneLoopPath) {
        boolean aux = false;
        for (String linhaTrace : listTrace) {
            linhaTrace=linhaTrace+",";    /*cambiarra*/
            if (!linhaTrace.equals(linhaOneLoopPath.trim())) {
                continue;
            } else {
                aux = true;
            }

        }
        return aux;

    }
    public void removeAllImplicitedScenary(){
        for(String s : listOneLoopPath){
            if(!listCenariosImplicitos.contains(s)){
               listCleanOneLoopPath.add(s);
            }
        }

    }
    public void removeImplicitedScenary(ArrayList<String> listScenary){
        for(String s : listScenary){
            if(!listCenariosImplicitos.contains(s)){
                listCleanOneLoopPath.add(s);
            }
        }

    }
    public void removeImplicitedScenary(String scenary){
            if(listCenariosImplicitos.contains(scenary)){
                listCleanOneLoopPath = (ArrayList<String>) listOneLoopPath.clone();
                listCleanOneLoopPath.remove(scenary);
        }

    }

    public void setListCenariosImplicitos(ArrayList<String> listCenariosImplicitos) {
        this.listCenariosImplicitos = listCenariosImplicitos;
    }

    public ArrayList<String> getListTrace() {
        return listTrace;
    }

    public void setListTrace(ArrayList<String> listTrace) {
        this.listTrace = listTrace;
    }

    public ArrayList<String> getListOneLoopPath() {
        return listOneLoopPath;
    }

    public void setListOneLoopPath(ArrayList<String> listOneLoopPath) {
        this.listOneLoopPath = listOneLoopPath;
    }

    public ArrayList<String> getListCleanOneLoopPath() {
        return listCleanOneLoopPath;
    }



    public void setListCleanOneLoopPath(ArrayList<String> listCleanOneLoopPath) {
        this.listCleanOneLoopPath = listCleanOneLoopPath;
    }

    public ArrayList<String> getListCenariosImplicitos() {
        return listCenariosImplicitos;
    }
}
