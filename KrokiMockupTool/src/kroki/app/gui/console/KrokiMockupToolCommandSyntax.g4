grammar KrokiMockupToolCommandSyntax;

@header{
	package kroki.app.gui.console;
}

// PARSER, sad cemo samo za make
makeCommand : (makeProject | makePackage | makeStdPanel | makeParentChildPanel | makeComponent | makeCombozoom | makeHierarchy)+;

makeProject : MAKEPROJECT '"' ELEMENTNAME '"' ;
makePackage : MAKEPACKAGE '"' ELEMENTNAME '"' IN ELEMENTNAME (('/' ELEMENTNAME)*)? '"' ;
makeStdPanel : MAKESTDPANEL '"' ELEMENTNAME '"' IN ELEMENTNAME ('/' ELEMENTNAME)+ (LBRAKET (COMPONENTS (';')?)+'}') ;
makeComponent : MAKECOMPONENT LBRAKET (COMPONENTS (';')?)+ '}' IN ELEMENTNAME ('/' ELEMENTNAME)+;
makeParentChildPanel : MAKEPARENTCHILDPANEL '"' ELEMENTNAME '"' IN ELEMENTNAME ('/' ELEMENTNAME)+ '"' (LBRAKET (HIERARCHYCOMPONENT (';')?)+ '}')?;
makeCombozoom : MAKECOMBOZOOM IN ELEMENTNAME ('/' ELEMENTNAME)+;
makeHierarchy : MAKEHIERARCHY IN ELEMENTNAME ('/' ELEMENTNAME)+;

// LEXER
NUMBER : '0'..'9'+ ;
LETTER : ('A'..'Z' | 'a'..'z')+ ;
WHITESPACE : [ \t\n\r]+ -> channel(HIDDEN) ; // za skipovanje space-a -> channel(HIDDEN) 
ELEMENTNAME : LETTER ( LETTER | NUMBER)* ;
MAKEPROJECT : 'make project';
MAKEPACKAGE : 'make package';
MAKESTDPANEL : 'make std-panel';
MAKEPARENTCHILDPANEL : 'make pc-panel';
MAKECOMBOZOOM : 'make combozoom';
MAKEHIERARCHY : 'make hierarchy';
MAKECOMPONENT : 'make component';
IN : ' in "';
LBRAKET : ' {';
TRUEFALSE : 'True' | 'False';
ORIENTATIONELEMENT : 'Horizontal' | 'Vertical' | 'Free' ;
ALIGNMENTELEMENT : 'Left' | 'Right' | 'Central' ;
TEXTFIELDDATATYPE : 'String' | 'Integer' | 'Long' | 'BigDecimal' | 'Date' ;
PERSISTENTTYPES : 'Char' | 'Varchar' | 'Text' | 'Integer' | 'Number' | 'Float' 
					| 'Decimal' | 'Boolean'  | 'Date' | 'Time' | 'DateTime' ;
DEFAULTVIEWMODES : 'TableView' | 'InputParameter' ;
DEFAULTOPERATIONMODES : 'Add' | 'Update' | 'Copy' | 'Search' | 'View' ; 
COLON : ':' ;
MINUS : '-';
COMMA : ',';
COMPONENTS : (PANELCOMPONENT | TEXTFIELDCOMPOENT | TEXTAREACOMPONENT | COMBOBOXCOMPONENT | CHECKBOXCOMPONENT
				| REPORTCOMPONENT | TRANSACTIONCOMPONENT | LINKCOMPONENT)+;
				
//da vidim da li je oke ovo da koristim  | za komponente elemenata
COMMONCOMPONENTATTRIBUTES : (('Visible' COLON TRUEFALSE)? | ('BackgroundColor' COLON ELEMENTNAME)? | ('ForegroundColor' COLON ELEMENTNAME)? 
							| ('FontColor' COLON ELEMENTNAME)? | ('DisplayFormat' COLON ELEMENTNAME)? | ('Mandatory' COLON TRUEFALSE)?
						    | ('Representative' COLON TRUEFALSE)? | ('AutoGo' COLON TRUEFALSE)? | ('Disabled' COLON TRUEFALSE)? 
						    | ('DefaultValue' COLON ELEMENTNAME)? | ('LabelToCode' COLON ELEMENTNAME)? | ('ColumnName' COLON ELEMENTNAME)? 
						    | ('PersistentType' COLON ELEMENTNAME)? | ('Length' COLON NUMBER)? | ('Precision' COLON NUMBER)?);

PANELCOMPONENT : 'panel' MINUS (('Label' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)? 
				| ('BackgroundColor' COLON ELEMENTNAME)? | ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)? 
				| ('BorderColor' COLON ELEMENTNAME)? | ('Orientation' COLON ELEMENTNAME)? | ('Alignment' COLON ELEMENTNAME)? )COMMA)+;
				
TEXTFIELDCOMPOENT :  'textfield' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | 'DataType' COLON ELEMENTNAME
					| 'TextFieldDataType' COLON ELEMENTNAME | (COMMONCOMPONENTATTRIBUTES)?)COMMA)+;

TEXTAREACOMPONENT : 'textarea' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | (COMMONCOMPONENTATTRIBUTES)? )COMMA)+;

COMBOBOXCOMPONENT : 'combobox' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | (COMMONCOMPONENTATTRIBUTES)? 
					| (('Values' COLON ELEMENTNAME)*)?)COMMA)+;

CHECKBOXCOMPONENT : 'checkbox' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | (COMMONCOMPONENTATTRIBUTES)?)COMMA)+;

