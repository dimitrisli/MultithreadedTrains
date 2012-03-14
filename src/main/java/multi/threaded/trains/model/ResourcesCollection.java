package multi.threaded.trains.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import multi.threaded.trains.base.Resource;
import multi.threaded.trains.utils.PropertiesLoader;

public class ResourcesCollection {

	private static Logger log = Logger.getLogger(ResourcesCollection.class);
	
	private static List<Resource> trainSimulatorResources;
	private static List<String> trainSimulatorResourcesByName;
	private static ResourcesCollection resourcesCollection = getSingletonResourcesCollection();
	
	private ResourcesCollection(){
		//although the implementation is multithreaded
		//we won't be making any changes to the list only
		//to the state of the resource if it is a station
		trainSimulatorResources = new CopyOnWriteArrayList<Resource>();
		//strings are immutable by virtue so no need
		//to have concurrent collection
		trainSimulatorResourcesByName = new ArrayList<String>();
	}
	
	private static ResourcesCollection getSingletonResourcesCollection(){
		if(resourcesCollection == null){
			resourcesCollection = new ResourcesCollection();
		}
		return resourcesCollection;
	}

	/**
	 * Emulate a quick dirty workaround for a circular
	 * one-way list. If the element is the last one return the
	 * first one.
	 * TODO: A proper circular list implementation support
	 * 
	 * @param currentResource
	 * @return
	 */
	public static String getNextTrainSimulatorResourceByName(String bringMeNextStrResource){
		//if no current resource then start from the start
		if(bringMeNextStrResource == null){
			return trainSimulatorResourcesByName.get(0);
		}
		for(String currentResourceStr : trainSimulatorResourcesByName){
			if(currentResourceStr.equals(bringMeNextStrResource)){
				int currentResourceStrPosition = trainSimulatorResourcesByName.indexOf(currentResourceStr);
				if(currentResourceStrPosition==trainSimulatorResourcesByName.size()-1){
					return trainSimulatorResourcesByName.get(0);
				} else {
					return trainSimulatorResourcesByName.get(++currentResourceStrPosition);
				}
			}
		}
		return null;
	}
	
	/**
	 * Same as above. This method was a perfect candidate for a generic
	 * methdod:
	 * public static <T,E> T getNextTrainSimulatorResource(E e)
	 * but "bringMeNextResource.getResourceName()" makes it impossible since
	 * in the above method where the argument is a String. Unless we
	 * introduce another layer of abstract skeletal implementations below
	 * the interfaces and above the implementation classes.
	 * 
	 * TODO: A proper circular list implementation support 
	 * 
	 * @param currentResource
	 * @return
	 */
	public static Resource getNextTrainSimulatorResource(Resource bringMeNextResource){
		//if no current resource then start from the start
		if(bringMeNextResource == null){
			return trainSimulatorResources.get(0);
		}
		for(Resource currentResource : trainSimulatorResources){
			if(currentResource.equals(bringMeNextResource)){
				int currentResourcePosition = trainSimulatorResources.indexOf(currentResource);
				if(currentResourcePosition==trainSimulatorResources.size()-1){
					return trainSimulatorResources.get(0);
				}else {
					return trainSimulatorResources.get(++currentResourcePosition);
				}
			}
		}
		return null;
	}
	
	public static void createStationAndRailResourcesForTheTrainSimulator(){
		for(int i=0; i< PropertiesLoader.getTotalStationNumber(); i++){
			//add station
			IStation newStation = Station.newConcurrentQueueCargoStation();
			trainSimulatorResources.add(newStation);
			trainSimulatorResourcesByName.add(newStation.getResourceName());
			log.info("Initialization - Adding: "+newStation.getStationName()+"...");
			//add track
			IRailTrack newRailTrack = RailTrack.newRailTrack();
			trainSimulatorResources.add(newRailTrack);
			trainSimulatorResourcesByName.add(newRailTrack.getResourceName());
			log.info("Initialization - Adding: "+newRailTrack.getResourceName()+"...");
		}
	}
	
	public static void createTrainsAndCargoGeneratorThreads(CountDownLatch latch){
		int totalTrainsNumber = PropertiesLoader.getTotalTrainNumber();
		int totalCargoGeneratorNumber = 1;
		ExecutorService executor = Executors.newFixedThreadPool(totalCargoGeneratorNumber + totalTrainsNumber);
		//create trains
		for(int i=0; i< totalTrainsNumber; i++){
			ITrain newTrain = Train.newTrain(latch);
			executor.execute(newTrain);
			log.info("Initialization - Adding: "+newTrain.getTrainName()+"...");
		}
		//create cargo generator thread
		CargoGenerator cargoGenerator = CargoGenerator.create(latch);
		executor.execute(cargoGenerator);
		log.info("Initialization - Adding: "+CargoGenerator.getCargoGeneratorName()+"...");
		//we are not expecting any other threads to join us
		executor.shutdown();
	}
}
