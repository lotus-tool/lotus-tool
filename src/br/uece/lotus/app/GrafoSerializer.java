/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.app;

import br.uece.lotus.util.XMLWritter;
import br.uece.lotus.model.ComponentModel;
import br.uece.lotus.model.TransitionModel;
import br.uece.lotus.model.StateModel;
import br.uece.lotus.model.ProjectModel;
import br.uece.lotus.view.ComponentEditor;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author emerson
 */
public class GrafoSerializer {

    private static ProjectModel mProjeto;
    private static ComponentModel mComponent;
    private static StateModel mVerticeModel;
    private static TransitionModel mTransicaoModel;

    public static void toStream(ProjectModel p, OutputStream stream) {
        XMLWritter xml = new XMLWritter(stream);
        xml.begin("project");
        xml.attr("version", "1.0");
        for (ComponentModel c : p.getComponents()) {
            xml.begin("component");
            xml.attr("name", c.getName());

            xml.begin("states");
            
            for (StateModel v : c.getVertices()) {
                xml.begin("state");
                xml.attr("id", v.getID());
                String s = v.getValue(ComponentEditor.TAG_POS_X);
                xml.attr("x", s == null ? "" : s);
                
                s = v.getValue(ComponentEditor.TAG_POS_Y);
                xml.attr("y", s == null ? "" : s);
                
                s = v.getValue(ComponentEditor.TAG_LABEL);
                xml.attr("label", s == null ? "" : s);
                
                s = v.getValue(ComponentEditor.TAG_DEFAULT);
                xml.attr("default", s == null ? "" : s);
//                for (Map.Entry<String, String> prop : v.getValues().entrySet()) {
//                    xml.begin("property");
//                    xml.attr("name", prop.getKey());
//                    xml.attr("value", prop.getValue());
//                    xml.end();
//                }
                xml.end();
            }
            
            xml.end();
            xml.begin("transitions");

            for (TransitionModel t : c.getTransicoes()) {
                xml.begin("transition");
                xml.attr("from", t.getOrigem().getID());
                xml.attr("to", t.getDestino().getID());                
                
                String s = t.getValue(ComponentEditor.TAG_PROBABILIDADE);                
                xml.attr("prob", s == null ? "" : s);
                s = t.getValue(ComponentEditor.TAG_LABEL);
                xml.attr("label", s == null ? "" : s);
                s = t.getValue(ComponentEditor.TAG_GUARD);
                xml.attr("guard", s == null ? "" : s);
                
//                System.out.println(t.getValue(ComponentEditor.TAG_LABEL));
//                for (Map.Entry<String, String> prop : t.getValues().entrySet()) {
//                    xml.begin("property");
//                    xml.attr("name", prop.getKey());
//                    xml.attr("value", prop.getValue());
//                    xml.end();
//                }
                xml.end();
            }
            xml.end();

            xml.end();
        }
        xml.end();
    }
    
    private static DefaultHandler handler = new DefaultHandler() {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch (qName) {
                case "project": {
                    parseProjectTag();
                    break;
                }
                case "component": {
                    parseComponentTag(attributes);
                    break;
                }
                case "state": {
                    parseVerticeTag(attributes);
                    break;
                }
                case "transition": {
                    parseTransicaoTag(attributes);
                    break;
                }
//                case "property": {
//                    parsePropertyTag(attributes);
//                    break;
//                }
            }
        }
    };

    public static ProjectModel parseStream(InputStream stream) throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(stream));
        return mProjeto;
    }

    private static void parseProjectTag() {
        mProjeto = new ProjectModel();
    }
    

    private static void parseComponentTag(Attributes attributes) {
        mComponent = new ComponentModel();
        mComponent.setName(attributes.getValue("name"));
        mProjeto.getComponents().add(mComponent);
    }

    private static void parseVerticeTag(Attributes attributes) {
        mVerticeModel = new StateModel();
        mTransicaoModel = null;
        mVerticeModel.setID(Integer.parseInt(attributes.getValue("id")));
        mVerticeModel.setValue(ComponentEditor.TAG_LABEL, attributes.getValue("label"));
        mVerticeModel.setValue(ComponentEditor.TAG_POS_X, attributes.getValue("x"));
        mVerticeModel.setValue(ComponentEditor.TAG_POS_Y, attributes.getValue("y"));
        mVerticeModel.setValue(ComponentEditor.TAG_DEFAULT, attributes.getValue("default"));
        mComponent.add(mVerticeModel);
    }

    private static void parseTransicaoTag(Attributes attributes) {
        mTransicaoModel = new TransitionModel();
        mVerticeModel = null;
        StateModel origem = mComponent.getVertice(Integer.parseInt(attributes.getValue("from")));
        StateModel destino = mComponent.getVertice(Integer.parseInt(attributes.getValue("to")));
        origem.getTransicoesSaida().add(mTransicaoModel);
        destino.getTransicoesEntrada().add(mTransicaoModel);
        mTransicaoModel.setOrigem(origem);
        mTransicaoModel.setDestino(destino);        
        String s = attributes.getValue("label");
        mTransicaoModel.setValue(ComponentEditor.TAG_LABEL, s.trim().isEmpty() ? null : s);
        s = attributes.getValue("prob");
        mTransicaoModel.setValue(ComponentEditor.TAG_PROBABILIDADE, s.trim().isEmpty() ? null : s);
        s = attributes.getValue("guard");
        mTransicaoModel.setValue(ComponentEditor.TAG_GUARD, s.trim().isEmpty() ? null : s);
        mComponent.add(mTransicaoModel);
    }
    
//    private static void parsePropertyTag(Attributes attributes) {
//        if (mVerticeModel != null) {
//            mVerticeModel.setValue(attributes.getValue("name"), attributes.getValue("value"));
//        } else if (mTransicaoModel != null) {
//            mTransicaoModel.setValue(attributes.getValue("name"), attributes.getValue("value"));
//        }
//    }
}
