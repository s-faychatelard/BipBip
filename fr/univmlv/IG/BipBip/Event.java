package fr.univmlv.IG.BipBip;

public class Event {

    private EventType type;
    private double x,y;
    
    public Event(EventType type,double x,double y) {
        this.type=type;
        this.x=x;
        this.y=y;        
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
    
}
