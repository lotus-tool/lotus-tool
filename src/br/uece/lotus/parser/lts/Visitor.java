package br.uece.lotus.parser.lts;

public interface Visitor {

    void visit(ASTNode n) throws Exception;
}
