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
package br.uece.lotus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
    
    private final Map<String, Object> mValues = new HashMap<String, Object>();

    public interface Listener {

        void onChange(Project project);

        void onComponentCreated(Project project, Component component);

        void onComponentRemoved(Project project, Component component);
    }

    private String mName;
    private final List<Component> mComponents = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();

    public void addComponent(Component c) {
        mComponents.add(c);
        //System.out.println(mListeners);
        for (Listener l : mListeners) {
            l.onComponentCreated(this, c);
        }
    }

    public void removeComponent(Component c) {
        mComponents.remove(c);
        for (Listener l : mListeners) {
            l.onComponentRemoved(this, c);
        }
    }

    public void removeComponentByIndex(int index) {
        Component c = mComponents.remove(index);
        for (Listener l : mListeners) {
            l.onComponentRemoved(this, c);
        }
    }

    public Component getComponent(int index) {
        return mComponents.get(index);
    }

    public Iterable<Component> getComponents() {
        return mComponents;
    }

    public int getComponentsCount() {
        return mComponents.size();
    }

    public int indexOfComponent(Component component) {
        int i = 0;
        for (Component c : mComponents) {
            if (c == component) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

}
