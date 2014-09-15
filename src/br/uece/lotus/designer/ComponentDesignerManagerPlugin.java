/*
 * The MIT License
 *
 * Copyright 2014 emerson.
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
package br.uece.lotus.designer;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.properties.PropertiesEditor;
import br.uece.lotus.viewer.BasicComponentViewer;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emerson
 */
public class ComponentDesignerManagerPlugin extends Plugin implements ComponentDesignerManager {

    private UserInterface mUserInterface;
    private ProjectExplorer mProjectExplorer;
    private PropertiesEditor mPropertiesEditor;
    private final ComponentDesignerImpl.Listener mBindDesignerWithPropertiesEditor;
    private final List<Listener> mListeners = new ArrayList<>();
    private List<Integer> mIds = new ArrayList<Integer>();
    private Component.Listener mComponentListener = new Component.Listener() {

        @Override
        public void onChange(Component c) {
            Integer id = (Integer) c.getValue("tab.id");
            if (id != null) {
                mUserInterface.getCenterPanel().renameTab(id, c.getName());
            }
        }

        @Override
        public void onStateCreated(Component component, State state) {

        }

        @Override
        public void onStateRemoved(Component component, State state) {

        }

        @Override
        public void onTransitionCreated(Component component, Transition t) {

        }

        @Override
        public void onTransitionRemoved(Component component, Transition state) {

        }
    };

    public ComponentDesignerManagerPlugin() {
        mBindDesignerWithPropertiesEditor = (ComponentDesignerImpl v) -> {
            mPropertiesEditor.edit(v.getSelectedView());
        };
    }

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mUserInterface = extensionManager.get(UserInterface.class
        );
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mPropertiesEditor = extensionManager.get(PropertiesEditor.class);

        mProjectExplorer.getComponentMenu()
                .addItem(Integer.MIN_VALUE, "Edit", () -> {
                    show(mProjectExplorer.getSelectedComponent());
                });

        mProjectExplorer.getComponentMenu()
                .addItem(Integer.MIN_VALUE, "-", null);
    }

    @Override
    public void show(Component c) {
        if (c == null) {
            return;
        }
        Integer tabId = (Integer) c.getValue("tab.id");
        if (tabId == null) {
            c.addListener(mComponentListener);
            ComponentDesignerImpl d = new ComponentDesignerImpl(new BasicComponentViewer());
            d.setComponent(c);
            if (mPropertiesEditor != null) {
                ((ComponentDesignerImpl) d).addListener(mBindDesignerWithPropertiesEditor);
            }
            tabId = mUserInterface.getCenterPanel().newTab(c.getName(), d, true);
            c.setValue("tab.id", tabId);
            c.setValue("gui", d);
            mIds.add(tabId);
            for (Listener l: mListeners) {
                l.onCreateComponentDesigner(d);
            }
        }
        mUserInterface.getCenterPanel().showTab(tabId);
    }

    @Override
    public void hide(Component c) {
        Integer id = (Integer) c.getValue("tab.id");
        if (id != null) {
            mUserInterface.getCenterPanel().closeTab(id);
        }
    }

    @Override
    public void hideAll() {
        for (int id : mIds) {
            mUserInterface.getCenterPanel().closeTab(id);
        }
    }
    
    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }
    
    @Override
    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    

}
