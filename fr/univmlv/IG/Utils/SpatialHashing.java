package fr.univmlv.IG.Utils;

import java.util.Comparator;
import java.util.TreeSet;

import fr.univmlv.IG.BipBip.Event.Event;

public class SpatialHashing {

   private static final double EARTH_DIAMETER = 12756.32;
   public static final int HASH_DEFAULT_LENGHT = 17; // (superficie de la terre : 510 067 420 / (4^17) = 0,0296898314 km2 theoriquement pour chaque zone)
   
   private static final int MAX_LAT = 90;
   private static final int MAX_LNG = 180;
   
   /**
    * @return a lexicographic treeSet of Events
    */
   public static TreeSet<Event> createTree() {
       return new TreeSet<Event> (new Comparator<Event>() {
           @Override
           public int compare(Event e1, Event e2) {
               return e1.getSpatialHash().compareTo(e2.getSpatialHash());
           }
       });
   }

   /**
    * @param lat
    * @param lng
    * @return the spatial hash of the given coordinates
    * The hash is computed using the quadrants recursive decomposition
    */
   public static String compute(double lat, double lng) {
	   if((lat > MAX_LAT && lat < -MAX_LAT) || (lng > MAX_LNG  && lng < -MAX_LNG))
		   throw new IllegalArgumentException();
	   
       return computeRec(lat+MAX_LAT, lng+MAX_LNG, MAX_LAT, MAX_LNG, new StringBuffer(), 2, 0);
   }

   /**
    * 
    * @param lat
    * @param lng
    * @param compLat
    * @param compLng
    * @param sb : StringBuilder used to build the hash
    * @param level
    * @param counter : number of recursive call
    * @return
    */
   private static String computeRec(double lat, double lng, double compLat, double compLng, StringBuffer sb, double level, int counter) {
       if (counter == HASH_DEFAULT_LENGHT)
           return sb.toString();
       
       counter++;

       if(lat > compLat) { // NORTH
           if(lng < compLng) { // WEST
               sb.append('0');
               return computeRec(lat,  lng, compLat+MAX_LAT/level, compLng-MAX_LNG/level,  sb,  level*2, counter);
           }
           else { // EAST

               sb.append('1');
               return computeRec(lat,  lng, compLat+MAX_LAT/level, compLng+MAX_LNG/level,  sb,  level*2, counter);
           }
       }

       else {  // SOUTH
           if(lng < compLng) { // WEST

               sb.append('2');
               return computeRec(lat,  lng, compLat-MAX_LAT/level, compLng-MAX_LNG/level,  sb,  level*2, counter);
           }
           else { // EAST
               sb.append('3');
               return computeRec(lat,  lng, compLat-MAX_LAT/level, compLng+MAX_LNG/level,  sb,  level*2, counter);
           }
       }
   }
   
   /**
    * @param lat1
    * @param lng1
    * @param lat2
    * @param lng2
    * @return distance between the two points in km
    */
	public static double computeDistance(double lat1, double lng1, double lat2, double lng2) {   	    	
		double dLatBy2 = Math.toRadians(lat2-lat1) / 2;
		double dLngBy2 = Math.toRadians(lng2-lng1) / 2;
		return EARTH_DIAMETER * Math.asin(Math.sqrt(Math.sin(dLatBy2) * Math.sin(dLatBy2) + Math.cos(lat1) * Math.cos(lat2) *  Math.sin(dLngBy2) * Math.sin(dLngBy2)));
	}
}