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
 * Se existe um processo no nodo pai (Local_process) copia para o choice, caso
 * contrário cria um novo nodo para o choice.
 *
 * @author Emerson C. Lima
 */
public class CriarVerticesAuxiliaresVisitor implements Visitor {

    private final Map<String, Integer> mTabelaSimbolos;
    private final ComponentModel mGrafo;
    private final ContextoCompilacao mContexto;

    public CriarVerticesAuxiliaresVisitor(ContextoCompilacao contexto) {
        mContexto = contexto;
        mTabelaSimbolos = contexto.getTabelaSimbolos();
        mGrafo = contexto.getGrafo();
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.CHOICE)) {
            return;
        }
        ASTNode pai = n.getParent();
        String nome = (String) pai.getTag(ContextoCompilacao.TAG_PROCESSO_NOME);

        Integer id = mTabelaSimbolos.get(nome);
        if (id == null) {
            id = mContexto.gerarNovoId();
            StateModel v = mGrafo.newVertice(id);
            v.setValue("label", "" + id);
            mTabelaSimbolos.put(nome, id);
        }
        n.tag(ContextoCompilacao.TAG_PROCESSO_ID, id);
        n.tag(ContextoCompilacao.TAG_PROCESSO_NOME, nome);
    }
}
