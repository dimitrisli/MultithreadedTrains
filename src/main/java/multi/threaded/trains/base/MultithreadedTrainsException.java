package multi.threaded.trains.base;

public class MultithreadedTrainsException extends Exception{

	private static final long serialVersionUID = 1L;

	private boolean recover;
	
	public MultithreadedTrainsException(ValidationError error){
		super(error.toString());
	}
	
	public boolean getCanRecover(){
		return this.recover;
	}
}
