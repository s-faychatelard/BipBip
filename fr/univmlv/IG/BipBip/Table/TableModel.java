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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import fr.univmlv.IG.BipBip.BipbipServer;
import fr.univmlv.IG.BipBip.EditDialog.EditDialog;
import fr.univmlv.IG.BipBip.EditDialog.EditDialog.AnswerEditDialog;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

/**
 * TableModel manage the JTable
 */
public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = -3121191093975659662L;
	
	private final Collection<TableListener> tableListeners = new ArrayList<TableListener>();
	
	private final String[] columns = { "Longitude", "Latitude", "Date", "Fiabilité", "Type", "Actions" };
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	/**
	 * Create a table model with the model of the application
	 * 
	 * @param events represent the model of the application
	 */
	public TableModel() {		
		EventModelImpl.getInstance().addEventListener(new EventModelListener() {
			
			@Override
			public void eventAdded(Event event) {
				TableModel.this.fireTableRowsInserted(EventModelImpl.getInstance().getEvents().indexOf(event), EventModelImpl.getInstance().getEvents().indexOf(event));
			}
			
			@Override
			public void eventModified(Event previousEvent, Event event) {
				TableModel.this.fireTableRowsUpdated(EventModelImpl.getInstance().getEvents().indexOf(event), EventModelImpl.getInstance().getEvents().indexOf(event));
			}
			
			@Override
			public void eventRemoved(int index) {
				TableModel.this.fireTableRowsDeleted(index, index);
			}
			
			@Override
			public void eventConfirmed(int index) {
				TableModel.this.fireTableRowsUpdated(index, index);
			}
			
			@Override
			public void eventUnconfirmed(int index) {
				TableModel.this.fireTableRowsUpdated(index, index);
			}
		});
	}
	
	/**
	 * Add a TableListener
	 * 
	 * @param listener
	 */
	public void addTableListener(TableListener listener) {
		tableListeners.add(listener);
	}
	
	/**
	 * Fire a locate event
	 * 
	 * @param index of cell in the JTable
	 */
	protected void fireLocateEventAtIndex(int index) {
		for (TableListener listener : tableListeners) {
			listener.eventLocateEventAtIndex(index);
		}
	}
	
	/**
	 * Return the information of edition (Only for ActionCell to permit catch of event in JButton)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columns[columnIndex].compareTo("Actions") == 0)
			return true;
		return false;
	}
	
	/**
	 * Return the value of a specific row and column
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.valueOf(EventModelImpl.getInstance().getEvents().get(rowIndex).getX());
		case 1:
			return String.valueOf(EventModelImpl.getInstance().getEvents().get(rowIndex).getY());
		case 2:
			return dateFormatter.format(EventModelImpl.getInstance().getEvents().get(rowIndex).getDate());
		case 3:
			return String.valueOf(EventModelImpl.getInstance().getEvents().get(rowIndex).getReliability());
		case 4:
			switch (EventModelImpl.getInstance().getEvents().get(rowIndex).getType()) {
			case RADAR_FIXE:
				return ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.FIXE);
			case RADAR_MOBILE:
				return ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.MOBILE);
			case ACCIDENT:
				return ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.ACCIDENT);
			case TRAVAUX:
				return ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.TRAVAUX);
			case DIVERS:
				return ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.DIVERS);
			}
		case 5:
			return new ActionCell(rowIndex, new ActionCellListener() {
				@Override
				public void eventEdit(int index) {
					EditDialog editDialog = new EditDialog(BipbipServer.frame, EventModelImpl.getInstance().getEvents().get(index), true);
					if (editDialog.getAnswer() == AnswerEditDialog.SAVE) {
						EventModelImpl.getInstance().modifyEvent(EventModelImpl.getInstance().getEvents().get(index), editDialog.getEvent());
					}
				}
				
				@Override
				public void eventDelete(int index) {
					EventModelImpl.getInstance().remove(index);
				}
				
				@Override
				public void eventLocate(int index) {
					TableModel.this.fireLocateEventAtIndex(index);
				}
			});
		default:
			break;
		}
		return null;
	}
	
	/**
	 * Return the number of elements in the table
	 */
	@Override
	public int getRowCount() {
		return EventModelImpl.getInstance().getEvents().size();
	}
	
	/**
	 * Return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}
	
	/**
	 * Return the number of column
	 */
	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	/**
	 * Return the class of cells in a specific column
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columns[columnIndex].compareTo("Type") == 0)
			 return Icon.class;
		if(columns[columnIndex].compareTo("Actions") == 0)
			 return ActionCell.class; 
		return String.class;
	}
}
