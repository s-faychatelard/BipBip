package fr.univmlv.IG.BipBip.Table;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
import fr.univmlv.IG.BipBip.Event.EventModel;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Map.Map;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = -3121191093975659662L;
	
	private final String[] columns = { "Longitude", "Latitude", "Date", "Type", "Actions" };
	private final ImageIcon fixe = new ImageIcon(Map.class.getResource("alert-fixe.png"));
	private final ImageIcon mobile = new ImageIcon(Map.class.getResource("alert-mobile.png"));
	private final ImageIcon accident = new ImageIcon(Map.class.getResource("alert-accident.png"));
	private final ImageIcon travaux = new ImageIcon(Map.class.getResource("alert-travaux.png"));
	private final ImageIcon divers = new ImageIcon(Map.class.getResource("alert-divers.png"));
	
	private final EventModel events;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	public TableModel(EventModel events) {
		this.events = events;
		
		((EventModelImpl)events).addEventListener(new EventModelListener() {
			
			@Override
			public void eventsAdded(List<? extends Event> events) {
				TableModel.this.fireTableDataChanged();
			}
			
			@Override
			public void eventAdded(Event event, int index) {
				TableModel.this.fireTableRowsInserted(index, index);
			}
			
			@Override
			public void eventModifyed(Event event, int index) {
				TableModel.this.fireTableRowsUpdated(index, index);
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
				return fixe;
			case RADAR_MOBILE:
				return mobile;
			case ACCIDENT:
				return accident;
			case TRAVAUX:
				return travaux;
			case DIVERS:
				return divers;
			}
		case 4:
			return new ActionCell(rowIndex, new ActionCellListener() {
				@Override
				public void eventEdit(int index) {
					System.out.println("Edit " + index);
				}
				
				@Override
				public void eventDelete(int index) {
					((EventModelImpl)events).remove(index);
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
		return "".getClass();
	}
}
