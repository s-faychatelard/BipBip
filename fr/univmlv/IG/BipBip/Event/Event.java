package fr.univmlv.IG.BipBip.Event;

import java.util.Date;

public class Event {

    private EventType type;
    private double x,y;
    private long date;
    private long dateErrone=0;
    
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
    
    public long getDateErrone() {
    	return this.dateErrone;
    }
    
    public void incrementCounter() {
    	// TODO count users
    }
    
    public void decrementCounter() {
    	// TODO count users
    	this.invalidate();
    }
    
    public void invalidate() {
    	this.dateErrone = new Date().getTime();
    }
    
    private void setDateErrone(long dateErrone) {
    	this.dateErrone = dateErrone;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result + (int) (dateErrone ^ (dateErrone >>> 32));
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
	
	public static Event fromString(String str) {
		String []elements = str.split(";");
		double x=0,y=0;
		EventType type=null;
		long date=0,dateErrone=0;
		for (int i=0; i<elements.length; i++) {
			date = Long.valueOf(elements[0]);
			dateErrone = Long.valueOf(elements[1]);
			type = EventType.valueOf(elements[2]);
			x = Double.valueOf(elements[3]);
			y = Double.valueOf(elements[4]);
		}
		Event evt = new Event(type, date, x, y);
		evt.setDateErrone(dateErrone);
		return evt;
	}
	
	@Override
	public String toString() {
		return this.date + ";" + this.dateErrone + ";" + this.getType().name() + ";" + this.getX() + ";" + this.getY();
	}
}
