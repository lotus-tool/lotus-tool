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
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

/**
 *
 * @author emerson
 */
public class ExtensibleFXItemHolder {

    public static Comparator<? super Node> NODE_COMPARATOR = (Node o1, Node o2) -> {
        Integer x = ((ExtensibleFXItemHolder) o1.getUserData()).weight;
        Integer y = ((ExtensibleFXItemHolder) o2.getUserData()).weight;
        return x.compareTo(y);
    };
    public static Comparator<? super MenuItem> MENUITEM_COMPARATOR = (MenuItem o1, MenuItem o2) -> {
        Integer x = ((ExtensibleFXItemHolder) o1.getUserData()).weight;
        Integer y = ((ExtensibleFXItemHolder) o2.getUserData()).weight;
        return x.compareTo(y);
    };

    public String name;
    public int weight;
}
