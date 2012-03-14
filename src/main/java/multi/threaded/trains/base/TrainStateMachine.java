package multi.threaded.trains.base;

/**
 * State pattern to model state transitions for
 * the train. All trains know what to do next given
 * their current state and don't need external supervision
 * for that.
 * 
 * @author Dimitris
 *
 */
public enum TrainStateMachine {
	COMMUTING("Commuting"){
		public TrainStateMachine stateMachineTransition(){
			return UNLOADING_CARGO_TO_STATION;
		}
	},
	UNLOADING_CARGO_TO_STATION("Unloading cargo to station"){
		public TrainStateMachine stateMachineTransition(){
			return LOADING_STATION_CARGO;
		}
	},
	LOADING_STATION_CARGO("Loading station cargo"){
		public TrainStateMachine stateMachineTransition(){
			return COMMUTING;
		}
	};
	
	public abstract TrainStateMachine stateMachineTransition();
	private String name;
	private TrainStateMachine(String name){
		this.name = name;
	}
	@Override
	public String toString() {
		return this.name;
	}
}
