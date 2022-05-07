/*
 * at.tugraz.ist.ase.fm - A Maven package for feature models
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

grammar FM4Conf;
import CommonLexer;

model : fm4confver modelname feature? relationship? constraint?;

fm4confver : FM4CONFversion;

modelname : MODELNAME CL identifier;

feature : FEATURE CL identifier (CM identifier)*;

relationship: RELATIONSHIP CL relationshiprule (CM relationshiprule)*;

constraint: CONSTRAINT CL constraintrule (CM constraintrule)*;

identifier: NAME;

relationshiprule : MANDATORY LP identifier CM identifier RP         # mandatory
                 | OPTIONAL LP identifier CM identifier RP          # optional
                 | ALTERNATIVE LP identifier (CM identifier)+ RP    # alternative
                 | OR LP identifier (CM identifier)+ RP             # or
                 ;

constraintrule : REQUIRES LP identifier CM identifier RP            # requires
                 | EXCLUDES LP identifier CM identifier RP          # excludes
                 ;