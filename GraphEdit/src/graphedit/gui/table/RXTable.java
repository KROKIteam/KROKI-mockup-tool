package graphedit.gui.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

/**
 * The RXTable provides some extensions to the default JTable
 *
 * 1) Select All editing - when a text related cell is placed in editing mode
 *    the text is selected. Controlled by invoking a "setSelectAll..." method.
 *
 * 2) reorderColumns - static convenience method for reodering table columns
 */
public class RXTable extends JTable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSelectAllForMouseEvent = false;
	private boolean isSelectAllForActionEvent = false;
	private boolean isSelectAllForKeyEvent = false;

//
// Constructors
//
    /**
     * Constructs a default <code>RXTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     */
    public RXTable()
    {
        this(null, null, null);
    }

    
    public RXTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
    }

    
    public RXTable(final Object[][] rowData, final Object[] columnNames)
    {
        super(rowData, columnNames);
    }
    
	/*
	 *  Override to provide Select All editing functionality
	 */
	public boolean editCellAt(int row, int column, EventObject e)
	{
		boolean result = super.editCellAt(row, column, e);

		if (isSelectAllForMouseEvent
		||  isSelectAllForActionEvent
		||  isSelectAllForKeyEvent)
		{
			selectAll(e);
		}

		return result;
	}

	/*
	 * Select the text when editing on a text related cell is started
	 */
	private void selectAll(EventObject e)
	{
		final Component editor = getEditorComponent();

		if (editor == null
		|| ! (editor instanceof JTextComponent))
			return;

		if (e == null)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  Typing in the cell was used to activate the editor

		if (e instanceof KeyEvent && isSelectAllForKeyEvent)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  F2 was used to activate the editor

		if (e instanceof ActionEvent && isSelectAllForActionEvent)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  A mouse click was used to activate the editor.
		//  Generally this is a double click and the second mouse click is
		//  passed to the editor which would remove the text selection unless
		//  we use the invokeLater()

		if (e instanceof MouseEvent && isSelectAllForMouseEvent)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					((JTextComponent)editor).selectAll();
				}
			});
		}
	}

//
//  Newly added methods
//
	/*
	 *  Sets the Select All property for for all event types
	 */
	public void setSelectAllForEdit(boolean isSelectAllForEdit)
	{
		setSelectAllForMouseEvent( isSelectAllForEdit );
		setSelectAllForActionEvent( isSelectAllForEdit );
		setSelectAllForKeyEvent( isSelectAllForEdit );
	}

	/*
	 *  Set the Select All property when editing is invoked by the mouse
	 */
	public void setSelectAllForMouseEvent(boolean isSelectAllForMouseEvent)
	{
		this.isSelectAllForMouseEvent = isSelectAllForMouseEvent;
	}

	/*
	 *  Set the Select All property when editing is invoked by the "F2" key
	 */
	public void setSelectAllForActionEvent(boolean isSelectAllForActionEvent)
	{
		this.isSelectAllForActionEvent = isSelectAllForActionEvent;
	}

	/*
	 *  Set the Select All property when editing is invoked by
	 *  typing directly into the cell
	 */
	public void setSelectAllForKeyEvent(boolean isSelectAllForKeyEvent)
	{
		this.isSelectAllForKeyEvent = isSelectAllForKeyEvent;
	}

//
//  Static, convenience methods
//
	/**
	 *  Convenience method to order the table columns of a table. The columns
	 *  are ordered based on the column names specified in the array. If the
	 *  column name is not found then no column is moved. This means you can
	 *  specify a null value to preserve the current order of a given column.
	 *
     *  @param table        the table containing the columns to be sorted
     *  @param columnNames  an array containing the column names in the
     *                      order they should be displayed
	 */
	public static void reorderColumns(JTable table, Object... columnNames)
	{
		TableColumnModel model = table.getColumnModel();

		for (int newIndex = 0; newIndex < columnNames.length; newIndex++)
		{
			try
			{
				Object columnName = columnNames[newIndex];
				int index = model.getColumnIndex(columnName);
				model.moveColumn(index, newIndex);
			}
			catch(IllegalArgumentException e) {}
		}
	}
}  // End of Class RXTable
