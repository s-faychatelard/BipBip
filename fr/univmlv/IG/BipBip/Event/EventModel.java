package fr.univmlv.IG.BipBip.Event;

import java.util.List;

public interface EventModel {
	public List<? extends Event> getEvents();
	public List<? extends Event> getEventsFromBeginning();
	public void addEvent(Event event);
	public void modifyEvent(Event previousEvent, Event event);
	public void remove(int i);
	public void remove(Event event);
	
	public void confirm(Event event);
	public void unconfirm(Event event);
}
