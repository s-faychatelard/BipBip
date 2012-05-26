package fr.univmlv.IG.Utils;

import java.util.Comparator;
import java.util.TreeSet;

import fr.univmlv.IG.BipBip.Event.Event;

public class SpatialHashing {

   private static final int HASH_LENGHT = 23; // (superficie de la terre : 510 067 420 / (4^21) = 0,115 m² theoriquement pour chaque zone)
   private static final int MAX_LAT = 90;
   private static final int MAX_LNG = 180;


   public static TreeSet<Event> createTree() {
       return new TreeSet<Event> (new Comparator<Event>() {
           @Override
           public int compare(Event arg0, Event arg1) {
               return arg0.getSpatialHash().compareTo(arg1.getSpatialHash());
           }
       });
   }


   public static void main(String[] args) {

       TreeSet<Event> tree = new TreeSet<Event>(new Comparator<Event>() {

           @Override
           public int compare(Event arg0, Event arg1) {
               return arg0.getSpatialHash().compareTo(arg1.getSpatialHash());
           }


       });

//        for(BigDecimal i=new BigDecimal(0); i.compareTo(new BigDecimal(20))<1; i = new BigDecimal(0.0001).add(i)) // en gros chaque point ecarté de 10m
//            tree.add(new Event(EventType.ACCIDENT, 100000, i.doubleValue(), i.doubleValue()));

   }

   public static String compute(double lat, double lng) {
       return computeRec(lat+MAX_LAT, lng+MAX_LNG, MAX_LAT, MAX_LNG, new StringBuffer(), 1);
   }

   private static String computeRec(double lat, double lng, double compLat, double compLng, StringBuffer sb, double level) {

       if (level >= Math.pow(2,HASH_LENGHT))
           return sb.toString();

       if(lat < compLat) { // NORTH
           if(lng < compLng) { // WEST
               sb.append('0');
               return computeRec(lat,  lng, compLat-MAX_LAT/level, compLng-MAX_LNG/level,  sb,  level*2);
           }
           else { // EAST
               sb.append('1');
               return computeRec(lat,  lng, compLat-MAX_LAT/level, compLng+MAX_LNG/level,  sb,  level*2);
           }
       }

       else {  // SOUTH
           if(lng < compLng) { // WEST
               sb.append('2');
               return computeRec(lat,  lng, compLat+MAX_LAT/level, compLng-MAX_LNG/level,  sb,  level*2);
           }
           else { // EAST
               sb.append('3');
               return computeRec(lat,  lng, compLat+MAX_LAT/level, compLng+MAX_LNG/level,  sb,  level*2);
           }
       }
   }
}