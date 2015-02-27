package kroki.mockup.model.layout;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Vector;

import kroki.mockup.model.Component;
import kroki.mockup.model.Composite;
import kroki.mockup.model.Insets;

/**
 * A <code>CardLayout</code> object is a layout manager for a
 * container. It treats each component in the container as a card.
 * Only one card is visible at a time, and the container acts as
 * a stack of cards. The first component added to a
 * <code>CardLayout</code> object is the visible component when the
 * container is first displayed.
 * <p>
 * The ordering of cards is determined by the container's own internal
 * ordering of its component objects. <code>CardLayout</code>
 * defines a set of methods that allow an application to flip
 * through these cards sequentially, or to show a specified card.
 * The {@link CardLayout#addLayoutComponent}
 * method can be used to associate a string identifier with a given card
 * for fast random access.
 *
 * @version 	1.49 05/05/07
 * @author 		Arthur van Hoff
 * @see         java.awt.Container
 * @since       JDK1.0
 */
@SuppressWarnings("unused")
public class CardLayout extends LayoutManager {

	private static final long serialVersionUID = -4328196481005934313L;

	@Override
	public Dimension maximumLayoutSize(Component c) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@SuppressWarnings("rawtypes")
	/*
	 * This creates a Vector to store associated
	 * pairs of components and their names.
	 * @see java.util.Vector
	 */
	Vector vector = new Vector();

	@Override
	public void layoutComponent(Component c) {
		if (c instanceof Composite) {
			layoutContainer((Composite) c);
		}
	}

	@Override
	public Dimension preferredLayoutSize(Component c) {
		if (c instanceof Composite) {
			return preferredLayoutSize((Composite) c);
		} else {
			return c.getPreferredSize();
		}
	}

	@Override
	public Dimension minimumLayoutSize(Component c) {
		if (c instanceof Composite) {
			return minimumLayoutSize((Composite) c);
		} else {
			return c.getMinimumSize();
		}
	}

	/*
	 * A pair of Component and String that represents its name.
	 */
	class Card implements Serializable {

		static final long serialVersionUID = 6640330810709497518L;
		public String name;
		public Component comp;

		public Card(String cardName, Component cardComponent) {
			name = cardName;
			comp = cardComponent;
		}
	}

	/*
	 * Index of Component currently displayed by CardLayout.
	 */
	int currentCard = 0;

	/**
	 * Creates a new card layout with gaps of size zero.
	 */
	public CardLayout() {
		this(0, 0);
	}

