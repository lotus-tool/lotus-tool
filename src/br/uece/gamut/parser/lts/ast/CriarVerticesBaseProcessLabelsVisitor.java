/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.model.ComponentModel;
import br.uece.gamut.model.StateModel;
import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.Visitor;
import br.uece.gamut.parser.lts.ContextoCompilacao;
import br.uece.gamut.parser.lts.LSAVisitor;
import java.util.Map;

/**
 *
 * @author emerson
 */
public class CriarVerticesBaseProcessLabelsVisitor implements Visitor {
    
    private final Map<String, Integer> mTabelaSimbolos;
    private final ComponentModel mGrafo;
    private final ContextoCompilacao mContexto;
        
    public CriarVerticesBaseProcessLabelsVisitor(ContextoCompilacao contexto) {
        mContexto = contexto;
        mTabelaSimbolos = contexto.getTabelaSimbolos();
        mGrafo = contexto.getGrafo();
    }
    
    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.BASE_PROCESS)) return;
        String nome = (String) n.getTag(ContextoCompilacao.TAG_PROCESSO_NOME);
        Integer id = mTabelaSimbolos.get(nome);
        if (id == null) {
            id = mContexto.gerarNovoId();
            StateModel v = mGrafo.newVertice(id);
            v.setValue("label", nome);
            id = v.getID();
            mTabelaSimbolos.put(nome, id);            
        }
        n.tag(ContextoCompilacao.TAG_PROCESSO_ID, id);
    }
    
}
