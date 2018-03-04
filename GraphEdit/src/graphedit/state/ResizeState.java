package graphedit.state;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;

import graphedit.app.MainFrame;
import graphedit.command.Command;
import graphedit.command.ResizeElementsCommand;
import graphedit.model.components.GraphElement;
import graphedit.model.components.LinkableElement;
import graphedit.properties.Preferences;
import graphedit.strategy.AsIsStrategy;
import graphedit.strategy.LinkStrategy;
import graphedit.strategy.RightAngledStrategy;
import graphedit.view.ElementPainter;
import graphedit.view.GraphEditView;
import graphedit.view.GraphEditView.GraphEditController;

public class ResizeState extends State {
	
	private int oldXPos, oldYPos;
	private int cursorType;
	private GraphElement selectedElement;
	private ElementPainter painter;
	private LinkableElement linkableElement;
	private Shape startShape;
	private double deltaX, deltaY;
	private double scaleX, scaleY;
	private Dimension2D minimumSize;
	private Preferences prefs = Preferences.getInstance();
	private LinkStrategy strategy;

	public ResizeState(GraphEditController controller, GraphEditView view) {
		super(controller);
		this.view = view;
	}
	
	public ResizeState() { }

	@Override
	public void keyPressed(KeyEvent e){
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		MainFrame.getInstance().setPositionTrack(e.getX(), e.getY());
		//deltaY = e.getY() - oldYPos;
		//oldYPos += deltaY;
		
		/*
		 * ubacen je kod koji pravi minimalan problem tj.
		 * povremeno se izgubi po koji pixel i onda nece da uradi resize onako
		 * kako se to ocekuje, iako je matematika dobra
		 * bug se izrazito primeti ako dovoljno brzo pomerate misa 
		 * levo-desno i/ili gore-dole
		 * 
		 * kod koji pravi problem je vidno izdvojen
		 */
		
		cursorType = view.getCursor().getType();
		if(cursorType == Cursor.E_RESIZE_CURSOR){ //resize desne ivice
			
			/////////////////////////////////////////////////////////////
			/* ako (pocetna x ordinata + minimalna sirina >= trenutna x pozicija)
			 * onda nemoj da reagujes
			 */
			if(startShape.getBounds().getLocation().getX()
					+ minimumSize.getWidth() >= e.getX()){
				return;
			}
			deltaX = e.getX() - oldXPos;
			oldXPos = e.getX();
			/////////////////////////////////////////////////////////////
			
			scaleX = 1 + deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1;
			//nema nikakvog smicanja
		}else if(cursorType == Cursor.W_RESIZE_CURSOR){ //resize leve ivice
			
			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getX()
					+ startShape.getBounds().getWidth()
					- minimumSize.getWidth() <= e.getX()){
				return;
			}
			deltaX = e.getX() - oldXPos;
			oldXPos = e.getX();
			/////////////////////////////////////////////////////////////
			
			//po y se ne menja
			scaleX = 1 - deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1;
			//smicanje u desno za deltaX
			if(deltaX != 0){
				painter.moveShape(deltaX, 0);
			}
		}else if(cursorType == Cursor.N_RESIZE_CURSOR){ //resize gornje ivice

			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ startShape.getBounds().getHeight()
					- minimumSize.getHeight() <= e.getY()){
				return;
			}
			deltaY = e.getY() - oldYPos;
			oldYPos = e.getY();
			/////////////////////////////////////////////////////////////
			
