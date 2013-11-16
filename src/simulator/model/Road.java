package simulator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import simulator.agent.Agent;
import simulator.util.MP;

public class Road implements CarAcceptor{
	CarAcceptor _nextAcceptor;
	
	Road() { 
		this._nextAcceptor = null;
	}
	
	// Created only by this package
	double endPosition = MP.roadLength;

	private List<Car> _cars = new ArrayList<Car>();

	public void accept(Car d) {
		if (d == null) { throw new IllegalArgumentException(); }
		_cars.add(d);
	}

	public boolean accept(Car c, double frontPosition) {
		_cars.remove(c);
		if(frontPosition>endPosition) {
			if(this._nextAcceptor instanceof Sink)//c = null; //Reaches the sink
				return false;
			return ((Road) this._nextAcceptor).accept(c,frontPosition-endPosition);
		} else {
			c.setCurrentRoad(this);
			c.setFrontPosition(frontPosition);
			_cars.add(c);
			return true;
		}
	}

	public List<Car> getCars() {
		return _cars;
	}

	public double distanceToObstacle(double fromPosition) {
		double obstaclePosition = this.distanceToCarBack(fromPosition);
		
		if (obstaclePosition == Double.POSITIVE_INFINITY) {
			double distanceToEnd = fromPosition-this.endPosition;
			if(distanceToEnd>=0)
			obstaclePosition = _nextAcceptor.distanceToObstacle(fromPosition-this.endPosition);
		}
		return obstaclePosition-fromPosition;
		//return 100;
	}

	private double distanceToCarBack(double fromPosition) {
		double carBackPosition = Double.POSITIVE_INFINITY;
		for (Car c : _cars){
			if (c.backPosition() >= fromPosition  && c.backPosition() < carBackPosition)
				carBackPosition = c.backPosition();
		}
		return carBackPosition;
	}

}
