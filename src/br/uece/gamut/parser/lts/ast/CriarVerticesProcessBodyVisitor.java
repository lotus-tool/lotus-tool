/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.Grafo;
import br.uece.gamut.Vertice;
import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.Visitor;
import br.uece.gamut.parser.lts.ContextoCompilacao;
import br.uece.gamut.parser.lts.LSAVisitor;
import java.util.Map;

/**
 *
 * @author emerson
 */
public class CriarVerticesProcessBodyVisitor implements Visitor {
    
    private final Map<String, Integer> mTabelaSimbolos;
    private final Grafo mGrafo;    
    private final ContextoCompilacao mContexto;

    public CriarVerticesProcessBodyVisitor(ContextoCompilacao contexto) {
        mContexto = contexto;
        mTabelaSimbolos = contexto.getTabelaSimbolos();
        mGrafo = contexto.getGrafo();
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.PROCESS_BODY)) return;
        String nome = (String) n.getTag(ContextoCompilacao.TAG_PROCESSO_NOME);                
        Integer id = mTabelaSimbolos.get(nome);
        System.out.println("nome: " + nome + ", id: " + id);
        if (id == null) {
            id = mContexto.gerarNovoId();
            Vertice v = mGrafo.newVertice(id);
            //v.setTag("label", nome);
            //id = v.getID();
            mTabelaSimbolos.put(nome, id);
            n.tag(ContextoCompilacao.TAG_PROCESSO_ID, id);
            
            ASTNode filho = n.getFirstChild();
            if (filho != null) {
                ASTNode neto = filho.getFirstChild();        
                if (neto.getType().equals(LSAVisitor.BASE_PROCESS)) {
                    nome = (String) neto.getTag(ContextoCompilacao.TAG_PROCESSO_NOME);            
                    mTabelaSimbolos.put(nome, id);
                }            
            }
        }
    }
    
}
