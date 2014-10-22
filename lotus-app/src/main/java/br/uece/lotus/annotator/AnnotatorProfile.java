/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uece.lotus.annotator;

import br.uece.lotus.Component;

/**
 *
 * @author emerson
 */
public class AnnotatorProfile {
    
    private Component targetComponent;
    private String source;
    private String target;    
    private String status;    

    @Override
    public String toString() {
        return String.format("%s <- %s [%s]", target, source, status);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }   

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Component getTargetComponent() {
        return this.targetComponent;
    }

    public void setTargetComponent(Component targetComponent) {
        this.targetComponent = targetComponent;
    }
    
    
}
