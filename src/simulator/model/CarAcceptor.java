package simulator.model;

import simulator.agent.Agent;

public interface CarAcceptor {
	public double distanceToObstacle(double fromPosition);
	public boolean acceptObstacle(Agent obstacle);
	public void addObstacle(Agent obstacle);
}
