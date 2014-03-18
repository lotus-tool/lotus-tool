package br.uece.lotus.parser.lts;

import java.io.IOException;
import java_cup.runtime.Symbol;

public class LSAParser {

    private LSAScanner scanner;
    private Symbol lookahead;
    private Symbol current;

    public LSAParser(LSAScanner scanner) throws IOException {
        this.scanner = scanner;
        lookahead = scanner.next_token();
    }

    /**
     * Goal ::= BProcessDef;
     */
    public ASTNode parse() throws IOException {
        return bProcessDef();
    }

    /**
     * BProcessDef ::= BProcessBody BProcessBodyTail;
     */
    private ASTNode bProcessDef() throws IOException {
        ASTNode b = bProcessBody();
        ASTNode t = bProcessBodyTail();
        
        next(Sym.DOT);
        t.addChildren(b);
        return new ASTNode().ofType(LSAVisitor.PROCESS_DEF).addChildren(t);
    }

    /**
     * BProcessBodyTail ::= <epsilon>;
     * BProcessBodyTail ::= COMMA BProcessBody BProcessBodyTail;
     */
    private ASTNode bProcessBodyTail() throws IOException {
        if (hasNext(Sym.COMMA)) {
            next(Sym.COMMA);
            ASTNode b = bProcessBody();
            ASTNode t = bProcessBodyTail();
            return t.addChildren(b);
        } else {
            return new ASTNode().ofType(LSAVisitor.PROCESS_BODY_LIST);
        }
    }

    /**
     * BProcessBody ::= UPPER_IDENTIFIER EQUALS BLocalProcess
     */
    private ASTNode bProcessBody() throws IOException {
        next(Sym.UPPER_IDENTIFIER);
        String value = (String) current.value;
        next(Sym.EQUALS);
        ASTNode l = bLocalProcess();
        return new ASTNode().ofType(LSAVisitor.PROCESS_BODY).tag(ContextoCompilacao.TAG_PROCESSO_NOME, value).addChildren(l);
    }

    /**
     * BLocalProcess ::= LPAREN BChoice RPAREN
     * BLocalProcess ::= BBaseProcess
     */
    private ASTNode bLocalProcess() throws IOException {
        if (hasNext(Sym.LPAREN)) {
            next(Sym.LPAREN);
            ASTNode c = bChoice();
            next(Sym.RPAREN);
            return new ASTNode().ofType(LSAVisitor.LOCAL_PROCESS).addChildren(c);
        } else {
            ASTNode b = bBaseProcess();
            return new ASTNode().ofType(LSAVisitor.LOCAL_PROCESS).addChildren(b);
        }
    }

    /**
     * BBaseProcess ::= END
     * BBaseProcess ::= STOP
     * BBaseProcess ::= ERROR
     * BBaseProcess ::= UPPER_IDENTIFIER BBaseProcessTail;
     */
    private ASTNode bBaseProcess() throws IOException {
        if (hasNext(Sym.END)) {
            next(Sym.END);
            return new ASTNode().ofType(LSAVisitor.BASE_PROCESS).tag(ContextoCompilacao.TAG_PROCESSO_NOME, "END");
        } else if (hasNext(Sym.STOP)) {
            next(Sym.STOP);
            return new ASTNode().ofType(LSAVisitor.BASE_PROCESS).tag(ContextoCompilacao.TAG_PROCESSO_NOME, "STOP");
        } else if (hasNext(Sym.ERROR)) {
            next(Sym.ERROR);
            return new ASTNode().ofType(LSAVisitor.BASE_PROCESS).tag(ContextoCompilacao.TAG_PROCESSO_NOME, "ERROR");
        } else {
            next(Sym.UPPER_IDENTIFIER);
            String value = (String) current.value;
            ASTNode t = bBaseProcessTail();
            return new ASTNode().ofType(LSAVisitor.BASE_PROCESS).tag(ContextoCompilacao.TAG_PROCESSO_NOME, value).addChildren(t);
        }
    }

    /**
     * BBaseProcessTail ::= <epsilon>
     * BBaseProcessTail ::= SCOLON BBaseProcess BBaseProcessTail
     */
    private ASTNode bBaseProcessTail() throws IOException {
        if (hasNext(Sym.SCOLON)) {
            next(Sym.SCOLON);
            ASTNode b = bBaseProcess();
            ASTNode t = bBaseProcessTail();
            return t.addChildren(b);
        } else {
            return new ASTNode().ofType(LSAVisitor.BASE_PROCESS_TAIL);
        }
    }
    
   /**
     * BAction :: ActionLabel ACTION BLocalProcess     
     */
    private ASTNode bAction() throws IOException {
        ASTNode a = actionLabel();
        next(Sym.ACTION);
        ASTNode p = bLocalProcess();
        return new ASTNode().ofType(LSAVisitor.BACTION).addChildren(a, p);
    }

    /**
     * BChoice ::= BAction BChoiceTail
     */
    private ASTNode bChoice() throws IOException {
        ASTNode a = bAction();
        ASTNode t = bChoiceTail();
        return new ASTNode().ofType(LSAVisitor.CHOICE).addChildren(a, t);
    }

    /**
     * BChoiceTail ::= <epsilo>
     * BChoiceTail ::= PIPE BAction BChoiceTail     
     */
    private ASTNode bChoiceTail() throws IOException {
        if (hasNext(Sym.PIPE)) {
            next(Sym.PIPE);
            ASTNode p = bAction();
            ASTNode t = bChoiceTail();
            t.addChildren(p);
            return t;
        } else {
            return new ASTNode().ofType(LSAVisitor.CHOICE_TAIL);
        }
    }

    /**
     * ActionLabel ::= LOWER_IDENTIFIER ActionLabelTail
     */
    private ASTNode actionLabel() throws IOException {
        next(Sym.LOWER_IDENTIFIER);
        String value = (String) current.value;
        ASTNode a = actionLabelTail();
        return new ASTNode().ofType(LSAVisitor.ACTION_LABEL).addChildren(a).tag(ContextoCompilacao.TAG_TRANSICAO_NOME, value);
    }

    /**
     * ActionLabelTail ::= DOT LOWER_IDENTIFIER ActionLabelTail
     */
    private ASTNode actionLabelTail() throws IOException {
        if (hasNext(Sym.DOT)) {
            next(Sym.DOT);
            next(Sym.LOWER_IDENTIFIER);
            String value = (String) current.value;
            ASTNode t = actionLabelTail();
            ASTNode a = new ASTNode().ofType(LSAVisitor.ACTION_LABEL_TAIL).tag("id", value).addChildren(t);
            t.addChildren(a);
            return t;
        } else {
            return new ASTNode().ofType(LSAVisitor.ACTION_LABEL_TAIL);
        }
    }

    private void next(int sym) throws IOException {
        current = lookahead;
        lookahead = scanner.next_token();
        
        if (current.sym != sym) {
            System.out.println(scanner.yytext());
            throw new RuntimeException("era esperado " + sym + ", mas " + current.sym + " foi encontrado!");
        }
    }

    private boolean hasNext(int sym) {
        return lookahead != null && lookahead.sym == sym;
    }
}
