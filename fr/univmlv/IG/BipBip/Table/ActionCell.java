package fr.univmlv.IG.BipBip.Table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionCell extends JPanel {
	private static final long serialVersionUID = -6646136351553964151L;
	
	private final JButton btnEdit;
	private final JButton btnDelete;
	private final Collection<ActionCellListener> actionCellListeners = new ArrayList<ActionCellListener>();

	public ActionCell(final int index, ActionCellListener listener) {
		this.setLayout(new FlowLayout());
		this.addActionCellListener(listener);
		
		btnEdit = new JButton("Edit");
		btnDelete = new JButton("Del");
		this.add(btnEdit);
		this.add(btnDelete);
		
		btnEdit.setPreferredSize(new Dimension(30, 30));
		btnDelete.setPreferredSize(new Dimension(30, 30));
		this.setPreferredSize(new Dimension(60, 30));
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionCell.this.fireEventEdit(index);
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionCell.this.fireEventDelete(index);
			}
		});
		
		/* This is just because when you click on a button the background is redraw with the wrong color */
		MouseListener defaultMouseListener = new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				ActionCell.this.setBackground(new Color(.35f, .58f, .92f));
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		};
		
		btnEdit.addMouseListener(defaultMouseListener);
		btnDelete.addMouseListener(defaultMouseListener);
		this.addMouseListener(defaultMouseListener);
	}
	
	private void addActionCellListener(ActionCellListener listener) {
		actionCellListeners.add(listener);
	}
	
	protected void fireEventEdit(int index) {
		for(ActionCellListener listener : actionCellListeners)
			listener.eventEdit(index);
	}
	
	protected void fireEventDelete(int index) {
		for(ActionCellListener listener : actionCellListeners)
			listener.eventDelete(index);
	}
}
