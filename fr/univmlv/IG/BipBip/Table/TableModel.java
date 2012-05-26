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
import fr.univmlv.IG.BipBip.Event.EventModel;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = -3121191093975659662L;
	
	private final Collection<TableListener> tableListeners = new ArrayList<TableListener>();
	
	private final String[] columns = { "Longitude", "Latitude", "Date", "Type", "Actions" };
	
	private final EventModel events;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	public TableModel(EventModel events) {
		this.events = events;
		
		((EventModelImpl)events).addEventListener(new EventModelListener() {
			
			@Override
			public void eventAdded(Event event) {
				TableModel.this.fireTableRowsInserted(TableModel.this.events.getEvents().indexOf(event), TableModel.this.events.getEvents().indexOf(event));
			}
			
			@Override
			public void eventModified(Event previousEvent, Event event) {
				TableModel.this.fireTableRowsUpdated(TableModel.this.events.getEvents().indexOf(event), TableModel.this.events.getEvents().indexOf(event));
			}
			
			@Override
			public void eventRemoved(int index) {
				TableModel.this.fireTableRowsDeleted(index, index);
			}
			
			@Override
			public void eventConfirmed(int index) {}
			
			@Override
			public void eventUnconfirmed(int index) {}
		});
	}
	
	public void addTableListener(TableListener listener) {
		tableListeners.add(listener);
	}
	
	protected void fireLocateEventAtIndex(int index) {
		for (TableListener listener : tableListeners) {
			listener.eventLocateEventAtIndex(index);
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columns[columnIndex].compareTo("Actions") == 0)
			return true;
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.valueOf(events.getEvents().get(rowIndex).getX());
		case 1:
			return String.valueOf(events.getEvents().get(rowIndex).getY());
		case 2:
			return dateFormatter.format(events.getEvents().get(rowIndex).getDate());
		case 3:
			switch (events.getEvents().get(rowIndex).getType()) {
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
		case 4:
			return new ActionCell(rowIndex, new ActionCellListener() {
				@Override
				public void eventEdit(int index) {
					EditDialog editDialog = new EditDialog(BipbipServer.frame, ((EventModelImpl)events).getEvents().get(index), true);
					if (editDialog.getAnswer() == AnswerEditDialog.SAVE) {
						((EventModelImpl)events).modifyEvent(((EventModelImpl)events).getEvents().get(index), editDialog.getEvent());
					}
				}
				
				@Override
				public void eventDelete(int index) {
					((EventModelImpl)events).remove(index);
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
	
	@Override
	public int getRowCount() {
		return events.getEvents().size();
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columns[columnIndex].compareTo("Type") == 0)
			 return Icon.class;
		if(columns[columnIndex].compareTo("Actions") == 0)
			 return ActionCell.class; 
		return String.class;
	}
}
