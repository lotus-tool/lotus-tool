/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.annotator;

import br.uece.lotus.Component;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author emerson
 */
public class AnnotatorManager {             
    
    interface Listener {
        void onChange(AnnotatorManager annotatorManager);
    }
    
    private final List<Listener> mListeners = new ArrayList<>();
    private final List<AnnotatorProfile> mProfiles = new ArrayList<>();
    private static final AnnotatorManager instance = new AnnotatorManager();

    public static AnnotatorManager getInstance() {
        return instance;
    }
    
    public void add(AnnotatorProfile profile) {
        mProfiles.add(profile);
        for (Listener l: mListeners) {
            l.onChange(this);
        }
    }
    
    public void remove(AnnotatorProfile profile) {
        mProfiles.remove(profile);
        for (Listener l: mListeners) {
            l.onChange(this);
        }
    }
    
    void addListener(Listener l) {
        mListeners.add(l);
    }
    
    void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
    public List<AnnotatorProfile> getProfiles() {        
        return Collections.unmodifiableList(mProfiles);
    }
    
    public int getProfilesCount() {
        return mProfiles.size();
    }
    
    public void startAll() {
        for (AnnotatorProfile p: mProfiles) {
            start(p);
        }
    }
    
    private void start(AnnotatorProfile p) {
        Thread t = new Thread(() -> {
            try {
                InputStream input = new FileInputStream(p.getSource());
                Component component = p.getTargetComponent();
                ProbabilisticAnnotator alg = new ProbabilisticAnnotator();
                alg.annotate(component, input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
    
    
}
