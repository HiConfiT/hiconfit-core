/*
 * at.tugraz.ist.ase.fm - A Maven package for feature models
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

lexer grammar CommonLexer;

@lexer::header {
}

/*********************************************
 * KEYWORDS
 **********************************************/

FM4CONFversion : 'FM4Conf-v1.0';

MODELNAME : 'MODEL';
FEATURE : 'FEATURES';
RELATIONSHIP : 'RELATIONSHIPS';
CONSTRAINT : 'CONSTRAINTS';

MANDATORY : 'mandatory';
OPTIONAL : 'optional';
ALTERNATIVE : 'alternative';
OR : 'or';
REQUIRES : 'requires';
EXCLUDES : 'excludes';

//DD:'..';
//DO:'.';
CM:',';

//PL:'+';
//MN:'-';
SC:';';
CL:':';
//DC:'::';
LP:'(';
RP:')';

/*********************************************
 * GENERAL
 **********************************************/

NAME : ID ( SPACE ID )* ;

COMMENT
    :   '%' ~('\n'|'\r')* '\n' -> skip
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) -> skip
    ; // toss out whitespace

/*********************************************
 * FRAGMENTS
 **********************************************/

fragment ID : ID_HEAD ID_TAIL* ;
fragment ID_HEAD : LETTER ;
fragment ID_TAIL : LETTER | DIGIT;
fragment LETTER : [a-zA-Z_-] ;
fragment DIGIT : [0-9] ;
fragment SPACE : ' '+ ;