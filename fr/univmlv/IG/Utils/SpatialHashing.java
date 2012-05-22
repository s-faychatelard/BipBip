package fr.univmlv.IG.Utils;

public class SpatialHashing {
	
	public static int MAX_LAT = 90; 
	public static int MAX_LNG = 180; 

	
	public static void main(String[] args) {
		System.out.println(compute(14.2f, 13.3f));
		System.out.println(compute(-14.2f, 13.3f));

	}
	
	public static String compute(float lat, float lng) {
		
		return computeRec(lat+90, lng+180, new StringBuffer(), 2);
	}
	
	private static String computeRec(float lat, float lng, StringBuffer sb, int level) {
		
		if(lat > MAX_LAT/level) { // NORTH
			if(lng < MAX_LNG/level) { // WEST
				sb.append('0');
			}
			else { // EAST
				sb.append('1');
			}
			
		}
		
		else {  // SOUTH
			if(lng < MAX_LNG/level) { // WEST
				sb.append('2');
			}
			else { // EAST
				sb.append('3');
			}
		}
		
		if (level >= Math.pow(2, 15))
			return sb.toString();
		
		return computeRec(lat,  lng,  sb,  level*2);
	}

}
