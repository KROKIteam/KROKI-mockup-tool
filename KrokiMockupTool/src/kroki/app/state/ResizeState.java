package kroki.app.state;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import kroki.app.command.CommandManager;
import kroki.app.command.ResizeCommand;
import kroki.app.view.Canvas;
import kroki.app.view.Handle;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;

/**
 * Class represent resize state - which allows elements to be resized
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ResizeState extends State {

    private Handle handle;
    private VisibleElement visibleElement;
    private Point elPosition;
    private Dimension elSize;

    public ResizeState(Context context) {
        super(context, "app.state.resize");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            context.goNext(State.SELECT_STATE);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        Dimension minSize = visibleElement.getComponent().getMinimumSize();
        Dimension maxSize = visibleElement.getComponent().getMaximumSize();
        Point newPosition = e.getPoint();
        if (elPosition == null) {
            elPosition = (Point) visibleElement.getComponent().getAbsolutePosition().clone();
        }
        if (elSize == null) {
            elSize = (Dimension) visibleElement.getComponent().getDimension().clone();
        }
        int resX = 0;
        int resY = 0;
        int newWidth = 0;
        int newHeight = 0;

        switch (handle) {
            case North: {
                resX = elPosition.x;
                resY = elPosition.y - newPosition.y;
                newWidth = elSize.width;
                newHeight = elSize.height + resY;
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                } else {
                    elPosition.y = newPosition.y;
                }
                break;
            }
            case South: {
                resX = elPosition.x;
                resY = newPosition.y - (elPosition.y + elSize.height);
                newWidth = elSize.width;
                newHeight = elSize.height + resY;
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                }
                break;
            }
            case East: {
                resY = elPosition.y;
                resX = newPosition.x - (elPosition.x + elSize.width);
                newWidth = elSize.width + resX;
                newHeight = elSize.height;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                }
                break;
            }
            case West: {
                resY = elPosition.y;
                resX = newPosition.x - elPosition.x;
                newWidth = elSize.width - resX;
                newHeight = elSize.height;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                } else {
                    elPosition.x = newPosition.x;
                }
                break;
            }
            case SouthEast: {
                resX = newPosition.x - (elPosition.x + elSize.width);
                resY = newPosition.y - (elPosition.y + elSize.height);
                newWidth = elSize.width + resX;
                newHeight = elSize.height + resY;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                }
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                }
                break;
            }
            case SouthWest: {
                resX = newPosition.x - elPosition.x;
                resY = newPosition.y - (elPosition.y + elSize.height);
                newWidth = elSize.width - resX;
                newHeight = elSize.height + resY;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                } else {
                    elPosition.x = newPosition.x;
                }
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                }
                break;
            }
            case NorthEast: {
                resX = newPosition.x - (elPosition.x + elSize.width);
                resY = elPosition.y - newPosition.y;
                newWidth = elSize.width + resX;
                newHeight = elSize.height + resY;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                }
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                } else {
                    elPosition.y = newPosition.y;
                }
                break;
            }
            case NorthWest: {
                resX = newPosition.x - elPosition.x;
                resY = elPosition.y - newPosition.y;
                newWidth = elSize.width - resX;
                newHeight = elSize.height + resY;
                if (newWidth < minSize.width) {
                    newWidth = minSize.width;
                } else if (newWidth > maxSize.width) {
                    newWidth = maxSize.width;
                } else {
                    elPosition.x = newPosition.x;
                }
                if (newHeight < minSize.height) {
                    newHeight = minSize.height;
                } else if (newHeight > maxSize.height) {
                    newHeight = maxSize.height;
                } else {
                    elPosition.y = newPosition.y;
                }
                break;
            }
        }
        elSize.setSize(newWidth, newHeight);

        c.setSelectionRectangleBounds(elPosition.x, elPosition.y, elSize.width, elSize.height);
        c.setShowSelectionRectangle(true);
        c.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Canvas c = context.getTabbedPaneController().getCurrentTabContent();
        CommandManager commandManager = c.getCommandManager();
        VisibleClass visibleClass = c.getVisibleClass();

        if (elPosition != null && elSize != null) {

            //dodavanje komande
            ResizeCommand resizeCommand = new ResizeCommand(visibleElement, elPosition, elSize);
            commandManager.addCommand(resizeCommand);

            visibleClass.update();
            c.setShowSelectionRectangle(false);
            elPosition = null;
            elSize = null;
            c.repaint();
        }

        context.goNext(State.SELECT_STATE);
    }

    public void setHandle(Handle handle) {
        this.handle = handle;
    }

    public void setVisibleElement(VisibleElement visibleElement) {
        this.visibleElement = visibleElement;
    }
}
