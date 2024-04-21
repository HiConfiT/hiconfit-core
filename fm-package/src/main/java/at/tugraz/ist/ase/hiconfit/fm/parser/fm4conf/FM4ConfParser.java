/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/HiConfiT/hiconfit-core/fm-package/src/main/java/at/tugraz/ist/ase/hiconfit/fm/parser/fm4conf/FM4Conf.g4 by ANTLR 4.13.1
package at.tugraz.ist.ase.hiconfit.fm.parser.fm4conf;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class FM4ConfParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		FM4CONFversion=1, MODELNAME=2, FEATURE=3, RELATIONSHIP=4, CONSTRAINT=5, 
		MANDATORY=6, OPTIONAL=7, ALTERNATIVE=8, OR=9, REQUIRES=10, EXCLUDES=11, 
		NOT_OPT=12, AND_OPT=13, OR_OPT=14, CM=15, SC=16, CL=17, LP=18, RP=19, 
		NAME=20, COMMENT=21, WS=22;
	public static final int
		RULE_model = 0, RULE_fm4confver = 1, RULE_modelname = 2, RULE_feature = 3, 
		RULE_relationship = 4, RULE_constraint = 5, RULE_identifier = 6, RULE_relationshiprule = 7, 
		RULE_constraintrule = 8, RULE_cnfrule = 9, RULE_element = 10, RULE_logic_operator = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"model", "fm4confver", "modelname", "feature", "relationship", "constraint", 
			"identifier", "relationshiprule", "constraintrule", "cnfrule", "element", 
			"logic_operator"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'FM4Conf-v1.0'", "'MODEL'", "'FEATURES'", "'RELATIONSHIPS'", "'CONSTRAINTS'", 
			"'mandatory'", "'optional'", "'alternative'", "'or'", "'requires'", "'excludes'", 
			"'~'", "'/\\'", "'\\/'", "','", "';'", "':'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FM4CONFversion", "MODELNAME", "FEATURE", "RELATIONSHIP", "CONSTRAINT", 
			"MANDATORY", "OPTIONAL", "ALTERNATIVE", "OR", "REQUIRES", "EXCLUDES", 
			"NOT_OPT", "AND_OPT", "OR_OPT", "CM", "SC", "CL", "LP", "RP", "NAME", 
			"COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "FM4Conf.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FM4ConfParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelContext extends ParserRuleContext {
		public Fm4confverContext fm4confver() {
			return getRuleContext(Fm4confverContext.class,0);
		}
		public ModelnameContext modelname() {
			return getRuleContext(ModelnameContext.class,0);
		}
		public FeatureContext feature() {
			return getRuleContext(FeatureContext.class,0);
		}
		public RelationshipContext relationship() {
			return getRuleContext(RelationshipContext.class,0);
		}
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public ModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_model; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterModel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitModel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitModel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelContext model() throws RecognitionException {
		ModelContext _localctx = new ModelContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_model);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			fm4confver();
			setState(25);
			modelname();
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FEATURE) {
				{
				setState(26);
				feature();
				}
			}

			setState(30);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RELATIONSHIP) {
				{
				setState(29);
				relationship();
				}
			}

			setState(33);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONSTRAINT) {
				{
				setState(32);
				constraint();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Fm4confverContext extends ParserRuleContext {
		public TerminalNode FM4CONFversion() { return getToken(FM4ConfParser.FM4CONFversion, 0); }
		public Fm4confverContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fm4confver; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterFm4confver(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitFm4confver(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitFm4confver(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Fm4confverContext fm4confver() throws RecognitionException {
		Fm4confverContext _localctx = new Fm4confverContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_fm4confver);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			match(FM4CONFversion);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ModelnameContext extends ParserRuleContext {
		public TerminalNode MODELNAME() { return getToken(FM4ConfParser.MODELNAME, 0); }
		public TerminalNode CL() { return getToken(FM4ConfParser.CL, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ModelnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modelname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterModelname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitModelname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitModelname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModelnameContext modelname() throws RecognitionException {
		ModelnameContext _localctx = new ModelnameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_modelname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			match(MODELNAME);
			setState(38);
			match(CL);
			setState(39);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FeatureContext extends ParserRuleContext {
		public TerminalNode FEATURE() { return getToken(FM4ConfParser.FEATURE, 0); }
		public TerminalNode CL() { return getToken(FM4ConfParser.CL, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public List<TerminalNode> CM() { return getTokens(FM4ConfParser.CM); }
		public TerminalNode CM(int i) {
			return getToken(FM4ConfParser.CM, i);
		}
		public FeatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_feature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterFeature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitFeature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitFeature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeatureContext feature() throws RecognitionException {
		FeatureContext _localctx = new FeatureContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_feature);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(FEATURE);
			setState(42);
			match(CL);
			setState(43);
			identifier();
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(44);
				match(CM);
				setState(45);
				identifier();
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationshipContext extends ParserRuleContext {
		public TerminalNode RELATIONSHIP() { return getToken(FM4ConfParser.RELATIONSHIP, 0); }
		public TerminalNode CL() { return getToken(FM4ConfParser.CL, 0); }
		public List<RelationshipruleContext> relationshiprule() {
			return getRuleContexts(RelationshipruleContext.class);
		}
		public RelationshipruleContext relationshiprule(int i) {
			return getRuleContext(RelationshipruleContext.class,i);
		}
		public List<TerminalNode> CM() { return getTokens(FM4ConfParser.CM); }
		public TerminalNode CM(int i) {
			return getToken(FM4ConfParser.CM, i);
		}
		public RelationshipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationship; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterRelationship(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitRelationship(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitRelationship(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationshipContext relationship() throws RecognitionException {
		RelationshipContext _localctx = new RelationshipContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_relationship);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(RELATIONSHIP);
			setState(52);
			match(CL);
			setState(53);
			relationshiprule();
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(54);
				match(CM);
				setState(55);
				relationshiprule();
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstraintContext extends ParserRuleContext {
		public TerminalNode CONSTRAINT() { return getToken(FM4ConfParser.CONSTRAINT, 0); }
		public TerminalNode CL() { return getToken(FM4ConfParser.CL, 0); }
		public List<ConstraintruleContext> constraintrule() {
			return getRuleContexts(ConstraintruleContext.class);
		}
		public ConstraintruleContext constraintrule(int i) {
			return getRuleContext(ConstraintruleContext.class,i);
		}
		public List<TerminalNode> CM() { return getTokens(FM4ConfParser.CM); }
		public TerminalNode CM(int i) {
			return getToken(FM4ConfParser.CM, i);
		}
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		ConstraintContext _localctx = new ConstraintContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_constraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			match(CONSTRAINT);
			setState(62);
			match(CL);
			setState(63);
			constraintrule();
			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(64);
				match(CM);
				setState(65);
				constraintrule();
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(FM4ConfParser.NAME, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationshipruleContext extends ParserRuleContext {
		public RelationshipruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationshiprule; }
	 
		public RelationshipruleContext() { }
		public void copyFrom(RelationshipruleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrContext extends RelationshipruleContext {
		public TerminalNode OR() { return getToken(FM4ConfParser.OR, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public List<TerminalNode> CM() { return getTokens(FM4ConfParser.CM); }
		public TerminalNode CM(int i) {
			return getToken(FM4ConfParser.CM, i);
		}
		public OrContext(RelationshipruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AlternativeContext extends RelationshipruleContext {
		public TerminalNode ALTERNATIVE() { return getToken(FM4ConfParser.ALTERNATIVE, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public List<TerminalNode> CM() { return getTokens(FM4ConfParser.CM); }
		public TerminalNode CM(int i) {
			return getToken(FM4ConfParser.CM, i);
		}
		public AlternativeContext(RelationshipruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterAlternative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitAlternative(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitAlternative(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OptionalContext extends RelationshipruleContext {
		public TerminalNode OPTIONAL() { return getToken(FM4ConfParser.OPTIONAL, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode CM() { return getToken(FM4ConfParser.CM, 0); }
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public OptionalContext(RelationshipruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterOptional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitOptional(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitOptional(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MandatoryContext extends RelationshipruleContext {
		public TerminalNode MANDATORY() { return getToken(FM4ConfParser.MANDATORY, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode CM() { return getToken(FM4ConfParser.CM, 0); }
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public MandatoryContext(RelationshipruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterMandatory(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitMandatory(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitMandatory(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationshipruleContext relationshiprule() throws RecognitionException {
		RelationshipruleContext _localctx = new RelationshipruleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_relationshiprule);
		int _la;
		try {
			setState(109);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MANDATORY:
				_localctx = new MandatoryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(73);
				match(MANDATORY);
				setState(74);
				match(LP);
				setState(75);
				identifier();
				setState(76);
				match(CM);
				setState(77);
				identifier();
				setState(78);
				match(RP);
				}
				break;
			case OPTIONAL:
				_localctx = new OptionalContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				match(OPTIONAL);
				setState(81);
				match(LP);
				setState(82);
				identifier();
				setState(83);
				match(CM);
				setState(84);
				identifier();
				setState(85);
				match(RP);
				}
				break;
			case ALTERNATIVE:
				_localctx = new AlternativeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(87);
				match(ALTERNATIVE);
				setState(88);
				match(LP);
				setState(89);
				identifier();
				setState(92); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(90);
					match(CM);
					setState(91);
					identifier();
					}
					}
					setState(94); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CM );
				setState(96);
				match(RP);
				}
				break;
			case OR:
				_localctx = new OrContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(98);
				match(OR);
				setState(99);
				match(LP);
				setState(100);
				identifier();
				setState(103); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(101);
					match(CM);
					setState(102);
					identifier();
					}
					}
					setState(105); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CM );
				setState(107);
				match(RP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstraintruleContext extends ParserRuleContext {
		public ConstraintruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintrule; }
	 
		public ConstraintruleContext() { }
		public void copyFrom(ConstraintruleContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExcludesContext extends ConstraintruleContext {
		public TerminalNode EXCLUDES() { return getToken(FM4ConfParser.EXCLUDES, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode CM() { return getToken(FM4ConfParser.CM, 0); }
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public ExcludesContext(ConstraintruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterExcludes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitExcludes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitExcludes(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CnfContext extends ConstraintruleContext {
		public CnfruleContext cnfrule() {
			return getRuleContext(CnfruleContext.class,0);
		}
		public CnfContext(ConstraintruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterCnf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitCnf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitCnf(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RequiresContext extends ConstraintruleContext {
		public TerminalNode REQUIRES() { return getToken(FM4ConfParser.REQUIRES, 0); }
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode CM() { return getToken(FM4ConfParser.CM, 0); }
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public RequiresContext(ConstraintruleContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterRequires(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitRequires(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitRequires(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintruleContext constraintrule() throws RecognitionException {
		ConstraintruleContext _localctx = new ConstraintruleContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_constraintrule);
		try {
			setState(126);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case REQUIRES:
				_localctx = new RequiresContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(111);
				match(REQUIRES);
				setState(112);
				match(LP);
				setState(113);
				identifier();
				setState(114);
				match(CM);
				setState(115);
				identifier();
				setState(116);
				match(RP);
				}
				break;
			case EXCLUDES:
				_localctx = new ExcludesContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				match(EXCLUDES);
				setState(119);
				match(LP);
				setState(120);
				identifier();
				setState(121);
				match(CM);
				setState(122);
				identifier();
				setState(123);
				match(RP);
				}
				break;
			case NOT_OPT:
			case LP:
			case NAME:
				_localctx = new CnfContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(125);
				cnfrule();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CnfruleContext extends ParserRuleContext {
		public TerminalNode LP() { return getToken(FM4ConfParser.LP, 0); }
		public List<CnfruleContext> cnfrule() {
			return getRuleContexts(CnfruleContext.class);
		}
		public CnfruleContext cnfrule(int i) {
			return getRuleContext(CnfruleContext.class,i);
		}
		public Logic_operatorContext logic_operator() {
			return getRuleContext(Logic_operatorContext.class,0);
		}
		public TerminalNode RP() { return getToken(FM4ConfParser.RP, 0); }
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public CnfruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cnfrule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterCnfrule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitCnfrule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitCnfrule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CnfruleContext cnfrule() throws RecognitionException {
		CnfruleContext _localctx = new CnfruleContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_cnfrule);
		int _la;
		try {
			setState(140);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LP:
				enterOuterAlt(_localctx, 1);
				{
				setState(128);
				match(LP);
				setState(129);
				cnfrule();
				setState(130);
				logic_operator();
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 1314816L) != 0)) {
					{
					{
					setState(131);
					cnfrule();
					}
					}
					setState(136);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(137);
				match(RP);
				}
				break;
			case NOT_OPT:
			case NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(139);
				element();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode NOT_OPT() { return getToken(FM4ConfParser.NOT_OPT, 0); }
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_element);
		try {
			setState(145);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(142);
				identifier();
				}
				break;
			case NOT_OPT:
				enterOuterAlt(_localctx, 2);
				{
				setState(143);
				match(NOT_OPT);
				setState(144);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Logic_operatorContext extends ParserRuleContext {
		public TerminalNode AND_OPT() { return getToken(FM4ConfParser.AND_OPT, 0); }
		public TerminalNode OR_OPT() { return getToken(FM4ConfParser.OR_OPT, 0); }
		public Logic_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logic_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).enterLogic_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FM4ConfListener ) ((FM4ConfListener)listener).exitLogic_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FM4ConfVisitor ) return ((FM4ConfVisitor<? extends T>)visitor).visitLogic_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Logic_operatorContext logic_operator() throws RecognitionException {
		Logic_operatorContext _localctx = new Logic_operatorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_logic_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			_la = _input.LA(1);
			if ( !(_la==AND_OPT || _la==OR_OPT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0016\u0096\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u001c\b\u0000\u0001\u0000"+
		"\u0003\u0000\u001f\b\u0000\u0001\u0000\u0003\u0000\"\b\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003/\b\u0003"+
		"\n\u0003\f\u00032\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0005\u00049\b\u0004\n\u0004\f\u0004<\t\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005C\b\u0005"+
		"\n\u0005\f\u0005F\t\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007"+
		"]\b\u0007\u000b\u0007\f\u0007^\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007h\b\u0007\u000b"+
		"\u0007\f\u0007i\u0001\u0007\u0001\u0007\u0003\u0007n\b\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u007f\b\b\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0005\t\u0085\b\t\n\t\f\t\u0088\t\t\u0001\t\u0001\t\u0001\t"+
		"\u0003\t\u008d\b\t\u0001\n\u0001\n\u0001\n\u0003\n\u0092\b\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0000\u0000\f\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0000\u0001\u0001\u0000\r\u000e\u0099\u0000\u0018"+
		"\u0001\u0000\u0000\u0000\u0002#\u0001\u0000\u0000\u0000\u0004%\u0001\u0000"+
		"\u0000\u0000\u0006)\u0001\u0000\u0000\u0000\b3\u0001\u0000\u0000\u0000"+
		"\n=\u0001\u0000\u0000\u0000\fG\u0001\u0000\u0000\u0000\u000em\u0001\u0000"+
		"\u0000\u0000\u0010~\u0001\u0000\u0000\u0000\u0012\u008c\u0001\u0000\u0000"+
		"\u0000\u0014\u0091\u0001\u0000\u0000\u0000\u0016\u0093\u0001\u0000\u0000"+
		"\u0000\u0018\u0019\u0003\u0002\u0001\u0000\u0019\u001b\u0003\u0004\u0002"+
		"\u0000\u001a\u001c\u0003\u0006\u0003\u0000\u001b\u001a\u0001\u0000\u0000"+
		"\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u001e\u0001\u0000\u0000"+
		"\u0000\u001d\u001f\u0003\b\u0004\u0000\u001e\u001d\u0001\u0000\u0000\u0000"+
		"\u001e\u001f\u0001\u0000\u0000\u0000\u001f!\u0001\u0000\u0000\u0000 \""+
		"\u0003\n\u0005\u0000! \u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000"+
		"\"\u0001\u0001\u0000\u0000\u0000#$\u0005\u0001\u0000\u0000$\u0003\u0001"+
		"\u0000\u0000\u0000%&\u0005\u0002\u0000\u0000&\'\u0005\u0011\u0000\u0000"+
		"\'(\u0003\f\u0006\u0000(\u0005\u0001\u0000\u0000\u0000)*\u0005\u0003\u0000"+
		"\u0000*+\u0005\u0011\u0000\u0000+0\u0003\f\u0006\u0000,-\u0005\u000f\u0000"+
		"\u0000-/\u0003\f\u0006\u0000.,\u0001\u0000\u0000\u0000/2\u0001\u0000\u0000"+
		"\u00000.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u00001\u0007\u0001"+
		"\u0000\u0000\u000020\u0001\u0000\u0000\u000034\u0005\u0004\u0000\u0000"+
		"45\u0005\u0011\u0000\u00005:\u0003\u000e\u0007\u000067\u0005\u000f\u0000"+
		"\u000079\u0003\u000e\u0007\u000086\u0001\u0000\u0000\u00009<\u0001\u0000"+
		"\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;\t\u0001"+
		"\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000=>\u0005\u0005\u0000\u0000"+
		">?\u0005\u0011\u0000\u0000?D\u0003\u0010\b\u0000@A\u0005\u000f\u0000\u0000"+
		"AC\u0003\u0010\b\u0000B@\u0001\u0000\u0000\u0000CF\u0001\u0000\u0000\u0000"+
		"DB\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000E\u000b\u0001\u0000"+
		"\u0000\u0000FD\u0001\u0000\u0000\u0000GH\u0005\u0014\u0000\u0000H\r\u0001"+
		"\u0000\u0000\u0000IJ\u0005\u0006\u0000\u0000JK\u0005\u0012\u0000\u0000"+
		"KL\u0003\f\u0006\u0000LM\u0005\u000f\u0000\u0000MN\u0003\f\u0006\u0000"+
		"NO\u0005\u0013\u0000\u0000On\u0001\u0000\u0000\u0000PQ\u0005\u0007\u0000"+
		"\u0000QR\u0005\u0012\u0000\u0000RS\u0003\f\u0006\u0000ST\u0005\u000f\u0000"+
		"\u0000TU\u0003\f\u0006\u0000UV\u0005\u0013\u0000\u0000Vn\u0001\u0000\u0000"+
		"\u0000WX\u0005\b\u0000\u0000XY\u0005\u0012\u0000\u0000Y\\\u0003\f\u0006"+
		"\u0000Z[\u0005\u000f\u0000\u0000[]\u0003\f\u0006\u0000\\Z\u0001\u0000"+
		"\u0000\u0000]^\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000\u0000^_\u0001"+
		"\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0005\u0013\u0000\u0000"+
		"an\u0001\u0000\u0000\u0000bc\u0005\t\u0000\u0000cd\u0005\u0012\u0000\u0000"+
		"dg\u0003\f\u0006\u0000ef\u0005\u000f\u0000\u0000fh\u0003\f\u0006\u0000"+
		"ge\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000"+
		"\u0000ij\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000kl\u0005\u0013"+
		"\u0000\u0000ln\u0001\u0000\u0000\u0000mI\u0001\u0000\u0000\u0000mP\u0001"+
		"\u0000\u0000\u0000mW\u0001\u0000\u0000\u0000mb\u0001\u0000\u0000\u0000"+
		"n\u000f\u0001\u0000\u0000\u0000op\u0005\n\u0000\u0000pq\u0005\u0012\u0000"+
		"\u0000qr\u0003\f\u0006\u0000rs\u0005\u000f\u0000\u0000st\u0003\f\u0006"+
		"\u0000tu\u0005\u0013\u0000\u0000u\u007f\u0001\u0000\u0000\u0000vw\u0005"+
		"\u000b\u0000\u0000wx\u0005\u0012\u0000\u0000xy\u0003\f\u0006\u0000yz\u0005"+
		"\u000f\u0000\u0000z{\u0003\f\u0006\u0000{|\u0005\u0013\u0000\u0000|\u007f"+
		"\u0001\u0000\u0000\u0000}\u007f\u0003\u0012\t\u0000~o\u0001\u0000\u0000"+
		"\u0000~v\u0001\u0000\u0000\u0000~}\u0001\u0000\u0000\u0000\u007f\u0011"+
		"\u0001\u0000\u0000\u0000\u0080\u0081\u0005\u0012\u0000\u0000\u0081\u0082"+
		"\u0003\u0012\t\u0000\u0082\u0086\u0003\u0016\u000b\u0000\u0083\u0085\u0003"+
		"\u0012\t\u0000\u0084\u0083\u0001\u0000\u0000\u0000\u0085\u0088\u0001\u0000"+
		"\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000"+
		"\u0000\u0000\u0087\u0089\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0005\u0013\u0000\u0000\u008a\u008d\u0001\u0000"+
		"\u0000\u0000\u008b\u008d\u0003\u0014\n\u0000\u008c\u0080\u0001\u0000\u0000"+
		"\u0000\u008c\u008b\u0001\u0000\u0000\u0000\u008d\u0013\u0001\u0000\u0000"+
		"\u0000\u008e\u0092\u0003\f\u0006\u0000\u008f\u0090\u0005\f\u0000\u0000"+
		"\u0090\u0092\u0003\f\u0006\u0000\u0091\u008e\u0001\u0000\u0000\u0000\u0091"+
		"\u008f\u0001\u0000\u0000\u0000\u0092\u0015\u0001\u0000\u0000\u0000\u0093"+
		"\u0094\u0007\u0000\u0000\u0000\u0094\u0017\u0001\u0000\u0000\u0000\r\u001b"+
		"\u001e!0:D^im~\u0086\u008c\u0091";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}