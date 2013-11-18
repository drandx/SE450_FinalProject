package simulator.model;

import simulator.agent.Agent;

public class Sink implements CarAcceptor{

	public Sink() {
		// TODO Auto-generated constructor stub
	}
	
	
	public double distanceToObstacle(double fromPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public boolean acceptObstacle(Agent obstacle) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void addObstacle(Agent obstacle) {
		// TODO Auto-generated method stub
		
	}
	
	

}
