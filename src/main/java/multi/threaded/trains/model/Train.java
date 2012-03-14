package multi.threaded.trains.model;

import java.util.concurrent.CountDownLatch;

import multi.threaded.trains.base.Resource;
import multi.threaded.trains.base.TrainStateMachine;
import multi.threaded.trains.base.ValidationError;
import multi.threaded.trains.utils.NumberUtils;

import org.apache.log4j.Logger;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

/**
 * The Train object.
 * 
 * Note we don't override equals(obj) since we 
 * want referential equality that is provide it 
 * for us in the Object#equals(Object). Same goes for
 * Object#hashCode().
 * 
 * @author Dimitris
 *
 */
public class Train implements ITrain {
	
	private static int trainNameCounterUniqueIdentifier = 0;
	private static final Logger log = Logger.getLogger(Train.class);
	
	private final String trainName;
	private ListMultimap<String,ICargo> deliveryCargoMap;
	private int currentTrainCargoSpaceLeft;
	private double trainSpeed = NumberUtils.getRandomTrainSpeed();
	private Resource currentResource;
	private CountDownLatch latch;
	/*
	 * enums are by default final so we get
	 * multithreaded visibility for free. But since it's always
	 * good to encapsulate state and expose mutator methods
	 * guarding against any unwanted actions therefore this is private.
	 */
	private TrainStateMachine currentTrainState;
	
	
	private Train(ListMultimap<String,ICargo> deliveryCargoMap, CountDownLatch latch){
		trainName = TRAIN_NAME_PREFIX + ++trainNameCounterUniqueIdentifier;
		this.deliveryCargoMap = deliveryCargoMap;
		currentTrainCargoSpaceLeft = trainCargoCapacity;
		currentTrainState = TrainStateMachine.UNLOADING_CARGO_TO_STATION;
		this.latch = latch;
	}
	
	/**
	 * Factory train method. We pick on this implementation of
	 * guava's library Multimap because we want to maintain duplicate
	 * values (cargo units) for the same key (delivery city). 
	 * Decorating it with a multithreaded wrapper although 
	 * not necessary since a trains cargo multimap state will not
	 * be concurrently accessed by more than one station or any other train.
	 * 
	 * @param capacity
	 * @return
	 */
	public static ITrain newTrain(CountDownLatch latch){
		ListMultimap<String, ICargo> deliveryCargoMap = ArrayListMultimap.create();
		return new Train(Multimaps.synchronizedListMultimap(deliveryCargoMap), latch);
	}
	
	public void loadTrainCargo(ICargo cargoUnit){
		try {
			Thread.sleep(trainCargoUnitLoadTime);
		} catch (InterruptedException e) {
			log.error(ValidationError.ERROR_WHILE_UNLOADING_TRAIN_CARGO);
		}
	}
	
	public String getTrainName(){
		return this.trainName;
	}
	
	public TrainStateMachine getCurrentTrainState(){
		return this.currentTrainState;
	}
	
	public TrainStateMachine transitionToNextTrainState(){
		currentTrainState = currentTrainState.stateMachineTransition();
		return currentTrainState;
	}
	
	public void lockRailTrackAndCommute(IRailTrack railTrack){
		synchronized(railTrack){
			currentResource = railTrack;
			logCurrentTrainOverallState();
			log.info("Exclusively commuting on rail track: "+currentResource.getResourceName()+"...");
			try {
				Thread.sleep((long)(trainSpeed * 1000));
			} catch (InterruptedException e) {
				log.error(ValidationError.ERROR_WHILE_TRAIN_COMMUTING);
			}
		}
	}
	
	public void loadStationCargo(IStation station){
		currentResource = station;
		boolean hasLoaded = false;
		logCurrentTrainOverallState();
		while(currentTrainCargoSpaceLeft > 0 && station.isStationCargoAvailable()){
			ICargo currentStationCargo = station.loadToTrainStationCargo();
			this.deliveryCargoMap.put(currentStationCargo.getDeliveryStation().getStationName(), currentStationCargo);
			currentTrainCargoSpaceLeft--;
			hasLoaded = true;
			log.info("Loading a new cargo unit from station: "+station.getResourceName()+"...");
			try {
				Thread.sleep(trainCargoUnitLoadTime);
			} catch (InterruptedException e) {
				log.error(ValidationError.ERROR_WHILE_LOADING_TRAIN_CARGO_FROM_STATION);
			}
		}
		if(!hasLoaded){
			log.info("No cargo loaded from station: "+station.getStationName());
		}
	}
	
	public void unloadTrainCargoToDestinationStation(IStation station){
		currentResource = station;
		boolean hasUnloaded = false;
		logCurrentTrainOverallState();
		if(deliveryCargoMap.containsKey(currentResource.getResourceName())){
			int numOfCargosForThisStation = deliveryCargoMap.get(currentResource.getResourceName()).size();
			deliveryCargoMap.removeAll(currentResource.getResourceName());
			currentTrainCargoSpaceLeft += numOfCargosForThisStation;
			hasUnloaded = true;
			log.info("Unloading "+numOfCargosForThisStation+" cargo unit"+(numOfCargosForThisStation>1?"s ":" ")+"at station: "+currentResource.getResourceName()+"...");
			try {
				Thread.sleep(trainCargoUnitUnloadTime);
			} catch (InterruptedException e) {
				log.error(ValidationError.ERROR_WHILE_UNLOADING_TRAIN_CARGO_TO_STATION);
			}
		}
		if(!hasUnloaded){
			log.info("No cargo to be unloaded at station: "+station.getStationName());
		}
	}

	/**
	 * that's where the train threads will spend most of their
	 * lives, looping between their allowed 3 states, performing the
	 * operations according to the train state machine and then moving
	 * to the next state etc.
	 */
	@Override
	public void run() {
		try {
			Thread.currentThread().setName(trainName);
			latch.await();

			while(true){
				switch(currentTrainState){
					case COMMUTING:{
						Resource nextResource = ResourcesCollection.getNextTrainSimulatorResource(currentResource);
						IRailTrack railTrack = (IRailTrack) nextResource;
						lockRailTrackAndCommute(railTrack);
						break;}
					case UNLOADING_CARGO_TO_STATION:{
						Resource nextResource = ResourcesCollection.getNextTrainSimulatorResource(currentResource);
						IStation station = (IStation) nextResource;
						unloadTrainCargoToDestinationStation(station);
						break;}
					case LOADING_STATION_CARGO:{
						//we know the loading follows the unloading
						//on the station therefore getting directly
						//the resource from the train instance that
						//we know for sure it's a Station that the 
						//previous state (UNLOADING_CARGO_STATION)
						//has setup
						IStation station = (IStation) currentResource;
						loadStationCargo(station);
						break;}
				}
				transitionToNextTrainState();
			}
		} catch (InterruptedException e) {
			log.error(ValidationError.ERROR_WHILE_TRAINS_AWAITING_COUNTDOWN_LATCH_SIGNAL);
		}
	}
	
	private void logCurrentTrainOverallState(){
		log.info(this.toString());
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder(getTrainName()).append(": ");
		output.append(currentTrainState.toString()).append(" ").append(currentResource.getResourceName());
		output.append(". Current cargo size: ").append(deliveryCargoMap.size());
		if(!deliveryCargoMap.isEmpty()){
			output.append(". Cargo with delivery stations: ");
			output.append(Joiner.on(", ").skipNulls().join(deliveryCargoMap.keys()));
		}
		return output.toString();
	}
}
