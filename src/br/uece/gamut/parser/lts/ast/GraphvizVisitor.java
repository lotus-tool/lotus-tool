package br.uece.gamut.parser.lts.ast;

import br.uece.gamut.parser.lts.ASTNode;
import br.uece.gamut.parser.lts.Visitor;
import java.io.PrintStream;
import java.util.Map.Entry;

public class GraphvizVisitor implements Visitor {

    private static final String CABECALHO = "digraph minijava {";
    private static final String RODAPE = "}";
    private PrintStream buffer;
    private ASTNode raiz;
    private boolean flag = true;

    public GraphvizVisitor(PrintStream out) {
        buffer = out;
    }

    @Override
    public void visit(ASTNode n) throws Exception {
        if (flag) {
            flag = false;
            buffer.append(CABECALHO);
        }

        buffer.printf("N%d [label=\"%s\\n", n.hashCode(), n.getType());
        if (n.getTags() != null) {
            for (Entry<String, Object> tag : n.getTags().entrySet()) {
                buffer.printf("%s: %s\\n", tag.getKey(), tag.getValue());
            }
        }
        buffer.append("\"]");        
        for (ASTNode child : n.getChildren()) {
            buffer.printf("N%d->N%d\n", n.hashCode(), child.hashCode());
        }        
        if (n.getParent() == null) {
            buffer.append(RODAPE);
        }
    }
}
