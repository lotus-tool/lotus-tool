/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.ContextoCompilacao;
import br.uece.gamut.parser.lts.Visitor;
import br.uece.gamut.parser.lts.LSAVisitor;

/**
 *
 * @author emerson
 */
public class ResolverActionLabelsVisitor implements Visitor {
    
    public ResolverActionLabelsVisitor() {
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.ACTION_LABEL_TAIL)) return;
        String nome = (String) n.getTag(ContextoCompilacao.TAG_TRANSICAO_NOME);
        if (nome == null) return;
        ASTNode pai = n.getParent();        
        String nomePai = (String) pai.getTag(ContextoCompilacao.TAG_TRANSICAO_NOME);
        String transicao = nomePai + "." + nome;
        pai.tag(ContextoCompilacao.TAG_TRANSICAO_NOME, transicao);
        pai.removeChildren(n);
    }
    
}
