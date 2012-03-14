package multi.threaded.trains.model;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

import multi.threaded.trains.base.Resource;
import multi.threaded.trains.utils.PropertiesLoader;

/**
 * The Station object.
 * 
 * Note we don't override equals(obj) since we 
 * want referential equality that is provide it 
 * for us in the Object#equals(Object). Same goes for
 * Object#hashCode().
 * 
 * @author Dimitris
 *
 */
public class Station implements IStation {

	private static int stationNameCounterUniqueIdentifier = 0;
	
	private final String stationName;
	private Queue<ICargo> stationCargo;
	
	private IRailTrack nextRailTrack;
	
	private Station(Queue<ICargo> stationCargo){
		stationName = STATION_NAME_PREFIX + ++stationNameCounterUniqueIdentifier;
		this.stationCargo = stationCargo;
	}
	
	/*
	 * We'll maintain this map of all stations so that
	 * to decouple the station from the cargoGenerator. 
	 */
	private static Map<String, IStation> stationsByName = new ConcurrentHashMap<String, IStation>();
	
	/*
	 * My initial implementation involved LinkedBlockingDeque and hold
	 * it under a deque interface with the idea that trains could claim
	 * cargos from both ends. The blocking functionality was undesirable
	 * since the beginning therefore the initial decision to hold it under
	 * the Deque interface. Along the way it didn't work out but I'm leaving
	 * this in to demonstrate my thought process.
	 */
	public static IStation newBlockingDequeCargoStation(){
		IStation newStation = new Station(new LinkedBlockingDeque<ICargo>(PropertiesLoader.getStationCargoCapacity()));
		stationsByName.put(newStation.getStationName(), newStation);
		return newStation;
	}
		
	public static IStation newConcurrentQueueCargoStation(){
		IStation newStation = new Station(new ConcurrentLinkedQueue<ICargo>());
		stationsByName.put(newStation.getStationName(), newStation);
		return newStation;
	}
	
	public static IStation getStationByName(String stationName){
		return stationsByName.get(stationName);
	}
	
	/**
	 * Delivery cargo from nearby cities
	 * 
	 * @return boolean if the station cargo has been
	 * 		   successfully added
	 */
	@Override
	public boolean addStationCargo(ICargo cargo) {
		return isStationCargoFull()?false:stationCargo.offer(cargo);
	}

	/**
	 * Loading station cargo to parked trains
	 * 
	 * @return boolean if the station cargo has been
	 * 		   successfully removed
	 */
	@Override
	public ICargo loadToTrainStationCargo() {
		return  stationCargo.poll();
	}
	
//	/**
//	 * "Work Stealing" by additionally loading 
//	 * station cargo to other parked trains.
//	 * 
//	 * @return boolean if the station cargo has been
//	 * 		   successfully removed
//	 */
//	@Override
//	public boolean facilitateRemoveStationCargo(ICargo cargo) {
//		return isStationCargoAvailable() && stationCargo.pollLast()!=null?true:false;
//	}
	
	public IRailTrack getNextRailTrack(){
		return this.nextRailTrack;
	}
	
	public boolean isStationCargoAvailable(){
		return !stationCargo.isEmpty();
	}
	
	private boolean isStationCargoFull(){
		return stationCargo.size() == PropertiesLoader.getStationCargoCapacity();
	}
	
	public String getStationName(){
		return this.stationName;
	}
	
	public String getResourceName(){
		return getStationName();
	}

	@Override
	public ResourceType getResourceType() {
		return Resource.ResourceType.STATION;
	}
	
	@Override
	public String toString() {
		return stationName;
	}
}
