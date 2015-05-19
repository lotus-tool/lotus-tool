/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.tools.uml.xmi.AtorAndClasse;
import br.uece.lotus.tools.uml.xmi.Collaboration;
import br.uece.lotus.tools.uml.xmi.Interaction;
import br.uece.lotus.tools.uml.xmi.NamespaceOwnedElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
            System.out.println("\nIndividuo: "+ aac.getNome()+ " xmi.id = "+aac.getXmiID());
        }
        for(NamespaceOwnedElement noe : elementos){
            System.out.println("\nClassifierRole: "+noe.getXmiIDClassifierRole()+"\nClassifierRoleBase: "+noe.getXmiIDREFclassifierBase());
        }
        for(Interaction i : interacoes){
            System.out.println("\nOrigem: "+ i.getEnviando()+"\n"+
                                "Destino: "+ i.getRecebendo()+"\n"+
                                "Menssagem: "+ i.getNomeMensagem()+"\n"+
                                "IDInteracao: "+ i.getXmiIDREFcollaboration()+"\n");
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
    private String msg, envia,recebe;
    private boolean atributosMaisDe1Message = false;
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
            AtorAndClasse acc = new AtorAndClasse(atts.getValue(1), atts.getValue(0));
            atoresAndClasses.add(acc);
        }
        //------------------------------Collaborations--------------------------------------------------------------
        if(tagAtual.equals("UML:Collaboration") && atts.getLength()>1){
            collaborationID = atts.getValue("xmi.id");
            atributosMaisDe1collaboration = true;
            collaborationTagAberta = true;
        }
        //-------------------------------ClassifierRole e Base-------------------------------------------------------
        if(tagAtual.equals("UML:ClassifierRole") && collaborationTagAberta && atts.getLength()>1){
            classifierRoleTagAberta = true;
            atributosMaisDe1Role = true;
            classifierRoleID = atts.getValue("xmi.id");
            if(!atts.getValue("name").equals("")){
                AtorAndClasse aac2 = new AtorAndClasse(atts.getValue("name"), atts.getValue("xmi.id"));
                NamespaceOwnedElement noe2 = new NamespaceOwnedElement(atts.getValue("xmi.id"), atts.getValue("xmi.id"));
                atoresAndClasses.add(aac2);
                elementos.add(noe2);
            }
        }
        if(tagAtual.equals("UML:ClassifierRole.base") && classifierRoleTagAberta){
            classifierRoleBaseTagAberta = true;
        }
        if(tagAtual.equals("UML:Classifier") && classifierRoleBaseTagAberta){
            classifierRoleBasaID = atts.getValue("xmi.idref");
            NamespaceOwnedElement noe = new NamespaceOwnedElement(classifierRoleID, classifierRoleBasaID);
            elementos.add(noe);
            classifierRoleBaseTagAberta = false;
            classifierRoleTagAberta = false;
            atributosMaisDe1Role = false;
            classifierRoleBasaID = "";
            classifierRoleID = "";
        }
        //-------------------------------Menssagens-------------------------------------------------------------
        if(tagAtual.equals("UML:Interaction.context") && collaborationTagAberta){
            interactionContextTagAberta = true;
        }
        if(tagAtual.equals("UML:Collaboration") && interactionContextTagAberta){
            idREFinteractionToCollaboration = atts.getValue("xmi.idref");
            interactionContextTagAberta = false;
        }
        if(tagAtual.equals("UML:Interaction.message")){
            interactionMessageTagAberta = true;
            System.out.println("entrou no interaction.message");
        }
        if(tagAtual.equals("UML:Message") && interactionMessageTagAberta && atts.getLength()>1){
            atributosMaisDe1Message = true;
            messageTagAberta = true;
            msg = atts.getValue("name");
        }
        if(tagAtual.equals("UML:Message.sender") && messageTagAberta){
            messageSenderTagAberta = true;
        }
        if(tagAtual.equals("UML:Message.receiver") && messageTagAberta){
            messageReceiverTagAberta = true;
        }
        if(tagAtual.equals("UML:ClassifierRole") && messageSenderTagAberta){
            envia = atts.getValue("xmi.idref");
            messageSenderTagAberta = false;
        }
        if(tagAtual.equals("UML:ClassifierRole") && messageReceiverTagAberta){
            recebe = atts.getValue("xmi.idref");
            messageReceiverTagAberta = false;
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tagAtual = "";
        if("UML:Package".equals(qName)){
            packageInvalido = "";
        }
        
        if("UML:Message".equals(qName) && interactionMessageTagAberta && atributosMaisDe1Message){
            messageTagAberta = false;
            atributosMaisDe1Message = false;
            Interaction inter = new Interaction(idREFinteractionToCollaboration, msg, envia, recebe);
            interacoes.add(inter);
            msg = "";
            envia = "";
            recebe = "";
        }
        
        if("UML:Interaction.message".equals(qName)){
            idREFinteractionToCollaboration = "";
            interactionMessageTagAberta = false;
        }
        
        if("UML:Collaboration".equals(qName) && atributosMaisDe1collaboration){
            collaborationTagAberta = false;
            atributosMaisDe1collaboration = false;
            Collaboration clb = new Collaboration(collaborationID);
            clb.setGrupoCollaboration(elementos);
            clb.setGrupoMessagens(interacoes);
            collaboration.add(clb);
            collaborationID = "";
        }
    }
    
}