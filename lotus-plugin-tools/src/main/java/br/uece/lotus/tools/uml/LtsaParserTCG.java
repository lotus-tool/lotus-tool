/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.lotus.tools.uml;

import br.uece.lotus.Component;
import br.uece.lotus.tools.uml.xmi.InteractionFragments;
import java.util.List;

/**
 *
 * @author Bruno Barbosa
 */
public class LtsaParserTCG {
    
    private final List<Mensagem> comunicacao;
    private final List<TabelaReferenciaID> relativoClassifier;
    private final List<InteractionFragments> loopsOuAlts;
    private final Component c;
    private static int idDisponivel;

    public LtsaParserTCG(List<Mensagem> comunicacao, List<TabelaReferenciaID> relativoClassifier, List<InteractionFragments> loopsOuAlts , Component c) {
        this.comunicacao = comunicacao;
        this.relativoClassifier = relativoClassifier;
        this.loopsOuAlts = loopsOuAlts;
        this.c = c;
        idDisponivel = this.relativoClassifier.size()+1;
    }
    
    public Component parseLTSA(){
        
        return c;
    }
}
