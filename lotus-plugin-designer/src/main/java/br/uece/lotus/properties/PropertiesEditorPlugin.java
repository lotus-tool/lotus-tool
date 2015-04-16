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

package br.uece.lotus.properties;

import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

/**
 *
 * @author emerson
 */
public class PropertiesEditorPlugin extends Plugin implements PropertiesEditor {
    
    private UserInterface mUserInterface;
    private PropertiesEditorController mPropertiesEditorController;

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        /*
        mUserInterface = extensionManager.get(UserInterface.class);
        
        URL location = getClass().getResource("/fxml/PropertiesEditor.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = (Parent) fxmlLoader.load(location.openStream());
        mPropertiesEditorController = fxmlLoader.getController();
        mUserInterface.getRightPanel().newTab("Properties", root, false);
        */
    }

    
    
    @Override
    public void edit(Object obj) {                
        mPropertiesEditorController.changeObject(obj);
    }
    
}
