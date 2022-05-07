/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CA-CDR-V2/fm-package/src/main/java/at/tugraz/ist/ase/fm/parser/fm4conf/FM4Conf.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.fm.parser.fm4conf;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FM4ConfParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		FM4CONFversion=1, MODELNAME=2, FEATURE=3, RELATIONSHIP=4, CONSTRAINT=5, 
		MANDATORY=6, OPTIONAL=7, ALTERNATIVE=8, OR=9, REQUIRES=10, EXCLUDES=11, 
		CM=12, SC=13, CL=14, LP=15, RP=16, NAME=17, COMMENT=18, WS=19;
	public static final int
		RULE_model = 0, RULE_fm4confver = 1, RULE_modelname = 2, RULE_feature = 3, 
		RULE_relationship = 4, RULE_constraint = 5, RULE_identifier = 6, RULE_relationshiprule = 7, 
		RULE_constraintrule = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"model", "fm4confver", "modelname", "feature", "relationship", "constraint", 
			"identifier", "relationshiprule", "constraintrule"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'FM4Conf-v1.0'", "'MODEL'", "'FEATURES'", "'RELATIONSHIPS'", "'CONSTRAINTS'", 
			"'mandatory'", "'optional'", "'alternative'", "'or'", "'requires'", "'excludes'", 
			"','", "';'", "':'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FM4CONFversion", "MODELNAME", "FEATURE", "RELATIONSHIP", "CONSTRAINT", 
			"MANDATORY", "OPTIONAL", "ALTERNATIVE", "OR", "REQUIRES", "EXCLUDES", 
			"CM", "SC", "CL", "LP", "RP", "NAME", "COMMENT", "WS"
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
			setState(18);
			fm4confver();
			setState(19);
			modelname();
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FEATURE) {
				{
				setState(20);
				feature();
				}
			}

			setState(24);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==RELATIONSHIP) {
				{
				setState(23);
				relationship();
				}
			}

			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CONSTRAINT) {
				{
				setState(26);
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
			setState(29);
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
			setState(31);
			match(MODELNAME);
			setState(32);
			match(CL);
			setState(33);
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
			setState(35);
			match(FEATURE);
			setState(36);
			match(CL);
			setState(37);
			identifier();
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(38);
				match(CM);
				setState(39);
				identifier();
				}
				}
				setState(44);
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
			setState(45);
			match(RELATIONSHIP);
			setState(46);
			match(CL);
			setState(47);
			relationshiprule();
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(48);
				match(CM);
				setState(49);
				relationshiprule();
				}
				}
				setState(54);
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
			setState(55);
			match(CONSTRAINT);
			setState(56);
			match(CL);
			setState(57);
			constraintrule();
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CM) {
				{
				{
				setState(58);
				match(CM);
				setState(59);
				constraintrule();
				}
				}
				setState(64);
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
			setState(65);
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
			setState(103);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MANDATORY:
				_localctx = new MandatoryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				match(MANDATORY);
				setState(68);
				match(LP);
				setState(69);
				identifier();
				setState(70);
				match(CM);
				setState(71);
				identifier();
				setState(72);
				match(RP);
				}
				break;
			case OPTIONAL:
				_localctx = new OptionalContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(74);
				match(OPTIONAL);
				setState(75);
				match(LP);
				setState(76);
				identifier();
				setState(77);
				match(CM);
				setState(78);
				identifier();
				setState(79);
				match(RP);
				}
				break;
			case ALTERNATIVE:
				_localctx = new AlternativeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				match(ALTERNATIVE);
				setState(82);
				match(LP);
				setState(83);
				identifier();
				setState(86); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(84);
					match(CM);
					setState(85);
					identifier();
					}
					}
					setState(88); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CM );
				setState(90);
				match(RP);
				}
				break;
			case OR:
				_localctx = new OrContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(92);
				match(OR);
				setState(93);
				match(LP);
				setState(94);
				identifier();
				setState(97); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(95);
					match(CM);
					setState(96);
					identifier();
					}
					}
					setState(99); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CM );
				setState(101);
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
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case REQUIRES:
				_localctx = new RequiresContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				match(REQUIRES);
				setState(106);
				match(LP);
				setState(107);
				identifier();
				setState(108);
				match(CM);
				setState(109);
				identifier();
				setState(110);
				match(RP);
				}
				break;
			case EXCLUDES:
				_localctx = new ExcludesContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(112);
				match(EXCLUDES);
				setState(113);
				match(LP);
				setState(114);
				identifier();
				setState(115);
				match(CM);
				setState(116);
				identifier();
				setState(117);
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

	public static final String _serializedATN =
		"\u0004\u0001\u0013z\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\u0016\b\u0000"+
		"\u0001\u0000\u0003\u0000\u0019\b\u0000\u0001\u0000\u0003\u0000\u001c\b"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u0003)\b\u0003\n\u0003\f\u0003,\t\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0005\u00043\b\u0004\n\u0004\f\u00046\t"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005=\b\u0005\n\u0005\f\u0005@\t\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0004\u0007W\b\u0007\u000b\u0007\f\u0007X\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0004\u0007"+
		"b\b\u0007\u000b\u0007\f\u0007c\u0001\u0007\u0001\u0007\u0003\u0007h\b"+
		"\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\bx\b\b\u0001\b"+
		"\u0000\u0000\t\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0000\u0000|"+
		"\u0000\u0012\u0001\u0000\u0000\u0000\u0002\u001d\u0001\u0000\u0000\u0000"+
		"\u0004\u001f\u0001\u0000\u0000\u0000\u0006#\u0001\u0000\u0000\u0000\b"+
		"-\u0001\u0000\u0000\u0000\n7\u0001\u0000\u0000\u0000\fA\u0001\u0000\u0000"+
		"\u0000\u000eg\u0001\u0000\u0000\u0000\u0010w\u0001\u0000\u0000\u0000\u0012"+
		"\u0013\u0003\u0002\u0001\u0000\u0013\u0015\u0003\u0004\u0002\u0000\u0014"+
		"\u0016\u0003\u0006\u0003\u0000\u0015\u0014\u0001\u0000\u0000\u0000\u0015"+
		"\u0016\u0001\u0000\u0000\u0000\u0016\u0018\u0001\u0000\u0000\u0000\u0017"+
		"\u0019\u0003\b\u0004\u0000\u0018\u0017\u0001\u0000\u0000\u0000\u0018\u0019"+
		"\u0001\u0000\u0000\u0000\u0019\u001b\u0001\u0000\u0000\u0000\u001a\u001c"+
		"\u0003\n\u0005\u0000\u001b\u001a\u0001\u0000\u0000\u0000\u001b\u001c\u0001"+
		"\u0000\u0000\u0000\u001c\u0001\u0001\u0000\u0000\u0000\u001d\u001e\u0005"+
		"\u0001\u0000\u0000\u001e\u0003\u0001\u0000\u0000\u0000\u001f \u0005\u0002"+
		"\u0000\u0000 !\u0005\u000e\u0000\u0000!\"\u0003\f\u0006\u0000\"\u0005"+
		"\u0001\u0000\u0000\u0000#$\u0005\u0003\u0000\u0000$%\u0005\u000e\u0000"+
		"\u0000%*\u0003\f\u0006\u0000&\'\u0005\f\u0000\u0000\')\u0003\f\u0006\u0000"+
		"(&\u0001\u0000\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000"+
		"\u0000*+\u0001\u0000\u0000\u0000+\u0007\u0001\u0000\u0000\u0000,*\u0001"+
		"\u0000\u0000\u0000-.\u0005\u0004\u0000\u0000./\u0005\u000e\u0000\u0000"+
		"/4\u0003\u000e\u0007\u000001\u0005\f\u0000\u000013\u0003\u000e\u0007\u0000"+
		"20\u0001\u0000\u0000\u000036\u0001\u0000\u0000\u000042\u0001\u0000\u0000"+
		"\u000045\u0001\u0000\u0000\u00005\t\u0001\u0000\u0000\u000064\u0001\u0000"+
		"\u0000\u000078\u0005\u0005\u0000\u000089\u0005\u000e\u0000\u00009>\u0003"+
		"\u0010\b\u0000:;\u0005\f\u0000\u0000;=\u0003\u0010\b\u0000<:\u0001\u0000"+
		"\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000\u0000\u0000>?\u0001"+
		"\u0000\u0000\u0000?\u000b\u0001\u0000\u0000\u0000@>\u0001\u0000\u0000"+
		"\u0000AB\u0005\u0011\u0000\u0000B\r\u0001\u0000\u0000\u0000CD\u0005\u0006"+
		"\u0000\u0000DE\u0005\u000f\u0000\u0000EF\u0003\f\u0006\u0000FG\u0005\f"+
		"\u0000\u0000GH\u0003\f\u0006\u0000HI\u0005\u0010\u0000\u0000Ih\u0001\u0000"+
		"\u0000\u0000JK\u0005\u0007\u0000\u0000KL\u0005\u000f\u0000\u0000LM\u0003"+
		"\f\u0006\u0000MN\u0005\f\u0000\u0000NO\u0003\f\u0006\u0000OP\u0005\u0010"+
		"\u0000\u0000Ph\u0001\u0000\u0000\u0000QR\u0005\b\u0000\u0000RS\u0005\u000f"+
		"\u0000\u0000SV\u0003\f\u0006\u0000TU\u0005\f\u0000\u0000UW\u0003\f\u0006"+
		"\u0000VT\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000XV\u0001\u0000"+
		"\u0000\u0000XY\u0001\u0000\u0000\u0000YZ\u0001\u0000\u0000\u0000Z[\u0005"+
		"\u0010\u0000\u0000[h\u0001\u0000\u0000\u0000\\]\u0005\t\u0000\u0000]^"+
		"\u0005\u000f\u0000\u0000^a\u0003\f\u0006\u0000_`\u0005\f\u0000\u0000`"+
		"b\u0003\f\u0006\u0000a_\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000de\u0001\u0000\u0000"+
		"\u0000ef\u0005\u0010\u0000\u0000fh\u0001\u0000\u0000\u0000gC\u0001\u0000"+
		"\u0000\u0000gJ\u0001\u0000\u0000\u0000gQ\u0001\u0000\u0000\u0000g\\\u0001"+
		"\u0000\u0000\u0000h\u000f\u0001\u0000\u0000\u0000ij\u0005\n\u0000\u0000"+
		"jk\u0005\u000f\u0000\u0000kl\u0003\f\u0006\u0000lm\u0005\f\u0000\u0000"+
		"mn\u0003\f\u0006\u0000no\u0005\u0010\u0000\u0000ox\u0001\u0000\u0000\u0000"+
		"pq\u0005\u000b\u0000\u0000qr\u0005\u000f\u0000\u0000rs\u0003\f\u0006\u0000"+
		"st\u0005\f\u0000\u0000tu\u0003\f\u0006\u0000uv\u0005\u0010\u0000\u0000"+
		"vx\u0001\u0000\u0000\u0000wi\u0001\u0000\u0000\u0000wp\u0001\u0000\u0000"+
		"\u0000x\u0011\u0001\u0000\u0000\u0000\n\u0015\u0018\u001b*4>Xcgw";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}