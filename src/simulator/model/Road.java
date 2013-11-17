package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.agent.Agent;

public class Road implements CarAcceptor{
	CarAcceptor _nextAcceptor;
	
	Road() { 
		this._nextAcceptor = null;
	}
	
	// Created only by this package
	double endPosition = Math.random() * (MP.maxroadLength - MP.minroadLength) + MP.minroadLength;

	private List<Agent> _obstacles = new ArrayList<Agent>();

	public void acceptObstacle(Agent d) {
		if (d == null) { throw new IllegalArgumentException(); }
		_obstacles.add(d);
	}

	public boolean accept(Car c, double frontPosition) {
		_obstacles.remove(c);
		if(frontPosition>endPosition) {
			if(this._nextAcceptor instanceof Sink)//c = null; //Reaches the sink
				return false;
			return ((Road) this._nextAcceptor).accept(c,frontPosition-endPosition);
		} else {
			c.setCurrentRoad(this);
			c.setFrontPosition(frontPosition);
			_obstacles.add(c);
			return true;
		}
	}

	public List<Agent> getCars() {
		return _obstacles;
	}

	public double distanceToObstacle(double fromPosition) {
		double obstaclePosition = this.distanceToBackSide(fromPosition);
		
		if (obstaclePosition == Double.POSITIVE_INFINITY) {
			double distanceToEnd = fromPosition-this.endPosition;
			if(distanceToEnd>=0)
			obstaclePosition = _nextAcceptor.distanceToObstacle(fromPosition-this.endPosition);
		}
		return obstaclePosition-fromPosition;
		//return 100;
	}

	private double distanceToBackSide(double fromPosition) {
		double carBackPosition = Double.POSITIVE_INFINITY;
		for (Agent cAgent : _obstacles){
			if(cAgent instanceof Car){
				Car c = (Car)cAgent;
				if (c.backPosition() >= fromPosition  && c.backPosition() < carBackPosition)
					carBackPosition = c.backPosition();
			}
			/*else if(cAgent instanceof LightController){
				LightController lController = (LightController)cAgent;
			}*/
	
		}
		return carBackPosition;
	}

}
