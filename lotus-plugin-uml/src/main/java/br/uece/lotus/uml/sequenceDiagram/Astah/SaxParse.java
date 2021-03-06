/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.sequenceDiagram.Astah;

import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.AtorAndClasse;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.Collaboration;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.CombinedFragments;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.Interaction;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.InteractionFragments;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.InteractionOperand;
import br.uece.lotus.uml.sequenceDiagram.Astah.xmi.NamespaceOwnedElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Bruno Barbosa
 */
public class SaxParse extends DefaultHandler{
    
    private final DiagramaSequencia ds = new DiagramaSequencia();
    private final ArrayList<AtorAndClasse> atoresAndClasses = new ArrayList<>();
    private final ArrayList<Collaboration> collaboration = new ArrayList<>();
    private final ArrayList<NamespaceOwnedElement> elementos = new ArrayList<>();
    private final ArrayList<Interaction> interacoes = new ArrayList<>();
    private final ArrayList<InteractionFragments> loopsOuFluxos = new ArrayList<>();
    
    public DiagramaSequencia fazerParse(File xmi){
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        
        try{
            saxParser = factory.newSAXParser();
            saxParser.parse(xmi, this);
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SaxParse.class.getName()).log(Level.SEVERE, null, ex);
        }
        ds.setAtoresEclasses(atoresAndClasses);
        ds.setGrupo(collaboration);
        return ds;
    }
    
    @Override
    public void startDocument() {
        System.out.println("\nIniciando o Parsing...\n");
    }
    
    @Override
    public void endDocument() {
        System.out.println("\nFim do Parsing...");
        
        for(AtorAndClasse aac : atoresAndClasses){
            System.out.println("\nIndividuo: "+ aac.getNome()+ " xmi.id = "+aac.getXmiID()+"  tipo: "+aac.getTipo());
        }
        for(NamespaceOwnedElement noe : elementos){
            System.out.println("\nClassifierRole: "+noe.getXmiIDClassifierRole()+"\nClassifierRoleBase: "+noe.getXmiIDREFclassifierBase());
        }
        for(Interaction i : interacoes){
            System.out.println("\nOrigem: "+ i.getEnviando()+"\n"+
                                "Destino: "+ i.getRecebendo()+"\n"+
                                "Menssagem: "+ i.getNomeMensagem()+"\n"+
                                "Xmi.Id: "+i.getXmiIDMsg()+"\n"+
                                "IDInteracao: "+ i.getXmiIDREFcollaboration()+"\n");
        }
        for(InteractionFragments inf : loopsOuFluxos){
            for(CombinedFragments comb : inf.getCombinedFrags()){
                System.out.println("\nCombinedFragments: "+comb.getXmiIdCombinedFragment()+"  Operator: "+comb.getOperator());
                for(InteractionOperand intop : comb.getInteractionOperands()){
                    System.out.println("\nInteractionOperand: "+intop.getXmiIdIteractionOperand()+"\tName: "+intop.getInteractionConstraintName());
                    System.out.println("------------Msg-----&&----Operand Frags---------");
                    for(String s : intop.getXmiIdRefMsg()){
                        System.out.println("MensagemId: "+s);
                    }
                    for(String s : intop.getInteractionOperandFrags()){
                        System.out.println("CombinedFrag: "+s);
                    }
                }
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    private String tagAtual;
    private String packageInvalido;
    private String collaborationID;
    private boolean atributosMaisDe1collaboration=false, collaborationTagAberta = false;
    ///////////////////////////////////////////////////////////////////////////////////////
    private String classifierRoleID, classifierRoleBasaID;
    private boolean classifierRoleTagAberta = false, atributosMaisDe1Role = false;
    private boolean classifierRoleBaseTagAberta = false;
    //////////////////////////////////////////////////////////////////////////////////////
    private boolean interactionContextTagAberta = false, interactionMessageTagAberta = false;
    private String idREFinteractionToCollaboration;
    private boolean messageTagAberta = false, messageSenderTagAberta = false, messageReceiverTagAberta = false;
    private String xmiIdMsg,msg,envia,recebe;
    private boolean atributosMaisDe1Message = false;
    //////////////////////////////////////////////////////////////////////////////////////
    private int controleTag = 0;
    private String xmiIdCombinedFrag = "", operator = "", xmiIdInteractionOperand = "", nomeCombinedFrag = "", interactionConstraintName = "";
    private List<String> xmiIdRefMsg = new ArrayList<>();
    private List<String> combinedFragIdRef = new ArrayList<>();//blocos dentro de bloco
    private List<InteractionOperand> interactionOperands = new ArrayList<>();
    private List<CombinedFragments> combinedFragments = new ArrayList<>();
    //////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        tagAtual = qName;
        //-------------------------------class e ators----------------------------------------------------------------
        if("UML:Package".equals(qName)){
            packageInvalido = qName;//Desabilitar tag package que contem várias class
        }
        //extraindo atores e classes
        if((tagAtual.equals("UML:Actor") || tagAtual.equals("UML:Class")) && !packageInvalido.equals("UML:Package")){
            AtorAndClasse acc = new AtorAndClasse("", "","");
            if(tagAtual.equals("UML:Actor")){
                acc.setNome(atts.getValue("name").replaceAll("\\+", ""));
                acc.setXmiID(atts.getValue("xmi.id"));
                acc.setTipo("actor");
            }
            else if(tagAtual.equals("UML:Class")){
                acc.setNome(atts.getValue("name").replaceAll("\\+", ""));
                acc.setXmiID(atts.getValue("xmi.id"));
                acc.setTipo("class");
            }
            atoresAndClasses.add(acc);
            System.out.println("Inicio tag: "+tagAtual+" "+acc.getNome()+" "+acc.getXmiID()+" "+acc.getTipo());
        }
        //------------------------------Collaborations--------------------------------------------------------------
        if(tagAtual.equals("UML:Collaboration") && atts.getLength()>1){
            collaborationID = atts.getValue("xmi.id");
            atributosMaisDe1collaboration = true;
            collaborationTagAberta = true;
            System.out.println("Inicio tag: "+tagAtual+" "+collaborationID);
        }
        //-------------------------------ClassifierRole e Base-------------------------------------------------------
        if(tagAtual.equals("UML:ClassifierRole") && collaborationTagAberta && atts.getLength()>1){
            classifierRoleTagAberta = true;
            atributosMaisDe1Role = true;
            classifierRoleID = atts.getValue("xmi.id");
            System.out.println("Inicio tag: "+tagAtual+" "+classifierRoleID);
            if(!atts.getValue("name").equals("")){
                String atorEClasse = atts.getValue("name").replaceAll("\\+", "");
                AtorAndClasse aac2 = new AtorAndClasse(atorEClasse, atts.getValue("xmi.id"), "class");
                NamespaceOwnedElement noe2 = new NamespaceOwnedElement(atts.getValue("xmi.id"), atts.getValue("xmi.id"));
                atoresAndClasses.add(aac2);
                elementos.add(noe2);
                System.out.println("add noe: role: "+noe2.getXmiIDClassifierRole()+ " base: "+noe2.getXmiIDREFclassifierBase());
                System.out.println("ator: "+atorEClasse+" xmi.id: "+aac2.getXmiID());
            }
        }
        if(tagAtual.equals("UML:ClassifierRole.base") && classifierRoleTagAberta){
            classifierRoleBaseTagAberta = true;
            System.out.println("Inicio tag: "+tagAtual);
        }
        if(tagAtual.equals("UML:Classifier") && classifierRoleBaseTagAberta){
            classifierRoleBasaID = atts.getValue("xmi.idref");
            System.out.println("Inicio tag: "+tagAtual+ " xmi.idref: "+classifierRoleBasaID);
            NamespaceOwnedElement noe = new NamespaceOwnedElement(classifierRoleID, classifierRoleBasaID);
            elementos.add(noe);
            System.out.println("add noe role: "+classifierRoleID+" base: "+classifierRoleBasaID);
            classifierRoleBaseTagAberta = false;
            classifierRoleTagAberta = false;
            atributosMaisDe1Role = false;
            classifierRoleBasaID = "";
            classifierRoleID = "";
        }
        //-------------------------------Menssagens-------------------------------------------------------------
        if(tagAtual.equals("UML:Interaction.context") && collaborationTagAberta){
            System.out.println("Inicio tag: "+tagAtual);
            interactionContextTagAberta = true;
        }
        if(tagAtual.equals("UML:Collaboration") && interactionContextTagAberta){
            idREFinteractionToCollaboration = atts.getValue("xmi.idref");
            interactionContextTagAberta = false;
            System.out.println("Inicio tag: "+tagAtual +" xmi.idref: "+idREFinteractionToCollaboration);
        }
        if(tagAtual.equals("UML:Interaction.message")){
            interactionMessageTagAberta = true;
            System.out.println("Inicio tag: "+tagAtual);
        }
        if(tagAtual.equals("UML:Message") && interactionMessageTagAberta && atts.getLength()>1){
            atributosMaisDe1Message = true;
            messageTagAberta = true;
            msg = atts.getValue("name");
            xmiIdMsg = atts.getValue("xmi.id");
            System.out.println("Inicio tag: "+tagAtual+" name:"+msg+" xmi.id: "+xmiIdMsg);
        }
        if(tagAtual.equals("UML:Message.sender") && messageTagAberta){
            System.out.println("Inicio tag: "+tagAtual);
            messageSenderTagAberta = true;
        }
        if(tagAtual.equals("UML:Message.receiver") && messageTagAberta){
            System.out.println("Inicio tag: "+tagAtual);
            messageReceiverTagAberta = true;
        }
        if(tagAtual.equals("UML:ClassifierRole") && messageSenderTagAberta){
            envia = atts.getValue("xmi.idref");
            messageSenderTagAberta = false;
            System.out.println("Inicio tag: "+tagAtual+" xmi.idref: "+envia);
        }
        if(tagAtual.equals("UML:ClassifierRole") && messageReceiverTagAberta){
            recebe = atts.getValue("xmi.idref");
            messageReceiverTagAberta = false;
            System.out.println("Inicio tag: "+tagAtual+" xmi.idref: "+recebe);
        }
        //----------------------------------Loops e Alts----------------------------------------------
        if(tagAtual.equals("UML:Interaction.fragments")){
            controleTag++;
            System.out.println("Inicio tag: "+tagAtual);
        }
        if(tagAtual.equals("UML:CombinedFragment") && controleTag==1){
            controleTag++;
            xmiIdCombinedFrag = atts.getValue("xmi.id");
            operator = atts.getValue("operator");
            nomeCombinedFrag = atts.getValue("name");
            System.out.println("Inicio tag: "+tagAtual+" xmi.id: "+xmiIdCombinedFrag+" name: "+nomeCombinedFrag+" operator: "+operator);
        }
        if(tagAtual.equals("UML:InteractionOperand") && controleTag==2){
            controleTag++;
            xmiIdInteractionOperand = atts.getValue("xmi.id");
            System.out.println("Inicio tag: "+tagAtual+" xmi.id: "+xmiIdInteractionOperand);
        }
        if(tagAtual.equals("UML:ModelElement.constraint") && controleTag==3){
            controleTag++;
            System.out.println("Inicio tag: "+tagAtual);
        }
        if(tagAtual.equals("UML:InteractionConstraint") && controleTag==4){
            interactionConstraintName = atts.getValue("name");
            System.out.println("Inicio tag: "+tagAtual+" name: "+interactionConstraintName);
        }
        if(tagAtual.equals("UML:Message") && controleTag==3){
            xmiIdRefMsg.add(atts.getValue("xmi.idref"));
            System.out.println("Inicio tag: "+tagAtual+" xmi.idref: "+xmiIdRefMsg);
        }
        if(tagAtual.equals("UML:CombinedFragment") && controleTag==3){
            combinedFragIdRef.add(atts.getValue("xmi.idref"));
            System.out.println("Inicio tag: "+tagAtual);
            System.out.println("adiciona xmi.idref: "+atts.getValue("xmi.idref"));
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tagAtual = "";
        if("UML:Package".equals(qName)){
            packageInvalido = "";
        }
        if("UML:InteractionOperand".equals(qName) && controleTag==3){
            controleTag--;
            InteractionOperand io = new InteractionOperand(xmiIdInteractionOperand, interactionConstraintName , null, null);
            List<String> aux1 = new ArrayList<>();
            List<String> aux2 = new ArrayList<>();
            aux1.addAll(xmiIdRefMsg);
            aux2.addAll(combinedFragIdRef);
            io.setXmiIdRefMsg(aux1);
            io.setInteractionOperandFrags(aux2);
            interactionOperands.add(io);
            xmiIdRefMsg.clear();
            combinedFragIdRef.clear();
            System.out.println("Fim tag: "+qName);
        }
        if("UML:ModelElement.constraint".equals(qName) && controleTag==4){
            controleTag--;
            System.out.println("Fim tag: "+qName);
        }
        if("UML:CombinedFragment".equals(qName) && controleTag==2){
            controleTag--;
            CombinedFragments combFrag = new CombinedFragments(xmiIdCombinedFrag, operator,nomeCombinedFrag ,null);
            List<InteractionOperand> aux = new ArrayList<>();
            aux.addAll(interactionOperands);
            combFrag.setInteractionOperands(aux);
            combinedFragments.add(combFrag);
            interactionOperands.clear();
            System.out.println("Fim tag: "+qName);
        }
        if("UML:Interaction.fragments".equals(qName) && controleTag==1){
            controleTag--;
            List<CombinedFragments> aux = new ArrayList<>();
            aux.addAll(combinedFragments);
            InteractionFragments inf = new InteractionFragments();
            inf.setCombinedFrags(aux);
            loopsOuFluxos.add(inf);
            combinedFragments.clear();
            System.out.println("Fim tag: "+qName);
        }
        if("UML:Message".equals(qName) && interactionMessageTagAberta && atributosMaisDe1Message){
            messageTagAberta = false;
            atributosMaisDe1Message = false;
            Interaction inter = new Interaction(idREFinteractionToCollaboration, xmiIdMsg, msg, envia, recebe);
            interacoes.add(inter);
            msg = "";
            envia = "";
            recebe = "";
            System.out.println("Fim tag: "+qName);
        }
        if("UML:Interaction.message".equals(qName)){
            idREFinteractionToCollaboration = "";
            interactionMessageTagAberta = false;
            System.out.println("Fim tag: "+qName);
        }
        if("UML:Collaboration".equals(qName) && atributosMaisDe1collaboration){
            collaborationTagAberta = false;
            atributosMaisDe1collaboration = false;
            Collaboration clb = new Collaboration(collaborationID);
            clb.setGrupoCollaboration(elementos);
            clb.setGrupoMessagens(interacoes);
            clb.setGrupoLoopOrFluxo(loopsOuFluxos);
            collaboration.add(clb);
            collaborationID = "";
            System.out.println("Fim tag: "+qName);
        }
    }
}