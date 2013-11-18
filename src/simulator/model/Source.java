package simulator.model;

import simulator.agent.Agent;
import simulator.agent.TimeServer;

public class Source implements Agent{
	Road _first;
	TimeServer _timeServer;

	public Source(Road first, TimeServer ts){
		this._first = first;
		this._timeServer = ts;
	}

	public void run(double time) {
		Car car = new Car();
		car.setTimeServer(this._timeServer);
		car.setObserver((Road) this._first);
		this._first.acceptObstacle(car);
		this._timeServer.enqueue(this._timeServer.currentTime()+time, car);
		this._timeServer.enqueue(time+5.0D, this);
		
	}

	
}
