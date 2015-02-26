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
package br.uece.seed.app;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 *
 * @author emerson
 */
public class ExtensibleFXMenuBar implements ExtensibleMenu, MenuItemBuilderFX.Container {

    private final MenuBar mMenuBar;
    private Runnable mDefaultAction;

    public ExtensibleFXMenuBar(MenuBar menuBar) {
        mMenuBar = menuBar;
    }

    @Override
    public void addItem(int weight, String path, Runnable action) {
        newItem(path)
                .setWeight(weight)
                .setAction(action)
                .create();
    }

    @Override
    public void triggerDefaultAction() {
        if (mDefaultAction != null) {
            mDefaultAction.run();
        }
    }

    @Override
    public ItemBuilder newItem(String path) {
        MenuItemBuilderFX itm = new MenuItemBuilderFX(this);
        itm.setPath(path);
        return itm;
    }

    @Override
    public void inject(String path, MenuItem item, Runnable defaultAction) {
        Menu m;
        String[] arrPaths = path.split("/");
        String menuRaiz = arrPaths[0];
        m = findMenu(mMenuBar.getMenus(), menuRaiz);
        if (m == null) {
            m = new Menu(menuRaiz);
            ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
            h.name = menuRaiz;
            h.weight = ((ExtensibleFXItemHolder) item.getUserData()).weight;
            m.setUserData(h);
            mMenuBar.getMenus().add(m);
            mMenuBar.getMenus().sort((Menu o1, Menu o2) -> {
                ExtensibleFXItemHolder h1 = (ExtensibleFXItemHolder) o1.getUserData();
                ExtensibleFXItemHolder h2 = (ExtensibleFXItemHolder) o2.getUserData();
                return ((Integer) h1.weight).compareTo(h2.weight);
            });
        }
        List<MenuItem> atual = m.getItems();
        for (int i = 1; i < arrPaths.length; i++) {
            Menu proximo = findMenuItem(atual, arrPaths[i]);
            if (proximo == null) {
                proximo = new Menu(arrPaths[i]);
                ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
                h.name = arrPaths[i];
                h.weight = ((ExtensibleFXItemHolder) item.getUserData()).weight;
                proximo.setUserData(h);
                atual.add(proximo);                
                atual.sort(ExtensibleFXItemHolder.MENUITEM_COMPARATOR);                
            }
            atual = proximo.getItems();
        }
        atual.add(item);        
        atual.sort(ExtensibleFXItemHolder.MENUITEM_COMPARATOR);
        if (defaultAction != null) {
            mDefaultAction = defaultAction;
        }
    }

    private Menu findMenuItem(List<MenuItem> lst, String name) {
        for (MenuItem itm : lst) {
            ExtensibleFXItemHolder h = (ExtensibleFXItemHolder) itm.getUserData();
            //System.out.println("findMenuItem: " + h.name);
            if (h.name.equals(name)) {
                return (Menu) itm;
            }
        }
        return null;
    }

    private Menu findMenu(List<Menu> lst, String name) {
        for (Menu itm : lst) {
            ExtensibleFXItemHolder h = (ExtensibleFXItemHolder) itm.getUserData();
            //System.out.println("findMenu: " + h.name);
            if (h.name.equals(name)) {
                return itm;
            }
        }
        return null;
    }
}
