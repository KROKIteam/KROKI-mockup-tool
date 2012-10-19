/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * Komponenta koja predstavlja paletu osnovnog skupa boja. Ukoliko skup nijansi nije dovoljan paleta nudi i dugme za otvaranje <code>JColorChooser</code> panela. Izgled palete prikazan je na slici:
 * <br/><img src="../../resources/images/ui-color-palette.png"/>
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ColorPalette extends JPanel {

    /**Boja se primenjuje na ono što se nalazi u prvom planu komponente - tekst*/
    public static final int FG_COLOR = 0;
    /**Boja se promenjuje na podzadinu komponente koja se prikazuje*/
    public static final int BG_COLOR = 1;
    /**Vrednost ovog polja može biti <code>ColorPalette.FG_COLOR</code> ili <code>ColorPalette.BG_COLOR</code>*/
    private int what;
    /**Širina sličice koja predstavlja boju na paleti*/
    private static final int width = 15;
    /**Visina sličice koja predstavlja boju na paleti*/
    private static final int height = 15;
    /**Broj nijansi osnovnih boja*/
    private static final int tones = 4;
    /**Lista svih nijansi*/
    private List<Color[]> colorList = new ArrayList<Color[]>();
    /**Lista nijansi crne boje*/
    private Color[] blackTones = new Color[tones];
    /**Lista nijansi bele boje*/
    private Color[] whiteTones = new Color[tones];
    /**Lista nijansi crvene boje*/
    private Color[] redTons = new Color[tones];
    /**Lista nijansi plave boje*/
    private Color[] blueTones = new Color[tones];
    /**Lista nijansi zelene boje*/
    private Color[] greenTones = new Color[tones];
    /**Lista nijansi žute boje*/
    private Color[] yellowTones = new Color[tones];
    /**Odabrana boja*/
    private Color choosenColor;

    /**
     * Konstruktor ColorPalette klase.
     * @param what - indikator da li se radi o paleti koja podesava boju prednjeg dela komponente ili njene podzadine. Vrednost ovog polja može biti <code>ColorPalette.FG_COLOR</code> ili <code>ColorPalette.BG_COLOR</code>
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
     * Kreira paletu
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
     * Kreira ikonicu za odabir boje
     * @param color boja
     * @return vraća ikonicu čija je boja jednaka prosleđenoj
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
     * Kreira ikonicu za odabir boje kada se komponenta za odabir nalazi u Rollover stanju
     * @param color boja ikonice
     * @return vraća ikonicu
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

    /**Kreira nijanse crne boje*/
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

    /**Kreira nijanse bele boje*/
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

    /**Kreira nijanse crvene boje*/
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

    /**Kreira nijanse plave boje*/
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

    /**Kreira nijanse žute boje*/
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

    /**Kreira nijanse zelene boje*/
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

    /**Vraća odabranu boju*/
    public Color getChoosenColor() {
        return choosenColor;
    }

    /**Podešava odabranu boju na prosleđenu vrednost*/
    public void setChoosenColor(Color choosenColor) {
        this.choosenColor = choosenColor;
    }

    /**Akcija koja se odvija nakon odabira boje*/
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