REPORTCOMPONENT : 'report' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)? 
					| ('BackgroundColor' COLON ELEMENTNAME)? | ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)? 
					| ('HasParametersForm' COLON TRUEFALSE)? | ('FilteredByKey' COLON TRUEFALSE)? | ('PersistentOperation' COLON ELEMENTNAME)? 
					| ('RefreshNow' COLON TRUEFALSE)? | ('RefreshAll' COLON TRUEFALSE)? | ('AskConfirmation' COLON TRUEFALSE)? 
					| ('ShowErrors' COLON TRUEFALSE)? | ('ConfirmationMessage' COLON ELEMENTNAME)?)COMMA)+;
					
TRANSACTIONCOMPONENT : 'transaction' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)?
					| ('BackgroundColor' COLON ELEMENTNAME)? | ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)?
					| ('HasParametersForm' COLON TRUEFALSE)? | ('FilteredByKey' COLON TRUEFALSE)? | ('PersistentOperation' COLON ELEMENTNAME)? 
					| ('RefreshNow' COLON TRUEFALSE)? | ('RefreshAll' COLON TRUEFALSE)? | ('AskConfirmation' COLON TRUEFALSE)? 
					| ('ShowErrors' COLON TRUEFALSE)? | ('ConfirmationMessage' COLON ELEMENTNAME)?)COMMA)+;
					
LINKCOMPONENT : 'link' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)? | ('BackgroundColor' COLON ELEMENTNAME)? 
	|				| ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)? | ('Add' COLON TRUEFALSE)? | ('Update' COLON TRUEFALSE)?
					| ('Copy' COLON TRUEFALSE)? | ('Delete' COLON TRUEFALSE)? | ('Search' COLON TRUEFALSE)? | ('ChangeMode' COLON TRUEFALSE)? 
					| ('DataNavigation' COLON TRUEFALSE)? | ('Mandatory' COLON TRUEFALSE)? | ('DefaultViewMode' COLON DEFAULTVIEWMODES)?
					| ('DefaultOperationMode' COLON DEFAULTOPERATIONMODES)? | ('ConfirmDelete' COLON TRUEFALSE)? | ('StayInAddMode' COLON TRUEFALSE)?
					| ('GoToLastAdded' COLON TRUEFALSE)? | ('DataFilter' COLON ELEMENTNAME)? | ('SortBy' COLON ELEMENTNAME)? | ('TargetPanel' COLON ELEMENTNAME)?
					| ('TargetZoom' COLON ELEMENTNAME)? | ('Position' COLON NUMBER)? | ('AutoActivate' COLON TRUEFALSE)? 
					| ('DisplayRepresentative' COLON TRUEFALSE)? | ('DisplayIdentifier' COLON TRUEFALSE)?)COMMA)+;
					
HIERARCHYCOMPONENT : 'hierarchy' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)? | ('BackgroundColor' COLON ELEMENTNAME)? 
					| ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)? | ('Add' COLON TRUEFALSE)? | ('Update' COLON TRUEFALSE)?
					| ('Copy' COLON TRUEFALSE)? | ('Delete' COLON TRUEFALSE)? | ('Search' COLON TRUEFALSE)? | ('ChangeMode' COLON TRUEFALSE)? 
					| ('DataNavigation' COLON TRUEFALSE)? | ('Mandatory' COLON TRUEFALSE)? | ('DefaultViewMode' COLON DEFAULTVIEWMODES)?
					| ('DefaultOperationMode' COLON DEFAULTOPERATIONMODES)? | ('ConfirmDelete' COLON TRUEFALSE)? | ('StayInAddMode' COLON TRUEFALSE)?
					| ('GoToLastAdded' COLON TRUEFALSE)? | ('DataFilter' COLON ELEMENTNAME)? | ('SortBy' COLON ELEMENTNAME)? | ('ActivationPanel' COLON ELEMENTNAME)?
					| ('TargetPanel' COLON ELEMENTNAME)? | ('AppliedToPanel' COLON ELEMENTNAME)? | ('HierarchyPanel' COLON ELEMENTNAME)? )COMMA)+;
					
COMBOZOOMCOMPONENT : 'combozoom' MINUS (('Label' COLON ELEMENTNAME | 'ElementName' COLON ELEMENTNAME | ('Visible' COLON TRUEFALSE)? | ('BackgroundColor' COLON ELEMENTNAME)? 
					| ('ForegroundColor' COLON ELEMENTNAME)? | ('FontColor' COLON ELEMENTNAME)? | ('Add' COLON TRUEFALSE)? | ('Update' COLON TRUEFALSE)?
					| ('Copy' COLON TRUEFALSE)? | ('Delete' COLON TRUEFALSE)? | ('Search' COLON TRUEFALSE)? | ('ChangeMode' COLON TRUEFALSE)? 
					| ('DataNavigation' COLON TRUEFALSE)? | ('Mandatory' COLON TRUEFALSE)? | ('DefaultViewMode' COLON DEFAULTVIEWMODES)?
					| ('DefaultOperationMode' COLON DEFAULTOPERATIONMODES)? | ('ConfirmDelete' COLON TRUEFALSE)? | ('StayInAddMode' COLON TRUEFALSE)?
					| ('GoToLastAdded' COLON TRUEFALSE)? | ('DataFilter' COLON ELEMENTNAME)? | ('SortBy' COLON ELEMENTNAME)? | ('ActivationPanel' COLON ELEMENTNAME)?
					| ('TargetPanel' COLON ELEMENTNAME)?)COMMA)+;