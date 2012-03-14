package multi.threaded.trains.model;

import multi.threaded.trains.utils.PropertiesLoader;


public interface ITrain extends ITrainStateAutomata,Runnable {

	public static final String TRAIN_NAME_PREFIX = "TRAIN-";
	public static final int trainCargoCapacity = PropertiesLoader.getTrainCargoCapacity();
	public static final int trainCargoUnitLoadTime = PropertiesLoader.getTrainCargoUnitLoadTime();
	public static final int trainCargoUnitUnloadTime = PropertiesLoader.getTrainCargoUnitUnloadTime();
	public static final int trainMaxSpeed = PropertiesLoader.getTrainMaxSpeed();

	public abstract String getTrainName();
}
