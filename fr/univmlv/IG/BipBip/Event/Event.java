package fr.univmlv.IG.BipBip.Event;

import java.util.Date;

public class Event {

    private EventType type;
    private double x,y;
    private long date;
    private long endDate=0;
    
    //TODO implement number of users which alert
    
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
    }
    
    /**
     * Create an event
     * 
     * @param type of the event
     * @param x longitude of the event
     * @param y latitude of the event
     */
    public Event(EventType type, double x,double y) {
    	this(type, new Date().getTime(), x, y);
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
     * This is call on a confirmation from a client
     */
    public void incrementCounter() {
    	// TODO count users
    }
    
    /**
     * This is call when a client unconfirm the event
     */
    public void decrementCounter() {
    	// TODO count users
    	this.invalidate();
    }
    
    /**
     * Set the event has ended
     */
    public void invalidate() {
    	this.endDate = new Date().getTime();
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
    
    /**
     * Set the end date of the event
     * 
     * @param endDate
     */
    private void setEndDate(long endDate) {
    	this.endDate = endDate;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result + (int) (endDate ^ (endDate >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
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
		if (Double.doubleToLongBits((double)Math.round(x * 10000000) / 10000000) != Double.doubleToLongBits((double)Math.round(other.x * 10000000) / 10000000))
			return false;
		if (Double.doubleToLongBits((double)Math.round(y * 10000000) / 10000000) != Double.doubleToLongBits((double)Math.round(other.y * 10000000) / 10000000))
			return false;
		return true;
	}
	
	/**
	 * Create an event from a formatted string
	 * Date;EndDate;Type;X;Y
	 * 
	 * @param str
	 * @return the generate event
	 */
	public static Event fromString(String str) {
		String []elements = str.split(";");
		double x=0,y=0;
		EventType type=null;
		long date=0,endDate=0;
		for (int i=0; i<elements.length; i++) {
			date = Long.valueOf(elements[0]);
			endDate = Long.valueOf(elements[1]);
			type = EventType.valueOf(elements[2]);
			x = Double.valueOf(elements[3]);
			y = Double.valueOf(elements[4]);
		}
		Event evt = new Event(type, date, x, y);
		evt.setEndDate(endDate);
		return evt;
	}
	
	/**
	 * Create a formatted string of the event
	 * Date;EndDate;Type;X;Y
	 */
	@Override
	public String toString() {
		return this.date + ";" + this.endDate + ";" + this.getType().name() + ";" + this.getX() + ";" + this.getY();
	}
}
