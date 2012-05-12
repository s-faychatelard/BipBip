package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class StringTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JLabel lbl = new JLabel();
		if(row%2 == 0) {
			lbl.setText("<html><font color='black'>" + value + "</font></html>");
			lbl.setBackground(new Color(.93f, .93f, .93f));
		}
		else {
			lbl.setText("<html><font color='black'>" + value + "</font></html>");
			lbl.setBackground(new Color(.99f, .99f, .99f));
		}
		
		if(isSelected)
			lbl.setBackground(new Color(.35f, .58f, .92f));
		lbl.setOpaque(true);
		return lbl;
	}
}
