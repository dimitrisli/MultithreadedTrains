package multi.threaded.trains.model;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import multi.threaded.trains.base.ValidationError;
import multi.threaded.trains.utils.NumberUtils;

/**
 * The cargo generator thread. It randomly sleeps (the level 
 * of randomness is attached according to a parameter) and
 * when it wakes up it randomly picks a station to add a 
 * cargo unit with a randomly generated delivery station (that
 * cannot be the current station).
 * 
 * @author Dimitris
 *
 */
public class CargoGenerator implements Runnable{

	private static final Logger log = Logger.getLogger(CargoGenerator.class);
	private static final String CARGO_GENERATOR_NAME = "CARGO_GENERATOR";
	private static CargoGenerator cargoGenerator;
	private CountDownLatch latch;
	private CargoGenerator(CountDownLatch latch){
		this.latch = latch;
	}
	
	public static CargoGenerator create(CountDownLatch latch){
		if(cargoGenerator==null){
			cargoGenerator = new CargoGenerator(latch);
		}
		return cargoGenerator;
	}
	
	public static String getCargoGeneratorName(){
		return CARGO_GENERATOR_NAME;
	}
	
	@Override
	public void run() {
		try {
			Thread.currentThread().setName(CARGO_GENERATOR_NAME);
			latch.await();
		
			while(true){
				try {
					Thread.sleep((long) (NumberUtils.getCargoGeneratorRandomSleepTime() * 1000));
					IStation randomCargoStation = Station.getStationByName(NumberUtils.getRandomBaseNameStation());
					IStation randomDeliveryStation = Station.getStationByName(NumberUtils.getRandomBaseNameStationWithoutStation(randomCargoStation.getStationName()));
					ICargo newStationCargo = Cargo.newCargo(randomDeliveryStation);
					boolean successfullyAddedCargo = randomCargoStation.addStationCargo(newStationCargo);
					if(successfullyAddedCargo){
						log.info("People from nearby city have added cargo to station: "+randomCargoStation.getStationName()+" with delivery station being: "+randomDeliveryStation.getStationName());
					} else {
						log.info("People from nearby city could not add any more cargo to station: "+randomCargoStation.getStationName()+" since it reached its capacity...");
					}
				} catch (InterruptedException e) {
					log.error(ValidationError.ERROR_WHILE_CARGO_GENERATOR_WAS_SLEEPING);
				}
			}
		} catch (InterruptedException e1) {
			log.error(ValidationError.ERROR_WHILE_CARGO_GENERATOR_WAS_SLEEPING);
		}
	}

}
