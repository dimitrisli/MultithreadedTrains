package multi.threaded.trains.utils;

import java.util.Random;

import multi.threaded.trains.model.IStation;

/**
 * Utility Class to conveniently provide static helper methods
 * dealing with number operations involving props entries that
 * are getting retrieved under the hood.
 * 
 * @author Dimitris
 *
 */
public class NumberUtils {

	private static final Random random = new Random();
	
	public static int getNextIntRandomNum(int max) {
		return random.nextInt(max);
	}
	
	public static double getRandomTrainSpeed(){
		return random.nextDouble() * PropertiesLoader.getTrainMaxSpeed();
	}
	
	public static double getRandomRailTrackDistanceUnits(){
		return random.nextDouble() * PropertiesLoader.getRailTrackMaxDistanceUnit();
	}
	
	public static double getCargoGeneratorRandomSleepTime(){
		return random.nextDouble() * PropertiesLoader.getCargoGeneratorMaxSleepTime();
	}
	
	public static String getRandomBaseNameStation(){
		return IStation.STATION_NAME_PREFIX+(getNextIntRandomNum(PropertiesLoader.getTotalStationNumber())+1);
	}
	
	//Facility method in order not to produce randomly
	//the delivery city same as the current one.
	public static String getRandomBaseNameStationWithoutStation(String stationToAvoid){
		String result=stationToAvoid;
		while(result.equals(stationToAvoid)){
			result = IStation.STATION_NAME_PREFIX+(getNextIntRandomNum(PropertiesLoader.getTotalStationNumber())+1);
		}
		return result;
	}
}
