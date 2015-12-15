/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.Astah;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.layout.TreeLayouter;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.AtorAndClasse;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.Collaboration;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.Interaction;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.InteractionFragments;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.NamespaceOwnedElement;
import java.io.File;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

/**
 *
 * @author Bruno Barbosa
 */
public class UmlPlugin extends Plugin{
    
    private List<AtorAndClasse> atoresEclasses;
    private final List<TabelaReferenciaID> relativo = new ArrayList<>();
    List<InteractionFragments> loopsOuAlts;
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private ProjectDialogsHelper dialogHelper = new ProjectDialogsHelper();
    private ExtensibleMenu mMainMenu;
    private static final int MENU_WEIGHT = 123;
    private Optional<String> parser;
    
    void start() {
             
    }
    
    private final Runnable mOpenDiagramSequence = () -> {
        File xmi;
        Project p = mProjectExplorer.getSelectedProject();
        if (p == null) {
            JOptionPane.showMessageDialog(null, "Select a Project!");
            return;
        }
        try{
            xmi = dialogHelper.findXMI("Import from UML", "XMI Files (*.xml)", "*.xml");
            DiagramaSequenciaFactory f = new DiagramaSequenciaFactoryMock();
            parser = dialogLtsParser();
            Component c = converterDiagSeq(f.build(xmi));
            c.setName("Componente UML ["+xmi.getName()+"]");
            p.addComponent(c);
        }catch(Exception e){
            e.getMessage();
        }
    };

    private Component converterDiagSeq(DiagramaSequencia build) {
        Component c = new Component();
        
        atoresEclasses = build.getAtoresEclasses();
        List<Collaboration> collaboration = build.getGrupo();
        String enviando="",recebendo="";
        loopsOuAlts = new ArrayList<>();
        List<Mensagem> comunicacao = new ArrayList<>();
        
        //Listando idsRelativos-------------------------------------------------------------------------
        for(int i=0;i<atoresEclasses.size();i++){
            TabelaReferenciaID id = new TabelaReferenciaID(i+1, atoresEclasses.get(i).getXmiID());
            relativo.add(id);
            System.out.println("Id relativo: "+ id.getIdRelativo()+" idClass: "+id.getIdClassOrActor());
        }
        //Penerando o Diagrama---------------------------------------------------------------------------
        for(Collaboration cbt : collaboration){
            loopsOuAlts = cbt.getGrupoLoopOrFluxo();
            for(Interaction inter : cbt.getGrupoMessagens()){
                if(inter.getXmiIDREFcollaboration().equals(cbt.getXmiIDcollaboration())){
                    for(NamespaceOwnedElement noe : cbt.getGrupoCollaboration()){
                        if(inter.getEnviando().equals(noe.getXmiIDClassifierRole())){
                            enviando = noe.getXmiIDREFclassifierBase();
                        }
                        if(inter.getRecebendo().equals(noe.getXmiIDClassifierRole())){
                            recebendo = noe.getXmiIDREFclassifierBase();
                        }
                    }
                    comunicacao.add(buscaDaClasse(enviando, recebendo, inter.getNomeMensagem(), inter.getXmiIDMsg()));
                }
            }
        }
        //Passando pra LTS-----------------------------------------------------------------------------
        switch (parser.get()) {
            case "Comun":
                LtsParser parserComun = new LtsParser(comunicacao, relativo, loopsOuAlts, c);
                c = parserComun.parseLTSA();
                break;
            case "Tcg":
                LtsParserTCG parserTcg = new LtsParserTCG(comunicacao, relativo, loopsOuAlts, c);
                c = parserTcg.parseLTSA();
                break;
        }
        
        new TreeLayouter().layout(c);
        return c;
    }
    
    private Mensagem buscaDaClasse(String idRefOrigem, String idRefDestino, String msg, String xmiIdMsg){
        Mensagem atorOuClasse = new Mensagem();
        
        for(AtorAndClasse ac : atoresEclasses){
            if(ac.getXmiID().equals(idRefOrigem)){
                atorOuClasse.setEnviando(new AtorAndClasse(ac.getNome(), ac.getXmiID(), ac.getTipo()));
            }
            if(ac.getXmiID().equals(idRefDestino)){
                atorOuClasse.setRecebendo(new AtorAndClasse(ac.getNome(), ac.getXmiID(), ac.getTipo()));
            }
        }
        atorOuClasse.setXmiIdMsg(xmiIdMsg);
        atorOuClasse.setMsg(msg);
        return atorOuClasse;
    }
    
    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mUserInterface = extensionManager.get(UserInterface.class);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        
        mMainMenu = mUserInterface.getMainMenu();
        
        mMainMenu.newItem("File/Import/From Sequence Diagram")
                .setWeight(MENU_WEIGHT)
                .setAction(mOpenDiagramSequence)
                .create();
    }
    
    private Optional<String> dialogLtsParser(){
        Dialog<String> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Model LTS parser");
        dialog.setHeaderText("Choose one of the options");
        
        ButtonType botaoOK = new ButtonType("Choose", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(botaoOK);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ToggleGroup escolha = new ToggleGroup();
        RadioButton comun = new RadioButton("Common Transitions");
        RadioButton tcg = new RadioButton("Transitions for TCG");
        escolha.getToggles().addAll(tcg,comun);
        
        grid.add(comun, 0, 0);
        grid.add(tcg, 1, 0);
        
        Node botao = dialog.getDialogPane().lookupButton(botaoOK);
        botao.setDisable(true);

        escolha.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            botao.setDisable(!newValue.isSelected());
        });

        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == botaoOK) {
                if(comun.isSelected()){
                    return "Comun";
                }
                else if(tcg.isSelected()){
                    return "Tcg";
                }
            }
            return null;
        });
        
        Optional<String> resultado = dialog.showAndWait();
        return resultado;
    }
}
