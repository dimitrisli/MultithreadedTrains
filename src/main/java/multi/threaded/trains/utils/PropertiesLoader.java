package multi.threaded.trains.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import multi.threaded.trains.base.MultithreadedTrainsException;
import multi.threaded.trains.base.ValidationError;

import org.apache.log4j.Logger;

/**
 * A structure way of bringing the props entries in the
 * java layer and make them available to the rest of the
 * application.
 * 
 * @author Dimitris
 *
 */
public class PropertiesLoader {

	private static final Logger log = Logger.getLogger(PropertiesLoader.class);
	
	private static Properties properties = new Properties();
	private PropertiesLoader(){}
	
	static{
			try {
				load();
			} catch (MultithreadedTrainsException mte) {
				log.info(mte.getMessage());
			}
		}
	
	private static void load() throws MultithreadedTrainsException{
		try{
		InputStream in = PropertiesLoader.class.getClass().getResourceAsStream("/multithreadedTrains.properties");
		properties.load(in);
		}catch(IOException ioe){
			throw new MultithreadedTrainsException(ValidationError.CANNOT_LOAD_PROPERTIES);
		}
	}
	
	public static int getStationCargoCapacity(){
		return get(PropertiesParams.STATIONS_CARGO_CAPACITY, ValidationError.CANNOT_FIND_STATIONS_CARGO_CAPACITY);
	}
	
	public static int getTrainCargoCapacity(){
		return get(PropertiesParams.TRAINS_CARGO_CAPACITY, ValidationError.CANNOT_FIND_TRAINS_CARGO_CAPACITY);
	}
	
	public static int getTotalTrainNumber(){
		return get(PropertiesParams.TRAINS_TOTAL_NUMBER, ValidationError.CANNOT_FIND_TRAINS_TOTAL_NUMBER);
	}
	
	public static int getTotalStationNumber(){
		return get(PropertiesParams.STATIONS_TOTAL_NUMBER, ValidationError.CANNOT_FIND_STATIONS_TOTAL_NUMBER);
	}
	
	public static int getTrainCargoUnitLoadTime(){
		return get(PropertiesParams.TRAINS_CARGO_UNIT_LOAD_TIME, ValidationError.CANNOT_FIND_TRAINS_CARGO_UNIT_LOAD_TIME);
	}
	
	public static int getTrainCargoUnitUnloadTime(){
		return get(PropertiesParams.TRAINS_CARGO_UNIT_UNLOAD_TIME, ValidationError.CANNOT_FIND_TRAINS_CARGO_UNIT_UNLOAD_TIME);
	}
	
	public static int getTrainMaxSpeed(){
		return get(PropertiesParams.TRAINS_MAX_SPEED, ValidationError.CANNOT_FIND_TRAINS_MAX_SPEED);
	}
	
	public static int getRailTrackMaxDistanceUnit(){
		return get(PropertiesParams.RAIL_TRACK_MAX_DISTANCE_UNIT, ValidationError.CANNOT_FIND_RAIL_TRACK_MAX_DISTANCE_UNIT);
	}
	
	public static int getCargoGeneratorMaxSleepTime(){
		return get(PropertiesParams.CARGO_GENERATOR_MAX_SLEEP_TIME, ValidationError.CANNOT_FIND_CARGO_GENERATOR_MAX_SLEEP_TIME);
	}
	
	private static int get(PropertiesParams property, ValidationError validation){
		String propStr = properties.getProperty(property.toString());
		if(propStr == null){
			log.warn(validation);
		} else {
			return Integer.parseInt(propStr);
		}
		return property.getDefaultValue();
	}
}
