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

import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination.Modifier;
import javax.swing.SwingUtilities;

/**
 *
 * @author emerson
 */
public class MenuItemBuilderFX implements ExtensibleMenu.ItemBuilder {

    private final Container mContainer;
    private boolean mHideText;
    private KeyCodeCombination mAccelerator;

    interface Container {
        void inject(String path, MenuItem b);
    }

    private boolean mAsSeparator;
    private InputStream mGraphic;
    private int mWeight;
    private Runnable mAction;
    private String mText;
    private String mPath = "";

    public MenuItemBuilderFX(Container container) {
        mContainer = container;
    }

    public void setPath(String path) {;
        mText = path;
        int i = path.lastIndexOf('/');
        if (i >= 0) {
            mPath = path.substring(0, i);
            mText = path.substring(i + 1);
        }
    }

    @Override
    public ExtensibleMenu.ItemBuilder showSeparator(boolean v) {
        mAsSeparator = v;
        return this;
    }

    @Override
    public ExtensibleMenu.ItemBuilder setGraphic(InputStream graphic) {
        mGraphic = graphic;
        return this;
    }

    @Override
    public ExtensibleMenu.ItemBuilder setWeight(int weight) {
        mWeight = weight;
        return this;
    }

    @Override
    public ExtensibleMenu.ItemBuilder setAction(Runnable action) {
        mAction = action;
        return this;
    }

    @Override
    public ExtensibleMenu.ItemBuilder setAccelerator(KeyCode key, Modifier... modifiers) {
        mAccelerator = new KeyCodeCombination(key, modifiers);
        return this;
    }
    
    @Override
    public ExtensibleMenu.ItemBuilder hideText(boolean v) {
        mHideText = v;
        return this;
    }

    String getPath() {
        return mPath;
    }
    
    int getWeight() {
        return mWeight;
    }

    @Override
    public void create() {
        MenuItem item;
        if (mAsSeparator) {
            item = new SeparatorMenuItem();
        } else {
            item = new MenuItem();
            if (!mHideText) {
                item.setText(mText);                
            }
            if (mGraphic != null) {
                item.setGraphic(new ImageView(new Image(mGraphic)));
            }
            if (mAccelerator != null) {
                item.setMnemonicParsing(true);
                item.setAccelerator(mAccelerator);
            }
            if (mAction != null) {
                item.setOnAction((ActionEvent e) -> {
                    mAction.run();
                });
            }
        }
        ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
        h.name = mText;
        h.weight = mWeight;
        item.setUserData(h);
        mContainer.inject(mPath, item);
    }
}
