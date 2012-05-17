package fr.univmlv.IG.BipBip.Map;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModel;
import fr.univmlv.IG.BipBip.Pin.Pin;

public class TimelineMap {
	
	private final MapPanel map;
	private final EventModel events;
	final ArrayList<Pin> pins = new ArrayList<Pin>();
	
	public TimelineMap(EventModel events) {
		this.events = events;
		
		map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        map.getControlPanel().setVisible(false);
        map.setUseAnimations(false);
		
		/* Replace pins and tooltip on map position event */
		map.addPropertyChangeListener("mapPosition", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				for(Pin pin : pins) {
					pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
					pin.repaint();
				}
				map.repaint();
			}
		});
	}
	
	public void setTime(int day, int hour) {
		ArrayList<Pin> tmpPins = new ArrayList<Pin>();
		for (Event e : events.getEventsFromBeginning()) {
			GregorianCalendar cEvent = new GregorianCalendar();
			cEvent.setTimeInMillis(e.getDate());
			
			GregorianCalendar cEventEnd = new GregorianCalendar();
			cEventEnd.setTimeInMillis(e.getEndDate());
			if (day == cEvent.get(Calendar.DAY_OF_WEEK) && hour == cEvent.get(Calendar.HOUR_OF_DAY)) {
				tmpPins.add(this.createPin(e));
			}
		}
		for (Pin p : pins)
			map.remove(p);
		pins.clear();
		pins.addAll(tmpPins);
		this.putPinsOnMap();
	}

	private Pin createPin(Event event) {
		/* Create pin */
		final Pin pin = new Pin(new Point.Double(event.getX(), event.getY()), event.getType(), "", false);
		pin.setLocation(MapPanel.lon2position(pin.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getCoords().y, map.getZoom()) - map.getMapPosition().y);
        return pin;
	}
	
	private void putPinsOnMap() {
		for (Pin p : pins)
			map.add(p);
		map.repaint();
	}
	
	public MapPanel getMapPanel() {
		return map;
	}
}
