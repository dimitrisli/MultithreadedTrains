package multi.threaded.trains.model;

/**
 * The Station and Train Cargo
 * 
 * Note we don't override equals(obj) since we 
 * want referential equality that is provide it 
 * for us in the Object#equals(Object). Same goes for
 * Object#hashCode().
 * 
 * @author Dimitris
 *
 */
public class Cargo implements ICargo{

	private final IStation deliveryStation;
	
	private Cargo(IStation deliveryStation){
		this.deliveryStation = deliveryStation;
	}
	
	public static ICargo newCargo(IStation deliveryStation){
		return new Cargo(deliveryStation);
	}
	
	@Override
	public final IStation getDeliveryStation(){
		return this.deliveryStation;
	}
	
	@Override
	public String toString() {
		return "A cargo with delivery address: "+deliveryStation.getStationName();
	}
}
