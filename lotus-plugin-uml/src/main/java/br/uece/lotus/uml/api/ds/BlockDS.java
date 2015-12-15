package br.uece.lotus.uml.api.ds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lva on 11/12/15.
 */
public class BlockDS {
    private double mLayoutY;
    private final List<Listener> mListeners = new ArrayList<>();
    private double mLayoutX;


    /*private final Component mComponent;*/

    public interface Listener{
        void onChange( BlockDS diagrama);
    }

    public double getLayoutX() {
        return mLayoutX;
    }
    public double getLayoutY() {
        return mLayoutY;
    }

    public void setLayoutX(double layoutX) {
        mLayoutX = layoutX;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }
    public void setLayoutY(double layoutY) {
        mLayoutY = layoutY;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
}

