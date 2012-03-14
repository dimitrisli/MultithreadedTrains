package multi.threaded.trains.model;


/**
 * The train can be in only three states and
 * these are the operations it should be doing
 * in each state.
 * 
 * @author Dimitris
 *
 */
public interface ITrainStateAutomata {

	//commuting
	public abstract void lockRailTrackAndCommute(IRailTrack railTrack);
	//unloading station cargo
	public abstract void unloadTrainCargoToDestinationStation(IStation station);
	//loading station cargo
	public abstract void loadStationCargo(IStation station);
}
