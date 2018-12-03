/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.msc.app.project;

import br.uece.lotus.BigState;
import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.msc.api.model.msc.ProjectMSC;
import br.uece.lotus.msc.api.model.msc.TransitionMSC;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscBlock;
import br.uece.lotus.msc.api.model.msc.bmsc.BmscComponent;
import br.uece.lotus.msc.api.model.msc.hmsc.GenericElement;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscBlock;
import br.uece.lotus.msc.api.model.msc.hmsc.InterceptionNode;
import br.uece.lotus.msc.api.viewer.hMSC.GenericElementView;
import br.uece.lotus.project.XMLWritter;
import br.uece.lotus.msc.api.model.msc.hmsc.HmscComponent;
import br.uece.lotus.msc.api.project.ProjectMSCSerializer;
import br.uece.lotus.msc.api.viewer.bMSC.BlockDSView;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;


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
public class ProjectMSCxmlSerializer implements ProjectMSCSerializer {

    private static ProjectMSC mProject;
    private static HmscComponent standardModeling;
    private static Component lts;
    private static BmscComponent bMSC;

    @Override
    public ProjectMSC parseStream(InputStream stream) throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(stream));
        return mProject;
    }

    @Override
    public void toStream(ProjectMSC p, OutputStream stream) throws Exception {


            XMLWritter xml = new XMLWritter(stream);
            xml.begin("project-msc");
            xml.attr("version", "1.0");
            xml.attr("name", p.getName());

            xml.begin("HmscComponent");
//            xml.attr("countGenericElement",p.getStandardModeling().getGenericElementList().size());
//            xml.attr("countHmscBlock",p.getStandardModeling().getHmscBlockList().size());
//            xml.attr("countIntercptionNode",p.getStandardModeling().getInterceptionNodeList().size());

            xml.begin("GenericElements");
            for(GenericElement genericElement : p.getStandardModeling().getGenericElementList()){
                xml.begin("GenericElement");
                xml.attr("type", genericElement.getClass().getSimpleName());
                xml.attr("id", genericElement.getID());
                xml.attr("x", genericElement.getLayoutX());
                xml.attr("y", genericElement.getLayoutY());
                xml.attr("label", genericElement.getLabel());
              //  xml.attr("view",genericElement.getValue("view"));

                if(genericElement instanceof HmscBlock){
                    HmscBlock hmscBlock = (HmscBlock) genericElement;
                    xml.attr("isInitial", hmscBlock.isInitial());
                    xml.attr("isExceptional",hmscBlock.isExceptional());

                    if(hmscBlock.isFull()){
                        xml.attr("isFull", "true");
                        xml.attr("bMSC", hmscBlock.getBmscComponet().getName());
                    }
                }



                xml.end();
            }
            xml.end();
            xml.begin("TransitionMSC");
            for(TransitionMSC t : p.getStandardModeling().getTransitionMSCList()){
                xml.begin("TransitionHMSC");
                if(t.getSource() instanceof GenericElement){
                    xml.attr("from", ((GenericElement) t.getSource()).getID());
                    //xml.attr("type", t.getSource().getClass().getSimpleName());
                }else {
                    xml.attr("from", ((GenericElementView) t.getSource()).getGenericElement().getID());
                   // xml.attr("type", ((GenericElementView) t.getSource()).getGenericElement().getClass().getSimpleName());
                }if(t.getDestiny() instanceof GenericElement){
                    xml.attr("to", ((GenericElement) t.getDestiny()).getID() );
                   // xml.attr("type", t.getDestiny().getClass().getSimpleName());
                }else {
                    xml.attr("to", ((GenericElementView) t.getDestiny()).getGenericElement().getID());
                    //xml.attr("type", ((GenericElementView) t.getDestiny()).getGenericElement().getClass().getSimpleName());
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

                String guard = t.getGuard();
                if (guard != null && !guard.isEmpty()) {
                    guard = guard.replace("&&","@EE;").replace("<","@LESS").replace(">","@MORE");
                    xml.attr("guard", guard);
                }

                String action = String.join("," ,t.getActions());
                if (action != null && !action.isEmpty()) {
                    xml.attr("action", action);
                }

                xml.end();
            }
            xml.end();

            Component composed = p.getLTS_Composed();
            if(composed != null){
                xml.begin("LTS-Composed");
                xml.begin("States");
                for(State v : composed.getStates()){
                    xml.begin("stateInBase");
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
                        s = s.replace("&&","@EE;").replace("<","@LESS").replace(">","@MORE");
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
            xml.end();//HmscComponent

            xml.begin("bMSCs");
            for(BmscComponent bmsc : p.getComponentsDS()){
                xml.begin("bMSC");
                xml.attr("name", bmsc.getName());
                xml.begin("Objects");
                for(BmscBlock o : bmsc.getBmscBlockList()){
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

                    if(t.getSource() instanceof BmscBlock){
                        xml.attr("from", ((BmscBlock) t.getSource()).getID());
                    }else {
                        xml.attr("from", ((BlockDSView) t.getSource()).getBlockDS().getID());
                    }
                    if(t.getDestiny() instanceof BmscBlock){
                        xml.attr("to", ((BmscBlock) t.getDestiny()).getID());
                    }else {
                        xml.attr("to", ((BlockDSView) t.getDestiny()).getBlockDS().getID());
                    }

                    xml.attr("sequence", String.valueOf(t.getIdSequence()));

                    String label = t.getLabel();
                    xml.attr("label", label == null ? "" : label);

                    String guard = t.getGuard();
                    if (guard != null) {
                        guard = guard.replace("&&","@EE;").replace("<","@LESS").replace(">","@MORE");
                        xml.attr("guard", guard);
                    }

                    String parameters = String.join(",", t.getParameters());
                    if (t.getParameters().size() > 0) {
                        xml.attr("parameters", parameters);
                    }

                    Integer i = (Integer) t.getValue("view.type");
                    if (i != null) {
                        xml.attr("view-type", String.valueOf(i));
                    }
                    xml.end();

//                    List<String> parameters = t.getParameters();
//                    xml.begin("Parameters");
//                    for(String parameter : parameters){
//                        xml.begin("Parameter");
//                        xml.attr("name", parameter);
//                        xml.end();
//                    }
//                    xml.end();//Parameters

                }
                xml.end();//TransitionMSC
                xml.end();//bMSC
            }
            xml.end();//bMSC'run
            xml.begin("FragmentsLTS");
            for(Component c : p.getFragments()){
                xml.begin("LTS");
                xml.attr("name", c.getName());
                xml.begin("States");
                for(State v : c.getStates()){
                    xml.begin("stateInBase");
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
                        s = s.replace("&&","@EE;").replace("<","@LESS").replace(">","@MORE");
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


    }

    private static final DefaultHandler handler = new DefaultHandler(){

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch(qName){
                case "project-msc":{
                    parseProjectTag(attributes);
                    break;
                }
                case "GenericElement":{
                    parseGenericElement_Tag(attributes);
                    break;
                }
                case "TransitionHMSC":{
                    parseTransitionHMSC_Tag(attributes);
                    break;
                }
                case "stateInBase":{
                    parseState_Tag(attributes);
                    break;
                }
                case "transition":{
                    parseTransition_Tag(attributes);
                    break;
                }
                case "bMSC":{
                    bMSC = new BmscComponent();
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
//                case "Parameters":{
//                    parseParameters(attributes);
//                    break;
//                }
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
                    BmscComponent bmsc = bMSC;
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
            mProject = new ProjectMSC();
            mProject.setName(attributes.getValue("name"));
            standardModeling = new HmscComponent();
            standardModeling.setName("Standard Modeling("+mProject.getName()+")");
            mProject.setComponentBuildDS(standardModeling);
        }

        private void parseGenericElement_Tag(Attributes attributes) {

            if(attributes.getValue("type").equals(HmscBlock.class.getSimpleName())){

                Integer id = Integer.parseInt(attributes.getValue("id"));
                HmscBlock hmscBlock = standardModeling.newHmsc(id);
                hmscBlock.setLabel(attributes.getValue("label"));
                hmscBlock.setLayoutX(Double.parseDouble(attributes.getValue("x")));
                hmscBlock.setLayoutY(Double.parseDouble(attributes.getValue("y")));
                hmscBlock.setInitial(Boolean.valueOf(attributes.getValue("isInitial")));
                hmscBlock.setExceptional(Boolean.valueOf(attributes.getValue("isExceptional")));
                hmscBlock.setFull(Boolean.valueOf(attributes.getValue("isFull")));
             //   hmscBlock.putValue("view",attributes.getValue("view"));


            }else {
                Integer id = Integer.parseInt(attributes.getValue("id"));
                InterceptionNode interceptionNode = standardModeling.newInterceptionNode(id);
                interceptionNode.setLayoutX(Double.parseDouble(attributes.getValue("x")));
                interceptionNode.setLayoutY(Double.parseDouble(attributes.getValue("y")));
               // interceptionNode.putValue("view",attributes.getValue("view"));
            }




        }

        private void parseTransitionHMSC_Tag(Attributes attributes) {
            String label = attributes.getValue("label");
            String guard = attributes.getValue("guard");
            List<String> actions = null;

            if(attributes.getValue("action")!= null){
                 actions = Arrays.asList(attributes.getValue("action").split(","));
            }




            if(guard != null){
                guard = guard.replace("@EE;","&&").replace("@LESS","<").replace("@MORE",">");
            }

            String prob = attributes.getValue("prob");
            String viewType = attributes.getValue("view-type");

            GenericElement  src = standardModeling.getGenericElementByID(Integer.parseInt(attributes.getValue("from")));
            GenericElement dst = standardModeling.getGenericElementByID(Integer.parseInt(attributes.getValue("to")));


            standardModeling.buildTransitionMSC(src, dst)
                    .setLabel(label)
                    .setGuard(guard)
                    .setActions(actions)
                    .setProbability(prob == null? null : Double.parseDouble(prob))
                    .setValue("view.type", viewType == null? null : Integer.parseInt(viewType))
                    .createForXmlHMSC();


        }

        private void parseState_Tag(Attributes attributes) {
            try {
                if(lts == null){
                    lts =  new Component();
                    lts.setAutoUpdateLabels(false);
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
            if(guard != null){
                guard = guard.replace("@EE;","&&").replace("@LESS","<").replace("@MORE",">");
            }

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
            BmscBlock object = bMSC.newBmscBlock(id);
            object.setLabel(attributes.getValue("name"));
            object.setLayoutX(Double.parseDouble(attributes.getValue("x")));
            object.setLayoutY(Double.parseDouble(attributes.getValue("y")));
        }

        private void parseTransitionBMSC_Tag(Attributes attributes) {
            String label = attributes.getValue("label");
            String sequence =  attributes.getValue("sequence");
            String guard = attributes.getValue("guard");

            if(guard != null){
                guard = guard.replace("@EE;","&&").replace("@LESS","<").replace("@MORE",">");
            }

            String viewType = attributes.getValue("view-type");
            String parameters = attributes.getValue("parameters");
            List<String> parametersList = null;

            if(parameters!= null){
                parametersList = Arrays.asList(parameters.split(","));
            }


            BmscBlock src = bMSC.getBMSC_ByID(Integer.parseInt(attributes.getValue("from")));
            BmscBlock dst = bMSC.getBMSC_ByID(Integer.parseInt(attributes.getValue("to")));

          //  List<String> parameters = attributes.get
            TransitionMSC transitionMSC= bMSC.buildTransition(src, dst)
                    .setLabel(label)
                    .setIdSequence(Integer.parseInt(sequence))
                    .setGuard(guard)
                    .setValue("view.type", viewType == null? null : Integer.parseInt(viewType))
                    .createForXmlBMSC();

            if(parametersList!=null){
                transitionMSC.setParameters(parametersList);
            }

        }

        private void bindHMSC(BmscComponent bmsc) {
            HmscComponent sm = mProject.getStandardModeling();
            for(HmscBlock h : sm.getHmscBlockList()){
                if(h.getLabel().equals(bmsc.getName())){
                    h.setDiagramSequence(bmsc);
                }
            }
        }

        //DEBUG-----------------------------------------
    /*  private void printHmsc() {
            System.out.println("--------------------HmscComponent-----------------");
            for(HmscBlock h : mProject.getParallelComponent().getHmscBlockList()){
                System.out.println("HmscBlock: "+h.getLabel()+" id:"+h.getID());
            }
            for(TransitionMSC t : mProject.getParallelComponent().getTransitionMSCList()){
                System.out.println("TransitionHMSC from: "+((HmscBlock)t.getSource()).getID()+" to: "+((HmscBlock)t.getDestiny()).getID());
            }
            System.out.println("-------------------------------------------------");
        }
/*
        private void printLTSComposed() {
            System.out.println("--------------------LTS Composed-----------------");
            for(State run : mProject.getLTS_Composed().getStates()){
                System.out.println("State: "+run.getLabel()+" x:"+run.getLayoutX()+" y:"+run.getLayoutY());
            }
            for(Transition t : mProject.getLTS_Composed().getTransitionMSCList()){
                System.out.println("Transition composed   from: "+t.getSource().getID()+" to: "+t.getDestiny().getID());
            }
            System.out.println("-------------------------------------------------");
        }

        private void printFragments() {
            System.out.println("--------------------LTS Fragment-----------------");
            for(Component c : mProject.getFragments()){
                System.out.println("Fragment : "+c.getName());
                for(State run : c.getStates()){
                    System.out.println("State: "+run.getLabel()+" x:"+run.getLayoutX()+" y:"+run.getLayoutY());
                }
                for(Transition t : c.getTransitionMSCList()){
                    System.out.println("Transition fragment   from: "+t.getSource().getID()+" to: "+t.getDestiny().getID());
                }
            }
            System.out.println("-------------------------------------------------");
        }*/
    };

    private void parseParameters(Attributes attributes) {

    }
}