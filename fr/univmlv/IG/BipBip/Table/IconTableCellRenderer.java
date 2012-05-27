package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer of the Icon cell to print correctly each EventType in the JTable
 */
public class IconTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = new JLabel((Icon)value, JLabel.CENTER);
		
		/* Alternate background color for more clarity */
		if(row%2 == 0) {
			lbl.setBackground(new Color(.93f, .93f, .93f));
		}
		else {
			lbl.setBackground(new Color(.99f, .99f, .99f));
		}
			
		if(isSelected)
			lbl.setBackground(new Color(.35f, .58f, .92f));
		
		lbl.setOpaque(true);
		return lbl;
	}
}
