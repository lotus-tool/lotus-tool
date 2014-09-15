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

import java.util.Comparator;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author emerson
 */
public class ExtensibleFXContextMenu implements ExtensibleMenu, MenuItemBuilderFX.Container {

    private final ContextMenu mContextMenu;

    public ExtensibleFXContextMenu(ContextMenu contextMenu) {
        mContextMenu = contextMenu;
    }

    @Override
    public void addItem(int weight, String path, Runnable action) {
        newItem(path)
                .setWeight(weight)
                .setAction(action)
                .showSeparator(path.equals("-"))
                .create();
    }

    @Override
    public ItemBuilder newItem(String path) {
        MenuItemBuilderFX itm = new MenuItemBuilderFX(this);
        itm.setPath(path);
        return itm;
    }

    @Override
    public void inject(String path, MenuItem item) {
        String[] arrPaths = path.split("/");
        List<MenuItem> atual = mContextMenu.getItems();
        for (int i = 0, n = arrPaths.length - 1; i < n; i++) {
            Menu proximo = findMenuItem(atual, arrPaths[i]);
            if (proximo == null) {
                proximo = new Menu(arrPaths[i]);
                ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
                h.name = arrPaths[i];
                h.weight = ((ExtensibleFXItemHolder) item.getUserData()).weight;
                proximo.setUserData(h);
                atual.add(proximo);
            }
            atual = proximo.getItems();
        }                
        atual.add(item);
        atual.sort(ExtensibleFXItemHolder.MENUITEM_COMPARATOR);
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

}
