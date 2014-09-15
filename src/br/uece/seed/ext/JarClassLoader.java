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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JarClassLoader extends ClassLoader {

    private static final Logger logger = Logger.getLogger(JarModule.class.getName());    
    private final HashMap<String, Class> classes = new HashMap<>();
    private final JarFile mJar;
    private final File mJarFile;

    public JarClassLoader(File path) throws IOException {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor        
        mJarFile = path;
        mJar = new JarFile(path);
    }

    @Override
    public Class loadClass(String className) throws ClassNotFoundException {
        logger.log(Level.FINEST, "loading class {0}...", className);
        Class c = findClass(className);        
        return c;
    }

    @Override
    public Class findClass(String className) {
        byte classByte[];
        Class result;

        result = (Class) classes.get(className);
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (ClassNotFoundException e) {
            //ignora
        }

        try {
            String path = className.replaceAll("\\.", "/") + ".class";            
            JarEntry entry = mJar.getJarEntry(path);
            if (entry == null) {
                return null;
            }
            InputStream is = mJar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public URL getResource(String name) {        
        String aux = "jar:file:" + mJarFile.getAbsolutePath() + "!/" + name;
        logger.log(Level.FINEST, "getting resource {0}...", aux);
        URL foo = null;
        try {
            foo = new URL(aux);
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Invalid Resource URL !", e);
        }
        return foo;
    }    
    
}