	/**
	 * Creates a new card layout with the specified horizontal and
	 * vertical gaps. The horizontal gaps are placed at the left and
	 * right edges. The vertical gaps are placed at the top and bottom
	 * edges.
	 * @param     hgap   the horizontal gap.
	 * @param     vgap   the vertical gap.
	 */
	public CardLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}

	/**
	 * Adds the specified component to this card layout's internal
	 * table of names. The object specified by <code>constraints</code>
	 * must be a string. The card layout stores this string as a key-value
	 * pair that can be used for random access to a particular card.
	 * By calling the <code>show</code> method, an application can
	 * display the component with the specified name.
	 * @param     comp          the component to be added.
	 * @param     constraints   a tag that identifies a particular
	 *                                        card in the layout.
	 * @see       java.awt.CardLayout#show(java.awt.Container, java.lang.String)
	 * @exception  IllegalArgumentException  if the constraint is not a string.
	 */
	public void addLayoutComponent(Component comp, Object constraints) {

		if (constraints == null) {
			constraints = "";
		}
		if (constraints instanceof String) {
			addLayoutComponent((String) constraints, comp);
		} else {
			throw new IllegalArgumentException(
					"cannot add to layout: constraint must be a string");
		}

	}

	/**
	 * @deprecated   replaced by
	 *      <code>addLayoutComponent(Component, Object)</code>.
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public void addLayoutComponent(String name, Component comp) {
		if (!vector.isEmpty()) {
			//comp.setVisible(false);
		}
		for (int i = 0; i < vector.size(); i++) {
			if (((Card) vector.get(i)).name.equals(name)) {
				((Card) vector.get(i)).comp = comp;
				return;
			}
		}
		vector.add(new Card(name, comp));

	}

	/**
	 * Removes the specified component from the layout.
	 * If the card was visible on top, the next card underneath it is shown.
	 * @param   comp   the component to be removed.
	 * @see     java.awt.Container#remove(java.awt.Component)
	 * @see     java.awt.Container#removeAll()
	 */
	public void removeLayoutComponent(Component comp) {
		for (int i = 0; i < vector.size(); i++) {
			if (((Card) vector.get(i)).comp == comp) {
				// if we remove current component we should show next one
				//                if (comp.isVisible() && (comp.getParent() != null)) {
				//                    next(comp.getParent());
				//                }

				vector.remove(i);

				// correct currentCard if this is necessary
				if (currentCard > i) {
					currentCard--;
				}
				break;
			}
		}
	}

	/**
	 * Determines the preferred size of the container argument using
	 * this card layout.
	 * @param   parent the parent container in which to do the layout
	 * @return  the preferred dimensions to lay out the subcomponents
	 *                of the specified container
	 * @see     java.awt.Container#getPreferredSize
	 * @see     java.awt.CardLayout#minimumLayoutSize
	 */
	public Dimension preferredLayoutSize(Composite parent) {

		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();
		int w = 0;
		int h = 0;

		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			Dimension d = comp.getPreferredSize();
			if (d.width > w) {
				w = d.width;
			}
			if (d.height > h) {
				h = d.height;
			}
		}
		return new Dimension(insets.left + insets.right + w + hgap
				* 2, insets.top + insets.bottom + h + vgap * 2);

	}

	/**
	 * Calculates the minimum size for the specified panel.
	 * @param     parent the parent container in which to do the layout
	 * @return    the minimum dimensions required to lay out the
	 *                subcomponents of the specified container
	 * @see       java.awt.Container#doLayout
	 * @see       java.awt.CardLayout#preferredLayoutSize
	 */
	public Dimension minimumLayoutSize(Composite parent) {

		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();
		int w = 0;
		int h = 0;

		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			Dimension d = comp.getMinimumSize();
			if (d.width > w) {
				w = d.width;
			}
			if (d.height > h) {
				h = d.height;
			}
		}
		return new Dimension(insets.left + insets.right + w + hgap
				* 2, insets.top + insets.bottom + h + vgap * 2);

	}

	/**
	 * Lays out the specified container using this card layout.
	 * <p>
	 * Each component in the <code>parent</code> container is reshaped
	 * to be the size of the container, minus space for surrounding
	 * insets, horizontal gaps, and vertical gaps.
	 *
	 * @param     parent the parent container in which to do the layout
	 * @see       java.awt.Container#doLayout
	 */
	public void layoutContainer(Composite parent) {
		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();
		Component comp = null;
		boolean currentFound = false;

		for (int i = 0; i < ncomponents; i++) {
			comp = parent.getComponent(i);

			//treba i za absolute position
			int relx = hgap + insets.left;
			int rely = vgap + insets.top;

			int absx = parent.getAbsolutePosition().x + relx;
			int absy = parent.getAbsolutePosition().y + rely;

			comp.getAbsolutePosition().setLocation(absx, absy);
			comp.getRelativePosition().setLocation(relx, rely);

			comp.getDimension().setSize(parent.getDimension().width - (hgap * 2 + insets.left + insets.right),
					parent.getDimension().height - (vgap * 2 + insets.top + insets.bottom));
			//            if (comp.isVisible()) {
			//                currentFound = true;
			//            }
		}

		if (!currentFound && ncomponents > 0) {
			//            parent.getComponent(0).setVisible(true);
		}

	}

	/**
	 * Make sure that the Container really has a CardLayout installed.
	 * Otherwise havoc can ensue!
	 */
	void checkLayout(Composite parent) {
		if (parent.getLayoutManager() != this) {
			throw new IllegalArgumentException("wrong parent for CardLayout");
		}
	}

	/**
	 * Flips to the first card of the container.
	 * @param     parent   the parent container in which to do the layout
	 * @see       java.awt.CardLayout#last
	 */
	public void first(Composite parent) {

		checkLayout(parent);
		int ncomponents = parent.getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			//            if (comp.isVisible()) {
			//                comp.setVisible(false);
			//                break;
			//            }
		}
		if (ncomponents > 0) {
			currentCard = 0;
			//            parent.getComponent(0).setVisible(true);
			//            parent.validate();
		}

	}

	/**
	 * Flips to the next card of the specified container. If the
	 * currently visible card is the last one, this method flips to the
	 * first card in the layout.
	 * @param     parent   the parent container in which to do the layout
	 * @see       java.awt.CardLayout#previous
	 */
	public void next(Composite parent) {
		checkLayout(parent);
		int ncomponents = parent.getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			//                if (comp.isVisible()) {
			//                    comp.setVisible(false);
			//                    currentCard = (i + 1) % ncomponents;
			//                    comp = parent.getComponent(currentCard);
			//                    comp.setVisible(true);
			//                    parent.validate();
			//                    return;
			//                }
		}
		showDefaultComponent(parent);

	}

	/**
	 * Flips to the previous card of the specified container. If the
	 * currently visible card is the first one, this method flips to the
	 * last card in the layout.
	 * @param     parent   the parent container in which to do the layout
	 * @see       java.awt.CardLayout#next
	 */
	public void previous(Composite parent) {
		checkLayout(parent);
		int ncomponents = parent.getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			//                if (comp.isVisible()) {
			//                    comp.setVisible(false);
			//                    currentCard = ((i > 0) ? i - 1 : ncomponents - 1);
			//                    comp = parent.getComponent(currentCard);
			//                    comp.setVisible(true);
			//                    parent.validate();
			//                    return;
			//                }
		}
		showDefaultComponent(parent);

	}

	void showDefaultComponent(Composite parent) {
		if (parent.getComponentCount() > 0) {
			currentCard = 0;
			//            parent.getComponent(0).setVisible(true);
			//            parent.validate();
		}
	}

	/**
	 * Flips to the last card of the container.
	 * @param     parent   the parent container in which to do the layout
	 * @see       java.awt.CardLayout#first
	 */
	public void last(Composite parent) {

		checkLayout(parent);
		int ncomponents = parent.getComponentCount();
		//            for (int i = 0; i < ncomponents; i++) {
		//                Component comp = parent.getComponent(i);
		//                if (comp.isVisible()) {
		//                    comp.setVisible(false);
		//                    break;
		//                }
		//            }
		//            if (ncomponents > 0) {
		//                currentCard = ncomponents - 1;
		//                parent.getComponent(currentCard).setVisible(true);
		//                parent.validate();
		//            }

	}

	/**
	 * Flips to the component that was added to this layout with the
	 * specified <code>name</code>, using <code>addLayoutComponent</code>.
	 * If no such component exists, then nothing happens.
	 * @param     parent   the parent container in which to do the layout
	 * @param     name     the component name
	 * @see       java.awt.CardLayout#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void show(Composite parent, String name) {

		checkLayout(parent);
		Component next = null;
		int ncomponents = vector.size();
		for (int i = 0; i < ncomponents; i++) {
			Card card = (Card) vector.get(i);
			if (card.name.equals(name)) {
				next = card.comp;
				currentCard = i;
				break;
			}
		}
		//            if ((next != null) && !next.isVisible()) {
		//                ncomponents = parent.getComponentCount();
		//                for (int i = 0; i < ncomponents; i++) {
		//                    Component comp = parent.getComponent(i);
		//                    if (comp.isVisible()) {
		//                        comp.setVisible(false);
		//                        break;
		//                    }
		//                }
		//                next.setVisible(true);
		//                parent.validate();
		//            }

	}

	/**
	 * Returns a string representation of the state of this card layout.
	 * @return    a string representation of this card layout.
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap
				+ "]";
	}
}
