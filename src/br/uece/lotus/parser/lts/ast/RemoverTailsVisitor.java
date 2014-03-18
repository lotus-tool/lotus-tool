package br.uece.lotus.parser.lts.ast;

import br.uece.lotus.parser.lts.ASTNode;
import br.uece.lotus.parser.lts.Visitor;
import br.uece.lotus.parser.lts.LSAVisitor;
import java.util.List;

public class RemoverTailsVisitor implements Visitor {

    public RemoverTailsVisitor() {
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        String aux = n.getType();
        switch (aux) {
            case LSAVisitor.ACTION_LABEL:
                adicionarNetosAoPai(n, LSAVisitor.ACTION_LABEL_TAIL);
                break;
            case LSAVisitor.BASE_PROCESS:
                adicionarNetosAoPai(n, LSAVisitor.BASE_PROCESS_TAIL);            
                break;
            case LSAVisitor.CHOICE:
                adicionarNetosAoPai(n, LSAVisitor.CHOICE_TAIL);
                break;
        }
    }

    private void adicionarNetosAoPai(ASTNode n, String tipoFilho) {
        ASTNode filho = n.getFirstChildByType(tipoFilho);
        if (filho != null) {
            List<ASTNode> netos = filho.getChildren();        
            n.addChildren(netos);
            n.removeChildren(filho);
        }
    }
    
}
