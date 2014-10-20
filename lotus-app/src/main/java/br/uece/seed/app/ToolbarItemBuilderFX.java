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
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author emerson
 */
public class ToolbarItemBuilderFX implements ExtensibleToolbar.ItemBuilder {

    private final Container mContainer;
    private boolean mHideText;

    interface Container {

        void inject(Node b);
    }

    private boolean mAsSeparator;
    private InputStream mGraphic;
    private int mWeight;
    private Runnable mAction;
    private String mText;
    private String mPath = "";
    private String tooltip;

    public ToolbarItemBuilderFX(String name, Container container) {
        mContainer = container;
        mText = name;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder showSeparator(boolean v) {
        mAsSeparator = v;
        return this;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder setGraphic(InputStream graphic) {
        mGraphic = graphic;
        return this;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder setWeight(int weight) {
        mWeight = weight;
        return this;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder setAction(Runnable action) {
        mAction = action;
        return this;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder hideText(boolean v) {
        mHideText = v;
        return this;
    }

    @Override
    public ExtensibleToolbar.ItemBuilder setTooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    int getWeight() {
        return mWeight;
    }

    @Override
    public void create() {
        Node item; 
        if (mAsSeparator) {
            item = new Separator(Orientation.VERTICAL);
        } else {
            Button aux = new Button();
            if (!mHideText) {
                aux.setText(mText);
            }
            if (mGraphic != null) {
                aux.setGraphic(new ImageView(new Image(mGraphic)));
            }
            if (mAction != null) {
                aux.setOnAction((ActionEvent e) -> {
                    mAction.run();
                });
            }
            item = aux;
        }
        ExtensibleFXItemHolder h = new ExtensibleFXItemHolder();
        h.name = mText;
        h.weight = mWeight;
        item.setUserData(h);
        mContainer.inject(item);
    }
}
