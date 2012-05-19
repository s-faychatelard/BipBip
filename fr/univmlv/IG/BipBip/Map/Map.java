package fr.univmlv.IG.BipBip.Map;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
import fr.univmlv.IG.BipBip.Event.EventModel;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Pin.Pin;
import fr.univmlv.IG.BipBip.Pin.PinListener;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;
import fr.univmlv.IG.BipBip.Tooltip.Tooltip;
import fr.univmlv.IG.BipBip.Tooltip.TooltipListener;

public class Map {
	
	private final MapPanel map;
	private final EventModel events;
	final ArrayList<Pin> pins = new ArrayList<Pin>();
	
	public Map(EventModel events) {
		this.events = events;
		
		map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        map.getControlPanel().setVisible(false);
        map.setUseAnimations(false);
        
        /* Tooltip to add other alert */
        final Tooltip tooltipAlert = new Tooltip();
        tooltipAlert.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.FIXE), "Radar fixe");
        tooltipAlert.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.MOBILE), "Radar mobile");
        tooltipAlert.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.ACCIDENT), "Accident");
        tooltipAlert.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.TRAVAUX), "Travaux");
        tooltipAlert.addButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Alert.DIVERS), "Divers").setLast(true);
        
        tooltipAlert.addTooltipListener(new TooltipListener() {
			@Override
			public void eventSelectedAtIndex(int index) {
				Point.Double coords = map.getLongitudeLatitude(new Point(tooltipAlert.getLocation().x + map.getMapPosition().x, tooltipAlert.getLocation().y + map.getMapPosition().y));
				
				switch (index) {
				case 0:
					Map.this.events.addEvent(new Event(EventType.RADAR_FIXE, new Date().getTime(), coords.x, coords.y));
					break;
				case 1:
					Map.this.events.addEvent(new Event(EventType.RADAR_MOBILE, new Date().getTime(), coords.x, coords.y));
					break;
				case 2:
					Map.this.events.addEvent(new Event(EventType.ACCIDENT, new Date().getTime(), coords.x, coords.y));
					break;
				case 3:
					Map.this.events.addEvent(new Event(EventType.TRAVAUX, new Date().getTime(), coords.x, coords.y));
					break;
				case 4:
					Map.this.events.addEvent(new Event(EventType.DIVERS, new Date().getTime(), coords.x, coords.y));
					break;
				default:
					assert(0==1);
					break;
				}
				map.remove(tooltipAlert);
				map.repaint();
			}
		});
		
		((EventModelImpl)events).addEventListener(new EventModelListener() {
			
			@Override
			public void eventsAdded(List<? extends Event> events) {
				for(Event event : events)
					Map.this.addPin(event);
				map.repaint();
			}
			
			@Override
			public void eventAdded(Event event, int index) {
				Map.this.addPin(event);
				map.repaint();
			}
			
			@Override
			public void eventModified(Event event, int index) {
				map.remove(pins.get(index));
				pins.remove(index);
				Map.this.addPin(event, index);
				map.repaint();
			}
			
			@Override
			public void eventRemoved(int index) {
				map.remove(pins.get(index));
				pins.remove(index);
				map.repaint();
			}
			
			@Override
			public void eventConfirmed(int index) {}
			
			@Override
			public void eventUnconfirmed(int index) {}
		});
		
		/* Replace pins and tooltip on map position event */
		map.addPropertyChangeListener("mapPosition", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				for(Pin pin : pins) {
					pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
					pin.repaint();
				}
				tooltipAlert.setLocation(MapPanel.lon2position(tooltipAlert.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(tooltipAlert.getCoords().y, map.getZoom()) - map.getMapPosition().y);
				map.repaint();
			}
		});
		
		/* Just for clear pins on click outside of anything */
        map.addMouseListener(new MouseListener() {
        	private long previousTime = 0;
        	private Point previousPosition = new Point();
        	
			@Override
			public void mouseReleased(MouseEvent e) {
				long currentTime = new Date().getTime();
				if (currentTime - previousTime > 200 && previousPosition.equals(e.getPoint())) {
					tooltipAlert.setLocation(e.getX(), e.getY());
					tooltipAlert.setCoords(map.getLongitudeLatitude(new Point(e.getPoint().x + map.getMapPosition().x, e.getPoint().y + map.getMapPosition().y)));
		        	map.add(tooltipAlert);
				}
				else if (previousPosition.equals(e.getPoint())) {
					map.remove(tooltipAlert);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				previousTime = new Date().getTime();
				previousPosition = e.getPoint();
				for(Pin pin : pins) {
					pin.clear();
				}
				map.repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	public void addPin(Event event, int index) {
		/* Create pin */
		final Pin pin = new Pin(new Point.Double(event.getX(), event.getY()), event.getType(), "Cliquez pour valider ou supprimer");
		pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
        map.add(pin);
        pin.addPinListener(new PinListener() {
        	@Override
        	public void eventSelected() {
        		for(Pin pin : pins) {
					pin.clear();
				}
        		map.repaint();
        	}
        	
			@Override
			public void eventConfirm(boolean confirm) {
				if(confirm) {
					((EventModelImpl)events).confirm(Map.this.events.getEvents().get(pins.indexOf(pin)));
				}
				else {
					((EventModelImpl)events).unconfirm(Map.this.events.getEvents().get(pins.indexOf(pin)));
				}
			}
		});
        pins.add(index, pin);
	}

	public void addPin(Event event) {
		this.addPin(event, pins.size());
	}
	
	public MapPanel getMapPanel() {
		return map;
	}
}
