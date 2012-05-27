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