			//po x se ne menja
			scaleX = 1;
			scaleY = 1 - deltaY/painter.getShape().getBounds().getHeight();
			//smicanje na gore za deltaY
			if(deltaY != 0){
				painter.moveShape(0, deltaY);
			}
		}else if(cursorType == Cursor.S_RESIZE_CURSOR){ //resize donje ivice
			
			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ minimumSize.getHeight() >= e.getY()){
				return;
			}
			deltaY = e.getY() - oldYPos;
			oldYPos = e.getY();
			/////////////////////////////////////////////////////////////
			
			//po x se ne menja
			scaleX = 1;
			scaleY = 1 + deltaY/painter.getShape().getBounds().getHeight();
			//nema smicanja
		}else if(cursorType == Cursor.NE_RESIZE_CURSOR){ //resize gornjeg-desnog coska

			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ startShape.getBounds().getHeight()
					- minimumSize.getHeight() <= e.getY()){
				deltaY = 0;
			}else{
				deltaY = e.getY() - oldYPos;
				oldYPos = e.getY();
			}
			if(startShape.getBounds().getLocation().getX()
					+ minimumSize.getWidth() >= e.getX()){
				deltaX = 0;
			}else{
				deltaX = e.getX() - oldXPos;
				oldXPos = e.getX();
			}
			/////////////////////////////////////////////////////////////
			
			//menja se po obe ordinate
			scaleX = 1 + deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1 - deltaY/painter.getShape().getBounds().getHeight();
			//smicanje na gore za deltaY
			painter.moveShape(0, deltaY);
		}else if(cursorType == Cursor.NW_RESIZE_CURSOR){ //resize gornjeg-levog coska

			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ startShape.getBounds().getHeight()
					- minimumSize.getHeight() <= e.getY()){
				deltaY = 0;
			}else{
				deltaY = e.getY() - oldYPos;
				oldYPos = e.getY();
			}
			if(startShape.getBounds().getLocation().getX()
					+ startShape.getBounds().getWidth()
					- minimumSize.getWidth() <= e.getX()){
				deltaX = 0;
			}else{
				deltaX = e.getX() - oldXPos;
				oldXPos = e.getX();
			}
			/////////////////////////////////////////////////////////////
			
			//menja se po obe ordinate
			scaleX = 1 - deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1 - deltaY/painter.getShape().getBounds().getHeight();
			//smicanje na levo za deltaX i na gore za deltaY
			painter.moveShape(deltaX, deltaY);
		}else if(cursorType == Cursor.SW_RESIZE_CURSOR){ //resize donjeg-levog coska

			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ minimumSize.getHeight() >= e.getY()){
				deltaY = 0;
			}else{
				deltaY = e.getY() - oldYPos;
				oldYPos = e.getY();
			}
			if(startShape.getBounds().getLocation().getX()
					+ startShape.getBounds().getWidth()
					- minimumSize.getWidth() <= e.getX()){
				deltaX = 0;
			}else{
				deltaX = e.getX() - oldXPos;
				oldXPos = e.getX();
			}
			/////////////////////////////////////////////////////////////
			
			//menja se po obe ordinate
			scaleX = 1 - deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1 + deltaY/painter.getShape().getBounds().getHeight();
			//smicanje na levo za deltaX
			painter.moveShape(deltaX, 0);
		}else{

			/////////////////////////////////////////////////////////////
			if(startShape.getBounds().getLocation().getY()
					+ minimumSize.getHeight() >= e.getY()){
				deltaY = 0;
			}else{
				deltaY = e.getY() - oldYPos;
				oldYPos = e.getY();
			}
			if(startShape.getBounds().getLocation().getX()
					+ minimumSize.getWidth() >= e.getX()){
				deltaX = 0;
			}else{
				deltaX = e.getX() - oldXPos;
				oldXPos = e.getX();
			}
			/////////////////////////////////////////////////////////////
			
			scaleX = 1 + deltaX/painter.getShape().getBounds().getWidth();
			scaleY = 1 + deltaY/painter.getShape().getBounds().getHeight();
		}
		
		if(minimumSize != null){
			if(minimumSize.getWidth() > painter.getShape().getBounds().width*scaleX){
				scaleX = 1;
			}
			if(minimumSize.getHeight() > painter.getShape().getBounds().height*scaleY){
				scaleY = 1;
			}
		}
		if(scaleX == 1.0 && scaleY == 1.0){
			return;
		}
		painter.resizeShape(scaleX, scaleY);
		if(selectedElement instanceof LinkableElement){
			linkableElement = (LinkableElement)selectedElement;
			linkableElement.scaleAllConnectors(scaleX,scaleY,painter.getShape());
		}
		
		view.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		oldXPos = e.getX();
		oldYPos = e.getY();
		selectedElement = view.getSelectionModel().getSelectedElements().get(0);
		painter = view.getElementPainter(selectedElement);
		minimumSize = painter.getMinimumSize();
		startShape = painter.getShape();
		cursorType = view.getCursor().getType();
		if (prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true")) 
				strategy = new RightAngledStrategy();
		 else 
				strategy = new AsIsStrategy();
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		if(selectedElement instanceof LinkableElement){
			linkableElement = (LinkableElement)selectedElement;
			linkableElement.scaleAllConnectors(scaleX,scaleY,startShape);
		}
		Command command = new ResizeElementsCommand( view, selectedElement,painter.getShape(),strategy);
		view.getModel().getCommandManager().executeCommand(command);
		this.switchToDefaultState();
	}
	
	@Override
	public boolean isAutoScrollOnDragEnabled() {
		return true;
	}

	@Override
	public boolean isAutoScrollOnMoveEnabled() {
		return false;
	}
}