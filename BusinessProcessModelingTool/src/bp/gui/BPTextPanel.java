package bp.gui;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import bp.event.TextChangeListener;
import bp.gui.format.Formatter;
import bp.model.util.BPKeyWords;
import bp.text.box.RootTextBox;

public class BPTextPanel extends JTextPane {

    /**
     * 
     */
    private static final long serialVersionUID = -4204064491415048407L;

    public static final Pattern FORMATTING_PATTERN;

    static {
        final StringBuilder sb = new StringBuilder();
        for (final String s : BPKeyWords.getKeyWords()) {
            sb.append("\\b");
            sb.append(s);
            sb.append("\\b");
            sb.append("|");
        }
        sb.delete(sb.length() - 1, sb.length());
        final String keywordPattern = sb.toString();
        final String valuePattern = "(\\b\\d+\\b)|((\".*?\")|(\".*\n?))";
        final String lineCommentPattern = "//.*[\n]?";
        final String commentPattern = "(?s)(/\\*(.)*?\\*/)|(/\\*.*\n?)";

        sb.delete(0, sb.length());
        sb.append("(");
        sb.append(keywordPattern);
        sb.append(")");
        sb.append("|");
        sb.append("(");
        sb.append(valuePattern);
        sb.append(")");
        sb.append("|");
        sb.append("(");
        sb.append(lineCommentPattern);
        sb.append(")");
        sb.append("|");
        sb.append("(");
        sb.append(commentPattern);
        sb.append(")");

        FORMATTING_PATTERN = Pattern.compile(sb.toString());
    }

    private StyledDocument doc = getStyledDocument();
    private MutableAttributeSet standardStyle;
    private MutableAttributeSet keywordStyle;
    private MutableAttributeSet valueStyle;
    private MutableAttributeSet commentStyle;

    private RootTextBox rootTextBox;

	public BPTextPanel() {
	}

	public BPTextPanel(RootTextBox rootTextBox) {
        initializeStyles();

        this.rootTextBox = rootTextBox;

        addTextChangedListener();
        addDocListener();

        try {
            getDocument().insertString(0, rootTextBox.generateText(), this.standardStyle);
        } catch (final BadLocationException e1) {
            e1.printStackTrace();
        }
    }
    
    public void setRootTextBox(RootTextBox rootTextBox) {
		this.rootTextBox = rootTextBox;
	}

	public StyledDocument getDoc() {
		return doc;
	}

	public void setDoc(StyledDocument doc) {
		this.doc = doc;
	}

	public MutableAttributeSet getStandardStyle() {
		return standardStyle;
	}

	public void setStandardStyle(MutableAttributeSet standardStyle) {
		this.standardStyle = standardStyle;
	}

	public MutableAttributeSet getKeywordStyle() {
		return keywordStyle;
	}

	public void setKeywordStyle(MutableAttributeSet keywordStyle) {
		this.keywordStyle = keywordStyle;
	}

	public MutableAttributeSet getValueStyle() {
		return valueStyle;
	}

	public void setValueStyle(MutableAttributeSet valueStyle) {
		this.valueStyle = valueStyle;
	}

	public MutableAttributeSet getCommentStyle() {
		return commentStyle;
	}

	public void setCommentStyle(MutableAttributeSet commentStyle) {
		this.commentStyle = commentStyle;
	}

	private void initializeStyles() {
        this.standardStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(this.standardStyle, Color.BLACK);
        StyleConstants.setFontFamily(this.standardStyle, "Consolas");
        StyleConstants.setFontSize(this.standardStyle, 11);

        this.keywordStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(this.keywordStyle, Color.BLUE);
        StyleConstants.setFontFamily(this.keywordStyle, "Consolas");
        StyleConstants.setFontSize(this.keywordStyle, 11);
        StyleConstants.setBold(this.keywordStyle, true);

        this.valueStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(this.valueStyle, new Color(178, 34, 34));
        StyleConstants.setFontFamily(this.valueStyle, "Consolas");
        StyleConstants.setFontSize(this.valueStyle, 11);

        this.commentStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(this.commentStyle, new Color(34, 139, 34));
        StyleConstants.setFontFamily(this.commentStyle, "Consolas");
        StyleConstants.setFontSize(this.commentStyle, 11);
    }

    private void addTextChangedListener() {
        getRootTextBox().addTextChangeListener(new TextChangeListener(getDocument()));
    }

    private void addDocListener() {
        getDocument().addDocumentListener(new DocumentListener() {
        	
            @Override
            public void removeUpdate(final DocumentEvent e) {
                new Thread(new Formatter(BPTextPanel.this)).start();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                new Thread(new Formatter(BPTextPanel.this)).start();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {

            }
        });
    }

    public RootTextBox getRootTextBox() {
        return this.rootTextBox;
    }

}
