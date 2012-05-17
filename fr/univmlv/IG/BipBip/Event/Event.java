package fr.univmlv.IG.BipBip.Event;

import java.util.Date;

public class Event {

    private EventType type;
    private double x,y;
    private long date;
    private long endDate=0;
    
    //TODO implement number of users which alert
    
    public Event(EventType type, long date, double x,double y) {
        this.type=type;
        this.date = date;
        this.x=x;
        this.y=y;
    }
    
    public Event(EventType type, double x,double y) {
    	this(type, new Date().getTime(), x, y);
    }

    public EventType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public long getDate() {
    	return this.date;
    }
    
    public long getEndDate() {
    	return this.endDate;
    }
    
    public void incrementCounter() {
    	// TODO count users
    }
    
    public void decrementCounter() {
    	// TODO count users
    	this.invalidate();
    }
    
    public void invalidate() {
    	this.endDate = new Date().getTime();
    }
    
    public void updateEvent(Event newEvent) {
    	this.type = newEvent.type;
    	this.x = newEvent.x;
    	this.y = newEvent.y;
    }
    
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
//		TODO : pas d'arrondi mais utiliser methode pour voir si deux points sont au même endroit
		if (Double.doubleToLongBits((double)Math.round(x * 10000000) / 10000000) != Double.doubleToLongBits((double)Math.round(other.x * 10000000) / 10000000))
			return false;
		if (Double.doubleToLongBits((double)Math.round(y * 10000000) / 10000000) != Double.doubleToLongBits((double)Math.round(other.y * 10000000) / 10000000))
			return false;
		return true;
	}
	
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
	
	@Override
	public String toString() {
		return this.date + ";" + this.endDate + ";" + this.getType().name() + ";" + this.getX() + ";" + this.getY();
	}
}
