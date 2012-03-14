package multi.threaded.trains.base;

public enum ValidationError {

	
	UNEXPECTED("Unexpected application error occured.", false),
	CANNOT_LOAD_PROPERTIES("The properties file could not be loaded", false),
	CANNOT_FIND_STATIONS_CARGO_CAPACITY("Cannot retrieve the station cargo capacity, defaulting to 13", true),
	CANNOT_FIND_TRAINS_CARGO_CAPACITY("Cannot retrieve the train cargo capacity, defaulting to 3", true),
	CANNOT_FIND_STATIONS_TOTAL_NUMBER("Cannot retrieve the total number of stations, defaulting to 8", true),
	CANNOT_FIND_TRAINS_TOTAL_NUMBER("Cannot retrieve the total number of trains, defaulting to 4", true),
	CANNOT_FIND_TRAINS_CARGO_UNIT_LOAD_TIME("Cannot retrieve the time taken for a train cargo unit to get loaded to the train, defaulting to 1", true),
	CANNOT_FIND_TRAINS_CARGO_UNIT_UNLOAD_TIME("Cannot retrieve the time taken for a train cargo unit to get unloaded to the station, defaulting to 1", true),
	ERROR_WHILE_UNLOADING_TRAIN_CARGO("There was an error while unloading the train cargo", false),
	CANNOT_FIND_TRAINS_MAX_SPEED("Cannot retrieve the max speed unit of trains, defaulting to 1", true),
	CANNOT_FIND_RAIL_TRACK_MAX_DISTANCE_UNIT("Cannot retrieve the max distance unit of rail track, defaulting to 4", true),
	ERROR_WHILE_TRAIN_COMMUTING("There was a problem while the train was commuting", true),
	ERROR_WHILE_LOADING_TRAIN_CARGO_FROM_STATION("There was a problem while loading cargo from station to train", false),
	ERROR_WHILE_UNLOADING_TRAIN_CARGO_TO_STATION("Error while unloading the cargo from train", false),
	CANNOT_FIND_CARGO_GENERATOR_MAX_SLEEP_TIME("Cannot retrieve the max sleep time of the cargoGenerator, defaulting to 1", true),
	ERROR_WHILE_CARGO_GENERATOR_WAS_SLEEPING("There was an error while the cargo generator was sleeping", false),
	ERROR_WHILE_TRAINS_AWAITING_COUNTDOWN_LATCH_SIGNAL("There was a problem while a train was awaiting on a countdownlatch signal", false),
	ERROR_WHILE_CARGO_GENERATOR_AWAITING_COUNTDOWN_LATCH_SIGNAL("There was a problem while the cargo generator was awaiting on a countdownlatch signal", false);

	
	private ValidationError(String message, boolean canRecover){
		this.message = message;
		this.canRecover = canRecover;
	}
	
	private String message;
	private boolean canRecover;
	
	@Override
	public String toString(){
		return message;
	}
	
	public boolean canRecover(){
		return this.canRecover;
	}
}
