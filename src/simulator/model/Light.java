package simulator.model;

import simulator.agent.Agent;

public class Light implements CarAcceptor{
	public enum lightState {GREEN, YELLOW, RED};
	private lightState _state;
	CarAcceptor _road;
		
	public Light() {
		
	}
	
	public double distanceToObstacle(double fromPosition) {
	    /*if state==GREEN
	      return nextRoad.distanceToObstacle(fromPosition);
	    else
	      return 0;
	      */
		
		return 0;
	  }
	
	  public lightState getState() {
		  return _state;
	  }

	public void set_state(lightState _state) {
		this._state = _state;
	}

	@Override
	public boolean acceptObstacle(Agent obstacle) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addObstacle(Agent obstacle) {
		// TODO Auto-generated method stub
		
	}
	  
	  

	
}
