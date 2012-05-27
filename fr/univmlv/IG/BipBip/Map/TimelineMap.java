package fr.univmlv.IG.BipBip.Map;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Pin.Pin;

public class TimelineMap {
	
	private final MapPanel map;
	final ArrayList<Pin> pins = new ArrayList<Pin>();
	
	/**
	 * Create a TimelineMap
	 * 
	 * @param events represent the Model of the application
	 */
	public TimelineMap() {		
		map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        map.getControlPanel().setVisible(false);
        map.setUseAnimations(false);
		
		/* Replace pins and tooltip on map position event */
		map.addPropertyChangeListener("mapPosition", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				for(Pin pin : pins) {
					pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);
					pin.repaint();
				}
				map.repaint();
			}
		});
	}
	
	/**
	 * Load all pins in the TimelineMap for a specific time
	 * 
	 * @param day
	 * @param hour
	 */
	public void setTime(int day, int hour) {
		ArrayList<Pin> tmpPins = new ArrayList<Pin>();
		for (Event e : EventModelImpl.getInstance().getEventsFromBeginning()) {
			Calendar cEvent = Calendar.getInstance(Locale.US);
			cEvent.setTimeInMillis(e.getDate());
			
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

	/**
	 * Create a pin
	 * 
	 * @param event of the pin
	 * @return the created pin
	 */
	private Pin createPin(Event event) {
		/* Create pin */
		final Pin pin = new Pin(event, "", false);
		pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);
        return pin;
	}
	
	/**
	 * Add pins on the map
	 */
	private void putPinsOnMap() {
		for (Pin p : pins)
			map.add(p);
		map.repaint();
	}
	
	/**
	 * Return the MapPanel component
	 * 
	 * @return the MapPanel
	 */
	public MapPanel getMapPanel() {
		return map;
	}
}
