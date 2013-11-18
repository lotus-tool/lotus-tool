package br.uece.gamut.parser.lts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASTNode {

    private String type;
    private int lineNumber;
    private Map<String, Object> tags = new HashMap<>();
    private List<ASTNode> children = new ArrayList<>();
    private ASTNode parent;
    private List<ASTNode> listaRemocoes = new ArrayList<>();
    private boolean visitando;

    public ASTNode ofType(String t) {
        this.type = t;
        return this;
    }

    public ASTNode atLine(int l) {
        this.lineNumber = l;
        return this;
    }

    public void accept(Visitor v) throws Exception {
        visitando = true;
        for (ASTNode n : children) {
            n.accept(v);
        }
        for (ASTNode n: listaRemocoes) {
            children.remove(n);
        }
        visitando = false;
        v.visit(this);
    }

    public int getChildCount() {
        return children.size();
    }

    public String getType() {
        return type;
    }

    public int getLine() {
        return lineNumber;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public ASTNode addChildren(List<ASTNode> nodes) {
        for (ASTNode n : nodes) {
            n.setParent(this);
            children.add(n);            
        }
        return this;
    }
    
    public ASTNode addChildren(ASTNode... nodes) {
        for (ASTNode n : nodes) {
            n.setParent(this);
            children.add(n);            
        }
        return this;
    }

    public ASTNode tag(String key, Object value) {
        tags.put(key, value);
        return this;
    }

    public void setParent(ASTNode n) {
        this.parent = n;
    }

    public ASTNode getParent() {
        return this.parent;
    }

    public Object getTag(String key) {
        return tags.get(key);        
    }

    public void removeAllChildren() {
        if (visitando) {
            listaRemocoes.addAll(children);
        } else {
            children.clear();   
        }        
    }

    public ASTNode getFirstChildByType(String type) {
        if (children == null) return null;
        for (ASTNode n: children) {
            if (n.getType().equals(type)) return n;
        }
        return null;
    }

    public Object tag(String process) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ASTNode getFirstChild() {        
        return children.isEmpty() ? null : children.get(0);
    }

    public void removeChildren(ASTNode filho) {
        if (visitando) {
            listaRemocoes.add(filho);
        } else {
            children.remove(filho);   
        }        
    }
}
