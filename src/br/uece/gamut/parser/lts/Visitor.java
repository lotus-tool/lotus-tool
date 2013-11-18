package br.uece.gamut.parser.lts;

public interface Visitor {

    void visit(ASTNode n) throws Exception;
}
