package fr.univmlv.IG.BipBip.Event;

public interface EventModelListener {
	void eventAdded(Event event);
	void eventModified(Event previousEvent, Event event);
	void eventRemoved(int index);
	
	void eventConfirmed(int index);
	void eventUnconfirmed(int index);
}
