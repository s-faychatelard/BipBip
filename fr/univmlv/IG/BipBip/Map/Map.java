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
package fr.univmlv.IG.BipBip.Map;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
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
	private final Tooltip tooltipAlert;
	final ArrayList<Pin> pins = new ArrayList<Pin>();
	private static final Object obj = new Object();
	
	/**
	 * Create a Map
	 * 
	 * @param events represent the Model of the application
	 */
	public Map() {		
		map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        map.getControlPanel().setVisible(false);
        map.setUseAnimations(false);
        
        /* Tooltip to add other alert */
        tooltipAlert = new Tooltip();
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
					EventModelImpl.getInstance().addEvent(new Event(EventType.RADAR_FIXE, new Date().getTime(), coords.x, coords.y));
					break;
				case 1:
					EventModelImpl.getInstance().addEvent(new Event(EventType.RADAR_MOBILE, new Date().getTime(), coords.x, coords.y));
					break;
				case 2:
					EventModelImpl.getInstance().addEvent(new Event(EventType.ACCIDENT, new Date().getTime(), coords.x, coords.y));
					break;
				case 3:
					EventModelImpl.getInstance().addEvent(new Event(EventType.TRAVAUX, new Date().getTime(), coords.x, coords.y));
					break;
				case 4:
					EventModelImpl.getInstance().addEvent(new Event(EventType.DIVERS, new Date().getTime(), coords.x, coords.y));
					break;
				default:
					assert(0==1);
					break;
				}
				map.remove(tooltipAlert);
				map.repaint();
			}
		});
		
        /* Model listener */
        EventModelImpl.getInstance().addEventListener(new EventModelListener() {
		
			@Override
			public void eventAdded(Event event) {
				Map.this.addPin(event);
				map.repaint();
			}
			
			@Override
			public void eventModified(Event previousEvent, Event event) {
				synchronized (obj) {
					for (Pin pin : pins) {
						if (pin.getEvent().getSpatialHash().equals(previousEvent.getSpatialHash())) {
							map.remove(pin);
							pins.remove(pin);
							Map.this.addPin(event);
							break;
						}
					}
				}
				map.repaint();
			}
			
			@Override
			public void eventRemoved(int index) {
				synchronized (obj) {
					map.remove(pins.get(index));
					pins.remove(index);
					map.repaint();
				}
			}
			
			@Override
			public void eventConfirmed(int index) {}
			
			@Override
			public void eventUnconfirmed(int index) {}
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
	
	/**
	 * Add a pin on the map
	 * 
	 * @param event of the Pin
	 * @param index to put in list of pins
	 */
	public void addPin(Event event, int index) {
		/* Create pin */
		final Pin pin = new Pin(event, "Cliquez pour valider ou supprimer");
		pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);
		synchronized (obj) {
			map.add(pin);
        	pins.add(index, pin);
		}
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
					EventModelImpl.getInstance().confirm(EventModelImpl.getInstance().getEvents().get(pins.indexOf(pin)));
				}
				else {
					EventModelImpl.getInstance().unconfirm(EventModelImpl.getInstance().getEvents().get(pins.indexOf(pin)));
				}
			}
		});
	}

	/**
	 * Add a pin on the map
	 * 
	 * @param event of the Pin
	 */
	public void addPin(Event event) {
		this.addPin(event, pins.size());
	}
	
	/**
	 * Refresh pins on the map
	 */
	public void refreshPins() {
		synchronized (obj) {
			for(Pin pin : pins) {
				pin.setLocation(MapPanel.lon2position(pin.getEvent().getX(), map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(pin.getEvent().getY(), map.getZoom()) - map.getMapPosition().y);
				pin.repaint();
			}
			tooltipAlert.setLocation(MapPanel.lon2position(tooltipAlert.getCoords().x, map.getZoom()) - map.getMapPosition().x, MapPanel.lat2position(tooltipAlert.getCoords().y, map.getZoom()) - map.getMapPosition().y);
		}
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
