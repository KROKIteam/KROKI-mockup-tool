// Generated from KrokiMockupToolCommandSyntax.g4 by ANTLR 4.4

	package kroki.app.gui.console;

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KrokiMockupToolCommandSyntaxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__3=1, T__2=2, T__1=3, T__0=4, NUMBER=5, LETTER=6, WHITESPACE=7, ELEMENTNAME=8, 
		MAKEPROJECT=9, MAKEPACKAGE=10, MAKESTDPANEL=11, MAKEPARENTCHILDPANEL=12, 
		MAKECOMBOZOOM=13, MAKEHIERARCHY=14, MAKECOMPONENT=15, IN=16, LBRAKET=17, 
		TRUEFALSE=18, ORIENTATIONELEMENT=19, ALIGNMENTELEMENT=20, TEXTFIELDDATATYPE=21, 
		PERSISTENTTYPES=22, DEFAULTVIEWMODES=23, DEFAULTOPERATIONMODES=24, COLON=25, 
		MINUS=26, COMMA=27, COMPONENTS=28, COMMONCOMPONENTATTRIBUTES=29, PANELCOMPONENT=30, 
		TEXTFIELDCOMPOENT=31, TEXTAREACOMPONENT=32, COMBOBOXCOMPONENT=33, CHECKBOXCOMPONENT=34, 
		REPORTCOMPONENT=35, TRANSACTIONCOMPONENT=36, LINKCOMPONENT=37, HIERARCHYCOMPONENT=38, 
		COMBOZOOMCOMPONENT=39;
	public static final String[] tokenNames = {
		"<INVALID>", "'/'", "'\"'", "';'", "'}'", "NUMBER", "LETTER", "WHITESPACE", 
		"ELEMENTNAME", "'make project'", "'make package'", "'make std-panel'", 
		"'make pc-panel'", "'make combozoom'", "'make hierarchy'", "'make component'", 
		"' in \"'", "' {'", "TRUEFALSE", "ORIENTATIONELEMENT", "ALIGNMENTELEMENT", 
		"TEXTFIELDDATATYPE", "PERSISTENTTYPES", "DEFAULTVIEWMODES", "DEFAULTOPERATIONMODES", 
		"':'", "'-'", "','", "COMPONENTS", "COMMONCOMPONENTATTRIBUTES", "PANELCOMPONENT", 
		"TEXTFIELDCOMPOENT", "TEXTAREACOMPONENT", "COMBOBOXCOMPONENT", "CHECKBOXCOMPONENT", 
		"REPORTCOMPONENT", "TRANSACTIONCOMPONENT", "LINKCOMPONENT", "HIERARCHYCOMPONENT", 
		"COMBOZOOMCOMPONENT"
	};
	public static final int
		RULE_makeCommand = 0, RULE_makeProject = 1, RULE_makePackage = 2, RULE_makeStdPanel = 3, 
		RULE_makeComponent = 4, RULE_makeParentChildPanel = 5, RULE_makeCombozoom = 6, 
		RULE_makeHierarchy = 7;
	public static final String[] ruleNames = {
		"makeCommand", "makeProject", "makePackage", "makeStdPanel", "makeComponent", 
		"makeParentChildPanel", "makeCombozoom", "makeHierarchy"
	};

	@Override
	public String getGrammarFileName() { return "KrokiMockupToolCommandSyntax.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KrokiMockupToolCommandSyntaxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class MakeCommandContext extends ParserRuleContext {
		public List<MakePackageContext> makePackage() {
			return getRuleContexts(MakePackageContext.class);
		}
		public MakePackageContext makePackage(int i) {
			return getRuleContext(MakePackageContext.class,i);
		}
		public MakeHierarchyContext makeHierarchy(int i) {
			return getRuleContext(MakeHierarchyContext.class,i);
		}
		public List<MakeCombozoomContext> makeCombozoom() {
			return getRuleContexts(MakeCombozoomContext.class);
		}
		public List<MakeStdPanelContext> makeStdPanel() {
			return getRuleContexts(MakeStdPanelContext.class);
		}
		public MakeCombozoomContext makeCombozoom(int i) {
			return getRuleContext(MakeCombozoomContext.class,i);
		}
		public List<MakeProjectContext> makeProject() {
			return getRuleContexts(MakeProjectContext.class);
		}
		public MakeParentChildPanelContext makeParentChildPanel(int i) {
			return getRuleContext(MakeParentChildPanelContext.class,i);
		}
		public MakeProjectContext makeProject(int i) {
			return getRuleContext(MakeProjectContext.class,i);
		}
		public MakeStdPanelContext makeStdPanel(int i) {
			return getRuleContext(MakeStdPanelContext.class,i);
		}
		public MakeComponentContext makeComponent(int i) {
			return getRuleContext(MakeComponentContext.class,i);
		}
		public List<MakeParentChildPanelContext> makeParentChildPanel() {
			return getRuleContexts(MakeParentChildPanelContext.class);
		}
		public List<MakeComponentContext> makeComponent() {
			return getRuleContexts(MakeComponentContext.class);
		}
		public List<MakeHierarchyContext> makeHierarchy() {
			return getRuleContexts(MakeHierarchyContext.class);
		}
		public MakeCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeCommand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeCommand(this);
		}
	}

	public final MakeCommandContext makeCommand() throws RecognitionException {
		MakeCommandContext _localctx = new MakeCommandContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_makeCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(23);
				switch (_input.LA(1)) {
				case MAKEPROJECT:
					{
					setState(16); makeProject();
					}
					break;
				case MAKEPACKAGE:
					{
					setState(17); makePackage();
					}
					break;
				case MAKESTDPANEL:
					{
					setState(18); makeStdPanel();
					}
					break;
				case MAKEPARENTCHILDPANEL:
					{
					setState(19); makeParentChildPanel();
					}
					break;
				case MAKECOMPONENT:
					{
					setState(20); makeComponent();
					}
					break;
				case MAKECOMBOZOOM:
					{
					setState(21); makeCombozoom();
					}
					break;
				case MAKEHIERARCHY:
					{
					setState(22); makeHierarchy();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(25); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MAKEPROJECT) | (1L << MAKEPACKAGE) | (1L << MAKESTDPANEL) | (1L << MAKEPARENTCHILDPANEL) | (1L << MAKECOMBOZOOM) | (1L << MAKEHIERARCHY) | (1L << MAKECOMPONENT))) != 0) );
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

	public static class MakeProjectContext extends ParserRuleContext {
		public TerminalNode ELEMENTNAME() { return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, 0); }
		public TerminalNode MAKEPROJECT() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKEPROJECT, 0); }
		public MakeProjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeProject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeProject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeProject(this);
		}
	}

	public final MakeProjectContext makeProject() throws RecognitionException {
		MakeProjectContext _localctx = new MakeProjectContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_makeProject);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27); match(MAKEPROJECT);
			setState(28); match(T__2);
			setState(29); match(ELEMENTNAME);
			setState(30); match(T__2);
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

	public static class MakePackageContext extends ParserRuleContext {
		public TerminalNode MAKEPACKAGE() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKEPACKAGE, 0); }
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public MakePackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makePackage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakePackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakePackage(this);
		}
	}

	public final MakePackageContext makePackage() throws RecognitionException {
		MakePackageContext _localctx = new MakePackageContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_makePackage);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32); match(MAKEPACKAGE);
			setState(33); match(T__2);
			setState(34); match(ELEMENTNAME);
			setState(35); match(T__2);
			setState(36); match(IN);
			setState(37); match(ELEMENTNAME);
			setState(45);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(38); match(T__3);
					setState(39); match(ELEMENTNAME);
					}
					}
					setState(44);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
			setState(47); match(T__2);
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

	public static class MakeStdPanelContext extends ParserRuleContext {
		public TerminalNode MAKESTDPANEL() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKESTDPANEL, 0); }
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode LBRAKET() { return getToken(KrokiMockupToolCommandSyntaxParser.LBRAKET, 0); }
		public TerminalNode COMPONENTS(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.COMPONENTS, i);
		}
		public List<TerminalNode> COMPONENTS() { return getTokens(KrokiMockupToolCommandSyntaxParser.COMPONENTS); }
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public MakeStdPanelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeStdPanel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeStdPanel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeStdPanel(this);
		}
	}

	public final MakeStdPanelContext makeStdPanel() throws RecognitionException {
		MakeStdPanelContext _localctx = new MakeStdPanelContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_makeStdPanel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49); match(MAKESTDPANEL);
			setState(50); match(T__2);
			setState(51); match(ELEMENTNAME);
			setState(52); match(T__2);
			setState(53); match(IN);
			setState(54); match(ELEMENTNAME);
			setState(57); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(55); match(T__3);
				setState(56); match(ELEMENTNAME);
				}
				}
				setState(59); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
			{
			setState(61); match(LBRAKET);
			setState(66); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(62); match(COMPONENTS);
				setState(64);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(63); match(T__1);
					}
				}

				}
				}
				setState(68); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMPONENTS );
			setState(70); match(T__0);
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

	public static class MakeComponentContext extends ParserRuleContext {
		public TerminalNode MAKECOMPONENT() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKECOMPONENT, 0); }
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode LBRAKET() { return getToken(KrokiMockupToolCommandSyntaxParser.LBRAKET, 0); }
		public TerminalNode COMPONENTS(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.COMPONENTS, i);
		}
		public List<TerminalNode> COMPONENTS() { return getTokens(KrokiMockupToolCommandSyntaxParser.COMPONENTS); }
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public MakeComponentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeComponent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeComponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeComponent(this);
		}
	}

	public final MakeComponentContext makeComponent() throws RecognitionException {
		MakeComponentContext _localctx = new MakeComponentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_makeComponent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); match(MAKECOMPONENT);
			setState(73); match(LBRAKET);
			setState(78); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(74); match(COMPONENTS);
				setState(76);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(75); match(T__1);
					}
				}

				}
				}
				setState(80); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMPONENTS );
			setState(82); match(T__0);
			setState(83); match(IN);
			setState(84); match(ELEMENTNAME);
			setState(87); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(85); match(T__3);
				setState(86); match(ELEMENTNAME);
				}
				}
				setState(89); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
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

	public static class MakeParentChildPanelContext extends ParserRuleContext {
		public TerminalNode MAKEPARENTCHILDPANEL() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKEPARENTCHILDPANEL, 0); }
		public List<TerminalNode> HIERARCHYCOMPONENT() { return getTokens(KrokiMockupToolCommandSyntaxParser.HIERARCHYCOMPONENT); }
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode LBRAKET() { return getToken(KrokiMockupToolCommandSyntaxParser.LBRAKET, 0); }
		public TerminalNode HIERARCHYCOMPONENT(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.HIERARCHYCOMPONENT, i);
		}
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public MakeParentChildPanelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeParentChildPanel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeParentChildPanel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeParentChildPanel(this);
		}
	}

	public final MakeParentChildPanelContext makeParentChildPanel() throws RecognitionException {
		MakeParentChildPanelContext _localctx = new MakeParentChildPanelContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_makeParentChildPanel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91); match(MAKEPARENTCHILDPANEL);
			setState(92); match(T__2);
			setState(93); match(ELEMENTNAME);
			setState(94); match(T__2);
			setState(95); match(IN);
			setState(96); match(ELEMENTNAME);
			setState(99); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(97); match(T__3);
				setState(98); match(ELEMENTNAME);
				}
				}
				setState(101); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
			setState(103); match(T__2);
			setState(114);
			_la = _input.LA(1);
			if (_la==LBRAKET) {
				{
				setState(104); match(LBRAKET);
				setState(109); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(105); match(HIERARCHYCOMPONENT);
					setState(107);
					_la = _input.LA(1);
					if (_la==T__1) {
						{
						setState(106); match(T__1);
						}
					}

					}
					}
					setState(111); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==HIERARCHYCOMPONENT );
				setState(113); match(T__0);
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

	public static class MakeCombozoomContext extends ParserRuleContext {
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public TerminalNode MAKECOMBOZOOM() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKECOMBOZOOM, 0); }
		public MakeCombozoomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeCombozoom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeCombozoom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeCombozoom(this);
		}
	}

	public final MakeCombozoomContext makeCombozoom() throws RecognitionException {
		MakeCombozoomContext _localctx = new MakeCombozoomContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_makeCombozoom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116); match(MAKECOMBOZOOM);
			setState(117); match(IN);
			setState(118); match(ELEMENTNAME);
			setState(121); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(119); match(T__3);
				setState(120); match(ELEMENTNAME);
				}
				}
				setState(123); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
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

	public static class MakeHierarchyContext extends ParserRuleContext {
		public List<TerminalNode> ELEMENTNAME() { return getTokens(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME); }
		public TerminalNode ELEMENTNAME(int i) {
			return getToken(KrokiMockupToolCommandSyntaxParser.ELEMENTNAME, i);
		}
		public TerminalNode MAKEHIERARCHY() { return getToken(KrokiMockupToolCommandSyntaxParser.MAKEHIERARCHY, 0); }
		public TerminalNode IN() { return getToken(KrokiMockupToolCommandSyntaxParser.IN, 0); }
		public MakeHierarchyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_makeHierarchy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).enterMakeHierarchy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KrokiMockupToolCommandSyntaxListener ) ((KrokiMockupToolCommandSyntaxListener)listener).exitMakeHierarchy(this);
		}
	}

	public final MakeHierarchyContext makeHierarchy() throws RecognitionException {
		MakeHierarchyContext _localctx = new MakeHierarchyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_makeHierarchy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125); match(MAKEHIERARCHY);
			setState(126); match(IN);
			setState(127); match(ELEMENTNAME);
			setState(130); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(128); match(T__3);
				setState(129); match(ELEMENTNAME);
				}
				}
				setState(132); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__3 );
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3)\u0089\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\6\2\32\n\2\r\2\16\2\33\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\7\4+\n\4\f\4\16\4.\13\4\5\4\60\n\4\3\4\3\4\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\6\5<\n\5\r\5\16\5=\3\5\3\5\3\5\5\5C\n\5\6\5E"+
		"\n\5\r\5\16\5F\3\5\3\5\3\6\3\6\3\6\3\6\5\6O\n\6\6\6Q\n\6\r\6\16\6R\3\6"+
		"\3\6\3\6\3\6\3\6\6\6Z\n\6\r\6\16\6[\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\6"+
		"\7f\n\7\r\7\16\7g\3\7\3\7\3\7\3\7\5\7n\n\7\6\7p\n\7\r\7\16\7q\3\7\5\7"+
		"u\n\7\3\b\3\b\3\b\3\b\3\b\6\b|\n\b\r\b\16\b}\3\t\3\t\3\t\3\t\3\t\6\t\u0085"+
		"\n\t\r\t\16\t\u0086\3\t\2\2\n\2\4\6\b\n\f\16\20\2\2\u0095\2\31\3\2\2\2"+
		"\4\35\3\2\2\2\6\"\3\2\2\2\b\63\3\2\2\2\nJ\3\2\2\2\f]\3\2\2\2\16v\3\2\2"+
		"\2\20\177\3\2\2\2\22\32\5\4\3\2\23\32\5\6\4\2\24\32\5\b\5\2\25\32\5\f"+
		"\7\2\26\32\5\n\6\2\27\32\5\16\b\2\30\32\5\20\t\2\31\22\3\2\2\2\31\23\3"+
		"\2\2\2\31\24\3\2\2\2\31\25\3\2\2\2\31\26\3\2\2\2\31\27\3\2\2\2\31\30\3"+
		"\2\2\2\32\33\3\2\2\2\33\31\3\2\2\2\33\34\3\2\2\2\34\3\3\2\2\2\35\36\7"+
		"\13\2\2\36\37\7\4\2\2\37 \7\n\2\2 !\7\4\2\2!\5\3\2\2\2\"#\7\f\2\2#$\7"+
		"\4\2\2$%\7\n\2\2%&\7\4\2\2&\'\7\22\2\2\'/\7\n\2\2()\7\3\2\2)+\7\n\2\2"+
		"*(\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2\2-\60\3\2\2\2.,\3\2\2\2/,\3\2\2"+
		"\2/\60\3\2\2\2\60\61\3\2\2\2\61\62\7\4\2\2\62\7\3\2\2\2\63\64\7\r\2\2"+
		"\64\65\7\4\2\2\65\66\7\n\2\2\66\67\7\4\2\2\678\7\22\2\28;\7\n\2\29:\7"+
		"\3\2\2:<\7\n\2\2;9\3\2\2\2<=\3\2\2\2=;\3\2\2\2=>\3\2\2\2>?\3\2\2\2?D\7"+
		"\23\2\2@B\7\36\2\2AC\7\5\2\2BA\3\2\2\2BC\3\2\2\2CE\3\2\2\2D@\3\2\2\2E"+
		"F\3\2\2\2FD\3\2\2\2FG\3\2\2\2GH\3\2\2\2HI\7\6\2\2I\t\3\2\2\2JK\7\21\2"+
		"\2KP\7\23\2\2LN\7\36\2\2MO\7\5\2\2NM\3\2\2\2NO\3\2\2\2OQ\3\2\2\2PL\3\2"+
		"\2\2QR\3\2\2\2RP\3\2\2\2RS\3\2\2\2ST\3\2\2\2TU\7\6\2\2UV\7\22\2\2VY\7"+
		"\n\2\2WX\7\3\2\2XZ\7\n\2\2YW\3\2\2\2Z[\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\"+
		"\13\3\2\2\2]^\7\16\2\2^_\7\4\2\2_`\7\n\2\2`a\7\4\2\2ab\7\22\2\2be\7\n"+
		"\2\2cd\7\3\2\2df\7\n\2\2ec\3\2\2\2fg\3\2\2\2ge\3\2\2\2gh\3\2\2\2hi\3\2"+
		"\2\2it\7\4\2\2jo\7\23\2\2km\7(\2\2ln\7\5\2\2ml\3\2\2\2mn\3\2\2\2np\3\2"+
		"\2\2ok\3\2\2\2pq\3\2\2\2qo\3\2\2\2qr\3\2\2\2rs\3\2\2\2su\7\6\2\2tj\3\2"+
		"\2\2tu\3\2\2\2u\r\3\2\2\2vw\7\17\2\2wx\7\22\2\2x{\7\n\2\2yz\7\3\2\2z|"+
		"\7\n\2\2{y\3\2\2\2|}\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\17\3\2\2\2\177\u0080"+
		"\7\20\2\2\u0080\u0081\7\22\2\2\u0081\u0084\7\n\2\2\u0082\u0083\7\3\2\2"+
		"\u0083\u0085\7\n\2\2\u0084\u0082\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084"+
		"\3\2\2\2\u0086\u0087\3\2\2\2\u0087\21\3\2\2\2\22\31\33,/=BFNR[gmqt}\u0086";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}