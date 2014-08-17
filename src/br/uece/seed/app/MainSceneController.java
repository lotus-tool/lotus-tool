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
package br.uece.seed.app;

import br.uece.seed.ext.Plugin;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;

public class MainSceneController extends Plugin implements Initializable, UserInterface {

    @FXML
    protected MenuBar mMnuPrincipal;
    @FXML
    protected TabPane mTabLeft;
    @FXML
    protected TabPane mTabCenter;
    @FXML
    protected TabPane mTabRight;
    @FXML
    protected ToolBar mToolbar;
    
    private ExtensibleMenu mExtensibleMainMenu;
    private ExtensibleTabPane mExtensibleLeftPanel;
    private ExtensibleTabPane mExtensibleCenterPanel;
    private ExtensibleTabPane mExtensibleRightPanel;
    private ExtensibleToolbar mExtensibleToolbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mExtensibleMainMenu = new ExtensibleFXMenuBar(mMnuPrincipal);
        mExtensibleLeftPanel = new ExtensibleFXTabPane(mTabLeft);
        mExtensibleCenterPanel = new ExtensibleFXTabPane(mTabCenter);
        mExtensibleRightPanel = new ExtensibleFXTabPane(mTabRight);
        mExtensibleToolbar = new ExtensibleFXToolbar(mToolbar);
    }

    @Override
    public void setTitle(String title) {
        Startup.getStage().setTitle(title);
    }

    @Override
    public ExtensibleMenu getMainMenu() {
        return mExtensibleMainMenu;
    }

    @Override
    public ExtensibleTabPane getLeftPanel() {
        return mExtensibleLeftPanel;
    }

    @Override
    public ExtensibleTabPane getCenterPanel() {
        return mExtensibleCenterPanel;
    }

    @Override
    public ExtensibleTabPane getRightPanel() {
        return mExtensibleRightPanel;
    }

    @Override
    public ExtensibleToolbar getToolBar() {
        return mExtensibleToolbar;
    }

}
