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
package br.uece.lotus.viewer;

import br.uece.lotus.Component;
import br.uece.lotus.State;
import br.uece.lotus.Transition;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import javax.imageio.ImageIO;

/**
 *
 * @author emerson
 */
public class ComponentViewImpl extends AnchorPane implements ComponentView, Component.Listener {

    private Component mComponent;
    private ContextMenu mStateContextMenu;
    private TransitionViewFactory mTransitionViewFactory;
    private StateViewFactory mStateViewFactory;
    private List<Listener> mListeners = new ArrayList<>();
    private List<StateView> mStateViews = new ArrayList<>();
    private List<TransitionView> mTransitionViews = new ArrayList<>();

    public ComponentViewImpl() {
        mTransitionViewFactory = new TransitionViewFactory();
        mStateViewFactory = new StateViewFactory();
        //setStyle("-fx-background-color: white;");
    }

    @Override
    public void setStateContextMenu(ContextMenu menu) {
        mStateContextMenu = menu;
    }

    public Component getComponent() {
        return mComponent;
    }

    public void setComponent(Component component) {
        if (mComponent != null) {
            mComponent.removeListener(this);
        }
        clear();
        if (component == null) {
            return;
        }
        mComponent = component;
        mComponent.addListener(this);
        for (State vm : mComponent.getStates()) {
            showState(vm);
        }
        for (Transition tm : mComponent.getTransitions()) {
            showTransition(tm);
        }
    }

    @Override
    public void onChange(Component component) {

    }

    @Override
    public void onStateCreated(Component component, State state) {
        showState(state);
    }

    @Override
    public void onStateRemoved(Component component, State state) {
        hideState(state);
    }

    @Override
    public void onTransitionCreated(Component component, Transition transition) {
        showTransition(transition);
    }

    @Override
    public void onTransitionRemoved(Component component, Transition transition) {
        hideTransition(transition);
    }

    private void hideState(State state) {
        ObservableList<Node> aux = getChildren();
        StateView view;
        State s = null;

        synchronized (this) {
            view = (StateView) state.getValue("view");
            if (view != null) {
                s = view.getState();
            }
        }
        if (view == null) {
            return;
        }
        for (Transition t : s.getOutgoingTransitions()) {
            hideTransition(t);
        }
        for (Transition t : s.getIncomingTransitions()) {
            hideTransition(t);
        }
        synchronized (this) {
            aux.remove(view);
            state.setValue("view", null);
            view.setState(null);
            mStateViews.remove(view);
        }
    }

    private void hideTransition(Transition t) {
        synchronized (this) {
            TransitionViewImpl view = (TransitionViewImpl) t.getValue("view");
            if (view != null) {
                mTransitionViews.remove(view);
                view.setTransition(null);
                t.setValue("view", null);
                getChildren().remove(view);
            }
        }

    }

    private void showState(State s) {
        StateView view;
        Node node;
        synchronized (this) {
            if (s.getValue("view") == null) {
                view = mStateViewFactory.create();
                mStateViews.add(view);
                node = view.getNode();
                //view.setStyle("-fx-border-width: 1px; -fx-border-color: red;");
                view.setState(s);
                s.setValue("view", view);
                getChildren().add(node);
            }
        }
    }

    private void showTransition(Transition t) {
        TransitionView view;
        Node node;
        synchronized (this) {
            if (t.getValue("view") == null) {
                view = mTransitionViewFactory.create(t);
                mTransitionViews.add(view);
                view.setTransition(t);
                t.setValue("view", view);
                node = view.getNode();
                getChildren().add(node);
                if (view instanceof SelfTransitionViewImpl) {
                    ((SelfTransitionViewImpl) view).getSourceStateView().getNode().toFront();
                } else {
                    node.toBack();
                }
                for (Listener l: mListeners) {
                    l.onTransitionViewCreated(this, view);
                }
            }
        }
    }

    public void clear() {
        if (mComponent == null) {
            return;
        }
        for (State s : mComponent.getStates()) {
            hideState(s);
        }
        mComponent = null;
    }

    @Override
    public void saveAsPng(File file) {
        WritableImage img = snapshot(new SnapshotParameters(), null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException ex) {
            Logger.getLogger(ComponentViewImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    
    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    @Override
    public StateView locateStateView(Point2D point) {
        for (StateView v: mStateViews) {
            if (v.isInsideBounds(point)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public TransitionView locateTransitionView(Point2D point) {
        for (TransitionView v: mTransitionViews) {
            if (v.isInsideBounds(point)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public AnchorPane getNode() {
        return this;
    }
}
