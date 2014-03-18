package br.uece.gamut.parser.lts;

import java_cup.runtime.Symbol;

%%
%cup
%public
%line
%class LSAScanner

%eofval{
    return new Symbol(Sym.EOF, yyline, 0, null);
%eofval}

LowerIdentifier 	= [a-z_][a-z_0-9]*
UpperIdentifier 	= [A-Z_][A-Z_0-9]*
eol 			= [\r\n]
white 			= {eol}|[ \t\f]

%%

"."     { return new Symbol(Sym.DOT, yyline, 0, null); }
","     { return new Symbol(Sym.COMMA, yyline, 0, null); }
"="     { return new Symbol(Sym.EQUALS, yyline, 0, null); }
"("     { return new Symbol(Sym.LPAREN, yyline, 0, null); }
")"     { return new Symbol(Sym.RPAREN, yyline, 0, null); }
";"     { return new Symbol(Sym.SCOLON, yyline, 0, null); }

"->"     { return new Symbol(Sym.ACTION, yyline, 0, null); }
"|"     { return new Symbol(Sym.PIPE, yyline, 0, null); }

"END"     { return new Symbol(Sym.END, yyline, 0, null); }
"STOP"     { return new Symbol(Sym.STOP, yyline, 0, null); }
"ERROR"     { return new Symbol(Sym.ERROR, yyline, 0, null); }

{LowerIdentifier} 	{ return new Symbol(Sym.LOWER_IDENTIFIER, yyline, 0, yytext()); }
{UpperIdentifier} 	{ return new Symbol(Sym.UPPER_IDENTIFIER, yyline, 0, yytext()); }

{white}+ 		{ /* ignora */ }
. 			{ System.err.println("Caracter ilegal: " + yytext()); }

  			