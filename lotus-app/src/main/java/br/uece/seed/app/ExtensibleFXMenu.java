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

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ExtensibleFXMenu implements ExtensibleMenu, MenuItemBuilderFX.Container {

    private final Menu mMenuRoot;
    private Runnable mDefaultAction;

    public ExtensibleFXMenu(Menu menu) {
        mMenuRoot = menu;
    }

    @Override
    public ItemBuilder newItem(String path) {
        return new MenuItemBuilderFX(this);
    }

    @Override
    public void triggerDefaultAction() {
        if (mDefaultAction != null) {
            mDefaultAction.run();
        }
    }

    @Override
    public void inject(String path, MenuItem item, Runnable defaultAction) {
        String[] arrPaths = path.split("/");
        List<MenuItem> atual = mMenuRoot.getItems();
        for (String strPath : arrPaths) {
            Menu proximo = findMenuItem(atual, strPath);
            if (proximo == null) {
                proximo = new Menu(strPath);
                ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
                h.name = strPath;
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
            if (h.name.equals(name)) {
                return (Menu) itm;
            }
        }
        return null;
    }

    @Override
    public void addItem(int weight, String path, Runnable action) {
        newItem(path).setWeight(weight).setAction(action).create();
    }
}
