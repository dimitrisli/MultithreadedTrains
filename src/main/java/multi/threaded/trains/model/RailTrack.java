package multi.threaded.trains.model;

import multi.threaded.trains.base.Resource;
import multi.threaded.trains.utils.NumberUtils;

/**
 * The RailTrack object.
 * 
 * Note we don't override equals(obj) since we 
 * want referential equality that is provide it 
 * for us in the Object#equals(Object). Same goes for
 * Object#hashCode().
 */
public class RailTrack implements IRailTrack {

	private static int railTrackNameCounterUniqueIdentifier = 0;
	
	private IStation nextStation;
	private final String railTrackName;
	private double railTrackDistance = NumberUtils.getRandomRailTrackDistanceUnits();
	
	private RailTrack(){
		railTrackName = RAIL_TRACK_PREFIX + ++railTrackNameCounterUniqueIdentifier;
	}
	
	public static IRailTrack newRailTrack(){
		
		return new RailTrack();
	}
	
	public IStation getNextStation(){
		return this.nextStation;
	}
	
	public double getRailTrackDistance(){
		return this.railTrackDistance;
	}
	
	public String getRailTrackName(){
		return this.railTrackName;
	}
	
	@Override
	public String getResourceName(){
		return getRailTrackName();
	}
	
	@Override
	public ResourceType getResourceType() {
		return Resource.ResourceType.RAIL_TRACK;
	}
	
	@Override
	public String toString() {
		return railTrackName;
	}
}
