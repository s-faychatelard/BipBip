package fr.univmlv.IG.BipBip.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EventModelImpl implements EventModel {
	
	private final Collection<EventModelListener> eventModelListeners = new ArrayList<EventModelListener>();
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Event> allEvents = new ArrayList<Event>();

	/**
	 * Get all valid events
	 */
	@Override
	public List<? extends Event> getEvents() {
		return Collections.unmodifiableList(events);
	}
	
	/**
	 * Get all valid and invalid events
	 */
	@Override
	public List<? extends Event> getEventsFromBeginning() {
		return Collections.unmodifiableList(allEvents);
	}
	
	/* Listeners */
	/**
	 * Add an EventModelListener
	 * 
	 * @param listener
	 */
	public void addEventListener(EventModelListener listener) {
		eventModelListeners.add(listener);
	}
	
	/**
	 * Fire when an event is added to the list
	 * 
	 * @param event added
	 */
	protected void fireEventAdded(Event event) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventAdded(event);
	}
	
	/**
	 * Fire when an event is modified in the list
	 * 
	 * @param event modified
	 */
	protected void fireEventModify(Event previousEvent, Event event) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventModified(previousEvent, event);
	}

	/**
	 * Fire when an event is removed from the list
	 * 
	 * @param event removed
	 */
	protected void fireEventRemoved(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventRemoved(position);
	}
	
	/**
	 * Fire when an event is confirmed
	 * 
	 * @param event confirmed
	 */
	public void fireEventConfirmed(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventConfirmed(position);
	}
	
	/**
	 * Fire when an event is unconfirmed
	 * 
	 * @param event unconfirmed
	 */
	public void fireEventUnconfirmed(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventUnconfirmed(position);
	}
	
	/* Implements */
	
	/**
	 * Add an event to the list
	 */
	@Override
	public void addEvent(Event event) {
		this.allEvents.add(event);
		/* Do not load if it is already invalid */
		if (event.getEndDate() != 0)
			return;
		this.events.add(event);
		this.fireEventAdded(events.get(events.size()-1));
	}
	
	/**
	 * Modify an event in the list
	 */
	@Override
	public void modifyEvent(Event previousEvent, Event event) {
		/* Because previousEvent reference will be modified */
		Event oldEvent = new Event(previousEvent.getType(), previousEvent.getX(), previousEvent.getY());
		for (Event e : events) {
			if (e.equals(previousEvent)) {
				e.updateEvent(event);
				this.fireEventModify(oldEvent, e);
				break;
			}
		}
	}

	/**
	 * Remove an event at index i in the list
	 */
	@Override
	public void remove(int i) {
		this.fireEventRemoved(i);
		this.events.get(i).invalidate();
		this.events.remove(i);
	}
	
	/**
	 * Remove an event from the list
	 */
	@Override
	public void remove(Event event) {
		for (Event e : events) {
			if (e.equals(event)) {
				this.remove(events.indexOf(e));
				break;
			}
		}
	}
	
	/**
	 * Confirm an event
	 */
	@Override
	public void confirm(Event event) {
		for (Event e : events) {
			if (e.equals(event)) {
				e.incrementCounter();
				this.fireEventConfirmed(events.indexOf(e));
				break;
			}
		}
	}
	
	/**
	 * Unconfirm an event
	 */
	@Override
	public void unconfirm(Event event) {
		for (Event e : events) {
			if (e.equals(event)) {
				e.decrementCounter();
				this.fireEventUnconfirmed(events.indexOf(e));
				if (e.getEndDate() != 0)
					this.remove(events.indexOf(e));
				break;
			}
		}
	}
}
