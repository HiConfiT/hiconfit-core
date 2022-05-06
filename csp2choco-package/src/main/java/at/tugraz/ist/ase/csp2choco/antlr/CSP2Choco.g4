grammar CSP2Choco;
import CommonLexer;

//options{
//    language = Java;
//    //output=AST;
//    tokenVocab=CSP2ChocoLexer;
//}

//@header {
//import org.chocosolver.parser.flatzinc.ast.*;
//import org.chocosolver.parser.flatzinc.ast.declaration.*;
//import org.chocosolver.parser.flatzinc.ast.expression.*;
//import org.chocosolver.solver.ResolutionPolicy;
//import org.chocosolver.solver.Model;
//
//import java.util.ArrayList;
//import java.util.List;
//}
//
//@members{
//
//public Datas datas;
//
//// the model
//public Model mModel;
//
//
//public boolean allSolutions, freeSearch;
//}

//@members{
//public Model model;
//
//void defineArithmeticConstraint() { }
//}

model : statement_list* ;

statement_list : (statement SC) ;

statement : constraint
          | requirement
          ;

constraint : CONSTRAINT expr;

requirement : REQUIREMENT expr;

expr:   '-' expr                                        # Minus
    |   expr op=('*'|'/') expr                          # MulDiv
    |   expr op=('+'|'-') expr                          # AddSub
    |   expr op=('>'|'>='|'<'|'<='|'=='|'!=') expr      # Comparation
    |   '!' expr                                        # Not
    |   expr op='&&' expr                               # And
    |   expr op='||' expr                               # Or
    |   expr op='->' expr                               # Implication
    |   IDENTIFIER                                      # id
    |   INT_CONST                                       # int
    |   '(' expr ')'                                    # parens
    ;