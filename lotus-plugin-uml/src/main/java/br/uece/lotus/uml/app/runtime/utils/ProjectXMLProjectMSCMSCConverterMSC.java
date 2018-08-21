package br.uece.lotus.uml.app.runtime.utils;



import br.uece.lotus.uml.app.runtime.model.custom.HMSCCustom;
import br.uece.lotus.uml.app.runtime.model.custom.ProjectMSCCustom;
import br.uece.lotus.uml.app.runtime.model.custom.TransitionHMSCCustom;
import br.uece.lotus.uml.app.runtime.model.custom.StantardModelingCustom;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectXMLProjectMSCMSCConverterMSC implements ProjectMSCConverter<Path> {

    private ProjectMSCCustom projectMSCCustom;
    private  DocumentBuilderFactory factory;
    private  DocumentBuilder documentBuilder;
    private StantardModelingCustom StantardModelingCustom;

    public ProjectXMLProjectMSCMSCConverterMSC() {
         factory = DocumentBuilderFactory.newInstance();
        try {
                documentBuilder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
        }

    }




    @Override
    public ProjectMSCCustom toConverter(Path path) throws Exception {
        // fazer o tratamento para quando for importanto um arquivo xml vazio ou algo parecido
        InputStream inputStream = Files.newInputStream(path);

        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();
        parseAboutProjectMSCCustom(document);
        return projectMSCCustom;
    }


    private void parseAboutProjectMSCCustom(Document document) {

        projectMSCCustom = new ProjectMSCCustom();

        NodeList projectMscNodeList = document.getElementsByTagName("project-msc");
        Node projectMscNode =  projectMscNodeList.item(0);
        Element projectMscElement = (Element) projectMscNode;
        String nameProjectMsc = projectMscElement.getAttribute("name");
        projectMSCCustom.setName(nameProjectMsc);

        parseAboutStandardModelingCustom(document);



    }

    private void parseAboutStandardModelingCustom(Document document) {
        StantardModelingCustom = new StantardModelingCustom();
        projectMSCCustom.addStantardModelingCustom(StantardModelingCustom);

        parseAboutHMSC(document);

        parseAboutTransitionHMSC(document);
    }



    private void parseAboutTransitionHMSC(Document document) {
        NodeList transitionHMSCNodeList = document.getElementsByTagName("TransitionHMSC");
        Node transitionHMSCNode;
        Element transitionHMSCElement;
        TransitionHMSCCustom transitionHMSCCustom;
        Integer srcHMSCInInt,dstHMSCInInt;
        String srcHMSCInString,dstHMSCInString, label;
        Double probability;

        HMSCCustom srcHMSCCustom, dstHMSCCustom;

        for(int i = 0; i < transitionHMSCNodeList.getLength(); i++ ){
           transitionHMSCNode = transitionHMSCNodeList.item(i);
           transitionHMSCElement = (Element) transitionHMSCNode;
           srcHMSCInString = transitionHMSCElement.getAttribute("from");
           srcHMSCInInt = Integer.valueOf(srcHMSCInString);

           dstHMSCInString = transitionHMSCElement.getAttribute("to");
           dstHMSCInInt = Integer.valueOf(dstHMSCInString);

           label = transitionHMSCElement.getAttribute("label");

//           if(label == null){
//               label = srcHMSCInString.concat("&").concat(dstHMSCInString);
//           }

           probability = Double.valueOf(transitionHMSCElement.getAttribute("prob"));

           srcHMSCCustom = StantardModelingCustom.getHMSCCustomFromId(srcHMSCInInt);
           dstHMSCCustom = StantardModelingCustom.getHMSCCustomFromId(dstHMSCInInt);

           transitionHMSCCustom = new TransitionHMSCCustom(StantardModelingCustom, srcHMSCCustom, dstHMSCCustom);
           transitionHMSCCustom.setLabel(label);
           transitionHMSCCustom.setProbability(probability);
        }

    }

    private void parseAboutHMSC(Document document) {

        Node HMSCNode;
        Element HMSCElement;
        String HMSCLabel, HMSCIdInString;
        Integer HMSCIdInInt;
        HMSCCustom HMSCCustom;

        NodeList HMSCNodeList = document.getElementsByTagName("hMSC");
        for(int i = 0; i<HMSCNodeList.getLength(); i++){
            HMSCNode = HMSCNodeList.item(i);
            HMSCElement = ((Element) HMSCNode);
            HMSCIdInString = HMSCElement.getAttribute("id");
            HMSCIdInInt = Integer.valueOf(HMSCIdInString);
            HMSCLabel = HMSCElement.getAttribute("label");

            HMSCCustom = new HMSCCustom(StantardModelingCustom, HMSCIdInInt);
            HMSCCustom.setLabel(HMSCLabel);


        }

    }


    @Override
    public void toUpdate(ProjectMSCCustom projectMSCCustom, Path pathSrc) throws Exception {
        InputStream inputStream = Files.newInputStream(pathSrc);

        Document document = documentBuilder.parse(inputStream);
        document.getDocumentElement().normalize();


        NodeList transitionHMSCNodeList = document.getElementsByTagName("TransitionHMSC");
        Node transitionHMSCNode;
        Element transitionHMSCElement;
        Integer srcHMSCInInt,dstHMSCInInt;
        String srcHMSCInString,dstHMSCInString;

        for(int i = 0; i < transitionHMSCNodeList.getLength(); i++ ){
            transitionHMSCNode = transitionHMSCNodeList.item(i);
            transitionHMSCElement = (Element) transitionHMSCNode;
            srcHMSCInString = transitionHMSCElement.getAttribute("from");
            srcHMSCInInt = Integer.valueOf(srcHMSCInString);

            dstHMSCInString = transitionHMSCElement.getAttribute("to");
            dstHMSCInInt = Integer.valueOf(dstHMSCInString);

            TransitionHMSCCustom currentTransitionHMSCCustom = StantardModelingCustom.getTransitionHMSCCustom(srcHMSCInInt, dstHMSCInInt);

            if(currentTransitionHMSCCustom == null){
                throw new Exception("TransitionHMSCCustom from xml not found!");
            }else {

                updateProbabilityInTranstionHMSCNode(transitionHMSCNode, currentTransitionHMSCCustom);
            }

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(pathSrc.toUri()));
        transformer.transform(source, result);
    }



    private void updateProbabilityInTranstionHMSCNode(Node transitionHMSCNode, TransitionHMSCCustom transitionHMSCCustom) {
        NamedNodeMap attr = transitionHMSCNode.getAttributes();
        Node probAttr = attr.getNamedItem("prob");
        Double updateProbabilityDouble = transitionHMSCCustom.getProbability();
        String updateProbabilityString = String.valueOf(updateProbabilityDouble);
        probAttr.setTextContent(updateProbabilityString);
    }

    @Override
    public Path toUndo(ProjectMSCCustom projectMSCCustom) throws Exception {
        return null;
    }


}
