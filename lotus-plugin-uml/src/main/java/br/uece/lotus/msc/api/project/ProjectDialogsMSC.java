/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.api.project;

import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.seed.app.DialogsHelper;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

/**
 *
 * @author Bruno Barbosa
 */
public class ProjectDialogsMSC extends Plugin{
    
    private FileChooser mFileChooser;
    private DialogsHelper mDialogsHelper;
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mDialogsHelper = extensionManager.get(DialogsHelper.class);
    }
    
    public boolean save(ProjectMSC project, ProjectMSCSerializer serializer, String title, String extensionDescription, String extension/*, boolean close*/) {
         return realSave(project, serializer, false, true, title, extensionDescription, extension/*, close*/);
    }
    
    public ProjectMSC open(ProjectMSCSerializer serializer, String title, String extensionDescription, String extension) {
        File file = getFileChooser(title, extensionDescription, extension, null).showOpenDialog(null);
        if (file == null) {
            return null;
        }
        ProjectMSC p = null;
        try (FileInputStream in = new FileInputStream(file)) {
            p = serializer.parseStream(in);
            p.putValue("file", file);
            if (p.getName() == null || p.getName().trim().isEmpty()) {
                p.setName("Untitled");
            }
        } catch (Exception e) {
            mDialogsHelper.showException(e);
        }
        return p;
    }
    
    private FileChooser getFileChooser(String title, String extensionDescription, String extension, String defaultFileName) {
        if (mFileChooser == null) {
            mFileChooser = new FileChooser();
            mFileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
        }
        mFileChooser.setInitialFileName(defaultFileName);
        mFileChooser.getExtensionFilters().clear();
        mFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(extensionDescription, extension),
                new FileChooser.ExtensionFilter("All files", "*")
        );
        mFileChooser.setTitle(title);
        return mFileChooser;
    }

    private boolean realSave(ProjectMSC project, ProjectMSCSerializer serializer, boolean forceShowDialog, boolean cacheFileName, String title, String extensionDescription, String extension) {
        if(project == null){
            getAlert("Information", "Please select a project!").show();
            return false;
        }
        File f = (File) project.getValue("file");
        if (forceShowDialog || f == null) {
            
            f = getFileChooser(title, extensionDescription, extension, project.getName()+".xml").showSaveDialog(null);
            if (f == null) {
                return false;
            }
        }
        try (FileOutputStream out = new FileOutputStream(f)) {
            serializer.toStream(project, out);
            if (cacheFileName) {
                project.putValue("file", f);
            }
        } catch (Exception e) {
            mDialogsHelper.showException(e);
        }
        return true;
    }
    
    private Alert getAlert(String type, String msg){
        Alert alerta = new Alert(Alert.AlertType.NONE);
        switch(type){
            case "":alerta.setAlertType(Alert.AlertType.NONE);break;
            case "Information": alerta.setAlertType(Alert.AlertType.INFORMATION);break;
            case "Confirmation": alerta.setAlertType(Alert.AlertType.CONFIRMATION);break;
            case "Error": alerta.setAlertType(Alert.AlertType.ERROR);break;
            case "Warning": alerta.setAlertType(Alert.AlertType.WARNING);
        }
        alerta.setContentText(msg);
        return alerta;
    }
}
