package br.uece.lotus.view;

import br.uece.lotus.model.Model;

public interface View {
    
    boolean pontoPertenceAoObjeto(double x, double y);
    void setSelecionado(boolean selecionado);
    boolean isSelecionado();
    void setDestacado(boolean destacado);
    void getPropriedades();   
    Model getModel();
    
}
