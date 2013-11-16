package simulator.model;
import simulator.agent.Agent;
import simulator.agent.TimeServer;
import simulator.util.MP;

public class Car implements Agent{
	private double _maxVelocity = (int) Math.ceil(Math.random() * MP.maxVelocity); // The maximum velocity of the car (in meters/second)
	private double _brakeDistance; // If distance to nearest obstacle is <= brakeDistance,then the car will start to slow down (in meters)
	private double _stopDistance; // If distance to nearest obstacle is == stopDistance,then the car will stop (in meters)
	private double length; // Length of the car (in meters)
	private java.awt.Color _color = new java.awt.Color((int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255));

	private Road currentRoad; //It's a rreference to the current road
	private TimeServer agents; //It's going to be a refference to the timeserver. !!! 

	private double _frontPositon;
	
	public double getPosition() {
		return _frontPositon;
	}

	public Car(double maxVelocity, double breakDistance, double stopDistance, double position) {
		this._maxVelocity = (int) Math.ceil(Math.random() * MP.maxVelocity);
		this._brakeDistance = 10.0;
		this._stopDistance = 1.0;
		this._frontPositon = position;
	}

	public java.awt.Color getColor() {
		return _color;
	}

	public void run(double time) {
		double distanceToObs = this.currentRoad.distanceToObstacle(this._frontPositon);
		
		if(distanceToObs==0)distanceToObs = Double.POSITIVE_INFINITY; //Allows parallel objects
		
		double velocity = (_maxVelocity / (_brakeDistance - _stopDistance)) * (distanceToObs - _stopDistance);
		velocity = Math.max(0.0, velocity);
		velocity = Math.min(_maxVelocity, velocity);
		_frontPositon = _frontPositon + velocity * time; //TODO: Velocity * time ????
		//this.agents.enqueue(10+time, this); //TimeStamp decides the how longs virtual time.
		//nextFrontPosition = _frontPosition + velocity * timeStep;
		if (currentRoad.accept(this, _frontPositon))
		       agents.enqueue(agents.currentTime()+MP.sleepSeconds,this);

	}

	public double maxVelocity(){
		return this._maxVelocity;
	}

	public double brakeDistance(){

		return this._brakeDistance;
	}

	public double stopDistance(){

		return this._stopDistance;
	}

	public void setTimeServer(TimeServer ts){
		this.agents = ts;
	}
	
	double backPosition() {
	    return _frontPositon-length;
	  }
	
	public void setCurrentRoad(Road road){
		this.currentRoad = road;
	}
	
	public void setFrontPosition(double position){
		this._frontPositon = position;
	}

}
