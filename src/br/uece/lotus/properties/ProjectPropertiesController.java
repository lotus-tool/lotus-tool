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

package br.uece.lotus.properties;

import br.uece.lotus.Component;
import br.uece.lotus.Project;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProjectPropertiesController implements Project.Listener, ChangeListener<String> {

    private TextField mEdtName;

    private Project mProject;
    private boolean mEmEdicao;
    private VBox mGrandParent;
    private Parent mPropertyWrapper;

    public void init(TextField edtName) {
        mGrandParent = (VBox) edtName.getParent().getParent();
        mPropertyWrapper = edtName.getParent();
        mEdtName = edtName;
        mEdtName.textProperty().addListener(this);
    }

    public void setVisible(boolean v) {
        final ObservableList<Node> children = mGrandParent.getChildren();
        if (v) {
            if (!children.contains(mPropertyWrapper)) {
                children.add(mPropertyWrapper);
            }
        } else {
            children.remove(mPropertyWrapper);
        }
    }

    public void changeProject(Project p) {
        if (mProject != null) {
            mProject.removeListener(this);
        }
        mProject = p;
        if (mProject != null) {
            mProject.addListener(this);
        }
    }

    @Override
    public void onChange(Project project) {
        mEmEdicao = true;
        mEdtName.setText(project.getName());
        mEmEdicao = false;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (mProject == null || mEmEdicao) {
            return;
        }
        if (observable == mEdtName.textProperty()) {
            mProject.setName(newValue);
        }
    }

    @Override
    public void onComponentCreated(Project project, Component component) {
        //nada        
    }

    @Override
    public void onComponentRemoved(Project project, Component component) {
        //nada        
    }

}
