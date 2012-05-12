package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import fr.univmlv.IG.BipBip.Event.EventModel;

public class Table {
	
	private final JScrollPane scrollPane;
	private final JTable table;
	
	public Table(EventModel events) {		
		table = new JTable();
		table.setBorder(null);
		table.setFocusable(false);
		table.setSize(400, 400);
		table.setBackground(new Color(.93f, .93f, .93f));
		table.setRowHeight(44);
		table.setModel(new TableModel(events));
		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.setDefaultRenderer(table.getColumnClass(0), new StringTableCellRenderer());
		table.setDefaultRenderer(table.getColumnClass(3), new IconTableCellRenderer());
		table.setDefaultRenderer(table.getColumnClass(4), new JPanelTableCellRenderer());
		table.setDefaultEditor(table.getColumnClass(4), new JPanelTableCellRenderer());
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		
		table.getColumnModel().getColumn(3).setMinWidth(35);
		table.getColumnModel().getColumn(3).setMaxWidth(35);
		table.getColumnModel().getColumn(4).setMinWidth(80);
		table.getColumnModel().getColumn(4).setMaxWidth(80);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setMinimumSize(new Dimension(250, 1));
		scrollPane.setPreferredSize(new Dimension(250, 1));
		scrollPane.setBorder(null);
	}
	
	public JComponent getPanel() {
		return (JComponent)this.scrollPane;
	}
}
