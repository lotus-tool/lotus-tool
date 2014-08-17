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

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

/**
 *
 * @author emerson
 */
public class ExtensibleFXToolbar implements ExtensibleToolbar, ToolbarItemBuilderFX.Container {

    private final ToolBar mToolbar;

    public ExtensibleFXToolbar(ToolBar toolbar) {
        mToolbar = toolbar;
    }

    @Override
    public void addItem(int weight, String name, Runnable action) {
        newItem(name).setWeight(weight).setAction(action).showSeparator(name.equals("-")).create();
    }

    @Override
    public ItemBuilder newItem(String name) {
        return new ToolbarItemBuilderFX(name, this);
    }

    @Override
    public void inject(Node n) {
        mToolbar.getItems().add(n);
        mToolbar.getItems().sort((Node o1, Node o2) -> {
            Integer x = ((ExtensibleFXItemHolder) o1.getUserData()).weight;
            Integer y = ((ExtensibleFXItemHolder) o2.getUserData()).weight;
            return x.compareTo(y);
        });
    }

}
