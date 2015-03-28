package kroki.app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import kroki.app.action.style.SetBgColorAction;
import kroki.app.action.style.SetFgColorAction;
import kroki.app.utils.ImageResource;
import kroki.app.utils.StringResource;
import net.miginfocom.swing.MigLayout;

/**
 * Component which represents a color palette. If the set of colors offered by the palette isn't sufficient.
 * it contains a button for opening <code>JColorChooser</code> panel.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ColorPalette extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**Component's foreground color i.e. text color*/ 
    public static final int FG_COLOR = 0;
    /**Compoenent's background color*/
    public static final int BG_COLOR = 1;
    /**<code>ColorPalette.FG_COLOR</code> or <code>ColorPalette.BG_COLOR</code>*/
    private int what;
    /**Width of the image which represent a color on the palette*/
    private static final int width = 15;
    /**Height of the image which represent a color on the palette*/
    private static final int height = 15;
    /**Number of tones of primary colors*/
    private static final int tones = 4;
    /**List containing all colors*/
    private List<Color[]> colorList = new ArrayList<Color[]>();
    /**List of black tones*/
    private Color[] blackTones = new Color[tones];
    /**List of white tones*/
    private Color[] whiteTones = new Color[tones];
    /**List of red tones*/
    private Color[] redTons = new Color[tones];
    /**List of blue tones*/
    private Color[] blueTones = new Color[tones];
    /**List of green tones*/
    private Color[] greenTones = new Color[tones];
    /**List of yellow tones*/
    private Color[] yellowTones = new Color[tones];
    /**Choosen color*/
    private Color choosenColor;

    /**
     * Constructor
     * Indicates if it is a pallete which sets the color of the front part of the component, or its background
     * The value can be either <code>ColorPalette.FG_COLOR</code> or <code>ColorPalette.BG_COLOR</code>
     */
    public ColorPalette(int what) {
        this.what = what;
        MigLayout layout = new MigLayout(
                "left", // Layout Constraints
                "0[]0[]0", // Column constraints
                "0[]0[]0");
        setLayout(layout);
        genBlackTones();
        genWhiteTones();
        genRedTones();
        genBlueTones();
        genGreenTones();
        genYellowTones();
        createUI();
    }

    /**
     * Creates the palette
     */
    private void createUI() {
        for (int i = 0; i < colorList.size(); i++) {
            Color[] cv = colorList.get(i);
            for (int j = 0; j < cv.length; j++) {
                final Color color = cv[j];
                JRadioButton radioButton = new JRadioButton();
                radioButton.setIcon((Icon) getColorIcon(color));
                radioButton.setRolloverEnabled(true);
                radioButton.setRolloverIcon(getRolloverIcon(color));
                radioButton.setAction(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        ColorPalette.this.setVisible(false);
                        ColorPalette.this.getParent().setVisible(false);
                        ColorPalette.this.setChoosenColor(color);
                        chooseColorActionPreformed(choosenColor, e);
                    }
                });
                String cell = "cell " + i + " " + j;
                this.add(radioButton, cell);
            }

        }
        JButton colorChooser = new JButton();
        colorChooser.setAction(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                choosenColor = JColorChooser.showDialog((JButton) e.getSource(), "Choose Background Color", Color.RED);
                chooseColorActionPreformed(choosenColor, e);
            }
        });
        ImageIcon smallIcon = new ImageIcon(ImageResource.getImageResource("action.palette.smallIcon"));
        colorChooser.getAction().putValue(AbstractAction.SMALL_ICON, smallIcon);
        colorChooser.getAction().putValue(AbstractAction.NAME, StringResource.getStringResource("action.palette.name"));
        colorChooser.getAction().putValue(AbstractAction.SHORT_DESCRIPTION, StringResource.getStringResource("action.palette.description"));

        String cell = "cell " + 0 + " " + 4 + " " + colorList.size() + ", grow ";
        this.add(colorChooser, cell);
    }

    /**
     * Create the color image
     * @param color Color
     * @return Icon whose color is defined by the given color 
     */
    private Icon getColorIcon(Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        g.setColor(color);
        g.drawRect(0, 0, width, height);
        g.fillRect(0, 0, width, height);
        g.dispose();

        return new ImageIcon(image);
    }

    /**
     * Creates the icon used to choose the color when the component is in the rollover state
     * @param color Color of the icon
     * @return Created icon
     */
    private Icon getRolloverIcon(Color color) {
        BufferedImage image = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setPaint(color);
        g.fillRect(0, 0, width + 1, height + 1);

        g.setPaint(Color.RED);
        g.setStroke(new BasicStroke(1));
        g.drawRect(0, 0, width, height);

        g.setPaint(Color.WHITE);
        g.setStroke(new BasicStroke(1));
        g.drawRect(2, 2, width - 4, height - 4);

        g.dispose();

        return new ImageIcon(image);
    }

    /**Generates black tones*/
    private void genBlackTones() {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                blackTones[i] = new Color(r, g, b);
            } else {
                r += 30;
                g += 30;
                b += 30;
                blackTones[i] = new Color(r, g, b);
            }
        }
        colorList.add(blackTones);
    }

    /**Generates white tones*/
    private void genWhiteTones() {
        int r = 255;
        int g = 255;
        int b = 255;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                whiteTones[i] = new Color(r, g, b);
            } else {
                r -= 10 * i;
                g -= 10 * i;
                b -= 10 * i;
                whiteTones[i] = new Color(r, g, b);
            }
        }
        colorList.add(whiteTones);
    }

    /**Generates red tones*/
    private void genRedTones() {
        int r = 150;
        int g = 0;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                redTons[i] = new Color(r, g, b);
            } else {
                r += 25;
                redTons[i] = new Color(r, g, b);
            }
        }
        colorList.add(redTons);
    }

    /**Generates blue tones*/
    private void genBlueTones() {
        int r = 0;
        int g = 0;
        int b = 150;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                blueTones[i] = new Color(r, g, b);
            } else {
                b += 25;
                blueTones[i] = new Color(r, g, b);
            }
        }
        colorList.add(blueTones);
    }

    /**Generates yellow tones*/
    private void genYellowTones() {
        int r = 255;
        int g = 150;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                yellowTones[i] = new Color(r, g, b);
            } else {
                g += 25;
                yellowTones[i] = new Color(r, g, b);
            }
        }
        colorList.add(yellowTones);
    }
    
    /**Generates green tones*/
    private void genGreenTones() {
        int r = 0;
        int g = 100;
        int b = 0;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                greenTones[i] = new Color(r, g, b);
            } else {
                g += 35;
                greenTones[i] = new Color(r, g, b);
            }
        }
        colorList.add(greenTones);
    }

    /**Returns the chosen color*/
    public Color getChoosenColor() {
        return choosenColor;
    }

    /**Sets the chosen color*/
    public void setChoosenColor(Color choosenColor) {
        this.choosenColor = choosenColor;
    }

    /**Action which is performed after the color has been chosen*/
    private void chooseColorActionPreformed(Color color, ActionEvent e) {
        AbstractAction action = null;
        if (what == 1) {
            action = new SetBgColorAction();
            ((SetBgColorAction) action).setColor(choosenColor);
        } else if (what == 0) {
            action = new SetFgColorAction();
            ((SetFgColorAction) action).setColor(choosenColor);
        } else {
            return;
        }
        action.actionPerformed(e);
    }
}
