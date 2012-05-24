package fr.univmlv.IG.BipBip.Event;

import java.util.List;
import java.util.TreeSet;

import fr.univmlv.IG.Utils.SpatialHashing;

public class EventModelImplTreeAdapter implements EventModel {
	
	public final EventModelImpl model;
	public final TreeSet<Event> tree;

	public EventModelImplTreeAdapter(final EventModelImpl model) {
		this.model = model;
		tree = SpatialHashing.createTree();
		tree.addAll(model.getEvents());
		
		model.addEventListener(new EventModelListener() {
			
			@Override
			public void eventUnconfirmed(int index) {}
			
			@Override
			public void eventConfirmed(int index) {}
			
			@Override
			public void eventsAdded(List<? extends Event> events) {
				EventModelImplTreeAdapter.this.addEvents(events);
			}
			
			@Override
			public void eventRemoved(int index) {
				EventModelImplTreeAdapter.this.remove(model.getEvents().get(index));
			}
			
			@Override
			public void eventModified(Event previousEvent, Event event) {
				EventModelImplTreeAdapter.this.modifyEvent(previousEvent, event);
			}
			
			@Override
			public void eventAdded(Event event) {
				EventModelImplTreeAdapter.this.addEvent(event);
			}
		});
	}
	
	@Override // TODO
	public List<? extends Event> getEvents() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<? extends Event> getEventsFromBeginning() {
		throw new UnsupportedOperationException();
	}

	/* Implements */
	@Override
	public void addEvents(List<? extends Event> events) {		
		tree.addAll(events);
	}
	
	@Override
	public void addEvent(Event event) {
		tree.add(event);
	}
	
	@Override // TODO
	public void modifyEvent(Event previousEvent, Event event) {
		tree.remove(previousEvent);
		tree.add(event);
	}

	@Override
	public void remove(int i) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void remove(Event event) {
		tree.remove(event);
	}
	
	@Override
	public void confirm(Event event) {
		//throw new UnsupportedOperationException();
	}
	
	@Override
	public void unconfirm(Event event) {
		throw new UnsupportedOperationException();
	}
}
