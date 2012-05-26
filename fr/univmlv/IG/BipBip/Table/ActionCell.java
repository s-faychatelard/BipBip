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

import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class ActionCell extends JPanel {
	private static final long serialVersionUID = -6646136351553964151L;
	
	private final JButton btnLocate;
	private final JButton btnEdit;
	private final JButton btnDelete;
	private final Collection<ActionCellListener> actionCellListeners = new ArrayList<ActionCellListener>();

	/**
	 * Create a cell with three buttons (Locate, Edit an Delete)
	 * 
	 * @param index of the ActionCell in the JTable
	 * @param listener of the ActionCell
	 */
	public ActionCell(final int index, ActionCellListener listener) {
		this.setLayout(new FlowLayout());
		this.addActionCellListener(listener);

		
		btnLocate = new JButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.LOCATE));
		btnEdit = new JButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.EDIT));
		btnDelete = new JButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.TRASH));
		
		btnLocate.setOpaque(false);
		btnLocate.setContentAreaFilled(false);
		btnLocate.setBorderPainted(false);
		btnLocate.setFocusable(false);
		btnLocate.setRolloverEnabled(false);

		btnEdit.setOpaque(false);
		btnEdit.setContentAreaFilled(false);
		btnEdit.setBorderPainted(false);
		btnEdit.setFocusable(false);
		btnEdit.setRolloverEnabled(false);

		btnDelete.setOpaque(false);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusable(false);
		btnDelete.setRolloverEnabled(false);

		this.add(btnLocate);
		this.add(btnEdit);
		this.add(btnDelete);
		
		btnLocate.setPreferredSize(new Dimension(30, 30));
		btnEdit.setPreferredSize(new Dimension(30, 30));
		btnDelete.setPreferredSize(new Dimension(30, 30));
		this.setPreferredSize(new Dimension(90, 30));
		
		btnLocate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionCell.this.fireEventLocate(index);
			}
		});
		
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
				/* Change the color of the cell on press event */
				ActionCell.this.setBackground(new Color(.35f, .58f, .92f));
			}
			
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
		};
		
		btnLocate.addMouseListener(defaultMouseListener);
		btnEdit.addMouseListener(defaultMouseListener);
		btnDelete.addMouseListener(defaultMouseListener);
		this.addMouseListener(defaultMouseListener);
	}
	
	/**
	 * Add a listener to the ActionCell
	 * Call for an locate, edit or delete clicked
	 * 
	 * @param listener of the ActionCell
	 */
	private void addActionCellListener(ActionCellListener listener) {
		actionCellListeners.add(listener);
	}
	
	/**
	 * Fire a locate event
	 * 
	 * @param index of the ActionCell in the JTable
	 */
	protected void fireEventLocate(int index) {
		for(ActionCellListener listener : actionCellListeners)
			listener.eventLocate(index);
	}
	
	/**
	 * Fire an edit event
	 * 
	 * @param index of the ActionCell in the JTable
	 */
	protected void fireEventEdit(int index) {
		for(ActionCellListener listener : actionCellListeners)
			listener.eventEdit(index);
	}
	
	/**
	 * Fire a delete event
	 * 
	 * @param index of the ActionCell in the JTable
	 */
	protected void fireEventDelete(int index) {
		for(ActionCellListener listener : actionCellListeners)
			listener.eventDelete(index);
	}
}
