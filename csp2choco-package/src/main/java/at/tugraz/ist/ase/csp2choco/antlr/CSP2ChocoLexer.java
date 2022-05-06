/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// Generated from /Users/manleviet/Development/GitHub/CSP2ChocoTranslator/src/main/java/at/tugraz/ist/ase/csp2choco/antlr/CSP2Choco.g4 by ANTLR 4.10.1
package at.tugraz.ist.ase.csp2choco.antlr;


import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CSP2ChocoLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, CONSTRAINT=2, REQUIREMENT=3, CM=4, SC=5, LP=6, RP=7, MUL=8, DIV=9, 
		ADD=10, SUB=11, AND=12, OR=13, EQU=14, NEQ=15, GRT=16, LES=17, GRE=18, 
		LEE=19, IMP=20, IDENTIFIER=21, COMMENT=22, WS=23, INT_CONST=24;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "CONSTRAINT", "REQUIREMENT", "CM", "SC", "LP", "RP", "MUL", "DIV", 
			"ADD", "SUB", "AND", "OR", "EQU", "NEQ", "GRT", "LES", "GRE", "LEE", 
			"IMP", "IDENTIFIER", "COMMENT", "WS", "INT_CONST", "NEGATIVE", "NUMBER", 
			"LETTER", "DIGIT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'!'", "'constraint'", "'requirement'", "','", "';'", "'('", "')'", 
			"'*'", "'/'", "'+'", "'-'", "'&&'", "'||'", "'=='", "'!='", "'>'", "'<'", 
			"'>='", "'<='", "'->'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "CONSTRAINT", "REQUIREMENT", "CM", "SC", "LP", "RP", "MUL", 
			"DIV", "ADD", "SUB", "AND", "OR", "EQU", "NEQ", "GRT", "LES", "GRE", 
			"LEE", "IMP", "IDENTIFIER", "COMMENT", "WS", "INT_CONST"
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


	public CSP2ChocoLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CSP2Choco.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0018\u00a9\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0005\u0014\u007f\b\u0014\n\u0014\f\u0014\u0082\t\u0014\u0001"+
		"\u0015\u0001\u0015\u0005\u0015\u0086\b\u0015\n\u0015\f\u0015\u0089\t\u0015"+
		"\u0001\u0015\u0003\u0015\u008c\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017"+
		"\u0001\u0017\u0001\u0017\u0001\u0018\u0003\u0018\u009a\b\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0005\u0019\u009f\b\u0019\n\u0019\f\u0019\u00a2"+
		"\t\u0019\u0003\u0019\u00a4\b\u0019\u0001\u001a\u0001\u001a\u0001\u001b"+
		"\u0001\u001b\u0000\u0000\u001c\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013"+
		"\'\u0014)\u0015+\u0016-\u0017/\u00181\u00003\u00005\u00007\u0000\u0001"+
		"\u0000\u0005\u0002\u0000\n\n\r\r\u0003\u0000\t\n\r\r  \u0001\u000019\u0003"+
		"\u0000AZ__az\u0001\u000009\u00ab\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000"+
		"\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-"+
		"\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00019\u0001\u0000"+
		"\u0000\u0000\u0003;\u0001\u0000\u0000\u0000\u0005F\u0001\u0000\u0000\u0000"+
		"\u0007R\u0001\u0000\u0000\u0000\tT\u0001\u0000\u0000\u0000\u000bV\u0001"+
		"\u0000\u0000\u0000\rX\u0001\u0000\u0000\u0000\u000fZ\u0001\u0000\u0000"+
		"\u0000\u0011\\\u0001\u0000\u0000\u0000\u0013^\u0001\u0000\u0000\u0000"+
		"\u0015`\u0001\u0000\u0000\u0000\u0017b\u0001\u0000\u0000\u0000\u0019e"+
		"\u0001\u0000\u0000\u0000\u001bh\u0001\u0000\u0000\u0000\u001dk\u0001\u0000"+
		"\u0000\u0000\u001fn\u0001\u0000\u0000\u0000!p\u0001\u0000\u0000\u0000"+
		"#r\u0001\u0000\u0000\u0000%u\u0001\u0000\u0000\u0000\'x\u0001\u0000\u0000"+
		"\u0000){\u0001\u0000\u0000\u0000+\u0083\u0001\u0000\u0000\u0000-\u0091"+
		"\u0001\u0000\u0000\u0000/\u0095\u0001\u0000\u0000\u00001\u0099\u0001\u0000"+
		"\u0000\u00003\u00a3\u0001\u0000\u0000\u00005\u00a5\u0001\u0000\u0000\u0000"+
		"7\u00a7\u0001\u0000\u0000\u00009:\u0005!\u0000\u0000:\u0002\u0001\u0000"+
		"\u0000\u0000;<\u0005c\u0000\u0000<=\u0005o\u0000\u0000=>\u0005n\u0000"+
		"\u0000>?\u0005s\u0000\u0000?@\u0005t\u0000\u0000@A\u0005r\u0000\u0000"+
		"AB\u0005a\u0000\u0000BC\u0005i\u0000\u0000CD\u0005n\u0000\u0000DE\u0005"+
		"t\u0000\u0000E\u0004\u0001\u0000\u0000\u0000FG\u0005r\u0000\u0000GH\u0005"+
		"e\u0000\u0000HI\u0005q\u0000\u0000IJ\u0005u\u0000\u0000JK\u0005i\u0000"+
		"\u0000KL\u0005r\u0000\u0000LM\u0005e\u0000\u0000MN\u0005m\u0000\u0000"+
		"NO\u0005e\u0000\u0000OP\u0005n\u0000\u0000PQ\u0005t\u0000\u0000Q\u0006"+
		"\u0001\u0000\u0000\u0000RS\u0005,\u0000\u0000S\b\u0001\u0000\u0000\u0000"+
		"TU\u0005;\u0000\u0000U\n\u0001\u0000\u0000\u0000VW\u0005(\u0000\u0000"+
		"W\f\u0001\u0000\u0000\u0000XY\u0005)\u0000\u0000Y\u000e\u0001\u0000\u0000"+
		"\u0000Z[\u0005*\u0000\u0000[\u0010\u0001\u0000\u0000\u0000\\]\u0005/\u0000"+
		"\u0000]\u0012\u0001\u0000\u0000\u0000^_\u0005+\u0000\u0000_\u0014\u0001"+
		"\u0000\u0000\u0000`a\u0005-\u0000\u0000a\u0016\u0001\u0000\u0000\u0000"+
		"bc\u0005&\u0000\u0000cd\u0005&\u0000\u0000d\u0018\u0001\u0000\u0000\u0000"+
		"ef\u0005|\u0000\u0000fg\u0005|\u0000\u0000g\u001a\u0001\u0000\u0000\u0000"+
		"hi\u0005=\u0000\u0000ij\u0005=\u0000\u0000j\u001c\u0001\u0000\u0000\u0000"+
		"kl\u0005!\u0000\u0000lm\u0005=\u0000\u0000m\u001e\u0001\u0000\u0000\u0000"+
		"no\u0005>\u0000\u0000o \u0001\u0000\u0000\u0000pq\u0005<\u0000\u0000q"+
		"\"\u0001\u0000\u0000\u0000rs\u0005>\u0000\u0000st\u0005=\u0000\u0000t"+
		"$\u0001\u0000\u0000\u0000uv\u0005<\u0000\u0000vw\u0005=\u0000\u0000w&"+
		"\u0001\u0000\u0000\u0000xy\u0005-\u0000\u0000yz\u0005>\u0000\u0000z(\u0001"+
		"\u0000\u0000\u0000{\u0080\u00035\u001a\u0000|\u007f\u00035\u001a\u0000"+
		"}\u007f\u00037\u001b\u0000~|\u0001\u0000\u0000\u0000~}\u0001\u0000\u0000"+
		"\u0000\u007f\u0082\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000\u0000"+
		"\u0080\u0081\u0001\u0000\u0000\u0000\u0081*\u0001\u0000\u0000\u0000\u0082"+
		"\u0080\u0001\u0000\u0000\u0000\u0083\u0087\u0005%\u0000\u0000\u0084\u0086"+
		"\b\u0000\u0000\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0086\u0089\u0001"+
		"\u0000\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001"+
		"\u0000\u0000\u0000\u0088\u008b\u0001\u0000\u0000\u0000\u0089\u0087\u0001"+
		"\u0000\u0000\u0000\u008a\u008c\u0005\r\u0000\u0000\u008b\u008a\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000"+
		"\u0000\u0000\u008d\u008e\u0005\n\u0000\u0000\u008e\u008f\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0006\u0015\u0000\u0000\u0090,\u0001\u0000\u0000\u0000"+
		"\u0091\u0092\u0007\u0001\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000"+
		"\u0093\u0094\u0006\u0016\u0000\u0000\u0094.\u0001\u0000\u0000\u0000\u0095"+
		"\u0096\u00031\u0018\u0000\u0096\u0097\u00033\u0019\u0000\u00970\u0001"+
		"\u0000\u0000\u0000\u0098\u009a\u0005-\u0000\u0000\u0099\u0098\u0001\u0000"+
		"\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a2\u0001\u0000\u0000"+
		"\u0000\u009b\u00a4\u00050\u0000\u0000\u009c\u00a0\u0007\u0002\u0000\u0000"+
		"\u009d\u009f\u00037\u001b\u0000\u009e\u009d\u0001\u0000\u0000\u0000\u009f"+
		"\u00a2\u0001\u0000\u0000\u0000\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0"+
		"\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a4\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a0\u0001\u0000\u0000\u0000\u00a3\u009b\u0001\u0000\u0000\u0000\u00a3"+
		"\u009c\u0001\u0000\u0000\u0000\u00a44\u0001\u0000\u0000\u0000\u00a5\u00a6"+
		"\u0007\u0003\u0000\u0000\u00a66\u0001\u0000\u0000\u0000\u00a7\u00a8\u0007"+
		"\u0004\u0000\u0000\u00a88\u0001\u0000\u0000\u0000\b\u0000~\u0080\u0087"+
		"\u008b\u0099\u00a0\u00a3\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}