lexer grammar DaMaLLexer;

INT : [0-9]+;
MUL : '*';
DIV : '/';
ADD : '+';
SUB : '-';
WS : [ \t]+ -> channel(HIDDEN);