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
package br.uece.lotus.helpers.window;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import br.uece.seed.app.ExtensibleTabPane;
import br.uece.seed.app.UserInterface;
import br.uece.seed.ext.ExtensionManager;
import br.uece.seed.ext.Plugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author emerson
 * @param <T>
 */
public abstract class DefaultWindowManagerPlugin<T extends Window> extends Plugin implements WindowManager {

//    private  Map<Component, T> mComponentWindowsMap = new HashMap<Component, T>();
    private ArrayList<T> mComponentWindows = new ArrayList<T>();
    private  Map<Component, Integer> mComponentWindowsIds = new HashMap<Component, Integer>();
    private  List<Listener> mListeners = new ArrayList<>();
    private ExtensibleTabPane mCenterPanel;

    private final Component.Listener mComponentListener = new Component.Listener() {
        @Override
        public void onChange(Component c) {
            System.out.println("entrou aqui");
            Integer id = mComponentWindowsIds.get(c);
            if (id != null) {
                System.out.println("onchange");
                mCenterPanel.renameTab(id, c.getName());
            }
        }

        @Override
        public void onStateCreated(Component component, State state) {

        }

        @Override
        public void onStateRemoved(Component component, State state) {

        }

        @Override
        public void onTransitionCreated(Component component, Transition t) {

        }

        @Override
        public void onTransitionRemoved(Component component, Transition state) {

        }
    };    
    private boolean mOnStartCalled;

    protected abstract T onCreate();
    protected abstract void onShow(T window, Component c);
    protected abstract void onHide(T window);

    @Override
    public void onStart(ExtensionManager extensionManager) throws Exception {
        mCenterPanel = ((UserInterface)extensionManager.get(UserInterface.class)).getCenterPanel();        
        mOnStartCalled = true;
    }
   
    @Override
    public void show(Component component) {
        checkIfStartedProperly();
//        T window = mComponentWindowsMap.get(component);
        T window= null;
        for(T w : mComponentWindows){
            if(w.getComponent()==component){
                window= w;
                continue;
            }
        }
        System.out.println("COMPONENT:"+component.getName());

        if (window==null){
            System.out.println("window DO "+ component.getName()+" é NULA ");
        }



        /*System.out.println("quantidade de elementos1: "+mComponentWindowsMap.size());*/

        if(window==null) {
            window = onCreate();
//            window.setComponent(component);
//            mComponentWindowsMap.put(component,  window);
            window.setComponent(component);
            mComponentWindows.add(window);
            System.out.println("window "+window +" é CRIADA ");

            //////////////////////////////
//            window.setComponent(component);
            //////////////////////////////////
            for (Listener l : mListeners) {
                l.onCreateWindow(window);
            }            
            component.addListener(mComponentListener);
//            mComponentWindowsMap.put(component,  (T)window);
            System.out.println("window "+ window+" foi ADD no Hash");
            if(/*mComponentWindowsMap.containsKey(component)*/mComponentWindows.contains(window)){
                System.out.println(" hashmap CONTEM a window que eu acabei de adicionar");
            }

        }
            /*System.out.println("quantidade de elementos2: "+mComponentWindowsMap.size());*/

        onShow(window, component);
        Integer id = mComponentWindowsIds.get(component);        
        boolean visivel = id != null && mCenterPanel.isShowing(id);        
        if (!visivel || id == null) {
            System.out.println("id ==null");
            id = mCenterPanel.newTab(window.getTitle(), window.getNode(), true);
            System.out.println("id :" +id);
            mComponentWindowsIds.put(component, id);
        }        
        mCenterPanel.showTab(id);
        System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><>");
    }

    private void checkIfStartedProperly() throws IllegalStateException {
        if (!mOnStartCalled) {
            throw new IllegalStateException("super.onStart() not called in your plugin!");
        }
    }

    @Override
    public void hide(Component component) {
//        System.out.println("hide component");
//        checkIfStartedProperly();
//        Integer id = mComponentWindowsIds.get(component);
//        if (id != null) {
//            T w = mComponentWindowsMap.get(component);
//            onHide(w);
//            mCenterPanel.closeTab(id);
//            component.removeListener(mComponentListener);
//            mComponentWindowsIds.remove(component);
//            mComponentWindowsMap.remove(component);
   //     }
    }

    @Override
    public void hideAll() {
//        checkIfStartedProperly();
//        List<Component> snapshot = new ArrayList<>(mComponentWindowsMap.keySet());
//        for (Component c : snapshot) {
//            hide(c);
//        }
    }

    @Override
    public void addListener(Listener l) {
        mListeners.add(l);
    }

}
