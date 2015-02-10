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
import br.uece.lotus.viewer.StateView;
import br.uece.lotus.viewer.TransitionView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class PropertiesEditorController implements Initializable {

    @FXML
    private TextField mEdtPositionX;
    @FXML
    private TextField mEdtPositionY;
    @FXML
    private TextField mEdtLabel;
    @FXML
    private TextField mEdtGuard;
    @FXML
    private TextField mEdtProbability;
    @FXML
    private TextField mEdtName;
    
    private final StatePropertiesController mStatePropertiesController = new StatePropertiesController();
    private final TransitionsPropertiesController mTransitionsPropertiesController = new TransitionsPropertiesController();
//    private final ProjectPropertiesController mProjectPropertiesController = new ProjectPropertiesController();
//    private final ComponentPropertiesController mComponentPropertiesController = new ComponentPropertiesController();

    public void changeObject(Object o) { 
//        mProjectPropertiesController.setVisible(false);
//        mProjectPropertiesController.changeProject(null);
//        mComponentPropertiesController.setVisible(false);
//        mComponentPropertiesController.changeComponent(null);
        mStatePropertiesController.setVisible(false);
        mStatePropertiesController.changeState(null);
        mTransitionsPropertiesController.setVisible(false);
        mTransitionsPropertiesController.changeTransition(null);

//        if (o instanceof Project) {
//            mProjectPropertiesController.setVisible(true);
//            mProjectPropertiesController.changeProject((Project) o);
//        } else if (o instanceof Component) {
//            mComponentPropertiesController.setVisible(true);
//            mComponentPropertiesController.changeComponent((Component) o);
//        } else 
            if (o instanceof StateView) {
            mStatePropertiesController.setVisible(true);
            mStatePropertiesController.changeState(((StateView) o).getState());
        } else if (o instanceof TransitionView) {
            mTransitionsPropertiesController.setVisible(true);
            mTransitionsPropertiesController.changeTransition(((TransitionView) o).getTransition());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {        
//        mProjectPropertiesController.init(mEdtName);
//        mComponentPropertiesController.init(mEdtName);
        mStatePropertiesController.init(mEdtPositionX, mEdtPositionY);
        mTransitionsPropertiesController.init(mEdtLabel, mEdtGuard, mEdtProbability);
        changeObject(null);
    }

}
