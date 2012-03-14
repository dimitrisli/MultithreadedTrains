package multi.threaded.trains.service;

import java.util.concurrent.CountDownLatch;

import multi.threaded.trains.model.ResourcesCollection;

/**
 * The main entry point of the application
 * 
 * @author Dimitris
 *
 */
public class MultithreadedTrains {

	public static void main(String[] args) {
	
		//countdownlatch for all trains to start at the
		//same time. Also the cargo generator is awaiting
		//on the same latch.
		CountDownLatch signalLatch = new CountDownLatch(1);
		
		//Naming main thread
		Thread.currentThread().setName("Multithreaded Trains");
		
		//Create stations and rail tracks
		ResourcesCollection.createStationAndRailResourcesForTheTrainSimulator();
		
		//Create trains and cargo generator threads
		ResourcesCollection.createTrainsAndCargoGeneratorThreads(signalLatch);
		
		//release train and cargo generator threads
		signalLatch.countDown();
	}
}
