package br.uece.lotus.app;

import javafx.scene.control.Menu;
import javafx.scene.control.ToolBar;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.meta.Author;

@PluginImplementation
@Author(name = "Emerson Lima")
public class UserInterfaceImpl implements UserInterface {

    @Override
    public Menu getMenuPrincipal() {
        return MainSceneController.menu;
    }

    @Override
    public ToolBar getBarraFerramentas() {
        return MainSceneController.barraFerramentas;
    }
    
}
