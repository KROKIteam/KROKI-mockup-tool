package bp.gui.format;

import java.util.regex.Matcher;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import bp.gui.BPTextPanel;

public class Formatter implements Runnable {

	@Override
    public void run() {
        formatAll();
    }

    /**
     * Formats are returns new start position
     */
    private Integer format(final Integer start, final Integer end, final Integer oldStart, final AttributeSet style) {
        getDoc().setCharacterAttributes(start, end - start, style, false);
        getDoc().setCharacterAttributes(oldStart, start - oldStart, standardStyle(), false);
        return end;
    }

    private void formatAll() {
        try {
            final Matcher m = BPTextPanel.FORMATTING_PATTERN.matcher(getDoc().getText(0, getDoc().getLength()));
            Integer start = 0;
            while (m.find()) {
                if (m.group(8) != null) {
                    // comment
                    start = format(m.start(8), m.end(8), start, commentStyle());
                } else if (m.group(7) != null) {
                    // line comment
                    start = format(m.start(7), m.end(7), start, commentStyle());
                } else if (m.group(2) != null) {
                    // value
                    start = format(m.start(2), m.end(2), start, valueStyle());
                } else if (m.group(1) != null) {
                    // keyword
                    start = format(m.start(1), m.end(1), start, keywordStyle());
                }
            }
            getDoc().setCharacterAttributes(start, getDoc().getLength() - start, standardStyle(), false);
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    private BPTextPanel textPanel;
    
    public Formatter() { }
    
    public Formatter(BPTextPanel textPanel) {
    	this.textPanel = textPanel;
    }
    
    private StyledDocument getDoc() {
        return textPanel.getDoc();
    }

    private AttributeSet standardStyle() {
        return textPanel.getStandardStyle();
    }

    private AttributeSet keywordStyle() {
        return textPanel.getKeywordStyle();
    }

    private AttributeSet valueStyle() {
        return textPanel.getValueStyle();
    }

    private AttributeSet commentStyle() {
        return textPanel.getCommentStyle();
    }

	public BPTextPanel getTextPanel() {
		return textPanel;
	}

	public void setTextPanel(BPTextPanel textPanel) {
		this.textPanel = textPanel;
	}
    
}
