package multi.threaded.trains.model;

import multi.threaded.trains.base.Resource;

public abstract interface IStation extends Resource {

	public static final String STATION_NAME_PREFIX = "STATION-";
	
	public abstract String getStationName();
	public abstract boolean addStationCargo(ICargo cargo);
	public abstract ICargo loadToTrainStationCargo();
	public abstract boolean isStationCargoAvailable();
	//public abstract boolean facilitateRemoveStationCargo(ICargo cargo);
}
