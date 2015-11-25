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

import br.uece.lotus.BigState;
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
        xml.attr("name", p.getName());
        for (Component c : p.getComponents()) {
            xml.begin("component");
            xml.attr("name", c.getName());

            xml.begin("states");

            for (State v : c.getStates()) {
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
            
            //GRAVANDO TODOS OS STATES DOS BIGSTATES
            for (BigState bigState : BigState.todosOsBigStates) {
                if (bigState.getState().getComponent().equals(c)) {
                    for (State state : bigState.getListaStates()) {
                        xml.begin("state");
                        xml.attr("id", state.getID() + ":" + bigState.getState().getID());
                        xml.attr("x", state.getLayoutX());
                        xml.attr("y", state.getLayoutY());
                        xml.attr("label", state.getLabel());
                        if (state.isInitial()) {
                            xml.attr("initial", "true");
                        }
                        if (state.isError()) {
                            xml.attr("error", "true");
                        }
                        if (state.isFinal()) {
                            xml.attr("final", "true");
                        }
                        if (BigState.verifyIsBigState(state)) {
                            xml.attr("bigstate", "true");
                        }
                        xml.end();
                    }
                }
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

                Integer i = (Integer) t.getValue("view.type");
                if (i != null) {
                    xml.attr("view-type", String.valueOf(i));
                }
                xml.end();
            }
            
            int lastBigState = 0;
            for (State state : c.getStates()) {
                BigState bigState = (BigState) state.getValue("bigstate");
                if (bigState != null) {
                    lastBigState++;
                    int lastTransition = 1;
                    for (Transition t : bigState.getAllTransitions()) {
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
                        xml.attr("vID", bigState.getState().getID()+":"+t.getPropertyBigState());
                        //EH A ULTIMA TRANSICAO DO COMPONENT ADD                        
                        if (lastBigState == c.getBigStatesCount() && lastTransition == bigState.getAllTransitions().size()) {
                            xml.attr("end", "true");
                        }
                        xml.end();
                        lastTransition++;
                    }
                }                
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
                    parseProjectTag(attributes);
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

    private static void parseProjectTag(Attributes attributes) {
        mProjeto = new Project();
        mProjeto.setName(attributes.getValue("name"));
    }

    private static void parseComponentTag(Attributes attributes) {
        mComponent = new Component();
        mComponent.setAutoUpdateLabels(false);
        mComponent.setName(attributes.getValue("name"));
        mProjeto.addComponent(mComponent);
    }

    private static void parseStateTag(Attributes attributes) {
        int id = Integer.parseInt(attributes.getValue("id").split(":")[0]);
        mState = mComponent.newState(id);
        mState.setLayoutX(Double.parseDouble(attributes.getValue("x")));
        mState.setLayoutY(Double.parseDouble(attributes.getValue("y")));
        if (Boolean.parseBoolean(attributes.getValue("initial"))) {
            mState.setAsInitial();
            mComponent.setInitialState(mState);
        }
        if (Boolean.parseBoolean(attributes.getValue("final"))) {
            mState.setFinal(true);
            mComponent.setFinalState(mState);
        }
        if (Boolean.parseBoolean(attributes.getValue("error"))) {
            mState.setError(true);
            mComponent.setErrorState(mState);
        }
        mState.setLabel(attributes.getValue("label"));
        //VERIFICANDO SE EH UM BIGSTATE        
        if (Boolean.parseBoolean(attributes.getValue("bigstate"))) {
            BigState bigState = new BigState();
            bigState.setState(mState);
            mState.setValue("bigstate", bigState);
            mState.setBig(true);
            BigState.todosOsBigStates.add(bigState);
        } else {
            if (attributes.getValue("id").split(":").length != 1) {
                int virtualID = Integer.parseInt(attributes.getValue("id").split(":")[1]);
                BigState bigStateSelec = BigState.getBigStateById(virtualID, mComponent);
                if (bigStateSelec != null) {
                    bigStateSelec.addState(mState);                    
                }
            }
        }
    }

    private static void parseTransitionTag(Attributes attributes) {
        String s = attributes.getValue("prob");
        String viewType = attributes.getValue("view-type");
        mTransition = mComponent.buildTransition(Integer.parseInt(attributes.getValue("from")), Integer.parseInt(attributes.getValue("to")))
                .setLabel(attributes.getValue("label"))
                .setProbability(s == null ? null : Double.parseDouble(s))
                .setGuard(attributes.getValue("guard"))
                .setValue("view.type", viewType == null ? null : Integer.parseInt(viewType))
                .create();
        if (attributes.getValue("vID") != null) {
            int virtualID = Integer.parseInt(attributes.getValue("vID").split(":")[0]);
            int property = Integer.parseInt(attributes.getValue("vID").split(":")[1]);
            BigState bigStateSelec = BigState.getBigStateById(virtualID, mComponent);
            if (bigStateSelec != null) {
                bigStateSelec.addTransition(mTransition, property);
            }
        }
        if (Boolean.parseBoolean(attributes.getValue("end"))) {
            BigState.removeStatesComponent();
        }
    }

}
