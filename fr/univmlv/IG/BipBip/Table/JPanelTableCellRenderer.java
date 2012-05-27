package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer of the ActionCell cell to print correctly each ActionCell in the JTable
 * This class Extends AbstractCellEditor to permit catch event on JButton of the ActionCell
 */
public class JPanelTableCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
	private static final long serialVersionUID = 7493104213926994406L;
	
	private Object value;
	
	@Override
	public Object getCellEditorValue() {
		return value;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		ActionCell panel = (ActionCell)value;
		this.value = value;
		return panel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		ActionCell panel = (ActionCell)value;
		
		/* Alternate background color for more clarity */
		if(row%2 == 0) {
			panel.setBackground(new Color(.93f, .93f, .93f)); //TODO : factoriser le l&f
		}
		else {
			panel.setBackground(new Color(.99f, .99f, .99f));
		}
		
		if(isSelected || hasFocus)
			panel.setBackground(new Color(.35f, .58f, .92f));
		
		panel.setOpaque(true);
		this.value = value;
		return panel;
	}
}
