package multi.threaded.trains.utils;

public enum PropertiesParams {

	STATIONS_TOTAL_NUMBER("stations.total.number", 8),
	STATIONS_CARGO_CAPACITY("stations.cargo.capacity", 13),
	
	TRAINS_TOTAL_NUMBER("trains.total.number", 4),
	TRAINS_CARGO_CAPACITY("trains.cargo.capacity", 3),
	TRAINS_CARGO_UNIT_LOAD_TIME("trains.cargo.unit.load.time",1000),
	TRAINS_CARGO_UNIT_UNLOAD_TIME("trains.cargo.unit.unload.time",1000),
	TRAINS_MAX_SPEED("trains.max.speed",1),
	
	RAIL_TRACK_MAX_DISTANCE_UNIT("rail.track.max.distance.unit",4),
	
	CARGO_GENERATOR_MAX_SLEEP_TIME("cargo.generator.max.sleep.time",1);
	
	private PropertiesParams(String param, int defaultValue){
		this.param = param;
		this.defaultValue = defaultValue;
	}
	
	private String param;
	private int defaultValue;
	
	@Override
	public String toString() {
		return this.param;
	}
	
	public int getDefaultValue(){
		return this.defaultValue;
	}
}
