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
package fr.univmlv.IG.BipBip.Event;

import java.util.List;
import java.util.TreeSet;

import fr.univmlv.IG.Utils.SpatialHashing;

public class EventModelImplTreeAdapter implements EventModel {
	public final TreeSet<Event> tree;

	public EventModelImplTreeAdapter() {
		tree = SpatialHashing.createTree();
		tree.addAll(EventModelImpl.getInstance().getEvents());
		
		EventModelImpl.getInstance().addEventListener(new EventModelListener() {
			
			@Override
			public void eventUnconfirmed(int index) {}
			
			@Override
			public void eventConfirmed(int index) {}
			
			@Override
			public void eventRemoved(int index) {
				EventModelImplTreeAdapter.this.remove(EventModelImpl.getInstance().getEvents().get(index));
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
	
	@Override
	public List<? extends Event> getEvents() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<? extends Event> getEventsFromBeginning() {
		throw new UnsupportedOperationException();
	}

	/* Implements */
	@Override
	public void addEvent(Event event) {
		tree.add(event);
	}
	
	@Override
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
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void unconfirm(Event event) {
		throw new UnsupportedOperationException();
	}
}

