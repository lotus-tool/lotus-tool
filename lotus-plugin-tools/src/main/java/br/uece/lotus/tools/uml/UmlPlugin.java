/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.project.ProjectDialogsHelper;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.tools.TreeLayouter;
import br.uece.seed.app.ExtensibleMenu;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import br.uece.lotus.tools.uml.xmi.AtorAndClasse;
import br.uece.lotus.tools.uml.xmi.Collaboration;
import br.uece.lotus.tools.uml.xmi.Interaction;
import br.uece.lotus.tools.uml.xmi.InteractionFragments;
import br.uece.lotus.tools.uml.xmi.NamespaceOwnedElement;
import java.io.File;

/**
 *
 * @author Bruno Barbosa
 */
public class UmlPlugin extends Plugin{
    
    private List<AtorAndClasse> atoresEclasses;
    private final List<TabelaReferenciaID> relativo = new ArrayList<>();
    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private ProjectDialogsHelper dialogHelper = new ProjectDialogsHelper();
    private ExtensibleMenu mMainMenu;
    private static final int MENU_WEIGHT = 123;
    
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
            Component c = converterDiagSeq(f.build(xmi));
            c.setName("Componente UML");
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
        List<InteractionFragments> loopsOuAlts = new ArrayList<>();
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
                    comunicacao.add(buscaDaClasse(enviando, recebendo, inter.getNomeMensagem()));
                }
            }
        }
        //Passando pra LTS-----------------------------------------------------------------------------
        LtsaParser parser = new LtsaParser(comunicacao, relativo, loopsOuAlts, c);
        c = parser.parseLTSA();
        new TreeLayouter().layout(c);
        return c;
    }
    
    private Mensagem buscaDaClasse(String idRefOrigem, String idRefDestino, String label){
        Mensagem atorOuClasse = new Mensagem();
        
        for(AtorAndClasse ac : atoresEclasses){
            if(ac.getXmiID().equals(idRefOrigem)){
                atorOuClasse.setEnviando(new AtorAndClasse(ac.getNome(), ac.getXmiID(), ac.getTipo()));
            }
            if(ac.getXmiID().equals(idRefDestino)){
                atorOuClasse.setRecebendo(new AtorAndClasse(ac.getNome(), ac.getXmiID(), ac.getTipo()));
            }
        }
        atorOuClasse.setLabel(label);
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
}
