package fr.univmlv.IG.BipBip.Event;

import java.util.List;

public interface EventModelListener {
	void eventsAdded(List<? extends Event> events);
	void eventAdded(Event event);
	void eventModified(Event previousEvent, Event event);
	void eventRemoved(int index);
	
	void eventConfirmed(int index);
	void eventUnconfirmed(int index);
}
