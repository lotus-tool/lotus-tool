/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.uml.app.project;

import br.uece.lotus.BigState;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.XMLWritter;
import br.uece.lotus.uml.api.ds.BlockDS;
import br.uece.lotus.uml.api.ds.ComponentDS;
import br.uece.lotus.uml.api.ds.Hmsc;
import br.uece.lotus.uml.api.ds.ProjectDS;
import br.uece.lotus.uml.api.ds.StandardModeling;
import br.uece.lotus.uml.api.ds.TransitionMSC;
import br.uece.lotus.uml.api.project.ProjectDSSerializer;
import br.uece.lotus.uml.api.viewer.bMSC.BlockDSView;
import br.uece.lotus.uml.api.viewer.hMSC.HmscView;
import br.uece.lotus.uml.api.viewer.transition.TransitionMSCView;
import java.io.InputStream;
import java.io.OutputStream;
import javafx.scene.shape.Line;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author Bruno Barbosa
 */
public class ProjectDSxmlSerializer implements ProjectDSSerializer{
    
    private static ProjectDS mProject;
    private static StandardModeling standardModeling;
    private static Component lts;
    private static ComponentDS bMSC;
    
    @Override
    public ProjectDS parseStream(InputStream stream) throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(stream));
        return mProject;
    }

    @Override
    public void toStream(ProjectDS p, OutputStream stream) throws Exception {
        
        try {
            XMLWritter xml = new XMLWritter(stream);
            xml.begin("project-msc");
            xml.attr("version", "1.0");
            xml.attr("name", p.getName());
            
                xml.begin("StandardModeling");
                    xml.begin("hMSCs");
                    for(Hmsc hmsc : p.getStandardModeling().getBlocos()){
                        xml.begin("hMSC");
                        xml.attr("id", hmsc.getID());
                        xml.attr("x", hmsc.getLayoutX());
                        xml.attr("y", hmsc.getLayoutY());
                        xml.attr("label", hmsc.getLabel());
                        
                        if(hmsc.isFull()){
                            xml.attr("isFull", "true");
                            xml.attr("bMSC", hmsc.getmDiagramSequence().getName());
                        }
                        
                        xml.end();
                    }
                    xml.end();
                    xml.begin("TransitionMSC");
                    for(TransitionMSC t : p.getStandardModeling().getTransitions()){
                        xml.begin("TransitionHMSC");
                        if(t.getSource() instanceof Hmsc){
                            xml.attr("from", ((Hmsc) t.getSource()).getID() );
                        }else {
                            xml.attr("from", ((HmscView) t.getSource()).getHMSC().getID());
                        }if(t.getDestiny() instanceof  Hmsc){
                            xml.attr("to", ((Hmsc) t.getDestiny()).getID() );
                        }else {
                            xml.attr("to", ((HmscView) t.getDestiny()).getHMSC().getID());
                        }
                        String label = t.getLabel();
                        xml.attr("label", label == null ? "" : label);
                        Double prob = t.getProbability();
                        if(prob != null){
                            xml.attr("prob", prob);
                        }
                        Integer i = (Integer) t.getValue("view.type");
                        if (i != null) {
                            xml.attr("view-type", String.valueOf(i));
                        }
                        xml.end();
                    }
                    xml.end();
                    
                    Component composed = p.getLTS_Composed();
                    if(composed != null){
                        xml.begin("LTS-Composed");
                            xml.begin("States");
                            for(State v : composed.getStates()){
                                xml.begin("state");
                                xml.attr("id", v.getID());
                                xml.attr("x", v.getLayoutX());
                                xml.attr("y", v.getLayoutY());
                                xml.attr("label", v.getLabel());

                                if (v.isInitial()) {
                                    xml.attr("initial", "true");
                                }
                                if (v.isError()) {
                                    xml.attr("error", "true");
                                }
                                if (v.isFinal()) {
                                    xml.attr("final", "true");
                                }
                                if (BigState.verifyIsBigState(v)) {
                                    xml.attr("bigstate", "true");
                                }
                                xml.end();
                            }
                            xml.end();
                            xml.begin("Transitions");
                                for (Transition t : composed.getTransitions()) {
                                    xml.begin("transition");
                                    xml.attr("from", t.getSource().getID());
                                    xml.attr("to", t.getDestiny().getID());
                                    Double d = t.getProbability();
                                    if (d != null) {
                                        xml.attr("prob", t.getProbability());
                                    }
                                    String s = t.getLabel();
                                    xml.attr("label", s == null ? "" : s);
                                    s = t.getGuard();
                                    if (s != null) {
                                        xml.attr("guard", s);
                                    }

                                    Integer i = (Integer) t.getValue("view.type");
                                    if (i != null) {
                                        xml.attr("view-type", String.valueOf(i));
                                    }
                                    xml.end();
                                }
                            xml.end();
                        xml.end();//Lts - composed
                    }
                xml.end();//StandardModeling
                
                xml.begin("bMSCs");
                    for(ComponentDS bmsc : p.getComponentsDS()){
                        xml.begin("bMSC");
                        xml.attr("name", bmsc.getName());
                            xml.begin("Objects");
                            for(BlockDS o : bmsc.getBlockDS()){
                                xml.begin("Object");
                                xml.attr("name", o.getLabel());
                                xml.attr("id", o.getID());
                                xml.attr("x", o.getLayoutX());
                                xml.attr("y", o.getLayoutY());
                                xml.end();
                            }
                            xml.end();//Objects
                            xml.begin("TransitionMSC");
                            for(TransitionMSC t : bmsc.getAllTransitions()){
                                xml.begin("TransitionBMSC");

                                if(t.getSource() instanceof BlockDS){
                                    xml.attr("from", ((BlockDS) t.getSource()).getID());
                                }else {
                                    xml.attr("from", ((BlockDSView) t.getSource()).getBlockDS().getID());
                                }
                                if(t.getDestiny() instanceof BlockDS){
                                    xml.attr("to", ((BlockDS) t.getDestiny()).getID());
                                }else {
                                    xml.attr("to", ((BlockDSView) t.getDestiny()).getBlockDS().getID());
                                }

                                xml.attr("sequence", String.valueOf(t.getIdSequence()));
                                
                                String label = t.getLabel();
                                xml.attr("label", label == null ? "" : label);
                                label = t.getGuard();
                                    if (label != null) {
                                        xml.attr("guard", label);
                                    }
                                Integer i = (Integer) t.getValue("view.type");
                                if (i != null) {
                                    xml.attr("view-type", String.valueOf(i));
                                }
                                xml.end();
                            }
                            xml.end();//TransitionMSC
                        xml.end();//bMSC
                    }
                xml.end();//bMSC's
                xml.begin("FragmentsLTS");
                    for(Component c : p.getFragments()){
                        xml.begin("LTS");
                        xml.attr("name", c.getName());
                            xml.begin("States");
                            for(State v : c.getStates()){
                                xml.begin("state");
                                xml.attr("id", v.getID());
                                xml.attr("x", v.getLayoutX());
                                xml.attr("y", v.getLayoutY());
                                xml.attr("label", v.getLabel());

                                if (v.isInitial()) {
                                    xml.attr("initial", "true");
                                }
                                if (v.isError()) {
                                    xml.attr("error", "true");
                                }
                                if (v.isFinal()) {
                                    xml.attr("final", "true");
                                }
                                if (BigState.verifyIsBigState(v)) {
                                    xml.attr("bigstate", "true");
                                }
                                xml.end();
                            }
                            xml.end();
                            xml.begin("Transitions");
                                for (Transition t : c.getTransitions()) {
                                    xml.begin("transition");
                                    xml.attr("from", t.getSource().getID());
                                    xml.attr("to", t.getDestiny().getID());
                                    Double d = t.getProbability();
                                    if (d != null) {
                                        xml.attr("prob", t.getProbability());
                                    }
                                    String s = t.getLabel();
                                    xml.attr("label", s == null ? "" : s);
                                    s = t.getGuard();
                                    if (s != null) {
                                        xml.attr("guard", s);
                                    }

                                    Integer i = (Integer) t.getValue("view.type");
                                    if (i != null) {
                                        xml.attr("view-type", String.valueOf(i));
                                    }
                                    xml.end();
                                }
                            xml.end();
                        xml.end();
                    }
                xml.end();//FragmentsLTS
                
            xml.end();
        } catch (Exception e) {
            System.out.println("Erro ao salvar o xml MSC");
            System.out.println(e.getMessage());
        }
        
    }
    
    private static final DefaultHandler handler = new DefaultHandler(){

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch(qName){
                case "project-msc":{
                    parseProjectTag(attributes);
                    break;
                }
                case "hMSC":{
                    parseHMSC_Tag(attributes);
                    break;
                }
                case "TransitionHMSC":{
                    parseTransitionHMSC_Tag(attributes);
                    break;
                }
                case "state":{
                    parseState_Tag(attributes);
                    break;
                }
                case "transition":{
                    parseTransition_Tag(attributes);
                    break;
                }
                case "bMSC":{
                    bMSC = new ComponentDS();
                    bMSC.setName(attributes.getValue("name"));
                    break;
                }
                case "Object":{
                    parseObject_Tag(attributes);
                    break;
                }
                case "TransitionBMSC":{
                    parseTransitionBMSC_Tag(attributes);
                    break;
                }
                case "LTS":{
                    lts = new Component();
                    lts.setName(attributes.getValue("name"));
                    break;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch(qName){
                case "LTS":{
                    Component c = lts;
                    mProject.addComponentFragmentLTS(c);
                    lts = null;
                    break;
                }
                case "LTS-Composed":{
                    Component c = lts;
                    mProject.setLTS_Composed(c);
                    lts = null;
                    break;
                }
                case "bMSC":{
                    ComponentDS bmsc = bMSC;
                    mProject.addComponent_bMSC(bmsc);
                    bindHMSC(bmsc);
                    bMSC = null;
                    break;
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            //printHmsc();
            //printLTSComposed();
            //printFragments();
        }

        private void parseProjectTag(Attributes attributes) {
            mProject = new ProjectDS();
            mProject.setName(attributes.getValue("name"));
            standardModeling = new StandardModeling();
            standardModeling.setName("Standard Modeling");
            mProject.setComponentBuildDS(standardModeling);
        }

        private void parseHMSC_Tag(Attributes attributes) {
            Integer id = Integer.parseInt(attributes.getValue("id"));
            Hmsc hmsc = standardModeling.newBlock(id);
            hmsc.setLabel(attributes.getValue("label"));
            hmsc.setLayoutX(Double.parseDouble(attributes.getValue("x")));
            hmsc.setLayoutY(Double.parseDouble(attributes.getValue("y")));
            
        }

        private void parseTransitionHMSC_Tag(Attributes attributes) {
            String label = attributes.getValue("label");
            String prob = attributes.getValue("prob");
            String viewType = attributes.getValue("view-type");
            
            Hmsc src = standardModeling.getBlocoByID(Integer.parseInt(attributes.getValue("from")));
            Hmsc dst = standardModeling.getBlocoByID(Integer.parseInt(attributes.getValue("to")));
            
            standardModeling.buildTransition(src, dst)
                                    .setLabel(label)
                                    .setProbability(prob == null? null : Double.parseDouble(prob))
                                    .setValue("view.type", viewType == null? null : Integer.parseInt(viewType))
                                    .createForXmlHMSC();
            
        }

        private void parseState_Tag(Attributes attributes) {
            try {
                if(lts == null){
                    lts =  new Component();
                }
                String id = attributes.getValue("id");
                String label = attributes.getValue("label");
                double x = Double.parseDouble(attributes.getValue("x"));
                double y = Double.parseDouble(attributes.getValue("y"));
                State s = lts.newState(Integer.parseInt(id));
                s.setLabel(label);
                s.setLayoutX(x);
                s.setLayoutY(y);
                if (Boolean.parseBoolean(attributes.getValue("initial"))) {
                    s.setAsInitial();
                    lts.setInitialState(s);
                }
                
            } catch (NumberFormatException | NullPointerException e) {
                //System.out.println("erro: "+e.getMessage());
            }
        }

        private void parseTransition_Tag(Attributes attributes) {
            Integer srcId = Integer.parseInt(attributes.getValue("from"));
            Integer dstId = Integer.parseInt(attributes.getValue("to"));
            String label = attributes.getValue("label");
            String prob = attributes.getValue("prob");
            String guard = attributes.getValue("guard");
            String viewType = attributes.getValue("view-type");
            
            lts.buildTransition(srcId, dstId)
                .setLabel(label)
                .setProbability(prob == null ? null : Double.parseDouble(prob))
                .setGuard(guard)
                .setValue("view.type", viewType == null ? null : Integer.parseInt(viewType))
                .create();
           
        }   
        
        private void parseObject_Tag(Attributes attributes) {
            Integer id = Integer.parseInt(attributes.getValue("id"));
            BlockDS object = bMSC.newBlockDS(id);
            object.setLabel(attributes.getValue("name"));
            object.setLayoutX(Double.parseDouble(attributes.getValue("x")));
            object.setLayoutY(Double.parseDouble(attributes.getValue("y")));
        }

        private void parseTransitionBMSC_Tag(Attributes attributes) {
            String label = attributes.getValue("label");
            String sequence =  attributes.getValue("sequence");
            String guard = attributes.getValue("guard");
            String viewType = attributes.getValue("view-type");
            
            BlockDS src = bMSC.getBMSC_ByID(Integer.parseInt(attributes.getValue("from")));
            BlockDS dst = bMSC.getBMSC_ByID(Integer.parseInt(attributes.getValue("to")));
            
            bMSC.buildTransition(src, dst)
                    .setLabel(label)
                    .setIdSequence(Integer.parseInt(sequence))
                    .setGuard(guard)
                    .setValue("view.type", viewType == null? null : Integer.parseInt(viewType))
                    .createForXmlBMSC();
        }
        
        private void bindHMSC(ComponentDS bmsc) {
            StandardModeling sm = mProject.getStandardModeling();
            for(Hmsc h : sm.getBlocos()){
                if(h.getLabel().equals(bmsc.getName())){
                    h.setmDiagramSequence(bmsc);
                }
            }
        }

        //DEBUG-----------------------------------------
    /*  private void printHmsc() {
            System.out.println("--------------------StandardModeling-----------------");
            for(Hmsc h : mProject.getStandardModeling().getBlocos()){
                System.out.println("Hmsc: "+h.getLabel()+" id:"+h.getID());
            }
            for(TransitionMSC t : mProject.getStandardModeling().getTransitions()){
                System.out.println("TransitionHMSC from: "+((Hmsc)t.getSource()).getID()+" to: "+((Hmsc)t.getDestiny()).getID());
            }
            System.out.println("-------------------------------------------------");
        }
/*
        private void printLTSComposed() {
            System.out.println("--------------------LTS Composed-----------------");
            for(State s : mProject.getLTS_Composed().getStates()){
                System.out.println("State: "+s.getLabel()+" x:"+s.getLayoutX()+" y:"+s.getLayoutY());
            }
            for(Transition t : mProject.getLTS_Composed().getTransitions()){
                System.out.println("Transition composed   from: "+t.getSource().getID()+" to: "+t.getDestiny().getID());
            }
            System.out.println("-------------------------------------------------");
        }

        private void printFragments() {
            System.out.println("--------------------LTS Fragment-----------------");
            for(Component c : mProject.getFragments()){
                System.out.println("Fragment : "+c.getName());
                for(State s : c.getStates()){
                    System.out.println("State: "+s.getLabel()+" x:"+s.getLayoutX()+" y:"+s.getLayoutY());
                }
                for(Transition t : c.getTransitions()){
                    System.out.println("Transition fragment   from: "+t.getSource().getID()+" to: "+t.getDestiny().getID());
                }
            }
            System.out.println("-------------------------------------------------");
        }*/
    };
}
