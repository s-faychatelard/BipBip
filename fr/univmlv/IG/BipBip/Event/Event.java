package fr.univmlv.IG.BipBip.Event;

import java.util.Date;

public class Event {

    private EventType type;
    private double x,y;
    private long date;
    /**
     * Start to 2 to not be removed immediately after an error of disapprove
     */
    private int counter=2;
    private long dateErrone=0;
    private boolean isAdmin=false;
    
    public Event(EventType type, long date, double x,double y) {
        this.type=type;
        this.date = date;
        this.x=x;
        this.y=y;
        isAdmin=true;
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
    	this.counter++;
    	if (this.counter > 2) {
    		this.counter = 2;
    	}
    }
    
    public void decrementCounter() {
    	if (isAdmin)
    		this.counter=0;
    	else
    		this.counter--;
    	if (this.counter == 0) {
    		this.dateErrone = new Date().getTime();
    	}
    }
}
