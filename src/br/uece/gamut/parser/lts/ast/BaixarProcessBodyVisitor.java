/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.Visitor;
import br.uece.gamut.parser.lts.ContextoCompilacao;
import br.uece.gamut.parser.lts.LSAVisitor;

/**
 *
 * @author emerson
 */
public class BaixarProcessBodyVisitor implements Visitor {

    public BaixarProcessBodyVisitor() {
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (!n.getType().equals(LSAVisitor.PROCESS_BODY)) {
            return;
        }
        
        ASTNode filho = n.getFirstChildByType(LSAVisitor.LOCAL_PROCESS);
        filho.tag(ContextoCompilacao.TAG_PROCESSO_NOME, n.getTag(ContextoCompilacao.TAG_PROCESSO_NOME));
        filho.tag(ContextoCompilacao.TAG_PROCESSO_ID, n.getTag(ContextoCompilacao.TAG_PROCESSO_ID));        
    }
    
}
