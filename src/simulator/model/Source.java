package simulator.model;

import simulator.agent.TimeServer;

public class Source implements CarAcceptor{
	Road _first;

	public Source(Road first){
		this._first = first;
	}
	
	@Override
	public double distanceToObstacle(double fromPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Car generateCar(TimeServer ts){
		Car car = new Car(0,0,0,0);
		car.setTimeServer(ts);
		car.setCurrentRoad((Road) this._first);
		this._first.accept(car);
		return car;
	}

	
}