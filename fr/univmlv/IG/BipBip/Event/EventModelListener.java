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

/**
 * Listener of the EventModel
 */
public interface EventModelListener {
	void eventAdded(Event event);
	void eventModified(Event previousEvent, Event event);
	void eventRemoved(int index);
	
	void eventConfirmed(int index);
	void eventUnconfirmed(int index);
}
