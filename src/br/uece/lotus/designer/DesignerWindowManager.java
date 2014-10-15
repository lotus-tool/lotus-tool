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
import br.uece.lotus.helpers.window.DefaultWindowManagerPlugin;
import br.uece.lotus.project.ProjectExplorer;
import br.uece.lotus.properties.PropertiesEditor;
import br.uece.lotus.viewer.BasicComponentViewer;
import br.uece.seed.ext.ExtensionManager;

/**
 *
 * @author emerson
 */
public class DesignerWindowManager extends DefaultWindowManagerPlugin<DesignerWindow> {

    private ProjectExplorer mProjectExplorer;
    private PropertiesEditor mPropertiesEditor;
    private DesignerWindowImpl.Listener mBindDesignerWithPropertiesEditor;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        super.onStart(extensionManager);
        mProjectExplorer = extensionManager.get(ProjectExplorer.class);
        mPropertiesEditor = extensionManager.get(PropertiesEditor.class);

        mBindDesignerWithPropertiesEditor = (DesignerWindowImpl w) -> {
            mPropertiesEditor.edit(((DesignerWindow) w).getSelectedView());
        };

        mProjectExplorer.getComponentMenu()
                .newItem("Edit")
                .setWeight(Integer.MIN_VALUE)
                .setAction(() -> {
                    Component c = mProjectExplorer.getSelectedComponent();
                    show(c);
                })
                .create();

        mProjectExplorer.getComponentMenu()
                .newItem("")
                .showSeparator(true)
                .setWeight(Integer.MIN_VALUE)
                .create();
    }

    @Override
    protected DesignerWindow onCreate() {
        DesignerWindowImpl w = new DesignerWindowImpl(new BasicComponentViewer());        
        if (mPropertiesEditor != null) {
            w.addListener(mBindDesignerWithPropertiesEditor);
        }
        return w;
    }

    @Override
    protected void onShow(DesignerWindow window, Component c) {
        window.setComponent(c);
    }

    @Override
    protected void onHide(DesignerWindow window) {

    }

}
