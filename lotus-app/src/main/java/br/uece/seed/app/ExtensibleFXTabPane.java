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

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;

/**
 *
 * @author emerson
 */
public class ExtensibleFXTabPane implements ExtensibleTabPane {

    private final TabPane mTabPane;
    private int counter;
    private final EventHandler<Event> mOnClose;

    public ExtensibleFXTabPane(TabPane tabPane) {
        mTabPane = tabPane;
        mOnClose = (Event e) -> {
            Tab t = (Tab) e.getSource();
            mTabPane.getTabs().remove(t);   
            if (mTabPane.getTabs().size() == 0) {
            	mTabPane.setMaxSize(0, 0);
            }
        };
    }

    @Override
    public int newTab(String name, Node content, boolean closable) {
        int id = counter++;
        Platform.runLater(() -> {
            Tab t = new Tab(name);
            t.setContent(content);
            t.setClosable(closable);
            t.setUserData(id);
            t.setOnClosed(mOnClose);
            mTabPane.getTabs().add(t);
            if (mTabPane.getTabs().size() > 0) {
            	mTabPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            }
        });
        return id;
    }

    @Override
    public void showTab(int id) {
        Platform.runLater(() -> {
            Tab t = getTabById(id);
            if (t != null) {
                mTabPane.getSelectionModel().select(t);
            }
        });
    }

    @Override
    public void closeTab(int id) {
        Platform.runLater(() -> {
            Tab t = getTabById(id);
            if (t != null) {
                mTabPane.getTabs().remove(t);
            }
            if (mTabPane.getTabs().size() == 0) {
            	mTabPane.setMaxSize(0, 0);
            }
        });
    }

    private Tab getTabById(int id) {
        for (Tab t : mTabPane.getTabs()) {            
            if (((Integer) t.getUserData()).equals(id)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void renameTab(Integer id, String name) {
        Platform.runLater(() -> {
            Tab tab = getTabById(id);
            if (tab != null) {
                tab.setText(name);
            }
        });
    }

    @Override
    public boolean isShowing(int id) {
        Tab t = getTabById(id);        
        return t != null;
    }

}
