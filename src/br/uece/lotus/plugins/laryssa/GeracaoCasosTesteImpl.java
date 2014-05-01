package br.uece.lotus.plugins.laryssa;

import br.uece.lotus.app.UserInterface;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.annotations.meta.Author;

@PluginImplementation
@Author(name = "Laryssa Muniz")
public class GeracaoCasosTesteImpl implements GeracaoCasosTeste {
    
    @InjectPlugin
    public UserInterface app;
    
    @Init    
    public void init() {
        System.out.println("implemente!");
    }
    
}
