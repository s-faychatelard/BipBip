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
	
	public long getMin() {
		long min=Long.MAX_VALUE;
		for (Event evt : allEvents) {
			if (evt.getDate() < min)
				min = evt.getDate();
		}
		return min;
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
	
	protected void fireEventModify(Event event, int index) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventModified(event, index);
	}
	
	protected void fireEventRemoved(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventRemoved(position);
	}
	
	public void fireEventConfirmed(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventConfirmed(position);
	}
	
	public void fireEventUnconfirmed(int position) {
		for(EventModelListener listener : eventModelListeners)
			listener.eventUnconfirmed(position);
	}
	
	/* Implements */
	@Override
	public void addEvents(List<? extends Event> events) {
		this.events.addAll(events);
		this.allEvents.addAll(events);
		this.fireEventsAdded(events);
	}
	
	@Override
	public void addEvent(Event event) {
		this.allEvents.add(event);
		/* Do not load if it is already invalid */
		if (event.getEndDate() != 0)
			return;
		this.events.add(event);
		this.fireEventAdded(events.get(events.size()-1), events.size()-1);
	}
	
	@Override
	public void modifyEvent(int index, Event event) {
		this.events.get(index).updateEvent(event);
		this.fireEventModify(this.events.get(index), index);
	}

	@Override
	public void remove(int i) {
		this.fireEventRemoved(i);
		this.events.get(i).invalidate();
		this.events.remove(i);
	}
	
	@Override
	public void remove(Event event) {
		for (Event e : events) {
			if (e.equals(event)) {
				this.remove(events.indexOf(e));
				break;
			}
		}
	}
	
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
