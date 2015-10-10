package graphedit.view.scrollers;

import graphedit.view.GraphEditView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class VerticalScrollBar extends JScrollBar{


	private static final long serialVersionUID = 1L;
	private GraphEditView view;
	private final int UP_SCROLL = 0, DOWN_SCROLL = 1;

	private final int interval = 50;
	private Timer timer;
	private Color gray = new Color(237,240,242);
	public VerticalScrollBar(GraphEditView view) {

		this.view = view;
		setOrientation(VERTICAL);
		setUI(new CustomUI());

	}

	class CustomUI extends BasicScrollBarUI {
		@Override
		protected void installListeners() {
			super.installListeners();
			if (incrButton != null) {
				incrButton.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						view.scrollDown();
					}

					@Override
					public void mousePressed(MouseEvent e) {
						timer = new Timer();
						timer.schedule(new ScrollTimerTask(DOWN_SCROLL), 0,interval);

					}

					@Override
					public void mouseReleased(MouseEvent e) {
						timer.cancel();
					}
				});

			}
			if (decrButton != null) {
				decrButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						view.scrollUp();
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						timer = new Timer();
						timer.schedule(new ScrollTimerTask(UP_SCROLL), 0,interval);
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						timer.cancel();
					}
				});
			}
		}
		
		@Override
		public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			
		}
		@Override
		public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds){
			Graphics2D g2 = (Graphics2D)g;
			g2.draw(trackBounds);
			g2.setColor(gray);
			g2.fill(trackBounds);
			g2.setColor(Color.GRAY);
			g2.draw(trackBounds);
		}
	}

	class ScrollTimerTask extends TimerTask {

		int direction;

		public ScrollTimerTask(int direction){
			this.direction = direction;
		}


		public void run() {
			if (direction == UP_SCROLL)
				getView().scrollUp();
			else 
				getView().scrollDown();
		}
	}

	public GraphEditView getView() {
		return view;
	}


}
