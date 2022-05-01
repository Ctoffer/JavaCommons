parser grammar DaMaLParser;

options { tokenVocab=DaMaLLexer; }

operation  : expr EOF ;
expr:	add ((MUL | DIV) add)* ;
add :   atom ((ADD | SUB) atom)* ;
atom : INT ;