package kroki.app.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/** Interface which represents a state in which the editor can be.
 * It is a part of the implementation of State design patter.
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public interface IState {

    public void mouseDragged(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void mouseWheelMoved(MouseWheelEvent e);

    public void mouseClicked(MouseEvent e);

    public void mouseEntered(MouseEvent e);

    public void mouseExited(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

    public void keyTyped(KeyEvent e);
}
