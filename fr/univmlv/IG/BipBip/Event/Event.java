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

import java.util.Calendar;
import java.util.Locale;

import fr.univmlv.IG.Utils.SpatialHashing;

public class Event {

   private EventType type = EventType.ACCIDENT;
   private double x,y;
   private long date;
   private long endDate=0;
   private int reliability = 1;
   private String spatialHash = null;

   /**
    * Create an event
    *
    * @param type of the event
    * @param date of the event
    * @param x longitude of the event
    * @param y latitude of the event
    */
   public Event(EventType type, long date, double x,double y) {
       this.type=type;
       this.date = date;
       this.x=x;
       this.y=y;
       this.spatialHash = SpatialHashing.compute(y, x);
   }

   /**
    * Create an event
    *
    * @param type of the event
    * @param x longitude of the event
    * @param y latitude of the event
    */
   public Event(EventType type, double x,double y) {
       this(type, Calendar.getInstance(Locale.FRANCE).getTimeInMillis(), x, y);
   }
   
   /**
    * Private constructor allowing to genereate mock events with a given spatialHash
    * (useful sosearch Events in a given area) 
    * @param spatialHash
    */
   private Event(String spatialHash) {
       this.spatialHash = spatialHash;
   }

   /**
    * Private constructor allowing to specify the spatialHash (avid to compute a new one 
    * if it has previously done)
    * used by fromString()
    */
   private Event(EventType type, long date, double x, double y, String spatialHash) {
	   this.type=type;
       this.date = date;
       this.x=x;
       this.y=y;
       this.spatialHash = spatialHash;
   }

   /**
    * Get the type of the event
    *
    * @return the type
    */
   public EventType getType() {
       return type;
   }

   /**
    * Get the longitude of the event
    *
    * @return the longitude
    */
   public double getX() {
       return x;
   }

   /**
    * Get the latitude of the event
    *
    * @return the latitude
    */
   public double getY() {
       return y;
   }

   /**
    * Get the begin date
    * This is a timestamp
    *
    * @return the begin date
    */
   public long getDate() {
       return this.date;
   }

   /**
    * Get the end date of the event
    * This is a timestamp
    *
    * @return the end date
    */
   public long getEndDate() {
       return this.endDate;
   }
   
   /**
    * Get the reliability of the event
    *
    * @return the latitude
    */
   public int getReliability() {
       return this.reliability;
   }

   /**
    * This is call on a confirmation from a client
    */
   public void incrementCounter() {
       this.reliability++;
   }

   /**
    * This is call when a client unconfirm the event
    */
   public void decrementCounter() {
	   this.invalidate();   
   }

   /**
    * Set the event has ended
    */
   public void invalidate() {
       this.endDate = Calendar.getInstance(Locale.US).getTimeInMillis();
   }

   /**
    * Update the event
    *
    * @param newEvent is the new value of the event
    */
   public void updateEvent(Event newEvent) {
       this.type = newEvent.type;
       this.x = newEvent.x;
       this.y = newEvent.y;
   }

   public String getSpatialHash() {
       return this.spatialHash;
   }
   
   /**
    * Check type and distance between to Event
    * 
    * @param other
    * 
    * @return true if is similar or false
    */
   public boolean isSame(Event other) {
       if (type != other.type)
           return false;
       if (SpatialHashing.computeDistance(this.getX(), this.getY(), other.getX(), other.getY()) > .5f)
    	   return false;
       return true;
   }
   
   /**
    * DO NOT COMPARE DATE
    */
   @Override
   public boolean equals(Object obj) {
       if (this == obj)
           return true;
       if (obj == null)
           return false;
       if (getClass() != obj.getClass())
           return false;
       Event other = (Event) obj;
       if (type != other.type)
           return false;
       if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
           return false;
       if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
           return false;
       return true;
   }


   /**
    * Create an event from a formatted string
    * Hash;Date;EndDate;Type;X;Y;Reliability
    *
    * @param str
    * @return the generate event
    */
   public static Event fromString(String str) {
       String []elements = str.split(";");
       String hash=null;
       long date=0,endDate=0;
       EventType type=null;
       double x=0,y=0;
       int reliability=0;
       for (int i=0; i<elements.length; i++) {
           hash = elements[0];
           date = Long.valueOf(elements[1]);
           endDate = Long.valueOf(elements[2]);
           type = EventType.valueOf(elements[3]);
           x = Double.valueOf(elements[4]);
           y = Double.valueOf(elements[5]);
           reliability = Integer.valueOf(elements[6]);
       }
       Event evt = new Event(type, date, x, y, hash);
       evt.endDate = endDate;
       evt.reliability = reliability;
       return evt;
   }

   /**
    * Create a formatted string of the event
    * Hash;Date;EndDate;Type;X;Y;Reliability
    */
   @Override
   public String toString() {
       return this.spatialHash + ";" + this.date + ";" + this.endDate + ";" + this.getType().name() + ";" + this.getX() + ";" + this.getY() + ";" + this.getReliability();
   }
   
   /**
    * @param spatialHash
    * @return mock Event for the given spatial hash
    */
   public static Event createMockEvent(String spatialHash) {
	   return new Event(spatialHash);
   }
   
}
