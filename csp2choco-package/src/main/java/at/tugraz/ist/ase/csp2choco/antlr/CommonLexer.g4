lexer grammar CommonLexer;

@lexer::header {
}

/*********************************************
 * KEYWORDS
 **********************************************/

//BOOL:'bool';
//TRUE:'true';
//FALSE:'false';
//INT:'int';
//FLOAT:'float';
//SET :'set';
//OF :'of';
//ARRAY :'array';
//VAR :'var';
CONSTRAINT :  'constraint';
REQUIREMENT : 'requirement';

//DD:'..';
//DO:'.';
CM:',';

//PL:'+';
//MN:'-';
SC:';';
//CL:':';
//DC:'::';
LP:'(';
RP:')';

MUL :   '*' ; // assigns token name to '*' used above in grammar
DIV :   '/' ;
ADD :   '+' ;
SUB :   '-' ;
AND :   '&&' ;
OR  :   '||' ;
EQU :   '==' ;
NEQ :   '!=' ;
GRT :   '>' ;
LES :   '<' ;
GRE :   '>=' ;
LEE :   '<=' ;
IMP :   '->' ;

/*********************************************
 * GENERAL
 **********************************************/

IDENTIFIER // match identifiers
    :   LETTER (LETTER | DIGIT)*
    ;

COMMENT
    :   '%' ~('\n'|'\r')* '\r'? '\n' -> skip
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) -> skip
    ; // toss out whitespace

/*********************************************
 * TYPES
 **********************************************/

INT_CONST
    :  NEGATIVE NUMBER
    ;

//FLOAT_
//    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
//    |   '.' ('0'..'9')+ EXPONENT?
//    |   ('0'..'9')+ EXPONENT
//    ;

//STRING
//    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
//    ;
//
//CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
//    ;

/*********************************************
 * FRAGMENTS
 **********************************************/
fragment
NEGATIVE : '-'? ;

fragment
NUMBER : '0' | [1-9] DIGIT*;

fragment
LETTER : [a-zA-Z_] ;

fragment
DIGIT : [0-9] ;

//fragment
//EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

//fragment
//ESC_SEQ
//    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
//    |   UNICODE_ESC
//    |   OCTAL_ESC
//    ;

//fragment
//OCTAL_ESC
//    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
//    |   '\\' ('0'..'7') ('0'..'7')
//    |   '\\' ('0'..'7')
//    ;

//fragment
//HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;
//
//fragment
//UNICODE_ESC
//    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
//    ;