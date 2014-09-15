/*
 * The MIT License
 *
 * Copyright 2014 Universidade Estadual do Ceará.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uece.lotus.project;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.io.InputStream;
import java.io.OutputStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ProjectXMLSerializer implements ProjectSerializer {

    private static Project mProjeto;
    private static Component mComponent;
    private static State mState;
    private static Transition mTransition;

    @Override
    public void toStream(Project p, OutputStream stream) {
        XMLWritter xml = new XMLWritter(stream);
        xml.begin("project");
        xml.attr("version", "1.0");
        for (Component c : p.getComponents()) {
            xml.begin("component");
            xml.attr("name", c.getName());

            xml.begin("states");
            
            for (State v : c.getStates()) {
                xml.begin("state");
                xml.attr("id", v.getID());                
                xml.attr("x", v.getLayoutX());
                xml.attr("y", v.getLayoutY());                                

                if (v.isInitial()) {
                    xml.attr("initial", "true");
                }               
                if (v.isError()) {
                    xml.attr("error", "true");
                }                
                if (v.isFinal()) {
                    xml.attr("final", "true");
                }
                xml.end();
            }
            
            xml.end();
            xml.begin("transitions");

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
                xml.end();
            }
            xml.end();

            xml.end();
        }
        xml.end();
    }
    
    private static final DefaultHandler handler = new DefaultHandler() {
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
                    parseStateTag(attributes);
                    break;
                }
                case "transition": {
                    parseTransitionTag(attributes);
                    break;
                }
            }
        }
    };

    @Override
    public Project parseStream(InputStream stream) throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(stream));
        return mProjeto;
    }

    private static void parseProjectTag() {
        mProjeto = new Project();
    }
    

    private static void parseComponentTag(Attributes attributes) {
        mComponent = new Component();
        mComponent.setName(attributes.getValue("name"));
        mProjeto.addComponent(mComponent);
    }

    private static void parseStateTag(Attributes attributes) {        
        int id = Integer.parseInt(attributes.getValue("id"));
        mState = mComponent.newState(id);
        mState.setLayoutX(Double.parseDouble(attributes.getValue("x")));
        mState.setLayoutY(Double.parseDouble(attributes.getValue("y")));        
        if (Boolean.parseBoolean(attributes.getValue("initial"))) {
            mState.setAsInitial();
        }
        if (Boolean.parseBoolean(attributes.getValue("final"))) {
            mState.setFinal(true);
        }
        if (Boolean.parseBoolean(attributes.getValue("error"))) {
            mState.setError(true);
        }        
    }

    private static void parseTransitionTag(Attributes attributes) {                
        mTransition = mComponent.newTransition(Integer.parseInt(attributes.getValue("from")), Integer.parseInt(attributes.getValue("to")));
        mTransition.setLabel(attributes.getValue("label"));
        String s = attributes.getValue("prob");
        mTransition.setProbability(s == null ? null : Double.parseDouble(s));
        mTransition.setGuard(attributes.getValue("guard"));
    }
    
}
