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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarModule implements Module {

    private static final Logger logger = Logger.getLogger(JarModule.class.getName());
    private final File mJar;
    private final ClassLoader mClassLoader;
    private List<Plugin> mPlugins;

    public JarModule(File jar) throws Exception {
        mJar = jar;
        mClassLoader = new JarClassLoader(mJar);
    }

    @Override
    public List<Plugin> getPlugins() {
        if (mPlugins == null) {
            mPlugins = loadPlugins();
        }
        return mPlugins;
    }

    private List<String> discoverAllJarClasses() throws IOException {
        List<String> aux = new ArrayList<>();
        try (ZipFile zip = new ZipFile(mJar)) {
            Enumeration<? extends ZipEntry> e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String s = entry.getName();
                if (s.endsWith(".class")) {
                    s = s.substring(0, s.length() - 6).replaceAll("/", ".");
                    aux.add(s);
                }
            }
        }
        return aux;
    }

    private List<Plugin> loadPlugins() {
        List<Plugin> aux = new ArrayList<>();
        try {
            List<String> canditatesPluginClass;
            canditatesPluginClass = discoverAllJarClasses();            
            for (String className : canditatesPluginClass) {
                try {
                    Class<?> pluginClass = mClassLoader.loadClass(className);
                    if (!Plugin.class.isAssignableFrom(pluginClass)) {
                        //logger.log(Level.INFO, "Class {0} does not extends Plugin!", className);
                    } else {
                        if(!className.equals("br.uece.lotus.msc.api.window.DefaultWindowManagerPluginDS")){// tratando erro de instancia de classe abstrata
                            logger.log(Level.INFO, "Plugin founded at {0}!", className);
                            Object pluginInstance = pluginClass.newInstance();
                            aux.add((Plugin) pluginInstance);
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger.log(Level.WARNING, null, e);
                    System.out.println("Erro na class: "+className);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return aux;
    }

}
