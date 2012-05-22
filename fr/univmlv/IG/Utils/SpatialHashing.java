package fr.univmlv.IG.Utils;

public class SpatialHashing {
	
	public static int MAX_LAT = 90; 
	public static int MAX_LNG = 180; 

	
	public static void main(String[] args) {
		
		//assert (compute(14.2f, 13.3f).equals(compute(14.3f, 13.3f)));
		
		
		System.out.println(compute(-50f, 20f));

		System.out.println(compute(14.2f, 13.3f));
		System.out.println(compute(14.2001f, 13.3f));

//		System.out.println(compute(30f, 13.3f));
//
//		System.out.println(compute(-14.2f, -13.3f));
//		System.out.println(compute(0f, 0f));


	}
	
	public static String compute(float lat, float lng) {
		
		return computeRec(lat+90, lng+180, MAX_LAT, MAX_LNG, new StringBuffer(), 1);
	}
	
	private static String computeRec(float lat, float lng, double compLat, double compLng, StringBuffer sb, int level) {

		if (level >= 25)
			return sb.toString();
		
		if(lat < compLat) { // NORTH
			if(lng < compLng) { // WEST
				sb.append('0');
				return computeRec(lat,  lng, compLat-MAX_LAT/Math.pow(2,level), compLng-MAX_LNG/Math.pow(2,level),  sb,  level+1);
			}
			else { // EAST
				sb.append('1');
				return computeRec(lat,  lng, compLat-MAX_LAT/Math.pow(2,level), compLng+MAX_LNG/Math.pow(2,level),  sb,  level+1);

			}
			
		}
		
		else {  // SOUTH
			if(lng < compLng) { // WEST
				sb.append('2');
				return computeRec(lat,  lng, compLat+MAX_LAT/Math.pow(2,level), compLng-MAX_LNG/Math.pow(2,level),  sb,  level+1);
			}
			else { // EAST
				sb.append('3');
				return computeRec(lat,  lng, compLat+MAX_LAT/Math.pow(2,level), compLng+MAX_LNG/Math.pow(2,level),  sb,  level+1);
			}
		}
	}

}
