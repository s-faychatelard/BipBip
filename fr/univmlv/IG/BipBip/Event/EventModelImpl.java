package fr.univmlv.IG.BipBip.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EventModelImpl implements EventModel {
	
	private final Collection<EventModelListener> eventModelListeners = new ArrayList<EventModelListener>();
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Event> allEvents = new ArrayList<Event>();

	@Override
	public List<? extends Event> getEvents() {
		return Collections.unmodifiableList(events);
	}
	
	@Override
	public List<? extends Event> getEventsFromBeginning() {
		return Collections.unmodifiableList(allEvents);
	}
	
	/* Listeners */
	public void addEventListener(EventModelListener listener) {
		eventModelListeners.add(listener);
	}
	
	protected void fireEventsAdded(List<? extends Event> events) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventsAdded(events);
	}
	
	protected void fireEventAdded(Event event, int index) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventAdded(event, index);
	}
	
	protected void fireEventRemoved(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventRemoved(position);
	}
	
	/* Implements */
	@Override
	public void addEvents(List<? extends Event> events) {
		this.events.addAll(events);
		allEvents.addAll(events);
		this.fireEventsAdded(events);
	}
	
	@Override
	public void addEvent(Event event) {
		events.add(event);
		allEvents.add(event);
		this.fireEventAdded(events.get(events.size()-1), events.size()-1);
	}

	@Override
	public void remove(int i) {
		events.remove(i);
		this.fireEventRemoved(i);
	}
}
