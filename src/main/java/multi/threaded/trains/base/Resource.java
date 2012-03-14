package multi.threaded.trains.base;

/**
 * Marker interface to group both Station and
 * RailTrack and allocate it under the train
 * to be stored based on the train's state, i.e.
 * if the train is commuting the relevant RailTrack will
 * be stored under Resource otherwise if it is parked
 * in a Station then that station will be stored in the
 * Resource.
 * 
 * This modelling helps since the Trains only know about 
 * a sequence of Resources, let them be stations or rail
 * tracks, and based on its state acts accordingly.
 * 
 * @author Dimitris
 *
 */
public abstract interface Resource {

	public abstract String getResourceName();
	public abstract ResourceType getResourceType();
	
	public enum ResourceType{
		STATION,RAIL_TRACK;
	}
}
