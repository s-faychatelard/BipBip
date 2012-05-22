package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import fr.univmlv.IG.BipBip.Event.EventModel;

public class Table {
	
	private final JScrollPane scrollPane;
	private final JTable table;
	private final TableModel tableModel;
	
	public Table(EventModel events) {
		this.tableModel = new TableModel(events);
		
		table = new JTable();
		table.setBorder(null);
		table.setFocusable(false);
		table.setSize(400, 400);
		table.setBackground(new Color(.93f, .93f, .93f));
		table.setRowHeight(44);
		table.setModel(this.tableModel);
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
		table.getColumnModel().getColumn(4).setMinWidth(110);
		table.getColumnModel().getColumn(4).setMaxWidth(110);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setMinimumSize(new Dimension(250, 1));
		scrollPane.setPreferredSize(new Dimension(250, 1));
		scrollPane.setBorder(null);
	}
	
	public TableModel getModel() {
		return this.tableModel;
	}
	
	public JComponent getPanel() {
		return (JComponent)this.scrollPane;
	}
}
