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

package br.uece.seed.ext;

import br.uece.seed.app.Startup;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtensionManager {

    private static final Logger logger = Logger.getLogger(Startup.class.getName());    
    private final List<Plugin> mPlugins = new ArrayList<>();    

    public void registerModule(Module m) {        
        for (Plugin p : m.getPlugins()) {
            registerPlugin(p);
        }
    }

    public void start() {
        //System.out.println(mPlugins);
        for (Plugin p : mPlugins) {
            try {
                p.onStart(this);
            } catch (Exception e) {                                
                logger.log(Level.WARNING, "Fail at module starting!", e);
            }
        }
    }

    public void stop() throws Exception {
        for (Plugin p : mPlugins) {
            try {
                p.onStop(this);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Fail at module stoping!", e);
            }
        }
    }

    public <T> T get(Class<?> aClass) {
        for (Plugin p : mPlugins) {
            //System.out.println(p);
            //System.out.println(aClass);
            //System.out.println(aClass.getName() + " = " + p.getClass() + "?");
            if (aClass.isAssignableFrom(p.getClass())) {
                return (T) p;
            }
        }
        return null;
    }

    public void registerPlugin(Plugin plugin) {
        mPlugins.add(plugin);
    }

}
