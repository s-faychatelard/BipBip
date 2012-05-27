/**
 * ESIPE Project - IR2 2011/2012 - IG
 * Copyright (C) 2012 ESIPE - Universite Paris-Est Marne-la-Vallee
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Please see : http://www.gnu.org/licenses/gpl.html
 *
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer of the String cell
 */
public class StringTableCellRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel lbl = new JLabel();
		
		/* Alternate background color for more clarity */
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
